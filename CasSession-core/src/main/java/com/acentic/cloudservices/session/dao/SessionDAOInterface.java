/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acentic.cloudservices.session.dao;

/**
 *
 * @author Ostrowski
 */
public interface SessionDAOInterface {
    
     public boolean unlockCMProjects(long idUser);
     public boolean unlockEdgeProjects(long idUser);
     public boolean unlockConfProjects(long idUser);

    public boolean isGpnsHotel(String ident);
     
}
