/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acentic.cloudservices.session.user;

import com.acentic.acs.database.common.entitySystemUser;
import com.acentic.cloudservices.hotel.SystemHotel.beans.SystemHotelBean;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Ostrowski
 */
public class sessionUserOverview extends ArrayList<sessionUserOverviewItem> {

    public sessionUserOverviewItem findById(String sessionId) {
        for (sessionUserOverviewItem el : this) {
            if (el.getSessionId().equalsIgnoreCase(sessionId)) {
                return el;
            }
        }
        return null;
    }

    public void createEntry(String sessionId) {
        sessionUserOverviewItem s = findById(sessionId);
        if (s == null) {
            s = new sessionUserOverviewItem();
            s.setSessionId(sessionId);
            this.add(s);
        }
    }

    public boolean removeBySessionId(String sessionId) {
        sessionUserOverviewItem s = findById(sessionId);
        if (s != null) {
            this.remove(s);
        }
        return false;
    }

    void updateUserData(entitySystemUser user) {
        if (user != null) {
            sessionUserOverviewItem s = findById(user.getSessionId());
            if (s != null) {
                s.setUserName(user.getDisplayName());
            }
        }
    }

    void updateHotelData(String sessionId, SystemHotelBean selectedHotel) {
        sessionUserOverviewItem s = findById(sessionId);
        if (s != null) {
            if ((selectedHotel != null) && (selectedHotel.isLoaded())) {
                 s.setIdent(selectedHotel.getIdent());
                 s.setHotelname(selectedHotel.getHotelName());
            }
            else {
                 s.setIdent("");
                 s.setHotelname("");
            }
        }
    }

    public void updateMenu(userSession userSessionBean, String menu) {
        sessionUserOverviewItem s = findById(userSessionBean.getLoggedInUser().getentSystemUser().getSessionId());
        if (s != null) {
            s.setLastMenu(menu);
        }
    }

    public void updateLastAction(userSession userSessionBean) {
        sessionUserOverviewItem s = findById(userSessionBean.getLoggedInUser().getentSystemUser().getSessionId());
        if (s != null) {
            s.setLastAction(new Date());
        }
    }
}
