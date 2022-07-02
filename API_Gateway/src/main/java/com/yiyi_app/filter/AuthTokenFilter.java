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

        System.out.println("路由网关拦截:" + uri);
        //检查白名单, 直接放行,无需登录获取token校验
        if(uri.contains("/api/auth/token") || uri.contains("/api/users/register")) {
            return chain.filter(exchange);
        }
        //从请求头中取出token
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
        //token校验通过，放行
        return chain.filter(exchange);
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
