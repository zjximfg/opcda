package com.imes.opcda.opc.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "tbl_opcserver")
public class OpcServer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer serverId;
    private String progId;        //"OPC.SimaticNet"
    private String clsid;            //B6EACB30-42D5-11d0-9517-0020AFAA4B3C  查询注册列表
    private String ipAddress;        //本机IP地址 "192.168.1.125" /或localhost
    private String domain;            //本机加入的域
    private String serverUser;        //本机的用户名
    private String serverPassword;    //本机用户名对应的密码

    private List<OpcConnection> opcConnections;
}
