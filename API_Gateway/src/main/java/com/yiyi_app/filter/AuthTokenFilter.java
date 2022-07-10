package com.yiyi_app.filter;

import com.alibaba.fastjson2.JSON;
import com.yiyi_app.util.JWTUtil;
import com.yiyi_app.util.ResponseCodeEnum;
import com.yiyi_app.util.ResponseResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 *  全局校验token的过滤器实现
 *
 * @date: 2022/7/2
 */
@Component
public class AuthTokenFilter implements GlobalFilter, Ordered {

    @Value("${secretKey:yiyi_app}")
    String secretKey;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        ServerHttpResponse serverHttpResponse = exchange.getResponse();
        String uri = serverHttpRequest.getURI().getPath();

        //检查白名单, 登录与注册请求直接放行,无需登录获取token校验
        if(uri.contains("/api/auth/token") || uri.contains("/api/users/register") || uri.contains("/api/items/categories") || uri.contains("/api/items/search") || uri.contains("/api/items/classify") || uri.contains("/api/items/hotItems") || uri.contains("/api/users/check")) {
            return chain.filter(exchange);
        }
        //如果是浏览商品的请求，如果没有token也直接放行，有则将uid放入请求头中再放行
        if(uri.contains("/api/items/item")) {
            String token_str=serverHttpRequest.getHeaders().getFirst("Authorization");
            if(token_str==null) {
                ServerHttpRequest mutableRequest=serverHttpRequest
                        .mutate()
                        .header("uid", "未登录")
                        .build();

                ServerWebExchange mutableExchange=exchange
                        .mutate()
                        .request(mutableRequest)
                        .build();
                return chain.filter(mutableExchange);
            }
            else {
                try{
                    JWTUtil.verifyToken(token_str, secretKey);
                }catch (Exception e) {
                    return getVoidMono(serverHttpResponse, ResponseCodeEnum.TOKEN_INVALID);
                }
                String uid_str=JWTUtil.getUIDfromToken(token_str);
                ServerHttpRequest mutableRequest=serverHttpRequest
                        .mutate()
                        .header("uid", uid_str)
                        .build();

                ServerWebExchange mutableExchange=exchange
                        .mutate()
                        .request(mutableRequest)
                        .build();
                return chain.filter(mutableExchange);
            }
        }

        //其余请求从请求头中取出token
        String token = serverHttpRequest.getHeaders().getFirst("Authorization");
        //如果未携带token
        if(token==null) {
            serverHttpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
            return getVoidMono(serverHttpResponse, ResponseCodeEnum.TOKEN_MISSION);
        }
        //对token进行校验
        try{
            JWTUtil.verifyToken(token, secretKey);
        }catch (Exception e) {
            return getVoidMono(serverHttpResponse, ResponseCodeEnum.TOKEN_INVALID);
        }
        //token校验通过,放行,并将uid放入请求头中
        String uid=JWTUtil.getUIDfromToken(token);
        ServerHttpRequest mutableRequest=serverHttpRequest
                .mutate()
                .header("uid", uid)
                .build();

        ServerWebExchange mutableExchange=exchange
                .mutate()
                .request(mutableRequest)
                .build();
        return chain.filter(mutableExchange);
    }

    private Mono<Void> getVoidMono(ServerHttpResponse response, ResponseCodeEnum codeEnum) {
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        ResponseResult responseResult = ResponseResult.error(codeEnum.getCode(), codeEnum.getMsg());
        DataBuffer dataBuffer = response.bufferFactory().wrap(JSON.toJSONString(responseResult).getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Flux.just(dataBuffer));
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
