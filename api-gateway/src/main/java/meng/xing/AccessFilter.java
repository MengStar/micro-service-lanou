package meng.xing;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import meng.xing.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class AccessFilter extends ZuulFilter {

    private static Logger log = LoggerFactory.getLogger(AccessFilter.class);

    @Autowired

    private TokenService tokenService;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    //todo :权限认证
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        log.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));
        /*
            请求token的请求直接转发
         */
        if (request.getServletPath().startsWith("/token"))
            return null;
         /*
            其余请求验证token
         */
        Object token = request.getParameter("token");
        if (token == null) {
            log.warn("access token is empty");
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            return null;
        }
        String username = tokenService.getUsernameFromToken((String) token);
        log.info("username :" + username);
        if (username != null) {
            Map<String, String[]> params = request.getParameterMap();
            Map<String, List<String>> queryParams = new HashMap<>();
            for (String in : params.keySet()) {
                List<String> valueList = Arrays.asList(params.get(in));
                queryParams.put(in, valueList);
            }
            queryParams.put("username", Collections.singletonList(username));
            ctx.setRequestQueryParams(queryParams);
            return null;
        }
        log.warn("access token is illegal");
        ctx.setSendZuulResponse(false);
        ctx.setResponseStatusCode(401);
        return null;
    }
}
