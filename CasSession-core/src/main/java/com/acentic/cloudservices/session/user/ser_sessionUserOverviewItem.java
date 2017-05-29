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
public class ser_sessionUserOverviewItem {
    
    private String userName;
    private String ident;
    private String hotelname;
    private Date lastAction;
    private String lastMenu;

    
    public ser_sessionUserOverviewItem(sessionUserOverviewItem item) {
        this.userName = item.getUserName();
        this.ident = item.getIdent();
        this.hotelname = item.getHotelname();
        this.lastAction = item.getLastAction();
        this.lastMenu = item.getLastMenu();
                
    }
    
    
    public ser_sessionUserOverviewItem() {
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
