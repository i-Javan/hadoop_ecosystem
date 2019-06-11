package com.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * @Author: LiangZF
 * @Description:
 * @Date: Created in 15:02 2019/6/6
 * @Modified By: LiangZF
 */
@Data
@Table(name="t_airQualityFacts")
@Entity
public class AirQualityFacts {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    Integer id;

    @Column(length = 255,name = "citycode")
    String citycode;

    @Column(length = 255,name = "PM25")
    String PM25;

    @Column(name = "time")
    @Temporal(TemporalType.TIMESTAMP)
    Date time;

    @Column(length = 255,name = "rdesc")
    String rdesc;

    @Column(length = 255,name = "PM10")
    String PM10;

    @Column(length = 255,name = "SO2")
    String SO2;

    @Column(length = 255,name = "o3")
    String o3;

    @Column(length = 255,name = "rcode")
    String rcode;

    @Column(length = 255,name = "CO")
    String CO;

    @Column(length = 255,name = "AQI")
    String AQI;

}
