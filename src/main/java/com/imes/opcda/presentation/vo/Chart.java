package com.imes.opcda.presentation.vo;

import lombok.Data;

@Data
public class Chart {
    private String date;
    private String value;

    public Chart(String date, String value) {
        this.date = date;
        this.value = value;
    }
}
