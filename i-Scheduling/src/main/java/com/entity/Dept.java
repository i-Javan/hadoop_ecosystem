package com.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name="DEPT")
public class Dept {
    @Id
    private Integer did;
    @Column(length = 255)
    private String dname;
}
