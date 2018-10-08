package com.qf.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail implements Serializable {
    private Integer id;
    private Integer oid;
    private Integer gid;
    private String gname;
    private String ginfo;
    private Double price;
    private Integer gcount;
    private String gimage;
}
