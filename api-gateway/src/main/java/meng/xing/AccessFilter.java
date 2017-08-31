package meng.xing;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class AccessFilter extends ZuulFilter {

    private static Logger log = LoggerFactory.getLogger(AccessFilter.class);
    @Autowired
    private UserService userService;

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

        Object accessToken = request.getParameter("accessToken");
        if (accessToken == null) {
            log.warn("access token is empty");
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            return null;
        }
        Map<String, String[]> params = request.getParameterMap();
        Map<String, List<String>> queryParams = new HashMap<>();
        for (String in : params.keySet()) {
            List<String> valueList = Arrays.asList(params.get(in));
            queryParams.put(in, valueList);
        }
        queryParams.put("accessToken", Collections.singletonList("11111111111"));
        ctx.setRequestQueryParams(queryParams);
        return null;
    }
}
