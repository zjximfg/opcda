package com.imes.opcda.opc.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;

@Entity
@Data
@Table(name = "tbl_updaterate")
public class UpdateRate {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;
    private Integer rate;  //ms
    private String name;
    private String cron; // "*/1 * * * * ?"
}
