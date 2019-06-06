package com.entity;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 黄金
 */
@Data
@Entity
@Table(name="gold")
@ApiModel("黄金")
public class Gold {
    // @GenericGenerator(name="idGenerator", strategy="uuid") //这个是hibernate的注解/生成32位UUID
    @Id
    @ApiModelProperty("ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty("品种")
    @Column(length = 255,name = "variety")
    private String variety;


    @ApiModelProperty("最新价")
    @Column(length = 255,name = "latestpri")
    private String latestpri;

    @ApiModelProperty("开盘价")
    @Column(length = 255,name = "openpri")
    private String openpri;

    @ApiModelProperty("最高价")
    @Column(length = 255,name = "maxpri")
    private String maxpri;

    @ApiModelProperty("最低价")
    @Column(length = 255,name = "minpri")
    private String minpri;

    @ApiModelProperty("涨跌幅")
    @Column(length = 255,name = "limits")
    private String limit;

    @ApiModelProperty("昨收价")
    @Column(length = 255,name = "yespri")
    private String yespri;

    @ApiModelProperty("总成交量")
    @Column(length = 255,name = "totalvol")
    private String totalvol;

    @ApiModelProperty("更新时间")
    @Column(name = "time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date  time;
}
