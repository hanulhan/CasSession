/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acentic.cloudservices.session.menu;

import com.acentic.acs.database.enums.ContentManagerFlash.CMLicenceTyp;
import com.acentic.cloudservices.base.SystemMenu.beans.SystemMenuItemBean;
import com.acentic.cloudservices.base.SystemMenu.beans.SystemMenuListInterface;
import com.acentic.cloudservices.interfaces.hotel.SystemHotelInterface;
import com.acentic.cloudservices.interfaces.loggedInUser.LoggedInUserInterface;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 * @author Ostrowski
 */
public class SystemMenuListManual implements ApplicationContextAware, SystemMenuListInterface {

    private ApplicationContext applicationContext;
    private ArrayList<SystemMenuItemBean> items;

    public SystemMenuListManual() {
        super();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public ArrayList<SystemMenuItemBean> getItems() {
        if (this.items == null) {
            items = new ArrayList<>();
        }
        return items;
    }

    @Override
    public void cleanUpForUserAndHotel(LoggedInUserInterface user, SystemHotelInterface hotel) {

        int i = 0;
        while (i < this.getItems().size()) {
            if (this.getItems().get(i).userHasRights(user, hotel)) {
                this.getItems().get(i).getSubmenus().cleanUpForUserAndHotel(user, hotel);
                i++;
            } else {
                this.getItems().remove(i);
            }
        }
        
    }
    
    @Override
    public int getTotalCount() {
        int i = 0;
        for (SystemMenuItemBean m : this.getItems()) {
            i = i + m.getSubmenus().getTotalCount();
        }
        return i;
    }
    
    
}
