package org.f108349.denis.dao;

import org.f108349.denis.configuration.SessionFactoryUtil;
import org.f108349.denis.dto.VehicleTypeDto;
import org.f108349.denis.entity.VehicleType;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class VehicleTypeDao {
    public static void saveVehicleType(VehicleTypeDto vehicleTypeDto) {
        VehicleType vehicleType = new VehicleType(vehicleTypeDto.getTypeName());
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(vehicleType);
            tx.commit();
        }
    }
    
    public static VehicleTypeDto getVehicleTypeById(String id) {
        VehicleTypeDto vehicleType;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            vehicleType = session
                    .createQuery("select new org.f108349.denis.dto.VehicleTypeDto(vt) from VehicleType vt " +
                            "where vt.isDeleted = false and id = :id", VehicleTypeDto.class)
                    .setParameter("id", id)
                    .uniqueResultOptional()
                    .orElse(null);            
            tx.commit();
        }
        
        return vehicleType;
    }
    
    public static List<VehicleTypeDto> getAllVehicleTypes() {
        List<VehicleTypeDto> vehicleTypes;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            vehicleTypes = session
                    .createQuery("select new org.f108349.denis.dto.VehicleTypeDto(vt) " +
                            "from VehicleType vt where vt.isDeleted = false", VehicleTypeDto.class)
                    .getResultList();
            tx.commit();
        }
        
        return vehicleTypes;
    }
    
    public static void updateVehicleType(VehicleTypeDto vehicleTypeDto) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            VehicleType vehicleType = session.get(VehicleType.class, vehicleTypeDto.getId());
            vehicleType.setTypeName(vehicleTypeDto.getTypeName());
            session.merge(vehicleType);
            tx.commit();
        }
    }
    
    public static void deleteVehicleType(String id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            VehicleType vehicleType = session.get(VehicleType.class, id);
            vehicleType.setDeleted(true);
            session.merge(vehicleType);
            tx.commit();
        }
    }
}
