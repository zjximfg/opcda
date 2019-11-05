package com.imes.opcda.presentation.vo;

import lombok.Data;

@Data
public class Accum {
    private String name;
    private String count;

    public Accum(String name, String count){
        this.name = name;
        this.count = count;
    }
}
