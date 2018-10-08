package com.qf.entity;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Results<T> implements Serializable{
    private  Integer code;
    private String msg;
    private T data;
}
