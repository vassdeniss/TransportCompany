package org.f108349.denis.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;

import java.util.List;

public abstract class BaseDao<T, V> {
    public T getByIdWhereNotDeleted(Session session, String id, Class<T> dtoType, Class<V> entityType) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(dtoType);
        Root<V> root = cq.from(entityType);
        
        cq.select(cb.construct(dtoType, root))
            .where(
                cb.and(cb.equal(root.get("id"), id),
                cb.isFalse(root.get("isDeleted"))
            )
        );
        
        return session.createQuery(cq).uniqueResultOptional().orElse(null);
    }
    
    public List<T> getAllWhereNotDeleted(Session session, Class<T> dtoType, Class<V> entityType) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(dtoType);
        Root<V> root = cq.from(entityType);
            
        cq.select(cb.construct(dtoType, root))
            .where(cb.isFalse(root.get("isDeleted")));
            
        return session.createQuery(cq).getResultList();
    }
}
