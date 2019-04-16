package com.imes.opcda.opc.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "tbl_opcconnection")
public class OpcConnection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer connectionId;
    private String protocolId; 	//"S7"
    private String connectionName; 	//"S7 connection_1"
    private Integer opcServerId;
    private Integer deleted = 0; // =0 正常， =1被删除
    //父类
    private OpcServer opcServer;
    //子类
    private List<OpcGroup> opcGroups;
}
