package com.qf.entity;

import lombok.*;

import java.io.Serializable;

/**
 * 使用Lombok插件实现set/get方法
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
/**
 * 实体类必须要实现序列化接口
 */
public class Goods implements Serializable{
    private Integer id;
    /**
     * 商品标题
     */
    private String title;
    /**
     * 商品简介
     */
    private String ginfo;
    /**
     * 库存
     */
    private Integer gcount;
    /**
     * 所属分类
     */
    private Integer tid =2;
    /**
     * 原价
     */
    private Double allprice;
    /**
     * 折后价
     */
    private Double price;
    /**
     * 商品图片
     */
    private String gimage;

}
