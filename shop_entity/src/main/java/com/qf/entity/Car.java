package com.qf.entity;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Car implements Serializable{
    private Integer id;
    private Integer gnumber;
    private Integer gid;
    private Integer uid;
    private Goods goods;
}
