package com.acentic.cloudservices.session.interceptors;

import com.acentic.cloudservices.session.user.userSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class RoleCheckerInterceptor implements Interceptor {

    /**
     *
     */
    @Autowired
    private userSession userSessionBean;

    private static final long serialVersionUID = 1L;
    protected static final Logger LOGGER = Logger.getLogger(RoleCheckerInterceptor.class);

    @Override
    public void destroy() {
        LOGGER.log(Level.DEBUG, "RoleCheckerInterceptor stopped");
    }

    @Override
    public void init() {
        LOGGER.log(Level.DEBUG, "RoleCheckerInterceptor started");
    }

    @Override
    public String intercept(ActionInvocation arg0) throws Exception {
        LOGGER.log(Level.TRACE, "RoleCheckerInterceptor context: " + ActionContext.getContext().getName());

        // test if allowed
        Map<String, String> parameters = arg0.getProxy().getConfig().getParams();
        List<String> neededRoles = new ArrayList<>();
        String s = parameters.get("roles");

        // no roles defined, allow
        if (s == null) {
            return arg0.invoke();
        }

        String[] strRoles = s.split(",");
        for (String strRole : strRoles) {
            neededRoles.add(strRole);
            LOGGER.log(Level.DEBUG, "role needed for action " + strRole);
        }

        // no roles defined, allow
        if (neededRoles.size() == 0) {
            return arg0.invoke();
        }

        if ((userSessionBean == null) || (userSessionBean.getId() == 0)) {
            LOGGER.log(Level.DEBUG, "no user, but no role needed, allowing access");
            return "FORBIDDEN";

        }

        boolean found = true;
        for (int i = 0; i < neededRoles.size(); i++) {
            found = userSessionBean.hasRole(neededRoles.get(i));
            if (found) {
                break;
            }
        }

        if (!found) {
            LOGGER.log(Level.INFO, "unallowed access " + userSessionBean.getDisplayName() + " for role " + neededRoles.toString());
            return "FORBIDDEN";
        }

        LOGGER.log(Level.DEBUG, "RoleCheckerInterceptor allow access for " + userSessionBean.getDisplayName() + " to  " + ActionContext.getContext().getName());
        return arg0.invoke();

    }

    public static String getUrl(HttpServletRequest req) {
        String reqUrl = req.getRequestURL().toString();
        String queryString = req.getQueryString();   // d=789
        if (queryString != null) {
            reqUrl += "?" + queryString;
        }
        return reqUrl;
    }

    public userSession getUserSessionBean() {
        return userSessionBean;
    }

    public void setUserSessionBean(userSession userSessionBean) {
        this.userSessionBean = userSessionBean;
    }

}
