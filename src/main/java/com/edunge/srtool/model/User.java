package com.edunge.srtool.model;

import javax.persistence.Entity;

@Entity
public class User extends BaseModel {
    private String name;
    private String phone;
    private String email;
    private String group;
}
