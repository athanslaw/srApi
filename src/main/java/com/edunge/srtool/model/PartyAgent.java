package com.edunge.srtool.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class PartyAgent extends AbstractBaseModel{
    private String firstname;
    private String lastname;
    private String phone;
    private String email;
    private String address;

    @ManyToOne
    @JoinColumn(name = "lga_id", nullable = false)
    private Lga lga;

    @ManyToOne
    @JoinColumn(name = "ward_id", nullable = false)
    private Ward ward;

    @ManyToOne
    @JoinColumn(name = "poling_unit_id", nullable = false)
    private PolingUnit polingUnit;

}
