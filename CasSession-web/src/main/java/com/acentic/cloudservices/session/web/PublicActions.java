/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acentic.cloudservices.session.web;

import com.acentic.cloudservices.base.SystemMenu.beans.SERSystemMenu;
import com.acentic.cloudservices.base.email.EMailSender;
import com.acentic.cloudservices.base.util.SystemRoleDef;
import com.acentic.cloudservices.hotel.SystemHotel.beans.SystemHotelBean;
import com.acentic.cloudservices.hotel.util.SpringHotelBeansDef;
import com.acentic.cloudservices.session.dao.SessionDAOInterface;
import com.acentic.cloudservices.session.user.userSession;
import com.acentic.cloudservices.session.util.SpringSessionBeansDef;
import com.acentic.cloudservices.user.SystemUser.beans.SystemUserBean;
import com.acentic.cloudservices.user.SystemUser.beans.ser_SystemUser;
import com.acentic.cloudservices.user.SystemUserGroup.beans.SystemUserGroupBean;
import com.acentic.cloudservices.user.SystemUserMessage.beans.SystemUserMessageBean;
import com.acentic.cloudservices.user.util.SpringUserBeansDef;
import com.acentic.cloudservices.util.EMailUtil;
import com.acentic.cloudservices.util.JsonStatus;
import com.acentic.cloudservices.util.PasswordGeneration;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.I18nInterceptor;
import org.apache.struts2.interceptor.SessionAware;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class PublicActions extends ActionSupport implements SessionAware, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private userSession userSessionBean;
    
    private static final Logger LOGGER = Logger.getLogger(PublicActions.class);
    private static final String LOGOUT_FROM_CAS = "LOGOUT_FROM_CAS";
    private static final String SHOW_GPNS_DASHBOARD = "gpns_dashboard";
    
    private Map<String, Object> session;
    private SessionDAOInterface daoInterface;
    private String locale;
    private String casServer;

    private String username;
    private String password;
    private String retypedPassword;
    private boolean loginFailed;

    private String email;
    private JsonStatus jsonStatus;
    private int defaultResendPWLinkSize;
    private EMailSender emailSender;
    private String link;
    private ser_SystemUser userProfile;
    private int defaultLoginNameSize;
    private int defaultPasswordSize;
    private String redirNamespace;
    private String redirAction;
    
    private long id;
    private String logoutHeadline;
    private String logoutReLogin;
    
    private SERLoggedInUser serLoggedInUser;
    
    
    // THIS IS JUST USED FOR ACS OLD !!! CAN BE REMOVED IF ACS NEW IS RUNNING
    public String doGetLoggedInUserInformation() {
        serLoggedInUser = new SERLoggedInUser();
        serLoggedInUser.setHotelaccount(true);
        
        String locale = ActionContext.getContext().getLocale().toString().toUpperCase();
        serLoggedInUser.setLocale(locale);
        String url = ServletActionContext.getRequest().getContextPath() + "/images/flags_short/";
        serLoggedInUser.getAvailable_languages().add(new SERAvailableLanguage("EN_GB", "EN", url + "gb.png", locale.equalsIgnoreCase("EN_GB")));
        serLoggedInUser.getAvailable_languages().add(new SERAvailableLanguage("EN_US", "EN/US", url + "us.png", locale.equalsIgnoreCase("EN_US")));
        serLoggedInUser.getAvailable_languages().add(new SERAvailableLanguage("DE", "DE", url + "de.png", locale.startsWith("DE")));

        if ((userSessionBean == null) || (userSessionBean.getLoggedInUser() == null) || (!userSessionBean.getLoggedInUser().isLoaded())) {
            return SUCCESS;
        }

        serLoggedInUser.setHotelaccount(userSessionBean.getLoggedInUser().isHotelAccount());
        serLoggedInUser.setSuperadmin(userSessionBean.getLoggedInUser().isSuperAdmin());
        serLoggedInUser.setServicepartner(userSessionBean.getLoggedInUser().isServicepartner());
        
        serLoggedInUser.setLoggedIn(true);
        serLoggedInUser.setDisplayName(userSessionBean.getLoggedInUser().getDisplayName());
        serLoggedInUser.setFirstName(userSessionBean.getLoggedInUser().getFirstName());
        serLoggedInUser.setLastName(userSessionBean.getLoggedInUser().getLastName());
        serLoggedInUser.setTempUser(userSessionBean.getLoggedInUser().isTempUser());

        serLoggedInUser.setOpenMessagesCount(userSessionBean.getLoggedInUser().getMessages().getItems().size());
        for (SystemUserMessageBean msg : userSessionBean.getLoggedInUser().getMessages().getItems()) {
            SERUserMessage entryMsg = new SERUserMessage(msg);
            serLoggedInUser.getOpenMessages().add(entryMsg);
        }

        serLoggedInUser.setCanChangeHotel(true);
        if ((userSessionBean.getLoggedInUser().getAssignedHotels().getItems().size() == 1) && (!userSessionBean.getLoggedInUser().hasRole(SystemRoleDef.ROLE_SYSTEM_SEE_ALL_IDENTS))) {
            serLoggedInUser.setCanChangeHotel(false);
        }

        if (userSessionBean.getSelectedHotel() != null) {
            serLoggedInUser.setSelectedHotel(new SERHotel(userSessionBean.getSelectedHotel()));
        }

        doTranslateMenu(userSessionBean.getSerMenu());
        serLoggedInUser.setSelectedMenu(userSessionBean.getSelectedMenu());
        serLoggedInUser.setDefaultMenu(null);
        if (userSessionBean.getSerMenu() != null) {
            for (SERSystemMenu m : userSessionBean.getSerMenu().getSubMenues()) {
                if (m.isDefaultMenu()) {
                    serLoggedInUser.setDefaultMenu(m.getSubMenues());
                    break;
                }
            }
        }

        return SUCCESS;
    }

    private void doTranslateMenu(SERSystemMenu defaultMenu) {
        defaultMenu.setText(this.getText(defaultMenu.getTextKey()));
        for (SERSystemMenu x : defaultMenu.getSubMenues()) {
            doTranslateMenu(x);
        }

    }
    
    
    public String doSwitchSystemUser() {
        LOGGER.log(Level.TRACE, "switching user");

        SystemUserBean systemUser = null;
        try {
            userSession loggedInUser = (userSession) applicationContext.getBean(SpringSessionBeansDef.USER_SESSION_BEAN);
            systemUser = (SystemUserBean) applicationContext.getBean(SpringUserBeansDef.SystemUserBean, id);
            if (!systemUser.isLoaded()) {
                LOGGER.log(Level.ERROR, "user not found username / password");
                loginFailed = true;
                return ERROR;
            }
            
            if (!loggedInUser.canSeeUser(systemUser)) {
                LOGGER.log(Level.ERROR, "user not found username / password");
                loginFailed = true;
                return ERROR;
            }
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, "unable to login user", e);
            internalDoLogout();
            return ERROR;
        }

        if (systemUser.isTempUser()) {
            boolean confValid = false;
            for (int i = 0; i < systemUser.getentSystemUser().getConferences().size(); i++) {
                if ((!systemUser.getentSystemUser().getConferences().get(i).isDeleted())
                        && (systemUser.getentSystemUser().getConferences().get(i).getEndDate().after(new Date()))) {
                    confValid = true;
                    break;
                }
            }
            if (!confValid) {
                LOGGER.log(Level.ERROR, "unable to login  temp user, conference not valid anymore");
                internalDoLogout();
                return ERROR;
            }
        }

        if ((locale != null) && (locale.isEmpty())) {
            LOGGER.log(Level.DEBUG, "locale parameter detected, setting locale to " + locale);
            Locale newlocale = localefromString(locale);
            ActionContext.getContext().setLocale(newlocale);
            HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            HttpSession session = request.getSession(true);
            session.setAttribute(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE, newlocale);
        }

        internalDoLogin(systemUser);
        return SUCCESS;
    }
    
    
    public String doSetLocale() {
        userSession loggedInUser = (userSession) applicationContext.getBean(SpringSessionBeansDef.USER_SESSION_BEAN);
        if (loggedInUser == null) 
            return SUCCESS;
        
        loggedInUser.resetMenu();
        redirNamespace="/session";
        redirAction = "doShowWelcome";
        if ((loggedInUser.getLastAction() != null) && (loggedInUser.getLastNameSpace() != null)) {
           redirNamespace="/" + loggedInUser.getLastNameSpace();
           redirAction = loggedInUser.getLastAction();
        }
        return SUCCESS;
    }
    
    public String setUserData() {
        jsonStatus = new JsonStatus();
        SystemUserBean user = null;
        try {
            userSession loggedInUser = (userSession) applicationContext.getBean(SpringSessionBeansDef.USER_SESSION_BEAN);
            user = (SystemUserBean) applicationContext.getBean(SpringUserBeansDef.SystemUserBean, loggedInUser.getId());
            if (!user.isLoaded()) {
                jsonStatus.setStatus(JsonStatus.JSON_ERROR);
                jsonStatus.setErrorMsg("no user logged in");
                LOGGER.log(Level.ERROR, "login try with invalid link detected");
                return SUCCESS;
            }
        } catch (Exception e) {
            jsonStatus.setStatus(JsonStatus.JSON_ERROR);
            jsonStatus.setErrorMsg(this.getText("session.profile.changed"));
            LOGGER.log(Level.ERROR, "unable to serialize user object", e);
            return SUCCESS;
        }

        if (userProfile == null) {
            jsonStatus.setStatus(JsonStatus.JSON_ERROR);
            jsonStatus.setErrorMsg("no user profile given");
            return SUCCESS;
        }
        if (StringUtils.isEmpty(userProfile.getFirstName())) {
            jsonStatus.setStatus(JsonStatus.JSON_ERROR);
            jsonStatus.setErrorMsg(this.getText("session.profile.nofirstname"));
            return SUCCESS;
        }

        if (StringUtils.isEmpty(userProfile.getLastName())) {
            jsonStatus.setStatus(JsonStatus.JSON_ERROR);
            jsonStatus.setErrorMsg(this.getText("session.profile.nolastname"));
            return SUCCESS;
        }

        if (StringUtils.isEmpty(userProfile.getEmail())) {
            jsonStatus.setStatus(JsonStatus.JSON_ERROR);
            jsonStatus.setErrorMsg(this.getText("session.profile.noemail"));
            return SUCCESS;
        }

        if (!EMailUtil.isValidEMail(userProfile.getEmail())) {
            jsonStatus.setStatus(JsonStatus.JSON_ERROR);
            jsonStatus.setErrorMsg(this.getText("session.profile.noemail"));
            return SUCCESS;
        }

        if (StringUtils.isEmpty(userProfile.getLoginName())) {
            jsonStatus.setStatus(JsonStatus.JSON_ERROR);
            jsonStatus.setErrorMsg(String.format(this.getText("session.profile.loginname"), defaultLoginNameSize));
            return SUCCESS;
        }

        if ((userProfile.getLoginName().length() < defaultLoginNameSize)) {
            jsonStatus.setStatus(JsonStatus.JSON_ERROR);
            jsonStatus.setErrorMsg(String.format(this.getText("session.profile.loginname"), defaultLoginNameSize));
            return SUCCESS;
        }

        if (StringUtils.isEmpty(password)) {
            jsonStatus.setStatus(JsonStatus.JSON_ERROR);
            jsonStatus.setErrorMsg(this.getText("session.profile.nopassword"));
            return SUCCESS;
        }

        if (StringUtils.isEmpty(retypedPassword)) {
            jsonStatus.setStatus(JsonStatus.JSON_ERROR);
            jsonStatus.setErrorMsg(this.getText("session.profile.noretypedpassword"));
            return SUCCESS;
        }

        if (!password.equals(retypedPassword)) {
            jsonStatus.setStatus(JsonStatus.JSON_ERROR);
            jsonStatus.setErrorMsg(this.getText("session.profile.nopasswordmatch"));
            return SUCCESS;
        }

        if ((password.length() < defaultPasswordSize)) {
            jsonStatus.setStatus(JsonStatus.JSON_ERROR);
            jsonStatus.setErrorMsg(String.format(this.getText("session.profile.passwordsize"), defaultLoginNameSize));
            return SUCCESS;
        }

        if (!PasswordGeneration.isValidPassword(password)) {
            jsonStatus.setStatus(JsonStatus.JSON_ERROR);
            jsonStatus.setErrorMsg(this.getText("session.profile.invalidpw"));
            return SUCCESS;
        }

        try {
            user.setRegisterLink(null);
            user.setResendPWLink(null);
            user.setFirstName(userProfile.getFirstName());
            user.setLastName(userProfile.getLastName());
            user.setTelefonMobile(userProfile.getTelefonMobile());
            user.setTelefonWork(userProfile.getTelefonWork());
            user.setEmail(userProfile.getEmail());
            user.setLoginName(userProfile.getLoginName());
            user.update();
            user.updatePassword(password);
        } catch (Exception e) {
            jsonStatus.setStatus(JsonStatus.JSON_ERROR);
            jsonStatus.setErrorMsg("unable to update user");
            LOGGER.log(Level.ERROR, "unable to update user", e);
            return SUCCESS;
        }

        jsonStatus.setErrorMsg(this.getText("session.profile.changed"));
        return SUCCESS;
    }

    public String getUserData() {
        jsonStatus = new JsonStatus();
        try {
            userSession loggedInUser = (userSession) applicationContext.getBean(SpringSessionBeansDef.USER_SESSION_BEAN);
            userProfile = new ser_SystemUser(loggedInUser.getLoggedInUser(), false, false, Boolean.FALSE);
        } catch (Exception e) {
            jsonStatus.setStatus(JsonStatus.JSON_ERROR);
            jsonStatus.setErrorMsg("unable to serialize user object");
            LOGGER.log(Level.ERROR, "unable to serialize user object", e);
        }
        return SUCCESS;
    }

    // calls if users enters his email and tries to login with link
    public String doUserLoginUnregistered() {
        loginFailed = false;
        SystemUserBean user = (SystemUserBean) applicationContext.getBean(SpringUserBeansDef.SystemUserBean);
        if ((!user.IntializeByRegisterLink(link)) || (!user.isLoaded())) {
            loginFailed = true;
            LOGGER.log(Level.ERROR, "login try with invalid link detected");
            return ERROR;
        }

        if (!user.getEmail().equalsIgnoreCase(email.trim())) {
            loginFailed = true;
            return INPUT;
        }

        internalDoLogin(user);
        return SUCCESS;

    }

    // unregistered users tries login with link and email
    // called for new users
    // and if user has reseted his account
    public String doUserTryLoginUnregistred() {
        SystemUserBean user = (SystemUserBean) applicationContext.getBean(SpringUserBeansDef.SystemUserBean);
        if ((!user.IntializeByRegisterLink(link)) || (!user.isLoaded())) {
            return ERROR;
        }

        return SUCCESS;

    }

    // get called from email if user clicks on reset link
    public String doUserResetPasswordByEMail() {
        LOGGER.log(Level.TRACE, "user tries to reset password");

        SystemUserBean user = (SystemUserBean) applicationContext.getBean(SpringUserBeansDef.SystemUserBean);
        if ((!user.IntializeByResendPWLink(link)) || (!user.isLoaded())) {
            return ERROR;
        }

        if (!user.resetUserForAuthenthication(defaultResendPWLinkSize)) {
            LOGGER.log(Level.ERROR, "unable to reset user with link");
            return ERROR;
        }

        if (!emailSender.sendReauthLink(user.getRegisterLink(), user.getEmail())) {
            return ERROR;
        }
        return SUCCESS;
    }

    public String doTryResetPassword() {
        jsonStatus = new JsonStatus();
        if (StringUtils.isEmpty(email)) {
            jsonStatus.setStatus(JsonStatus.JSON_ERROR);
            jsonStatus.setErrorMsg(this.getText("session.resendpw.noemail"));
            return SUCCESS;
        }

        SystemUserBean systemUser = null;
        try {

            systemUser = (SystemUserBean) applicationContext.getBean(SpringUserBeansDef.SystemUserBean);
            if (!systemUser.IntializeByEMail(email)) {
                jsonStatus.setStatus(JsonStatus.JSON_ERROR);
                jsonStatus.setErrorMsg(this.getText("session.resendpw.invalidemail"));
                return SUCCESS;
            }

            if (!systemUser.resetPasswordForSystemUser(defaultResendPWLinkSize)) {
                jsonStatus.setStatus(JsonStatus.JSON_ERROR);
                jsonStatus.setErrorMsg("Unable to store user ! could not create no passowrd link");
                return SUCCESS;
            }

            if (!emailSender.sendResendPWLink(systemUser.getResendPWLink(), systemUser.getEmail())) {
                jsonStatus.setStatus(JsonStatus.JSON_ERROR);
                jsonStatus.setErrorMsg("Unable to send reauthentication link to user");
                return SUCCESS;
            }

        } catch (Exception e) {
            LOGGER.log(Level.ERROR, "unable to reset password for user", e);
            internalDoLogout();
            return ERROR;
        }

        jsonStatus.setErrorMsg(this.getText("session.resendpw.reseted"));
        return SUCCESS;
    }

    public String doUserWantResetPassword() {
        LOGGER.log(Level.TRACE, "user wants to reset password");
        return SUCCESS;
    }
    
    public String accessPasswordResetConfirm() {
        LOGGER.log(Level.TRACE, "show confirmation");
        return SUCCESS;
    }
    

    public String doShowForbidden() {
        LOGGER.log(Level.TRACE, "do access forbidden page");
        return SUCCESS;
    }

    public String doAccessLoginUser() {
        LOGGER.log(Level.TRACE, "do access login user");
        return SUCCESS;
    }

    public String doLoginUser() {
        LOGGER.log(Level.TRACE, "do login cas user");
        loginFailed = false;

        if ((StringUtils.isEmpty(username)) || (StringUtils.isEmpty(password))) {
            LOGGER.log(Level.ERROR, "username or password not given");
            loginFailed = true;
            return INPUT;
        }

        SystemUserBean systemUser = null;
        try {

            systemUser = (SystemUserBean) applicationContext.getBean(SpringUserBeansDef.SystemUserBean);
            if (!systemUser.initializeByUser(username, password)) {
                LOGGER.log(Level.ERROR, "user not found username / password");
                loginFailed = true;
                return INPUT;
            }

        } catch (Exception e) {
            LOGGER.log(Level.ERROR, "unable to login user", e);
            internalDoLogout();
            return ERROR;
        }

        if (systemUser.isTempUser()) {
            boolean confValid = false;
            for (int i = 0; i < systemUser.getentSystemUser().getConferences().size(); i++) {
                if ((!systemUser.getentSystemUser().getConferences().get(i).isDeleted())
                        && (systemUser.getentSystemUser().getConferences().get(i).getEndDate().after(new Date()))) {
                    confValid = true;
                    break;
                }
            }
            if (!confValid) {
                LOGGER.log(Level.ERROR, "unable to login  temp user, conference not valid anymore");
                internalDoLogout();
                return ERROR;
            }
        }

        if ((locale != null) && (locale.isEmpty())) {
            LOGGER.log(Level.DEBUG, "locale parameter detected, setting locale to " + locale);
            Locale newlocale = localefromString(locale);
            ActionContext.getContext().setLocale(newlocale);
            HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            HttpSession session = request.getSession(true);
            session.setAttribute(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE, newlocale);
        }

        internalDoLogin(systemUser);
        
        if (systemUser.hasRole(SystemRoleDef.ROLE_GPNS_PROPERTY_DASHBOARD)) {
            userSession loggedInUser = (userSession) applicationContext.getBean(SpringSessionBeansDef.USER_SESSION_BEAN);
            if (loggedInUser.getSelectedHotel() != null) {
                if ((loggedInUser.getSelectedHotel().isForeignSystem())
                    || (daoInterface.isGpnsHotel(loggedInUser.getSelectedHotel().getIdent())))
                   return SHOW_GPNS_DASHBOARD;
            }
        }
        
        return SUCCESS;
    }

    public String doLoginCasUser() {
        LOGGER.log(Level.TRACE, "do login cas user");

        SystemUserBean systemUser = null;
        try {
            HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            AttributePrincipal principal = (AttributePrincipal) request.getUserPrincipal();
            @SuppressWarnings("rawtypes")
            Map attributes = principal.getAttributes();
            String uid = (String) attributes.get("uid");
            Long lUid = Long.valueOf(uid);
            systemUser = (SystemUserBean) applicationContext.getBean(SpringUserBeansDef.SystemUserBean, lUid);
            if (!systemUser.isLoaded()) {
                LOGGER.log(Level.ERROR, "unable to login cas user with id " + lUid);
                internalDoLogout();
                return ERROR;
            }

            LOGGER.log(Level.DEBUG, "request with local=" + request.getLocale().toString());
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, "unable to login cas user", e);
            internalDoLogout();
            return ERROR;
        }

        if (systemUser.isTempUser()) {
            boolean confValid = false;
            for (int i = 0; i < systemUser.getentSystemUser().getConferences().size(); i++) {
                if ((!systemUser.getentSystemUser().getConferences().get(i).isDeleted())
                        && (systemUser.getentSystemUser().getConferences().get(i).getEndDate().after(new Date()))) {
                    confValid = true;
                    break;
                }
            }
            if (!confValid) {
                LOGGER.log(Level.ERROR, "unable to login  temp user, conference not valid anymore");
                internalDoLogout();
                return ERROR;
            }
        }

        if ((locale != null) && (locale.isEmpty())) {
            LOGGER.log(Level.DEBUG, "locale parameter detected, setting locale to " + locale);
            Locale newlocale = localefromString(locale);
            ActionContext.getContext().setLocale(newlocale);
            HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            HttpSession session = request.getSession(true);
            session.setAttribute(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE, newlocale);
        }

        internalDoLogin(systemUser);

        if (systemUser.hasRole(SystemRoleDef.ROLE_GPNS_PROPERTY_DASHBOARD)) {
            userSession loggedInUser = (userSession) applicationContext.getBean(SpringSessionBeansDef.USER_SESSION_BEAN);
            if (loggedInUser.getSelectedHotel() != null) {
                if ((loggedInUser.getSelectedHotel().isForeignSystem())
                    || (daoInterface.isGpnsHotel(loggedInUser.getSelectedHotel().getIdent())))
                   return SHOW_GPNS_DASHBOARD;
            }
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
        internalDoLogout();

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

    private void internalDoLogout() {
        userSession loggedInUser = (userSession) applicationContext.getBean(SpringSessionBeansDef.USER_SESSION_BEAN);
        if (loggedInUser.getId() != 0) {
            daoInterface.unlockCMProjects(loggedInUser.getId());
            daoInterface.unlockEdgeProjects(loggedInUser.getId());
            daoInterface.unlockConfProjects(loggedInUser.getId());
        }

        loggedInUser.setSelectedHotel(null);
        loggedInUser.setLoggedInUser(null);

        HttpServletRequest request = ServletActionContext.getRequest();
        HttpSession sessionTmp = request.getSession(true);
        sessionTmp.invalidate();
    }

    private void internalDoLogin(SystemUserBean systemUser) {

        HttpServletRequest request = ServletActionContext.getRequest();
        HttpSession sessiontmp = request.getSession(true);
        systemUser.getentSystemUser().setLastWebAction(new Date());
        systemUser.getentSystemUser().setSessionId(sessiontmp.getId());
        systemUser.getentSystemUser().setLastlogin(new Date());
        systemUser.getAssignedHotels().getItems().size();
        systemUser.getAssignedRoles().getItems().size();
        for (SystemUserGroupBean group : systemUser.getAssignedGroups().getItems()) {
            group.getAssignedRoles().getItems().size();
        }
        systemUser.getMessages().getItems().size();

        userSession loggedInUser = (userSession) applicationContext.getBean(SpringSessionBeansDef.USER_SESSION_BEAN);
        loggedInUser.setLoggedInUser(systemUser);

        if ((systemUser.getLastIdent() != null) && (!systemUser.getLastIdent().isEmpty()) && (loggedInUser.canSeeIdent(systemUser.getLastIdent()))) {
            String hotelIdent = systemUser.getLastIdent();
            SystemHotelBean hotel = (SystemHotelBean) applicationContext.getBean(SpringHotelBeansDef.SystemHotelBean, hotelIdent);
            hotel.getCMLicences().getItems().size();
            hotel.getLicences().getItems().size();
            if (hotel.getHotelbrand() != null)
               hotel.getHotelbrand().getId();
            loggedInUser.setSelectedHotel(hotel);
        }

    }

    public String getCasServer() {
        return casServer;
    }

    public void setCasServer(String casServer) {
        this.casServer = casServer;
    }

    public SessionDAOInterface getDaoInterface() {
        return daoInterface;
    }

    public void setDaoInterface(SessionDAOInterface daoInterface) {
        this.daoInterface = daoInterface;
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

    public int getDefaultResendPWLinkSize() {
        return defaultResendPWLinkSize;
    }

    public void setDefaultResendPWLinkSize(int defaultResendPWLinkSize) {
        this.defaultResendPWLinkSize = defaultResendPWLinkSize;
    }

    public EMailSender getEmailSender() {
        return emailSender;
    }

    public void setEmailSender(EMailSender emailSender) {
        this.emailSender = emailSender;
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

    public int getDefaultLoginNameSize() {
        return defaultLoginNameSize;
    }

    public void setDefaultLoginNameSize(int defaultLoginNameSize) {
        this.defaultLoginNameSize = defaultLoginNameSize;
    }

    public String getRetypedPassword() {
        return retypedPassword;
    }

    public void setRetypedPassword(String retypedPassword) {
        this.retypedPassword = retypedPassword;
    }

    public int getDefaultPasswordSize() {
        return defaultPasswordSize;
    }

    public void setDefaultPasswordSize(int defaultPasswordSize) {
        this.defaultPasswordSize = defaultPasswordSize;
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

    public SERLoggedInUser getSerLoggedInUser() {
        return serLoggedInUser;
    }

    public userSession getUserSessionBean() {
        return userSessionBean;
    }

    public void setUserSessionBean(userSession userSessionBean) {
        this.userSessionBean = userSessionBean;
    }

    
}
