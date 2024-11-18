package org.f108349.denis;

import org.f108349.denis.configuration.SessionFactoryUtil;
import org.hibernate.Session;

public class Main {
    public static void main(String[] args) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            System.out.println("Session is open: " + session.isOpen());
        } catch (Exception e) {
            e.printStackTrace();
        }    
    }
}
