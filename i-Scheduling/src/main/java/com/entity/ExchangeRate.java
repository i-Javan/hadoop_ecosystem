package com.entity;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 汇率
 */
@Data
@Entity
@Table(name="exchange_rate")
@ApiModel("汇率")
public class ExchangeRate {
    // @GenericGenerator(name="idGenerator", strategy="uuid") //这个是hibernate的注解/生成32位UUID
    @Id
    @ApiModelProperty("ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty("银行折算价/中间价")
    @Column(length = 255)
    private String bankConversionPri;

    @ApiModelProperty("发布日期")
    @Column
    @Temporal(TemporalType.DATE)
    private Date date;

    @ApiModelProperty("现汇买入价")
    @Column(length = 255)
    private String fBuyPri;

    @ApiModelProperty("现汇卖出价")
    @Column(length = 255)
    private String fSellPri;

    @ApiModelProperty("现钞买入价")
    @Column(length = 255)
    private String mBuyPri;

    @ApiModelProperty("现钞卖出价")
    @Column(length = 255)
    private String mSellPri;

    @ApiModelProperty("货币名称")
    @Column(length = 255)
    private String name;

    @ApiModelProperty("发布时间")
    @Column
    @Temporal(TemporalType.TIME)
    private Date  time;
}
