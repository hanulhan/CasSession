/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acentic.cloudservices.session.user;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author uhansen
 */

public class regExTest extends TestCase {

    
    public regExTest() {
    }

    @Test
    public void testUserMenu() {
        String regExg = ".*\\b/(public|rest|js|images|vendors|css)/\\b.*";
        Pattern pattern = Pattern.compile(regExg);

        String s = "server:port/public/tuwas";
        Matcher matcher = pattern.matcher(s);
        Assert.assertTrue("no match !!!", matcher.matches());
        
        s = "server:js/ssss/tuwas";
         matcher = pattern.matcher(s);
        Assert.assertFalse("match !!!", matcher.matches());        
                
        s = "server:port/rest/tuwas";
        matcher = pattern.matcher(s);
        Assert.assertTrue("no match !!!", matcher.matches());

        s ="https://localhost:18443/CasSession-web/public/doAccessLoginUser.action";
        matcher = pattern.matcher(s);
        Assert.assertTrue("no match !!!", matcher.matches());        
        
    }
    
    
    
    
}
