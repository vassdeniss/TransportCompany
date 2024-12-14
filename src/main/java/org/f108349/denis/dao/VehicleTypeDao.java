package org.f108349.denis.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.f108349.denis.dto.VehicleTypeDto;
import org.f108349.denis.entity.Vehicle;
import org.f108349.denis.entity.VehicleType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class VehicleTypeDao extends BaseDao<VehicleTypeDto, VehicleType> {
    private final SessionFactory sessionFactory;
    
    public VehicleTypeDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    public void saveVehicleType(VehicleTypeDto vehicleTypeDto) {
        VehicleType vehicleType = new VehicleType(vehicleTypeDto.getTypeName());
        try (Session session = this.sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(vehicleType);
            tx.commit();
        }
    }
    
    public VehicleTypeDto getVehicleTypeByIdWhereNotDeleted(String id) {
        VehicleTypeDto vehicleType;
        try (Session session = this.sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            vehicleType = this.getByIdWhereNotDeleted(session, id, VehicleTypeDto.class, VehicleType.class);          
            tx.commit();
        }
        
        return vehicleType;
    }
    
    public List<VehicleTypeDto> getAllVehicleTypesWhereNotDeleted() {
        List<VehicleTypeDto> vehicleTypes;
        try (Session session = this.sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            vehicleTypes = this.getAllWhereNotDeleted(session, VehicleTypeDto.class, VehicleType.class);
            tx.commit();
        }
        
        return vehicleTypes;
    }
    
    public void deleteVehicleType(String id) {
        try (Session session = this.sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            VehicleType vehicleType = session.get(VehicleType.class, id);
            
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Long> query = cb.createQuery(Long.class);
            Root<Vehicle> root = query.from(Vehicle.class);
            
            query.select(cb.count(root))
                    .where(cb.equal(root.get("vehicleType").get("id"), id));
            
            Long count = session.createQuery(query).getSingleResult();
            
            if (count != null && count > 0) {
                throw new IllegalStateException("Cannot delete vehicle type; vehicles are still assigned.");
            }
            
            vehicleType.setDeleted(true);
            session.merge(vehicleType);
            tx.commit();
        }
    }
}
