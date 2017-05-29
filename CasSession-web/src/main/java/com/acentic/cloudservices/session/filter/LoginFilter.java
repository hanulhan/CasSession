package com.acentic.cloudservices.session.filter;

import com.acentic.cloudservices.session.user.userSession;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class LoginFilter implements Filter {

    private String excludePattern;
    private String loginUrl = "/CloudServices/public/doForceLogin.action";
    private Pattern pattern;
    
    private userSession userSessionBean;
    protected static final Logger LOGGER = Logger.getLogger(LoginFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String requestURI = req.getRequestURI();

        LOGGER.log(Level.TRACE, "checking login for uri " + requestURI);
        boolean bIgnore =  isRequestUrlExcluded(req);
        if ( (!bIgnore) && (!loggedIn())) {
            if (response instanceof HttpServletResponse) {
                LOGGER.log(Level.DEBUG, "user not logged in redirecting to login page");
                HttpServletResponse resp = (HttpServletResponse) response;
                resp.sendRedirect(loginUrl);
            } else {
                return;
            }
        }
        chain.doFilter(request, response);

    }

    private boolean loggedIn() {
        try {
            if ((userSessionBean == null) || (userSessionBean.getId() == 0)) {
                LOGGER.log(Level.DEBUG, "usersessionbean not set redirecting to login page");
                return false;
            }
            LOGGER.log(Level.DEBUG, "user logged in, granting access for " + userSessionBean.getDisplayName());
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, "error checking login", e);
            return false;
        }
        return true;
    }

    private String getUri(HttpServletRequest request) {
        // handle http dispatcher includes.
        String uri = (String) request.getAttribute("javax.servlet.include.servlet_path");
        if (uri != null) {
            return uri;
        }
        /*   	
        uri = RequestUtils.getServletPath(request);
        if (uri != null && !"".equals(uri)) {
            return uri;
        }
         */
        uri = request.getRequestURI();
        return uri.substring(request.getContextPath().length());

    }

    @Override
    public void init(FilterConfig filterConfig) {
        LOGGER.log(Level.DEBUG, "initializing LoginFiler");
        String s  =filterConfig.getInitParameter("excludePattern");
        if ((s != null) && (!s.isEmpty())) {
            excludePattern = s;
            pattern = Pattern.compile(excludePattern);
        }    
        s = filterConfig.getInitParameter("loginUrl");
        if ((s != null) && (!s.isEmpty())) {
            loginUrl = s;
        }

    }

    @Override
    public void destroy() {
        LOGGER.log(Level.DEBUG, "detroying LoginFiler");
    }


    private boolean isRequestUrlExcluded(HttpServletRequest req) {
        final StringBuffer urlBuffer = req.getRequestURL();
        if (req.getQueryString() != null) {
            urlBuffer.append("?").append(req.getQueryString());
        }
        final String requestUri = urlBuffer.toString();
        if (pattern == null)
            return true;
        Matcher matcher = pattern.matcher(requestUri);
        return (matcher.matches());
    }

    public userSession getUserSessionBean() {
        return userSessionBean;
    }

    public void setUserSessionBean(userSession userSessionBean) {
        this.userSessionBean = userSessionBean;
    }

    
}
