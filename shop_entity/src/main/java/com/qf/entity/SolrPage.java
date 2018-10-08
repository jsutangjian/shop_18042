package com.qf.entity;


import lombok.*;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class SolrPage<T> implements Serializable{
    /**
     * 当前页
     */
    private Integer currentPage =1;
    /**
     * 每页的条数
     */
    private Integer pageSize =4;
    /**
     * 总页数
     */
    private Integer pageCount;
    /**
     * 总条数
     */
    private Integer pageTotal;
    /**
     * 每页的数据
     */
    private List<T> pageDatas;

}
