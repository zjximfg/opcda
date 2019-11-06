package com.imes.opcda.presentation.vo;

import lombok.Data;

@Data
public class Actual {
    private Integer index;
    private String name;
    private String value;
    private String unit;

    public Actual(Integer index, String name, String value, String unit){
        this.index = index;
        this.name = name;
        this.value = value;
        this.unit = unit;
    }
}
