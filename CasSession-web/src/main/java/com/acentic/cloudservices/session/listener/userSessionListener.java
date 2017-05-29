package com.acentic.cloudservices.session.listener;

import com.acentic.cloudservices.session.user.sessionUserOverview;
import com.acentic.cloudservices.session.util.SpringSessionBeansDef;
import com.acentic.cloudservices.session.util.userSessionSettings;
import com.acentic.cloudservices.util.FileUtils;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.apache.commons.lang3.StringUtils;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class userSessionListener implements HttpSessionListener {

    protected static final Logger LOGGER = Logger.getLogger(userSessionListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent arg0) {
        HttpSession session = arg0.getSession();
        try {
            WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());
            sessionUserOverview userList = (sessionUserOverview) appContext.getBean(SpringSessionBeansDef.USER_SESSION_LIST);
            userSessionSettings settings = (userSessionSettings) appContext.getBean(SpringSessionBeansDef.USER_SESSION_SETTINGS);
            userList.createEntry(session.getId());
            session.setMaxInactiveInterval(settings.getWebTimeOut() * 60);
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "unable to " + session.getId());
        }

        LOGGER.log(Level.INFO, "session created with id " + session.getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent arg0) {
        HttpSession session = arg0.getSession();
        LOGGER.log(Level.INFO, "session destroyed with id " + session.getId());

        try {
            WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());
            sessionUserOverview userList = (sessionUserOverview) appContext.getBean(SpringSessionBeansDef.USER_SESSION_LIST);
            userList.removeBySessionId(session.getId());
            userSessionSettings settings = (userSessionSettings) appContext.getBean(SpringSessionBeansDef.USER_SESSION_SETTINGS);
            if (!StringUtils.isEmpty(settings.getSessionTempPath())) {
                String s = settings.getSessionTempPath()
                        + System.getProperty("file.separator")
                        + session.getId();
                FileUtils.removeDirectoryExt(s);
            }
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "unable to " + session.getId());
        }

        LOGGER.log(Level.INFO, "removing  temp directories for session " + session.getId());

    }


}
