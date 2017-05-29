package com.acentic.cloudservices.session.interceptors;

import com.acentic.cloudservices.base.SystemMenu.beans.SERSystemMenu;
import com.acentic.cloudservices.session.user.sessionUserOverview;
import com.acentic.cloudservices.session.user.userSession;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class MenuInterceptor implements Interceptor {

    /**
     *
     */
    @Autowired
    private userSession userSessionBean;
    private sessionUserOverview sessionList;

    private static final long serialVersionUID = 1L;
    protected static final Logger logger = Logger.getLogger(MenuInterceptor.class);

    @Override
    public void destroy() {
        logger.log(Level.DEBUG, "MenuInterceptor stopped");
    }

    @Override
    public void init() {
        logger.log(Level.DEBUG, "MenuInterceptor started");
    }

    @Override
    public String intercept(ActionInvocation arg0) throws Exception {
        logger.log(Level.DEBUG, "MenuInterceptor context: " + ActionContext.getContext().getName());

        if (userSessionBean == null) {
            logger.log(Level.DEBUG, "MenuInterceptor no user injected, passing through");
            return arg0.invoke();
        }

        if (userSessionBean.getLoggedInUser() == null) {
            logger.log(Level.DEBUG, "MenuInterceptor no user given, passing through");
            return arg0.invoke();
        }

        if ((arg0.getProxy().getNamespace() == null) || (arg0.getProxy().getNamespace().isEmpty()) || (arg0.getProxy().getNamespace().equals("/"))) {
            logger.log(Level.DEBUG, "MenuInterceptor no namespace given passing through");
            return arg0.invoke();
        }

        if ((arg0.getProxy().getActionName() == null) || (arg0.getProxy().getActionName().isEmpty())) {
            logger.log(Level.DEBUG, "MenuInterceptor no action given passing through");
            return arg0.invoke();
        }

        logger.log(Level.INFO, "user " + userSessionBean.getDisplayName() + " last action " + userSessionBean.getLastNameSpace() + "/" + userSessionBean.getLastAction());
        SERSystemMenu menu = findMenuEntry(userSessionBean.getSerMenu(), arg0.getProxy().getNamespace(), arg0.getProxy().getActionName());
        if (menu != null) {
            userSessionBean.setLastNameSpace(arg0.getProxy().getNamespace());
            userSessionBean.setLastAction(arg0.getProxy().getActionName());
            if (sessionList != null) {
                sessionList.updateMenu(userSessionBean, menu.getTextKey());
            }
            logger.log(Level.INFO, "user " + userSessionBean.getDisplayName() + " accessing " + arg0.getProxy().getNamespace() + "/" + arg0.getProxy().getActionName());
        }
        if (sessionList != null) {
            sessionList.updateLastAction(userSessionBean);
        }

        return arg0.invoke();
    }

    private SERSystemMenu findMenuEntry(SERSystemMenu menuitem, String nameSpace, String actionName) {
        if ((menuitem.getAction() != null) && (menuitem.getNamespace() != null)) {
            if ((menuitem.getAction().equalsIgnoreCase(actionName)) && (("/" + menuitem.getNamespace()).equalsIgnoreCase(nameSpace))) {
                return menuitem;
            }
        }
        if (menuitem.getSubMenues() != null) {
            for (SERSystemMenu m : menuitem.getSubMenues()) {
                SERSystemMenu r = findMenuEntry(m, nameSpace, actionName);
                if (r != null) {
                    return r;
                }
            }
        }

        return null;
    }

    private SERSystemMenu getMainMenu(SERSystemMenu mainMenu, SERSystemMenu menuItem) {
        for (SERSystemMenu m : mainMenu.getSubMenues()) {
            if (menuContains(m, menuItem)) {
                return m;
            }
        }
        return null;
    }

    private boolean menuContains(SERSystemMenu mainMenu, SERSystemMenu menuItem) {
        if (mainMenu.getId().longValue() == menuItem.getId().longValue()) {
            return true;
        }

        for (SERSystemMenu m : mainMenu.getSubMenues()) {
            if (menuContains(m, menuItem)) {
                return true;
            }
        }
        return false;
    }

    public userSession getUserSessionBean() {
        return userSessionBean;
    }

    public void setUserSessionBean(userSession userSessionBean) {
        this.userSessionBean = userSessionBean;
    }

    public sessionUserOverview getSessionList() {
        return sessionList;
    }

    public void setSessionList(sessionUserOverview sessionList) {
        this.sessionList = sessionList;
    }

}
