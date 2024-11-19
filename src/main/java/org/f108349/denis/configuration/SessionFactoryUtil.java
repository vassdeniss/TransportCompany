package org.f108349.denis.configuration;

import org.f108349.denis.entity.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class SessionFactoryUtil {
    private static SessionFactory sessionFactory;
    private static ServiceRegistry registry;
    
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Configuration config = new Configuration();
            try {
                config.addAnnotatedClass(   Company.class)
                    .addAnnotatedClass(Customer.class)
                    .addAnnotatedClass(EmployeeClassification.class)
                    .addAnnotatedClass(Employee.class)
                    .addAnnotatedClass(VehicleType.class)
                    .addAnnotatedClass(Vehicle.class)
                    .addAnnotatedClass(Order.class);
                registry = new StandardServiceRegistryBuilder()
                    .applySettings(config.getProperties()).build();
                sessionFactory = config.buildSessionFactory(registry);
            } catch (Exception e) {
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
                e.printStackTrace();
                throw new RuntimeException("There was an issue building the SessionFactory.");
            }        
        }

        return sessionFactory;
    }
    
    public static void shutdown() {
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
