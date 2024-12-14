package org.f108349.denis.dao;

import org.f108349.denis.dto.VehicleDto;
import org.f108349.denis.entity.Company;
import org.f108349.denis.entity.Vehicle;
import org.f108349.denis.entity.VehicleType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class VehicleDao extends BaseDao<VehicleDto, Vehicle> {
    private final SessionFactory sessionFactory;
    
    public VehicleDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    public boolean saveVehicle(VehicleDto vehicleDto) {
        try (Session session = this.sessionFactory.openSession()) {
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
    
    public VehicleDto getVehicleByIdWhereNotDeleted(String id) {
        VehicleDto vehicle;
        try (Session session = this.sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            vehicle = this.getByIdWhereNotDeleted(session, id, VehicleDto.class, Vehicle.class);
            tx.commit();
        }
        
        return vehicle;
    }
    
    public List<VehicleDto> getAllVehiclesWhereNotDeleted() {
        List<VehicleDto> vehicles;
        try (Session session = this.sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            vehicles = this.getAllWhereNotDeleted(session, VehicleDto.class, Vehicle.class);
            tx.commit();
        }
        
        return vehicles;
    }
    
    public void updateVehicle(VehicleDto vehicleDto) {
        try (Session session = this.sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Vehicle vehicle = session.get(Vehicle.class, vehicleDto.getId());
            
            vehicle.setModel(vehicleDto.getModel());
            vehicle.setLicensePlate(vehicleDto.getLicensePlate());
            vehicle.setCapacity(vehicleDto.getCapacity());
            
            this.applyCompanyAndVehicleType(session, vehicleDto, vehicle);
            
            session.merge(vehicle);
            tx.commit();
        }
    }
    
    public void deleteVehicle(String id) {
        try (Session session = this.sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Vehicle vehicle = session.get(Vehicle.class, id);
            vehicle.setDeleted(true);
            session.merge(vehicle);
            tx.commit();
        }
    }
    
    private void applyCompanyAndVehicleType(Session session, VehicleDto vehicleDto, Vehicle vehicle) {
        // TODO: replace base with else throw
        // TODO: could go to base
        if (vehicleDto.getCompanyId() != null) {
            Company company = session.createQuery(
                "SELECT c FROM Company c WHERE c.id = :id AND c.isDeleted = false", Company.class)
                .setParameter("id", vehicleDto.getCompanyId())
                .uniqueResultOptional()
                .orElseThrow(() -> new IllegalArgumentException("Invalid company ID provided."));
            vehicle.setCompany(company);
        }

        if (vehicleDto.getVehicleTypeId() != null) {
            VehicleType vehicleType = session.createQuery(
                "SELECT vt FROM VehicleType vt WHERE vt.id = :id AND vt.isDeleted = false", VehicleType.class)
                .setParameter("id", vehicleDto.getVehicleTypeId())
                .uniqueResultOptional()
                .orElseThrow(() -> new IllegalArgumentException("Invalid vehicle type ID provided"));
            vehicle.setVehicleType(vehicleType);
        }
    }
}
