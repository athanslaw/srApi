package com.edunge.srtool.model;

import javax.persistence.*;

@MappedSuperclass
public class BaseModel {

    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }
}
