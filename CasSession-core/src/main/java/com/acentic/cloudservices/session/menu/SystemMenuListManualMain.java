/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acentic.cloudservices.session.menu;

import com.acentic.acs.database.enums.ContentManagerFlash.CMLicenceTyp;
import com.acentic.cloudservices.base.SystemMenu.beans.SystemMenuItemBean;
import com.acentic.cloudservices.base.SystemMenu.beans.SystemMenuListInterface;
import com.acentic.cloudservices.base.util.SpringCoreBeansDef;
import com.acentic.cloudservices.base.util.SystemRoleDef;
import com.acentic.cloudservices.interfaces.hotel.SystemHotelInterface;
import com.acentic.cloudservices.interfaces.loggedInUser.LoggedInUserInterface;
import com.acentic.cloudservices.session.util.SpringSessionBeansDef;
import java.util.ArrayList;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import com.acentic.cloudservices.base.util.SystemLicenceDef;

/**
 *
 * @author Ostrowski
 */
public class SystemMenuListManualMain implements ApplicationContextAware, SystemMenuListInterface {

    private ApplicationContext applicationContext;
    private ArrayList<SystemMenuItemBean> items;

    public SystemMenuListManualMain() {
        super();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public ArrayList<SystemMenuItemBean> getItems() {
        if (this.items == null) {
            doInitItems();
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

    private void doInitItems() {
        items = new ArrayList<SystemMenuItemBean>();
        doCreateUserMenu();
        doCreateContentManager();
        doCreateAdvertising();
        doCreateManagement();
        doCreateHSIA();
        doCreateHSIAGroup();

    }

    private void doCreateUserMenu() {
        SystemMenuItemBean beanBenutzerVerwaltung = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        beanBenutzerVerwaltung.setDescription("Benutzerverwaltung");
        beanBenutzerVerwaltung.setTextKey("acs.systemmenu.defaultMenu");
        items.add(beanBenutzerVerwaltung);

        SystemMenuListManual subList = (SystemMenuListManual) applicationContext.getBean(SpringSessionBeansDef.SYSTEM_MENU_LIST_HELPER_USER_BEAN);
        beanBenutzerVerwaltung.setSubmenus(subList);

        SystemMenuItemBean b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("Benutzer");
        b.setTextKey("acs.systemmenu.usermamangement.user");
        b.setNamespace("security");
        b.setAction("doAccessSystemUsers");
        b.setSystemRole(SystemRoleDef.ROLE_SYSTEM_USER_MANAGEMENT);
        beanBenutzerVerwaltung.getSubmenus().getItems().add(b);

        b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("Benutzergruppen");
        b.setTextKey("acs.systemmenu.usermamangement.usergroups");
        b.setNamespace("security");
        b.setAction("doAccessSystemUserGroups");
        b.setSystemRole(SystemRoleDef.ROLE_SYSTEM_GROUP_MANAGEMENT);
        beanBenutzerVerwaltung.getSubmenus().getItems().add(b);

        b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("Sicherheitsrollen");
        b.setTextKey("acs.systemmenu.usermamangement.securityroles");
        b.setNamespace("security");
        b.setAction("doAccessSystemRoles");
        b.setSystemRole(SystemRoleDef.ROLE_SYSTEM_SUPER_ADMIN);
        beanBenutzerVerwaltung.getSubmenus().getItems().add(b);

        b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("Anwendungsgruppen");
        b.setTextKey("acs.systemmenu.usermamangement.usagegroups");
        b.setNamespace("security");
        b.setAction("doAccessSystemRoleAppGroups");
        b.setSystemRole(SystemRoleDef.ROLE_SYSTEM_SUPER_ADMIN);
        beanBenutzerVerwaltung.getSubmenus().getItems().add(b);

        b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("API-Benutzer verwalten");
        b.setTextKey("acs.systemmenu.usermamangement.apiuser");
        b.setNamespace("apiusers");
        b.setAction("doApiUserNew");
        b.setSystemRole(SystemRoleDef.ROLE_SYSTEM_SUPER_ADMIN);
        beanBenutzerVerwaltung.getSubmenus().getItems().add(b);

    }

    private void doCreateContentManager() {
        SystemMenuItemBean beanBenutzerVerwaltung = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        beanBenutzerVerwaltung.setDescription("ContentManager");
        beanBenutzerVerwaltung.setTextKey("acs.systemmenu.cmmain");
        items.add(beanBenutzerVerwaltung);

        SystemMenuListManual subList = (SystemMenuListManual) applicationContext.getBean(SpringSessionBeansDef.SYSTEM_MENU_LIST_HELPER_USER_BEAN);
        beanBenutzerVerwaltung.setSubmenus(subList);

        SystemMenuItemBean b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("Assetmanager");
        b.setTextKey("acs.systemmenu.cmmain.assetmananger");
        b.setNamespace("assetManager");
        b.setAction("accessAssetManager");
        b.setSystemRole(SystemRoleDef.ROLE_CM_ASSETMANAGER);
        b.setCmLicenceTyp(CMLicenceTyp.CM_HOTELINFORMATION);
        b.setNeededLicence(SystemLicenceDef.LICENCE_CONTENTMANAGER);
        beanBenutzerVerwaltung.getSubmenus().getItems().add(b);

//        b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
//        b.setDescription("Contentmamanger");
//        b.setTextKey("acs.systemmenu.cmmain.cmeditor");
//        b.setNamespace("assetManager");
//        b.setAction("accessAssetManager");
//        b.setSystemRole(SystemRoleDef.ROLE_CM_ASSETMANAGER);
//        b.setCmLicenceTyp(CMLicenceTyp.CM_HOTELINFORMATION);
//        beanBenutzerVerwaltung.getSubmenus().getItems().add(b);        
        b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("Shop");
        b.setTextKey("acs.systemmenu.cmmain.shop");
        b.setNamespace("shop");
        b.setAction("doAccessShop");
        b.setSystemRole(SystemRoleDef.ROLE_SHOP);
        b.setNeededLicence(SystemLicenceDef.LICENCE_SHOP);
        beanBenutzerVerwaltung.getSubmenus().getItems().add(b);

        b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("Hoteldirectory");
        b.setTextKey("acs.systemmenu.cmmain.hoteldir");
        b.setNamespace("hoteldir");
        b.setAction("doAccessHotelDirItems");
        b.setSystemRole(SystemRoleDef.ROLE_HOTELDIR);
        b.setNeededLicence(SystemLicenceDef.LICENCE_HOTELDIR);
        beanBenutzerVerwaltung.getSubmenus().getItems().add(b);

    }

    private void doCreateManagement() {
        SystemMenuItemBean beanManagement = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        beanManagement.setDescription("Verwaltung");
        beanManagement.setTextKey("acs.systemmenu.management");
        items.add(beanManagement);

        SystemMenuListManual subList = (SystemMenuListManual) applicationContext.getBean(SpringSessionBeansDef.SYSTEM_MENU_LIST_HELPER_USER_BEAN);
        beanManagement.setSubmenus(subList);

        // STAMMDATEN
        SystemMenuItemBean bStammData = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        bStammData.setDescription("Stammdaten");
        bStammData.setTextKey("acs.systemmenu.management.data");
        subList.getItems().add(bStammData);

        SystemMenuListManual subListData = (SystemMenuListManual) applicationContext.getBean(SpringSessionBeansDef.SYSTEM_MENU_LIST_HELPER_USER_BEAN);
        bStammData.setSubmenus(subListData);

        SystemMenuItemBean b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("Sprachen");
        b.setTextKey("acs.systemmenu.management.data.language");
        b.setNamespace("base");
        b.setAction("doAccessSystemLanguages");
        b.setSystemRole(SystemRoleDef.ROLE_SYSTEM_LANGUAGES);
        subListData.getItems().add(b);

        b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("Sprachen");
        b.setTextKey("acs.systemmenu.management.data.rssgenre");
        b.setNamespace("rssfeed");
        b.setAction("doAccessRssGenre");
        b.setSystemRole(SystemRoleDef.ROLE_SYSTEM_GENRES);
        subListData.getItems().add(b);

        b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("Sprachen");
        b.setTextKey("acs.systemmenu.management.data.fonts");
        b.setNamespace("base");
        b.setAction("doAccessSystemFonts");
        b.setSystemRole(SystemRoleDef.ROLE_SYSTEM_SUPER_ADMIN);
        subListData.getItems().add(b);

        // HOTELS
        SystemMenuItemBean bHotel = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        bHotel.setDescription("Hotels");
        bHotel.setTextKey("acs.systemmenu.management.hotels");
        subList.getItems().add(bHotel);

        subListData = (SystemMenuListManual) applicationContext.getBean(SpringSessionBeansDef.SYSTEM_MENU_LIST_HELPER_USER_BEAN);
        bHotel.setSubmenus(subListData);

        b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("Hotels");
        b.setTextKey("acs.systemmenu.management.hotels.hotels");
        b.setNamespace("hotel");
        b.setAction("doAccessHotels");
        b.setSystemRole(SystemRoleDef.ROLE_SYSTEM_HOTEL_LICENCES);
        subListData.getItems().add(b);

        b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("Hotelbrands");
        b.setTextKey("acs.systemmenu.management.hotels.hotelbrands");
        b.setNamespace("hotel");
        b.setAction("doAccessHotelBrands");
        b.setSystemRole(SystemRoleDef.ROLE_SYSTEM_BRANDS);
        subListData.getItems().add(b);

        b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("Lizenzen");
        b.setTextKey("acs.systemmenu.management.hotels.licences");
        b.setNamespace("hotel");
        b.setAction("doAccessSystemLicences");
        b.setSystemRole(SystemRoleDef.ROLE_SYSTEM_LICENCES);
        subListData.getItems().add(b);

        // FILMVERTEILUNG
        SystemMenuItemBean bMovies = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        bMovies.setDescription("Filmverteilung");
        bMovies.setTextKey("acs.systemmenu.management.movies");
        subList.getItems().add(bMovies);

        subListData = (SystemMenuListManual) applicationContext.getBean(SpringSessionBeansDef.SYSTEM_MENU_LIST_HELPER_USER_BEAN);
        bMovies.setSubmenus(subListData);

        b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("Filme in der Cloud");
        b.setTextKey("acs.systemmenu.management.movies.oncloud");
        b.setNamespace("base");
        b.setAction("doAccessSystemMovies");
        b.setSystemRole(SystemRoleDef.ROLE_SYSTEM_MOVIES_ON_CLOUD);
        subListData.getItems().add(b);

        // FEEDS
        SystemMenuItemBean bFeeds = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        bFeeds.setDescription("System Feeds");
        bFeeds.setTextKey("acs.systemmenu.management.feeds");
        subList.getItems().add(bFeeds);

        subListData = (SystemMenuListManual) applicationContext.getBean(SpringSessionBeansDef.SYSTEM_MENU_LIST_HELPER_USER_BEAN);
        bFeeds.setSubmenus(subListData);

        b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("Wetter");
        b.setTextKey("acs.systemmenu.management.feeds.weather");
        b.setNamespace("weather");
        b.setAction("doAccessWeather");
        b.setSystemRole(SystemRoleDef.ROLE_SYSTEM_WEATHER);
        subListData.getItems().add(b);

        b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("Wetter");
        b.setTextKey("acs.systemmenu.management.feeds.rss");
        b.setNamespace("rssfeed");
        b.setAction("doAccessRssFeeds");
        b.setSystemRole(SystemRoleDef.ROLE_SYSTEM_RSSFEEDS);
        subListData.getItems().add(b);

    }

    private void doCreateAdvertising() {
        SystemMenuItemBean beanAdv = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        beanAdv.setDescription("Advertising");
        beanAdv.setTextKey("acs.systemmenu.advertising");
        items.add(beanAdv);

        SystemMenuListManual subList = (SystemMenuListManual) applicationContext.getBean(SpringSessionBeansDef.SYSTEM_MENU_LIST_HELPER_USER_BEAN);
        beanAdv.setSubmenus(subList);

        // Inventar
        SystemMenuItemBean bInv = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        bInv.setDescription("Inventar");
        bInv.setTextKey("acs.systemmenu.advertising.inv");
        subList.getItems().add(bInv);

        SystemMenuListManual subListData = (SystemMenuListManual) applicationContext.getBean(SpringSessionBeansDef.SYSTEM_MENU_LIST_HELPER_USER_BEAN);
        bInv.setSubmenus(subListData);

        SystemMenuItemBean b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("Werbetreibende");
        b.setTextKey("acs.systemmenu.advertising.inv.client");
        b.setNamespace("advertising");
        b.setAction("clientslist");
        b.setSystemRole(SystemRoleDef.ROLE_ADS_INVENTORY_ADVERTISERS);
        subListData.getItems().add(b);

        b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("Werbetreibende");
        b.setTextKey("acs.systemmenu.advertising.inv.campaign");
        b.setNamespace("advertising");
        b.setAction("campaignslist");
        b.setSystemRole(SystemRoleDef.ROLE_ADS_INVENTORY_CAMPAIGNS);
        subListData.getItems().add(b);

        b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("Werbetreibende");
        b.setTextKey("acs.systemmenu.advertising.inv.banner");
        b.setNamespace("advertising");
        b.setAction("bannerslist");
        b.setSystemRole(SystemRoleDef.ROLE_ADS_INVENTORY_BANNERS);
        subListData.getItems().add(b);

        b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("Werbetreibende");
        b.setTextKey("acs.systemmenu.advertising.inv.zones");
        b.setNamespace("advertising");
        b.setAction("zoneslist");
        b.setSystemRole(SystemRoleDef.ROLE_ADS_INVENTORY_ZONES);
        subListData.getItems().add(b);

        b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("Werbetreibende");
        b.setTextKey("acs.systemmenu.advertising.inv.hotels");
        b.setNamespace("advertising");
        b.setAction("hotelslist");
        b.setSystemRole(SystemRoleDef.ROLE_ADS_INVENTORY_HOTELS);
        subListData.getItems().add(b);

        // Inventar
        SystemMenuItemBean bStat = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        bStat.setDescription("Statisiken");
        bStat.setTextKey("acs.systemmenu.advertising.stat");
        subList.getItems().add(bStat);

        subListData = (SystemMenuListManual) applicationContext.getBean(SpringSessionBeansDef.SYSTEM_MENU_LIST_HELPER_USER_BEAN);
        bStat.setSubmenus(subListData);

        b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("Werbetreibende und Kampagnen");
        b.setTextKey("acs.systemmenu.advertising.stat.advcampaign");
        b.setNamespace("advertising");
        b.setAction("advcampstatistic");
        b.setSystemRole(SystemRoleDef.ROLE_ADS_STATISTICS_ADVCAMP);
        subListData.getItems().add(b);

        b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("Hotels und Zonen");
        b.setTextKey("acs.systemmenu.advertising.stat.hotzon");
        b.setNamespace("advertising");
        b.setAction("hotzonestatistic");
        b.setSystemRole(SystemRoleDef.ROLE_ADS_STATISTICS_HOTZONE);
        subListData.getItems().add(b);

    }

    private void doCreateHSIA() {
        SystemMenuItemBean beanHSIA = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        beanHSIA.setDescription("HSIA");
        beanHSIA.setTextKey("acs.systemmenu.hsia");
        items.add(beanHSIA);

        SystemMenuListManual subList = (SystemMenuListManual) applicationContext.getBean(SpringSessionBeansDef.SYSTEM_MENU_LIST_HELPER_USER_BEAN);
        beanHSIA.setSubmenus(subList);

        // Konfiguration
        SystemMenuItemBean beanConfig = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        beanConfig.setDescription("Konfiguration");
        beanConfig.setTextKey("acs.systemmenu.hsia.config");
        subList.getItems().add(beanConfig);

        SystemMenuListManual subListData = (SystemMenuListManual) applicationContext.getBean(SpringSessionBeansDef.SYSTEM_MENU_LIST_HELPER_USER_BEAN);
        beanConfig.setSubmenus(subListData);

        SystemMenuItemBean b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("Hardwaretypen");
        b.setTextKey("acs.systemmenu.management.hsia.config.hardwaretyp");
        b.setNamespace("gpns");
        b.setAction("doAccessHardware");
        b.setSystemRole(SystemRoleDef.ROLE_GPNS_CONFIG);
        subListData.getItems().add(b);

        b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("Hersteller");
        b.setTextKey("acs.systemmenu.management.hsia.config.manufacturer");
        b.setNamespace("gpns");
        b.setAction("doAccessManufacturer");
        b.setSystemRole(SystemRoleDef.ROLE_GPNS_MANUFACTURER);
        subListData.getItems().add(b);

        b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("Circuittyps");
        b.setTextKey("acs.systemmenu.management.hsia.config.circuittyp");
        b.setNamespace("gpns");
        b.setAction("doAccessCircuitTyps");
        b.setSystemRole(SystemRoleDef.ROLE_GPNS_CONFIG);
        subListData.getItems().add(b);

        b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("SLA-Typs");
        b.setTextKey("acs.systemmenu.management.hsia.config.slatyp");
        b.setNamespace("gpns");
        b.setAction("doAccessSLATyps");
        b.setSystemRole(SystemRoleDef.ROLE_GPNS_CONFIG);
        subListData.getItems().add(b);

        // SLAS
        SystemMenuItemBean beanSLA = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        beanSLA.setDescription("SLA");
        beanSLA.setTextKey("acs.systemmenu.hsia.sla");
        subList.getItems().add(beanSLA);

        subListData = (SystemMenuListManual) applicationContext.getBean(SpringSessionBeansDef.SYSTEM_MENU_LIST_HELPER_USER_BEAN);
        beanSLA.setSubmenus(subListData);

        b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("SLA per Hotel");
        b.setTextKey("acs.systemmenu.hsia.sla.perHotel");
        b.setNamespace("gpns");
        b.setAction("accessSLAManagement");
        b.setSystemRole(SystemRoleDef.ROLE_GPNS_CONFIG);
        subListData.getItems().add(b);

        b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("SLA Daten erfassen");
        b.setTextKey("acs.systemmenu.management.hsia.sla.editsla");
        b.setNamespace("gpns");
        b.setAction("accessSLAData");
        b.setSystemRole(SystemRoleDef.ROLE_GPNS_MANUFACTURER);
        subListData.getItems().add(b);

        // NETWORK
        SystemMenuItemBean beanNetwork = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        beanNetwork.setDescription("Network devices");
        beanNetwork.setTextKey("acs.systemmenu.hsia.network");
        subList.getItems().add(beanNetwork);

        subListData = (SystemMenuListManual) applicationContext.getBean(SpringSessionBeansDef.SYSTEM_MENU_LIST_HELPER_USER_BEAN);
        beanNetwork.setSubmenus(subListData);

        b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("uptime report");
        b.setTextKey("acs.systemmenu.hsia.network.uptime");
        b.setNamespace("gpns");
        b.setAction("accessUptimeReport");
        b.setSystemRole(SystemRoleDef.ROLE_GPNS_UPTIMEREPORT);
        subListData.getItems().add(b);

        b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("dashboard");
        b.setTextKey("acs.systemmenu.hsia.network.dashboard");
        b.setNamespace("gpns");
        b.setAction("accessDashBoard");
        b.setSystemRole(SystemRoleDef.ROLE_GPNS_PROPERTY_DASHBOARD);
        subListData.getItems().add(b);

        b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("current connections");
        b.setTextKey("acs.systemmenu.hsia.network.connections");
        b.setNamespace("gpns");
        b.setAction("accessCurrentConnections");
        b.setSystemRole(SystemRoleDef.ROLE_GPNS_AP_CONNECTIONS);
        subListData.getItems().add(b);

        // Operations
        SystemMenuItemBean beanOperations = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        beanOperations.setDescription("Operations");
        beanOperations.setTextKey("acs.systemmenu.hsia.operations");
        subList.getItems().add(beanOperations);

        subListData = (SystemMenuListManual) applicationContext.getBean(SpringSessionBeansDef.SYSTEM_MENU_LIST_HELPER_USER_BEAN);
        beanOperations.setSubmenus(subListData);

        b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("current connections");
        b.setTextKey("acs.systemmenu.hsia.operations.acd");
        b.setNamespace("gpns");
        b.setAction("accessACDStatistic");
        b.setSystemRole(SystemRoleDef.ROLE_GPNS_ACD_SYSTEM_STATISTIC);
        subListData.getItems().add(b);

        b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("asset inventory");
        b.setTextKey("acs.systemmenu.hsia.operations.inventory");
        b.setNamespace("gpns");
        b.setAction("accessAssetInventoryReport");
        b.setSystemRole(SystemRoleDef.ROLE_GPNS_ASSET_INVENTORY);
        subListData.getItems().add(b);

        b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("tickets");
        b.setTextKey("acs.systemmenu.hsia.operations.tickets");
        b.setNamespace("gpns");
        b.setAction("accessOpenTickets");
        b.setSystemRole(SystemRoleDef.ROLE_GPNS);
        subListData.getItems().add(b);

        b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("sla report");
        b.setTextKey("acs.systemmenu.hsia.operations.slareport");
        b.setNamespace("gpns");
        b.setAction("accessSLAReport");
        b.setSystemRole(SystemRoleDef.ROLE_GPNS_SLA_SUM_REPORT);
        subListData.getItems().add(b);

        // Operations
        SystemMenuItemBean beanNetworkUtilization = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        beanNetworkUtilization.setDescription("Network utilization");
        beanNetworkUtilization.setTextKey("acs.systemmenu.hsia.networkutilization");
        subList.getItems().add(beanNetworkUtilization);

        subListData = (SystemMenuListManual) applicationContext.getBean(SpringSessionBeansDef.SYSTEM_MENU_LIST_HELPER_USER_BEAN);
        beanNetworkUtilization.setSubmenus(subListData);

        b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("bw calendar");
        b.setTextKey("acs.systemmenu.hsia.networkutilization.bwcalendar");
        b.setNamespace("gpns");
        b.setAction("accessBWCalendar");
        b.setSystemRole(SystemRoleDef.ROLE_GPNS_BW_CONSUMPTION_CALENDAR);
        subListData.getItems().add(b);

    }

    private void doCreateHSIAGroup() {
        SystemMenuItemBean beanHSIA = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        beanHSIA.setDescription("HSIA Group Wide");
        beanHSIA.setTextKey("acs.systemmenu.hsiagroup");
        items.add(beanHSIA);

        SystemMenuListManual subList = (SystemMenuListManual) applicationContext.getBean(SpringSessionBeansDef.SYSTEM_MENU_LIST_HELPER_USER_BEAN);
        beanHSIA.setSubmenus(subList);

        SystemMenuItemBean b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("Filter");
        b.setTextKey("acs.systemmenu.hsiagroup.filter");
        b.setNamespace("gpns");
        b.setAction("accessFilter");
        b.setSystemRole(SystemRoleDef.ROLE_GPNS);
        beanHSIA.getSubmenus().getItems().add(b);

        b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("Circuit report");
        b.setTextKey("acs.systemmenu.hsiagroup.circuitreport");
        b.setNamespace("gpns");
        b.setAction("accessGroupCircuitReport");
        b.setSystemRole(SystemRoleDef.ROLE_GPNS_GROUP_CIRCUIT_REPORT);
        beanHSIA.getSubmenus().getItems().add(b);
        
        b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("property ticket report");
        b.setTextKey("acs.systemmenu.hsiagroup.ticketreport");
        b.setNamespace("gpns");
        b.setAction("accessPropertyTicketReport");
        b.setSystemRole(SystemRoleDef.ROLE_GPNS_GROUP_PROPERTY_TICKET);
        beanHSIA.getSubmenus().getItems().add(b);
        
        b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("Installed Base Report");
        b.setTextKey("acs.systemmenu.hsiagroup.installedbase");
        b.setNamespace("gpns");
        b.setAction("accessInstalledBaseReport");
        b.setSystemRole(SystemRoleDef.ROLE_GPNS_GROUP_BASE_REPORT);
        beanHSIA.getSubmenus().getItems().add(b);
        
        b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("OS Report");
        b.setTextKey("acs.systemmenu.hsiagroup.iosreport");
        b.setNamespace("gpns");
        b.setAction("accessOSReport");
        b.setSystemRole(SystemRoleDef.ROLE_GPNS_GROUP_OS_DEVICES);
        beanHSIA.getSubmenus().getItems().add(b);
        
        b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("Guest usage by hotel zone");
        b.setTextKey("acs.systemmenu.hsiagroup.guestusage");
        b.setNamespace("gpns");
        b.setAction("accessGuestUsageReport");
        b.setSystemRole(SystemRoleDef.ROLE_GPNS_GROUP_GUESTUSAGE_BY_HOTELZONE);
        beanHSIA.getSubmenus().getItems().add(b);
        
        b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("Guest usage by hotel zone");
        b.setTextKey("acs.systemmenu.hsiagroup.guestpurchagse");
        b.setNamespace("gpns");
        b.setAction("accessGuestPurchaseReport");
        b.setSystemRole(SystemRoleDef.ROLE_GPNS_GROUP_GUESTROOM_PURCHASE);
        beanHSIA.getSubmenus().getItems().add(b);
        
        b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("Top 5 Problem Report");
        b.setTextKey("acs.systemmenu.hsiagroup.top5problem");
        b.setNamespace("gpns");
        b.setAction("accessTop5ProblemReport");
        b.setSystemRole(SystemRoleDef.ROLE_GPNS_GROUP_TOP5_PROBLEM);
        beanHSIA.getSubmenus().getItems().add(b);
        
        b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("Location usage Report");
        b.setTextKey("acs.systemmenu.hsiagroup.locationusage");
        b.setNamespace("gpns");
        b.setAction("accessLocationUsage");
        b.setSystemRole(SystemRoleDef.ROLE_GPNS_GROUP_LOCATION_USAGE);
        beanHSIA.getSubmenus().getItems().add(b);
        
        b = (SystemMenuItemBean) applicationContext.getBean(SpringCoreBeansDef.SystemMenuBean);
        b.setDescription("SLA Performance Report");
        b.setTextKey("acs.systemmenu.hsiagroup.slaperformance");
        b.setNamespace("gpns");
        b.setAction("accessGroupSLAPerformance");
        b.setSystemRole(SystemRoleDef.ROLE_GPNS_GROUP_SLA_PERFORMANCE);
        beanHSIA.getSubmenus().getItems().add(b);

    }
}
