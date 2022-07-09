package com.yiyi_app.config;

import com.yiyi_app.recommend.dto.UserActiveDTO;
import com.yiyi_app.recommend.dto.UserSimilarityDTO;
import com.yiyi_app.recommend.service.UserActiveService;
import com.yiyi_app.recommend.service.UserSimilarityService;
import com.yiyi_app.recommend.util.RecommendUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  设置定时任务，定期计算用户相似度并更新数据库数据
 * @author cjm
 * @date: 2022/7/7
 */
@Configuration
@EnableScheduling
public class UpdateScheduleTask {

    @Resource
    UserActiveService ActiveService;

    @Resource
    UserSimilarityService SimilarityService;

    @Scheduled(cron = "0/60 * * * * ?")
    private void SchedulingTaskForUpdate() {
        //获得当前所有用户的用户行为信息
        List<UserActiveDTO> userActiveDTOList = ActiveService.listAllUserActive();
        //将获得的用户行为信息打包封装
        ConcurrentHashMap<String, ConcurrentHashMap<String, Long>> activeMap = RecommendUtils.assembleUserBehavior(userActiveDTOList);
        //计算得到用户之间的相似度
        List<UserSimilarityDTO> userSimilarityDTOList = RecommendUtils.calcSimilarityBetweenUsers(activeMap);

        //遍历，如果uid,red_uid在数据库中已存在则更新相似度，否则新增相似度
        userSimilarityDTOList.forEach(userSimilarityDTO -> {
            UserSimilarityDTO dto=SimilarityService.getUserSimilarityByUID(userSimilarityDTO.getUid(), userSimilarityDTO.getRef_uid());
            if(dto!=null) {
                if(dto.getSimilarity()!=userSimilarityDTO.getSimilarity()) {
                    SimilarityService.updateUserSimilarity(userSimilarityDTO);
                }
            }
            else {
                SimilarityService.saveUserSimilarity(userSimilarityDTO);
            }
        });
    }
}
