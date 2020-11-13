package com.imes.opcda.opc.pojo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "tbl_opcitemstate_temp")
public class OpcItemStateTemp {

    @Id
    private String id;
    private Integer itemId;
    private String itemName;
    private String itemValue = "未读取到";

    private Timestamp datetime;
}

