/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acentic.cloudservices.session.user;

import com.acentic.cloudservices.hotel.SystemHotel.beans.SystemHotelBean;
import com.acentic.cloudservices.hotel.util.SpringHotelBeansDef;
import com.acentic.cloudservices.user.SystemUser.beans.SystemUserBean;
import com.acentic.cloudservices.user.util.SpringUserBeansDef;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author uhansen
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/SessionBeanTest-context.xml"})
@TransactionConfiguration(transactionManager = "transactionManagerCore", defaultRollback = true)
@Transactional
public class userSessionTest extends AbstractTransactionalJUnit4SpringContextTests {

    private EntityManager emCore;
    private static final Logger LOGGER = Logger.getLogger(userSession.class);
    private static final long USER_ALEX = 1L;
    private static final String HOTEL_VIDEOSYS = "VIDEOSYS";
    
    private static final long USER_PEJOUX= 32L;
    private static final String HOTEL_1ID00032 = "1ID00032";
    
    public userSessionTest() {
    }

    @Transactional(value = "transactionManagerCore")
    @Test
    public void testUserMenu() {
        SystemHotelBean hotel = null;
        SystemUserBean user = null;
        userSession uSession = (userSession)applicationContext.getBean("userSessonBeanTest");
        
        
        Assert.assertTrue("unable to create  session ", uSession != null);
        
        
        try {
            user = (SystemUserBean)applicationContext.getBean(SpringUserBeansDef.SystemUserBean, USER_ALEX);
            Assert.assertTrue("unable to load user ", user.isLoaded());
            uSession.setLoggedInUser(user);
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, "EXCEPTION:", e);
            Assert.assertTrue("unable to load user", false);
        }
        
        
        try {
            hotel = (SystemHotelBean)applicationContext.getBean(SpringHotelBeansDef.SystemHotelBean,HOTEL_VIDEOSYS);
            uSession.setSelectedHotel(hotel);
            Assert.assertTrue("unable to load hotel ", hotel.isLoaded());
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, "EXCEPTION:", e);
            Assert.assertTrue("unable to load hotel", false);
        }
        
        String s = "";
        try {
            s = uSession.MenuAsUl("/test/hans.action");
            Assert.assertTrue("unable to get menu", s != null);
            Assert.assertTrue("unable to get menu", s.length() > 0);
            
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, "EXCEPTION:", e);
            Assert.assertTrue("unable to get menu", false);
        }
    }
    
    
    
    @PersistenceContext(unitName = "PersistenceCore")
    public void setEmCore(EntityManager emCore) {
        this.emCore = emCore;
    }
    
    
    @Override
    @Resource(name = "datasourceCore")
    public void setDataSource(DataSource dataSource) {
        super.setDataSource(dataSource);
    }
    
}
