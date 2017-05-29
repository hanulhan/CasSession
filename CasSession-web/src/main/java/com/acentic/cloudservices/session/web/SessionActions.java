/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acentic.cloudservices.session.web;

import com.acentic.cloudservices.util.JsonStatus;
import com.acentic.cloudservices.base.SystemMenu.beans.SERSystemMenu;
import com.acentic.cloudservices.hotel.SystemHotel.beans.SystemHotelBean;
import com.acentic.cloudservices.hotel.SystemHotel.beans.SystemHotelListProxy;
import com.acentic.cloudservices.hotel.SystemHotel.beans.ser_SystemHotel;
import com.acentic.cloudservices.hotel.util.SpringHotelBeansDef;
import com.acentic.cloudservices.session.user.ser_sessionUserOverviewItem;
import com.acentic.cloudservices.session.user.sessionUserOverview;
import com.acentic.cloudservices.session.user.sessionUserOverviewItem;
import com.acentic.cloudservices.session.user.userSession;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SessionActions extends ActionSupport implements ServletRequestAware, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private SystemHotelListProxy hotelListProxy;
    private userSession userSessionBean;
    private static final Logger LOGGER = Logger.getLogger(SessionActions.class);
    private JsonStatus jsonStatus = new JsonStatus();
    private ByteArrayInputStream inputStream;
    private HttpServletRequest request;
    private static final int HTTP_DEF_PORT = 80;
    private static final int HTTPS_DEF_PORT = 443;
    private List<ser_SystemHotel> hotels;
    private String ident;
    private List<String>idents;

    private ser_loggedInUserInformation loggedInUser;
    private List<ser_sessionUserOverviewItem> sessions;
    private sessionUserOverview userSessions;

    
    public String doAccessActiveSessions() {
        LOGGER.log(Level.TRACE, "enter doAccessActiveSessions function");
        return SUCCESS;
    }   
    
    public String doGetSession() {
        jsonStatus = new JsonStatus();
        sessions = new ArrayList<>();
        try {
            for (sessionUserOverviewItem item : userSessions) {
                if (!StringUtils.isEmpty(item.getUserName())) {
                    ser_sessionUserOverviewItem s = new ser_sessionUserOverviewItem(item);
                    s.setLastMenu(this.getText(s.getLastMenu()));
                    sessions.add(s);
                }
            }

        } catch (Exception e) {
            LOGGER.log(Level.TRACE, "error listing active sessions", e);
        }
        return SUCCESS;
    }

    public String doShowWelcome() {
        LOGGER.log(Level.TRACE, "enter doShowWelcome function");
        return SUCCESS;
    }

    public String doGetLoggedInUserInformation() {
        LOGGER.log(Level.TRACE, "enter doGetLoggedInUserInformation function");
        jsonStatus = new JsonStatus();
        if (userSessionBean == null) {
            LOGGER.log(Level.ERROR, "no user logged in ");
            jsonStatus.setStatus(JsonStatus.JSON_ERROR);
            return SUCCESS;
        }

        loggedInUser = new ser_loggedInUserInformation(userSessionBean);
        return SUCCESS;
    }

    public String doSetHotel() {
        LOGGER.log(Level.TRACE, "enter doSetHotelList function");
        if (userSessionBean == null) {
            return ERROR;
        }
        SystemHotelBean b = (SystemHotelBean) applicationContext.getBean(SpringHotelBeansDef.SystemHotelBean, ident);
        if ((b == null) || (!b.isLoaded())) {
            return ERROR;
        }

        if (!userSessionBean.canSeeIdent(ident)) {
            return ERROR;
        }

        try {
            b.getCMLicences().getItems().size();
            b.getLicences().getItems().size();
            b.getGuilanguages().getItems().size();
            if ((b.getHotelbrand() != null) && (b.getHotelbrand().isLoaded())) {
                b.getHotelbrand().getId();
            }
            if ((b.getDefaultLanguage() != null) && (b.getDefaultLanguage().isLoaded())) {
                b.getDefaultLanguage().getId();
            }
            if ((b.getForeignSystem() != null) && (b.getForeignSystem().isLoaded())) {
                b.getForeignSystem().getIdent();
            }
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, "unable to set selected hotel", e);
        }
        userSessionBean.setSelectedHotel(b);

        return SUCCESS;

    }

    public String doShowHotelSelection() {
        LOGGER.log(Level.TRACE, "enter doShowHotelSelection function");
        if (userSessionBean == null) {
            return ERROR;
        }
        return SUCCESS;

    }

    public String doGetHotelList() {
        LOGGER.log(Level.TRACE, "enter doGetHotelList function");
        jsonStatus = new JsonStatus();

        try {
            // SystemHotelList l = (SystemHotelList) applicationContext.getBean(SpringHotelBeansDef.bean_SystemHotelList);
            hotels = new ArrayList<>();
            for (SystemHotelBean bean : hotelListProxy.getItems()) {
                if ((!bean.isInactive()) && (userSessionBean.canSeeIdent(bean.getIdent()))) {
                    try {
                        //LOGGER.log(Level.ERROR, "checking ident " + bean.getIdent());
                        ser_SystemHotel sh = new ser_SystemHotel(bean, true);
                        if ((userSessionBean.getSelectedHotel() != null) && (sh.getIdent().equals(userSessionBean.getSelectedHotel().getIdent()))) {
                            sh.setSelected(true);
                        }
                        hotels.add(sh);
                    } catch (Exception e) {
                        LOGGER.log(Level.ERROR, "unable to read hotellist by idents", e);
                        jsonStatus.setStatus(JsonStatus.JSON_ERROR);
                        return SUCCESS;
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, "unable to read hotellist by idents", e);
            jsonStatus.setStatus(JsonStatus.JSON_ERROR);
            return SUCCESS;
        }

        LOGGER.log(Level.TRACE, "leave doGetHotelList function");
        return SUCCESS;

    }

    public String doGetMenuUL() {
        String path = getBaseUrl(request);

        LOGGER.log(Level.DEBUG, "using path: " + path);
        doTranslateMenu(userSessionBean.getSerMenu());

        LOGGER.log(Level.DEBUG, "getMenuUL using path: " + path);
        LOGGER.log(Level.DEBUG, "lastNameSpace " + userSessionBean.getLastNameSpace());
        LOGGER.log(Level.DEBUG, "lastAction " + userSessionBean.getLastAction());

        try {
            inputStream = new ByteArrayInputStream(userSessionBean.MenuAsUl(path).getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            return null;
        }
        return SUCCESS;
    }

    public String doAccessFilter() {
        return SUCCESS;
    }

    public String doGetSelectedIdents() {
        try {
            idents = userSessionBean.getSelectedIdents();
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, "failed to list selected gpns idents", e);
            this.getJsonStatus().setStatus(JsonStatus.JSON_ERROR);
            this.getJsonStatus().setErrorMsg("failed to get selected idents");
            return SUCCESS;
        }
        return SUCCESS;

    }

    public String doSetSelectedIdents() {
        try {
            userSessionBean.setSelectedIdents(idents);
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, "failed to set selected gpns idents", e);
            this.getJsonStatus().setStatus(JsonStatus.JSON_ERROR);
            this.getJsonStatus().setErrorMsg("failed to get selected idents");
            return SUCCESS;
        }
        return SUCCESS;

    }



    public static String getBaseUrl(HttpServletRequest request) {
        if ((request.getServerPort() == HTTP_DEF_PORT) || (request.getServerPort() == HTTPS_DEF_PORT)) {
            return request.getScheme() + "://" + request.getServerName()
                    + request.getContextPath();
        } else {
            return request.getScheme() + "://" + request.getServerName() + ":"
                    + request.getServerPort() + request.getContextPath();
        }
    }

    public static boolean isNullOrEmpty(String data) {
        return (data == null || data.equals(""));
    }

    private void doTranslateMenu(SERSystemMenu defaultMenu) {
        defaultMenu.setText(this.getText(defaultMenu.getTextKey()));
        for (SERSystemMenu x : defaultMenu.getSubMenues()) {
            doTranslateMenu(x);
        }

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;

    }

    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    public JsonStatus getJsonStatus() {
        return jsonStatus;
    }

    public void setJsonStatus(JsonStatus jsonStatus) {
        this.jsonStatus = jsonStatus;
    }

    public userSession getUserSessionBean() {
        return userSessionBean;
    }

    public void setUserSessionBean(userSession userSessionBean) {
        this.userSessionBean = userSessionBean;
    }

    public ByteArrayInputStream getInputStream() {
        return inputStream;
    }

    public void setHotelListProxy(SystemHotelListProxy hotelListProxy) {
        this.hotelListProxy = hotelListProxy;
    }

    public SystemHotelListProxy getHotelListProxy() {
        return hotelListProxy;
    }

    public List<ser_SystemHotel> getHotels() {
        return hotels;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public ser_loggedInUserInformation getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(ser_loggedInUserInformation loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public List<ser_sessionUserOverviewItem> getSessions() {
        return sessions;
    }

    public sessionUserOverview getUserSessions() {
        return userSessions;
    }

    public void setUserSessions(sessionUserOverview userSessions) {
        this.userSessions = userSessions;
    }

    public List<String> getIdents() {
        return idents;
    }

    public void setIdents(List<String> idents) {
        this.idents = idents;
    }

}
