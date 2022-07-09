package com.yiyi_app.recommend.util;

import com.yiyi_app.recommend.dto.UserActiveDTO;
import com.yiyi_app.recommend.dto.UserSimilarityDTO;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RecommendUtils {

    /**
     * 将用户的购买行为组装成一个HashMap<uid, Map<itemId, hits>,这里使用线程安全的HashMap
     * @author cjm
     * @date: 2022/7/6
     */
    public static ConcurrentHashMap<String, ConcurrentHashMap<String, Long>> assembleUserBehavior(List<UserActiveDTO> userActiveDTOS) {
        ConcurrentHashMap<String, ConcurrentHashMap<String, Long>> activeMap=new ConcurrentHashMap<>();
        for(UserActiveDTO userActiveDTO : userActiveDTOS) {
            String uid=userActiveDTO.getUid();
            String itemId=userActiveDTO.getItemId();
            Long hits=userActiveDTO.getHits();

            if(activeMap.containsKey(uid)) {
                ConcurrentHashMap<String, Long> tempMap=activeMap.get(uid);
                tempMap.put(itemId, hits);
                activeMap.put(uid, tempMap);
            }
            else {
                ConcurrentHashMap<String, Long> tempMap2=new ConcurrentHashMap<>();
                tempMap2.put(itemId, hits);
                activeMap.put(uid, tempMap2);
            }
        }
        return activeMap;
    }


    /**
     * 计算用户之间的相似性并返回用户与用户之间的相似度对象
     * @author cjm
     * @date: 2022/7/6
     */
    public static List<UserSimilarityDTO> calcSimilarityBetweenUsers(ConcurrentHashMap<String, ConcurrentHashMap<String, Long>> activeMap) {
        List<UserSimilarityDTO> similarityDTOList=new ArrayList<>();
        Set<String> userSet=activeMap.keySet();

        List<String> uidList=new ArrayList<>(userSet);

        if(uidList.size()<2) {
            return similarityDTOList;
        }

        for(int i=0; i<uidList.size()-1; i++) {
            for(int j=i+1; j<uidList.size(); j++) {
                ConcurrentHashMap<String, Long> uidItemMap=activeMap.get(uidList.get(i));
                ConcurrentHashMap<String, Long> refItemMap=activeMap.get(uidList.get(j));

                Set<String> key1Set=uidItemMap.keySet();
                Set<String> key2Set=refItemMap.keySet();
                Iterator<String> it1=key1Set.iterator();
                Iterator<String> it2=key2Set.iterator();

                double similarity=0.0;
                double molecule=0.0;
                double denominator=1.0;

                double vector1=0.0;
                double vector2=0.0;

                while(it1.hasNext() && it2.hasNext()) {
                    String it1ID=it1.next();
                    String it2ID=it2.next();

                    Long hits1=uidItemMap.get(it1ID);
                    Long hits2=refItemMap.get(it2ID);

                    molecule += hits1*hits2;
                    vector1 += Math.pow(hits1, 2);
                    vector2 += Math.pow(hits2, 2);
                }
                denominator = Math.sqrt(vector1) * Math.sqrt(vector2);
                similarity = molecule/denominator;

                UserSimilarityDTO userSimilarityDTO=new UserSimilarityDTO();
                userSimilarityDTO.setUid(uidList.get(i));
                userSimilarityDTO.setRef_uid(uidList.get(j));
                userSimilarityDTO.setSimilarity(similarity);

                similarityDTOList.add(userSimilarityDTO);
            }
        }
        return similarityDTOList;
    }

    /**
     *  找出与uid行为最为相似的topN个用户
     *  @author cjm
     *  @date: 2022/7/6
     */
    public static List<String> getSimilarityTopN(String uid, List<UserSimilarityDTO> userSimilarityDTOList, int topN) {
        List<String> similarityTopNList = new ArrayList<>(topN);

        PriorityQueue<UserSimilarityDTO> minHeap=new PriorityQueue<>(new Comparator<UserSimilarityDTO>() {
            @Override
            public int compare(UserSimilarityDTO o1, UserSimilarityDTO o2) {
                if(o1.getSimilarity() - o2.getSimilarity()>0) {
                    return 1;
                }
                else if(o1.getSimilarity()-o2.getSimilarity()==0) {
                    return 0;
                }
                else {
                    return -1;
                }
            }
        });

        for(UserSimilarityDTO userSimilarityDTO : userSimilarityDTOList) {
            if(minHeap.size() < topN) {
                minHeap.offer(userSimilarityDTO);
            }
            else if(minHeap.peek().getSimilarity() < userSimilarityDTO.getSimilarity()) {
                minHeap.poll();
                minHeap.offer(userSimilarityDTO);
            }
        }
        for(UserSimilarityDTO userSimilarityDTO : minHeap) {
            similarityTopNList.add(Objects.equals(userSimilarityDTO.getUid(), uid) ? userSimilarityDTO.getRef_uid() : userSimilarityDTO.getUid());
        }
        return similarityTopNList;
    }

    /**
     * 根据TopN用户列表选出推荐的商品
     * @author cjm
     * @date: 2022/7/6
     */
    public static List<String> getRecommendItems(String uid, List<String> similarUserList, List<UserActiveDTO> userActiveDTOList) {
        List<String> recommendProductList = new ArrayList<>();

        // uid的浏览行为列表
        List<UserActiveDTO> userIdActiveList = findUsersBrowsBehavior(uid, userActiveDTOList);

        // 1.从与uid浏览行为相似的每个用户中找出一个推荐的二级类目
        for (String refId : similarUserList) {
            // 计算当前用户所点击的二级类目次数与被推荐的用户所点击的二级类目的次数的差值

            // 找到当前这个用户的浏览行为
            List<UserActiveDTO> currActiveList = findUsersBrowsBehavior(refId, userActiveDTOList);

            // 记录差值最大的二级类目的id
            String maxItemId = null;

            // 记录最大的差值
            double maxDifference = 0.0;
            for (int i = 0; i < currActiveList.size(); i++) {
                for(int j=0; j < userIdActiveList.size(); j++) {
                    // 求出点击量差值最大的二级类目，即为要推荐的类目
                    double difference = Math.abs(currActiveList.get(i).getHits() - userIdActiveList.get(j).getHits());
                    if (difference > maxDifference) {
                        maxDifference = difference;
                        maxItemId = currActiveList.get(i).getItemId();
                    }
                }
            }
            recommendProductList.add(maxItemId);
        }
        return recommendProductList;
    }

    public static List<UserActiveDTO> findUsersBrowsBehavior(String uid, List<UserActiveDTO> userActiveDTOList) {
        List<UserActiveDTO> currActiveList=new ArrayList<>();
        for (UserActiveDTO userActiveDTO : userActiveDTOList) {
            if (userActiveDTO.getUid().equals(uid)) {
                currActiveList.add(userActiveDTO);
            }
        }
        return currActiveList;
    }

}
