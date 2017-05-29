/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acentic.cloudservices.session.util;

/**
 *
 * @author Ostrowski
 */
public class userSessionSettings {
    
    private int webTimeOut = 60;
    private String sessionTempPath;

    public int getWebTimeOut() {
        return webTimeOut;
    }

    public void setWebTimeOut(int webTimeOut) {
        this.webTimeOut = webTimeOut;
    }

    public String getSessionTempPath() {
        return sessionTempPath;
    }

    public void setSessionTempPath(String sessionTempPath) {
        this.sessionTempPath = sessionTempPath;
    }
    
    
    
}
