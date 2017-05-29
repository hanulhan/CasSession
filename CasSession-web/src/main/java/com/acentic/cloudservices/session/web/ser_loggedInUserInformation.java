package com.acentic.cloudservices.session.web;

import com.acentic.cloudservices.hotel.SystemHotel.beans.ser_SystemHotel;
import com.acentic.cloudservices.session.user.userSession;

public class ser_loggedInUserInformation {

    private long id;
    private String displayName;
    
    // selectedHotel
    private ser_SystemHotel selectedHotel;
    private boolean hotelaccount;
    private boolean superadmin;
    private boolean servicepartner;

    public ser_loggedInUserInformation() {
        super();
    }

    public ser_loggedInUserInformation(userSession user) {
        this.id = user.getId();
        this.displayName = user.getDisplayName();
        hotelaccount = user.getLoggedInUser().isHotelAccount();
        superadmin = user.getLoggedInUser().isSuperAdmin();
        servicepartner = user.getLoggedInUser().isServicepartner();
        
        if ((user.getSelectedHotel() != null) && (user.getSelectedHotel().isLoaded()))
           selectedHotel = new  ser_SystemHotel(user.getSelectedHotel(), true);
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


    public ser_SystemHotel getSelectedHotel() {
        return selectedHotel;
    }

    public void setSelectedHotel(ser_SystemHotel selectedHotel) {
        this.selectedHotel = selectedHotel;
    }

    public boolean isHotelaccount() {
        return hotelaccount;
    }

    public boolean isSuperadmin() {
        return superadmin;
    }

    public boolean isServicepartner() {
        return servicepartner;
    }
    
    
}
