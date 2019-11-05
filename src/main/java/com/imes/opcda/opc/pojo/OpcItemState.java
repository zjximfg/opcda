package com.imes.opcda.opc.pojo;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "tbl_opcitemstate")
public class OpcItemState {

    @Id
    private String id;
    private Integer itemId;
    private String itemName;
    private String itemValue = "未读取到";

    private Timestamp datetime;
}

