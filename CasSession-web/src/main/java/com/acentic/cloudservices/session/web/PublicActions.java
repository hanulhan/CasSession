/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acentic.cloudservices.session.web;

import com.acentic.cloudservices.user.SystemUser.beans.SystemUserBean;
import com.acentic.cloudservices.user.SystemUser.beans.ser_SystemUser;
import com.acentic.cloudservices.user.util.SpringUserBeansDef;
import com.acentic.cloudservices.util.JsonStatus;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class PublicActions extends ActionSupport implements SessionAware, ApplicationContextAware {

    private ApplicationContext applicationContext;
    
    private static final Logger LOGGER = Logger.getLogger(PublicActions.class);
    private static final String LOGOUT_FROM_CAS = "LOGOUT_FROM_CAS";
    private static final String SHOW_GPNS_DASHBOARD = "gpns_dashboard";
    
    private Map<String, Object> session;
    private String locale;
    private String casServer;

    private String username;
    private String password;
    private String retypedPassword;
    private boolean loginFailed;

    private String email;
    private JsonStatus jsonStatus;
    private String link;
    private ser_SystemUser userProfile;
    private String redirNamespace;
    private String redirAction;
    
    private long id;
    private String logoutHeadline;
    private String logoutReLogin;
    
    
    

    public String doLoginCasUser() {
        LOGGER.log(Level.TRACE, "do login cas user");

        SystemUserBean systemUser = null;
        try {
            HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            AttributePrincipal principal = (AttributePrincipal) request.getUserPrincipal();
            if (principal == null) {
                throw new Exception("principal is null");
            } else {
                @SuppressWarnings("rawtypes")
                Map attributes = principal.getAttributes();
                String uid = (String) attributes.get("uid");
                Long lUid = Long.valueOf(uid);
                systemUser = (SystemUserBean) applicationContext.getBean(SpringUserBeansDef.SystemUserBean, lUid);
                if (!systemUser.isLoaded()) {
                    LOGGER.log(Level.ERROR, "unable to login cas user with id " + lUid);
                    return ERROR;
                }

                LOGGER.log(Level.DEBUG, "request with local=" + request.getLocale().toString());
            }
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, "unable to login cas user", e);
            return ERROR;
        }

        return SUCCESS;
    }

    private static Locale localefromString(String locale) {
        Locale l = null;
        String parts[] = locale.split("_", -1);
        if (parts.length == 1) {
            l = new Locale(parts[0]);
        } else if (parts.length == 2 || (parts.length == 3 && parts[2].startsWith("#"))) {
            l = new Locale(parts[0], parts[1]);
        } else {
            l = new Locale(parts[0], parts[1], parts[2]);
        }

        LOGGER.log(Level.DEBUG, "setting default locale to " + l.toString());
        return l;
    }

    public String doLogout() {
        LOGGER.log(Level.TRACE, "do logout user");
        logoutHeadline = this.getText("session.logout.headline");
        logoutReLogin = this.getText("session.logout.login") ;

        if (!StringUtils.isEmpty(casServer)) {
            LOGGER.log(Level.DEBUG, "do logout user from cas server, sending redirect");
            return LOGOUT_FROM_CAS;
        }
        return SUCCESS;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }

    public String getCasServer() {
        return casServer;
    }

    public void setCasServer(String casServer) {
        this.casServer = casServer;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoginFailed() {
        return loginFailed;
    }

    public void setLoginFailed(boolean loginFailed) {
        this.loginFailed = loginFailed;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public JsonStatus getJsonStatus() {
        return jsonStatus;
    }


    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public ser_SystemUser getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(ser_SystemUser userProfile) {
        this.userProfile = userProfile;
    }

    public String getRetypedPassword() {
        return retypedPassword;
    }

    public void setRetypedPassword(String retypedPassword) {
        this.retypedPassword = retypedPassword;
    }

    public String getRedirNamespace() {
        return redirNamespace;
    }

    public String getRedirAction() {
        return redirAction;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogoutHeadline() {
        return logoutHeadline;
    }

    public void setLogoutHeadline(String logoutHeadline) {
        this.logoutHeadline = logoutHeadline;
    }

    public String getLogoutReLogin() {
        return logoutReLogin;
    }

    public void setLogoutReLogin(String logoutReLogin) {
        this.logoutReLogin = logoutReLogin;
    }

   
}
