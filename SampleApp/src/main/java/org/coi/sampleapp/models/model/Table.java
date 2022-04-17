package org.coi.sampleapp.models.model;

import javax.persistence.*;

@Entity
@javax.persistence.Table(name = "table")
public class Table {
    private static final long serialVersionUID = -3073685778012946036L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "table_id", updatable = false, nullable = false)
    public Long table_id;
    public Long user_id;
}
