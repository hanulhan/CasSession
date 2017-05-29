/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acentic.cloudservices.session.user;

import java.util.Date;

/**
 *
 * @author Ostrowski
 */
public class sessionUserOverviewItem {
    
    private String sessionId;
    private String userName;
    private String ident;
    private String hotelname;
    private Date lastAction;
    private String lastMenu;
    
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getHotelname() {
        return hotelname;
    }

    public void setHotelname(String hotelname) {
        this.hotelname = hotelname;
    }

    public Date getLastAction() {
        return lastAction;
    }

    public void setLastAction(Date lastAction) {
        this.lastAction = lastAction;
    }

    public String getLastMenu() {
        return lastMenu;
    }

    public void setLastMenu(String lastMenu) {
        this.lastMenu = lastMenu;
    }

    
}
