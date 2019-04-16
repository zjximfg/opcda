package com.imes.opcda.opc.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
@Entity
@Data
@Table(name = "tbl_opcgroup")
public class OpcGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer groupId;
    private String groupName;
    private Integer updateRate;  //ms
    private Integer opcConnectionId;
    private Integer deleted = 0;
    private OpcConnection opcConnection;
    private List<OpcItem> opcItems;
    private boolean isOnline; //=true,在线状态，不存入数据库
    //private Group group; //opc 工具源类型
}
