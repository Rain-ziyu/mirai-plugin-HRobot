package com.happysnaker.entity;

import lombok.Data;

import java.util.List;

@Data
public class PageListResultDTO {
    private Integer recordsFiltered;
    private List data;
    private Integer recordsTotal;
}
