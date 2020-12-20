package com.edunge.srtool.model;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class State extends AbstractBaseModel {
    @OneToMany(mappedBy="state")
    Set<Lga> lgaSet;
}
