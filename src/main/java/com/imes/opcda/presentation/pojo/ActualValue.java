package com.imes.opcda.presentation.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@Table(name = "tbl_actual_value")
public class ActualValue {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;
    private String name;
    private Integer itemId;
    @Transient
    private String value;
}
