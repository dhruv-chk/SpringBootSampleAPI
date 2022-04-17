package org.coi.sampleapp.models.dao;

import org.coi.sampleapp.models.model.Table;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class TableDAO {
    private EntityManager em;

    public TableDAO(EntityManager em) {

        this.em = em;
    }
    public Long getTableID(Long table_id){
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<Table> root = countQuery.from(Table.class);
        countQuery.select(root.get("user_id"))
                .where(builder.and(builder.equal(root.get("table_id"), table_id)));
        Long business_id = em.createQuery(countQuery).getSingleResult();
        return business_id;
    }
}
