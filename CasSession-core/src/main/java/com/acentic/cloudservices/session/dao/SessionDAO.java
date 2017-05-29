package com.acentic.cloudservices.session.dao;


import com.acentic.acs.database.common.Hotels;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

public class SessionDAO implements SessionDAOInterface {

    private EntityManager em;
    private static final Logger LOGGER = Logger.getLogger(SessionDAO.class);

    public EntityManager getEm() {
        return em;
    }

    @PersistenceContext(unitName = "PersistenceCore")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Transactional(value = "transactionManagerCore")
    @Override
    public boolean unlockCMProjects(long idUser) {
        try {
            Query q1 = em.createNamedQuery("CmProject.unlockByUser");
            q1.setParameter("iduser", idUser);
            q1.executeUpdate();
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, "unable to cleanup cm projects locks by logout", e);
            return false;
        }
    }

    @Transactional(value = "transactionManagerCore")
    @Override
    public boolean unlockEdgeProjects(long idUser) {
        try {
            Query q1 = em.createNamedQuery("hotels.edgeunlockByUser");
            q1.setParameter("iduser", idUser);
            q1.executeUpdate();
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, "unable to cleanup cm projects locks by logout", e);
            return false;
        }
    }

    @Transactional(value = "transactionManagerCore")
    @Override
    public boolean unlockConfProjects(long idUser) {
        try {
            Query q1 = em.createNamedQuery("entityConferenceProject.unlockByUser");
            q1.setParameter("iduser", idUser);
            q1.executeUpdate();
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, "unable to cleanup cm projects locks by logout", e);
            return false;
        }
    }

    @Override
    public boolean isGpnsHotel(String ident) {
        try {
            Query q1 = em.createNamedQuery("hotels.findForeignSystemByIdent");
            q1.setParameter("ident", ident);
            Hotels hotel = (Hotels) q1.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, "unable to check if foreign system", e);
            return false;
        }
        
        

    }

}
