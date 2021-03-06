package com.imes.opcda.presentation.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "tbl_motor_state")
public class MotorState {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;
    private Integer code; // 代码
    private String intention; // 意义
}
