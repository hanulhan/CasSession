package com.acentic.cloudservices.session.web;

import com.acentic.cloudservices.base.SystemMenu.beans.SERSystemMenu;
import com.acentic.cloudservices.user.SystemUser.beans.SystemUserBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SERLoggedInUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean loggedIn = false;

    private long id;
    private String firstName;
    private String lastName;
    private String displayName;
    private String telefonWork;
    private String telefonMobile;
    private String emailaddress;
    private String loginName;

    private String locale;
    private List<SERAvailableLanguage> available_languages = new ArrayList<SERAvailableLanguage>();
    private List<SERUserMessage> openMessages = new ArrayList<SERUserMessage>();
    private List<SERSystemMenu> defaultMenu = new ArrayList<SERSystemMenu>();

    private int openMessagesCount;
    private SERHotel selectedHotel;
    private boolean canChangeHotel = false;
    private boolean tempUser = true;
    private SERSystemMenu selectedMenu;

    private boolean hotelaccount;
    private boolean superadmin;
    private boolean servicepartner;

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public List<SERAvailableLanguage> getAvailable_languages() {
        return available_languages;
    }

    public void setAvailable_languages(
            List<SERAvailableLanguage> available_languages) {
        this.available_languages = available_languages;
    }

    public int getOpenMessagesCount() {
        return openMessagesCount;
    }

    public void setOpenMessagesCount(int openMessagesCount) {
        this.openMessagesCount = openMessagesCount;
    }

    public List<SERUserMessage> getOpenMessages() {
        return openMessages;
    }

    public void setOpenMessages(List<SERUserMessage> openMessages) {
        this.openMessages = openMessages;
    }

    public SERHotel getSelectedHotel() {
        return selectedHotel;
    }

    public void setSelectedHotel(SERHotel selectedHotel) {
        this.selectedHotel = selectedHotel;
    }

    public boolean isCanChangeHotel() {
        return canChangeHotel;
    }

    public void setCanChangeHotel(boolean canChangeHotel) {
        this.canChangeHotel = canChangeHotel;
    }

    public List<SERSystemMenu> getDefaultMenu() {
        return defaultMenu;
    }

    public void setDefaultMenu(List<SERSystemMenu> defaultMenu) {
        this.defaultMenu = defaultMenu;
    }

    public SERSystemMenu getSelectedMenu() {
        return selectedMenu;
    }

    public void setSelectedMenu(SERSystemMenu selectedMenu) {
        this.selectedMenu = selectedMenu;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void initializeByUserBean(SystemUserBean user) {
        id = user.getId();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        telefonMobile = user.getTelefonMobile();
        telefonWork = user.getTelefonWork();
        emailaddress = user.getEmail();
        loginName = user.getLoginName();

    }

    public String getTelefonWork() {
        return telefonWork;
    }

    public void setTelefonWork(String telefonWork) {
        this.telefonWork = telefonWork;
    }

    public String getTelefonMobile() {
        return telefonMobile;
    }

    public void setTelefonMobile(String telefonMobile) {
        this.telefonMobile = telefonMobile;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @Override
    public String toString() {
        return "SERLoggedInUser [loggedIn=" + loggedIn + ", id=" + id
                + ", firstName=" + firstName + ", lastName=" + lastName
                + ", displayName=" + displayName + ", telefonWork="
                + telefonWork + ", telefonMobile=" + telefonMobile + ", email="
                + emailaddress + ", loginName=" + loginName + ", locale=" + locale
                + "]";
    }

    public String getEmailaddress() {
        return emailaddress;
    }

    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    public boolean isTempUser() {
        return tempUser;
    }

    public void setTempUser(boolean tempUser) {
        this.tempUser = tempUser;
    }

    public boolean isHotelaccount() {
        return hotelaccount;
    }

    public void setHotelaccount(boolean hotelaccount) {
        this.hotelaccount = hotelaccount;
    }

    public boolean isSuperadmin() {
        return superadmin;
    }

    public void setSuperadmin(boolean superadmin) {
        this.superadmin = superadmin;
    }

    public boolean isServicepartner() {
        return servicepartner;
    }

    public void setServicepartner(boolean servicepartner) {
        this.servicepartner = servicepartner;
    }
    
    

}
