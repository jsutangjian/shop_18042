package com.qf.entity;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Address implements Serializable {
    private Integer id;
    private String person;
    private String address;
    private String phone;
    private Integer code;
    private Integer uid;
    private Integer isdefault;

}
