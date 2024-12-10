package org.f108349.denis.configuration;

import org.f108349.denis.entity.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class SessionContainerFactoryUtil {
    private static SessionFactory sessionFactory;
    private static ServiceRegistry registry;
    
    public static SessionFactory getSessionFactory(Properties properties) {
        if (sessionFactory == null) {
            Configuration config = new Configuration();
            try {
                config.addProperties(properties);
                
                config.addAnnotatedClass(Company.class)
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
                throw new RuntimeException("There was an issue building the Test SessionFactory.");
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
