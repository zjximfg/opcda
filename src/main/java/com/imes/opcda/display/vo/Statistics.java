package com.imes.opcda.display.vo;

import lombok.Data;


@Data
public class Statistics {
    private String name;
    private String count;

    public Statistics(){ }

    public Statistics(String name, String count) {
        this.name = name;
        this.count = count;
    }
}
