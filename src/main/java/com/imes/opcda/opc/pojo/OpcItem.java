package com.imes.opcda.opc.pojo;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "tbl_opcitem")
@Entity
public class OpcItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer itemId;
    private String itemName;
    private String itemCatalog; // DB,M,I,Q
    private Integer dbNum; // 仅当itemCatalog为DB时不为空
    private String dataType; // X,B,W,D,INT,DINT,REAL,STRING
    private Integer dataAddressNum;
    private Integer dataBitOffset;
    private String comment; //注释
    private Integer isAlarm; //=1, 是报警信息。 =0，不是报警信息
    private Integer opcConnectionId;
    private Integer opcGroupId;
    private Integer deleted = 0;


    // private String value; //当前值 ，不存入数据库，
    private boolean isOnline; //=true,在线状态，不存入数据库
    private OpcConnection opcConnection;
    private OpcGroup opcGroup;

}
