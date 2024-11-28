package org.f108349.denis.dao;

import org.f108349.denis.configuration.SessionFactoryUtil;
import org.f108349.denis.dto.VehicleDto;
import org.f108349.denis.entity.Company;
import org.f108349.denis.entity.Vehicle;
import org.f108349.denis.entity.VehicleType;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class VehicleDao {
    public static boolean saveVehicle(VehicleDto vehicleDto) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            
            VehicleType vehicleType = session
                    .createQuery("select vt from VehicleType vt where vt.isDeleted = false and id = :id", VehicleType.class)
                    .setParameter("id", vehicleDto.getVehicleTypeId())
                    .uniqueResultOptional()
                    .orElse(null);             
            if (vehicleType == null) {
                System.out.println("Invalid vehicle type. Vehicle not saved.");
                tx.rollback();
                return false;
            }
            
            Company company = session
                    .createQuery("select c from Company c where c.isDeleted = false and id = :id", Company.class)
                    .setParameter("id", vehicleDto.getCompanyId())
                    .uniqueResultOptional()
                    .orElse(null); 
            if (company == null) {
                System.out.println("Invalid company ID. Vehicle not saved.");
                tx.rollback();
                return false;
            }
            
            Vehicle vehicle = new Vehicle(vehicleDto.getModel(), vehicleDto.getLicensePlate(), vehicleDto.getCapacity());
            vehicle.setCompany(company);
            vehicle.setVehicleType(vehicleType);
            session.persist(vehicle);
            tx.commit();
            return true;
        }
    }
    
    public static VehicleDto getVehicleById(String id) {
        VehicleDto vehicle;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            vehicle = session
                    .createQuery("select new org.f108349.denis.dto.VehicleDto(v) from Vehicle v " +
                            "where v.isDeleted = false and id = :id", VehicleDto.class)
                    .setParameter("id", id)
                    .uniqueResultOptional()
                    .orElse(null); 
            tx.commit();
        }
        
        return vehicle;
    }
    
    public static List<VehicleDto> getAllVehicles() {
        List<VehicleDto> vehicles;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            vehicles = session
                    .createQuery("select new org.f108349.denis.dto.VehicleDto(v) " +
                            "from Vehicle v where v.isDeleted = false", VehicleDto.class)
                    .getResultList();
            tx.commit();
        }
        
        return vehicles;
    }
    
    public static void updateVehicle(VehicleDto vehicleDto) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Vehicle vehicle = session.get(Vehicle.class, vehicleDto.getId());
            
            vehicle.setModel(vehicleDto.getModel());
            vehicle.setLicensePlate(vehicleDto.getLicensePlate());
            vehicle.setCapacity(vehicleDto.getCapacity());
            
            session.merge(vehicle);
            tx.commit();
        }
    }
    
    public static void deleteVehicle(String id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Vehicle vehicle = session.get(Vehicle.class, id);
            vehicle.setDeleted(true);
            session.merge(vehicle);
            tx.commit();
        }
    }
}
