package com.edunge.srtool.model;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.Set;

@Entity
public class Election extends AbstractBaseModel{
    private String description;
    private String year;
    private Date dateOfElection;

    @OneToMany(mappedBy = "election")
    private Set<Result> results;
}
