/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acentic.cloudservices.session.user;

import com.acentic.cloudservices.base.SystemMenu.beans.SERSystemMenu;
import com.acentic.cloudservices.base.SystemMenu.beans.SystemMenuListInterface;
import com.acentic.cloudservices.hotel.SystemHotel.beans.SystemHotelBean;
import com.acentic.cloudservices.hotel.util.SpringHotelBeansDef;
import com.acentic.cloudservices.interfaces.hotel.SystemHotelInterface;
import com.acentic.cloudservices.interfaces.loggedInUser.LoggedInUserInterface;
import com.acentic.cloudservices.session.util.SpringSessionBeansDef;
import com.acentic.cloudservices.user.SystemUser.beans.SystemUserBean;
import com.acentic.cloudservices.user.util.SpringUserBeansDef;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 * @author Ostrowski
 */
public class userSession implements LoggedInUserInterface, ApplicationContextAware {

    private static final Logger LOGGER = Logger.getLogger(userSession.class);
    private ApplicationContext applicationContext;
    private SystemUserBean loggedInUser;
    private SystemHotelBean selectedHotel;
    private SERSystemMenu serMenu;
    // for old menu
    private SERSystemMenu selectedMenu;

    private long userId;
    private String hotelIdent;
    private List<String> gpnsIdents;
    private List<String> userIdents;
    private String lastNameSpace;
    private String lastAction;

    // conference
    private long selectedConferenceProject;
    private long selectedConference;
    private boolean selectForConfWelcomeEditor = false;

    // contentmanager
    private long selectedCMProjectId;

    public userSession() {

    }

    public userSession(long userId, String hotelIdent) {
        this.userId = userId;
        this.hotelIdent = hotelIdent;
        if ((hotelIdent != null) && (!hotelIdent.isEmpty())) {
            gpnsIdents = new ArrayList<>();
            if (selectedHotel.isHSIASystem())
                gpnsIdents.add(hotelIdent);
        }
    }

    @PostConstruct
    public void initBean() {
        if (userId != 0) {
            loggedInUser = (SystemUserBean) applicationContext.getBean(SpringUserBeansDef.SystemUserBean, userId);
            if ((loggedInUser.getLastIdent() != null) && (!loggedInUser.getLastIdent().isEmpty()) && (loggedInUser.canSeeIdent(loggedInUser.getLastIdent()))) {
                hotelIdent = loggedInUser.getLastIdent();
            }
        }
        if (hotelIdent != null) {
            selectedHotel = (SystemHotelBean) applicationContext.getBean(SpringHotelBeansDef.SystemHotelBean, hotelIdent);
        }
    }

    public boolean isHotelSelected() {
        return ((this.getSelectedHotel() != null) && (this.getSelectedHotel().isLoaded()));

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

    public String MenuAsUl(String path) {
        SERSystemMenu menu = findMenuEntry(getSerMenu(), lastNameSpace, lastAction);
        if (menu != null) {
            getSerMenu().setAllInactive();
            menu.setActive(true);
            SERSystemMenu parent = menu;
            do {
                parent.setActive(true);
                parent = parent.getMain();
            } while (parent != null);
        }

        try {
            return getSerMenu().AsUL(0, path);
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, "unable to create menu list for user ", e);
            return null;
        }
    }

    public SERSystemMenu getSerMenu() {
        if ((serMenu == null) && (loggedInUser != null)) {
            LOGGER.log(Level.DEBUG, " loading new menu for user : " + loggedInUser.getDisplayName());
            SystemMenuListInterface systemMenu = (SystemMenuListInterface) applicationContext.getBean(SpringSessionBeansDef.SYSTEM_MENU_LIST_FOR_USER_BEAN);
            systemMenu.cleanUpForUserAndHotel(loggedInUser, selectedHotel);
            serMenu = new SERSystemMenu(systemMenu);
        }
        return serMenu;
    }

    public SystemHotelBean getSelectedHotel() {
        return selectedHotel;
    }

    public void setSelectedHotel(SystemHotelBean selectedHotel) {
        try {
            sessionUserOverview userList = (sessionUserOverview) applicationContext.getBean(SpringSessionBeansDef.USER_SESSION_LIST);
            userList.updateHotelData(loggedInUser.getentSystemUser().getSessionId(), selectedHotel);
        } catch (Exception e) {
        }

        if ((selectedHotel != null) && (mustChangeGPNSSelection())) {
            gpnsIdents = new ArrayList<>();
            if (selectedHotel.isHSIASystem())
                gpnsIdents.add(selectedHotel.getIdent());
        }

        this.selectedHotel = selectedHotel;
        this.serMenu = null;

        if (selectedHotel != null) {
            loggedInUser.setLastIdent(selectedHotel.getIdent());
            loggedInUser.update();
        }
    }

    public SystemUserBean getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(SystemUserBean loggedInUser) {
        this.loggedInUser = loggedInUser;
        this.serMenu = null;
        try {
            sessionUserOverview userList = (sessionUserOverview) applicationContext.getBean(SpringSessionBeansDef.USER_SESSION_LIST);
            if (loggedInUser == null) {
                userList.updateUserData(null);
            } else {
                userList.updateUserData(loggedInUser.getentSystemUser());
            }
        } catch (Exception e) {
        }
    }

    public String getDisplayName() {
        if ((loggedInUser != null) && (loggedInUser.isLoaded())) {
            return loggedInUser.getDisplayName();
        }
        return "";
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public long getId() {
        if (loggedInUser != null) {
            return loggedInUser.getId();
        }
        return 0L;
    }

    @Override
    public boolean hasRole(String ARole) {
        if (loggedInUser != null) {
            return loggedInUser.hasRole(ARole);
        }
        return false;
    }

    @Override
    public boolean canSeeUser(LoggedInUserInterface user) {
        if (loggedInUser != null) {
            return loggedInUser.canSeeUser(user);
        }
        return false;
    }

    @Override
    public boolean canSeeIdent(String ident) {
        if (loggedInUser != null) {
            return loggedInUser.canSeeIdent(ident);
        }
        return false;
    }

    @Override
    public String selectedIdent() {
        if (selectedHotel != null) {
            return selectedHotel.getIdent();
        }
        return "";
    }

    @Override
    public SystemHotelInterface selectedHotel() {
        return selectedHotel;
    }

    @Override
    public boolean hasAssetManagerRightsFullHotel(String ident) {
        if (loggedInUser != null) {
            return loggedInUser.hasAssetManagerRightsFullHotel(ident);
        }
        return false;
    }

    @Override
    public boolean hasAssetManagerRightsFullBrand(int brand) {
        if (loggedInUser != null) {
            return loggedInUser.hasAssetManagerRightsFullBrand(brand);
        }
        return false;
    }

    public String getLastNameSpace() {
        return lastNameSpace;
    }

    public void setLastNameSpace(String lastNameSpace) {
        this.lastNameSpace = lastNameSpace;
    }

    public String getLastAction() {
        return lastAction;
    }

    public void setLastAction(String lastAction) {
        this.lastAction = lastAction;
    }

    @Override
    public List<String> getSelectedGPNSIdents() {
        return gpnsIdents;
    }

    @Override
    public void setSelectedGPNSIdents(List<String> idents) {
        gpnsIdents = new ArrayList<>();
        for (String ident : idents) {
            if (canSeeIdent(ident)) {
                gpnsIdents.add(ident);
            }
        }
    }

    @Override
    public List<String> getSelectedIdents() {
        return userIdents;
    }

    @Override
    public void setSelectedIdents(List<String> idents) {
        userIdents = new ArrayList<>();
        for (String ident : idents) {
            if (canSeeIdent(ident)) {
                userIdents.add(ident);
            }
        }
    }

    private boolean mustChangeGPNSSelection() {
        if ((getSelectedGPNSIdents() == null)
                || (getSelectedGPNSIdents().isEmpty())) {
            return true;
        }

        if ((getSelectedHotel() == null)
                || (getSelectedHotel().getIdent().isEmpty())) {
            return true;
        }

        String lastIdent = getSelectedHotel().getIdent();
        if ((getSelectedGPNSIdents().size() == 1)
                && (getSelectedGPNSIdents().get(0).equalsIgnoreCase(lastIdent))) {
            return true;
        }

        return false;
    }

    public void resetMenu() {
        this.serMenu = null;
    }

    public long getSelectedConferenceProject() {
        return selectedConferenceProject;
    }

    public void setSelectedConferenceProject(long selectedConferenceProject) {
        this.selectedConferenceProject = selectedConferenceProject;
    }

    public long getSelectedConference() {
        return selectedConference;
    }

    public void setSelectedConference(long selectedConference) {
        this.selectedConference = selectedConference;
    }

    public long getSelectedCMProject() {
        try {
            return this.getLoggedInUser().getentSystemUser().getSelectedCMProject().getId();
        } catch (Exception e) {
            return 0L;
        }
    }


    public boolean isSelectForConfWelcomeEditor() {
        return selectForConfWelcomeEditor;
    }

    public void setSelectForConfWelcomeEditor(boolean selectForConfWelcomeEditor) {
        this.selectForConfWelcomeEditor = selectForConfWelcomeEditor;
    }

    public long getSelectedCMProjectId() {
        return selectedCMProjectId;
    }

    public void setSelectedCMProjectId(long selectedCMProjectId) {
        this.selectedCMProjectId = selectedCMProjectId;
    }

    public SERSystemMenu getSelectedMenu() {
        return selectedMenu;
    }

    public void setSelectedMenu(SERSystemMenu selectedMenu) {
        this.selectedMenu = selectedMenu;
    }

    @Override
    public String toString() {
        return "userSession{" + "userId=" + userId + ", hotelIdent=" + hotelIdent + '}';
    }


}
