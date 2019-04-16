package com.imes.opcda.opc.service.impl;

import com.imes.opcda.common.pojo.OpcConfig;
import com.imes.opcda.opc.mapper.OpcServerMapper;
import com.imes.opcda.opc.pojo.OpcServer;
import com.imes.opcda.opc.service.OpcServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
public class OpcServerServiceImpl implements OpcServerService {
    @Autowired
    private OpcConfig opcConfig;
    @Autowired
    private OpcServerMapper opcServerMapper;

    @Override
    public List<String> getProgIds() {
        Set<String> set = opcConfig.getServerCatalog().keySet();
        return new ArrayList<>(set);
    }

    @Override
    public List<String> getClsids() {
        Collection<String> collection = opcConfig.getServerCatalog().values();
        return new ArrayList<>(collection);
    }

    @Override
    public OpcServer getOpcServer() {
        OpcServer opcServer = opcServerMapper.selectByPrimaryKey(1);
        //如果没找到，自动创建一个默认的服务器。
        if (opcServer == null){
            opcServer = new OpcServer();
            opcServer.setClsid(this.getClsids().get(0));
            opcServer.setProgId(this.getProgIds().get(0));
            opcServer.setIpAddress("localhost");
            opcServer.setDomain("WORKGROUP");
            opcServer.setServerUser("Administrator");
            opcServer.setServerPassword("");
            opcServerMapper.insert(opcServer);
        }
        opcServer = opcServerMapper.selectByPrimaryKey(1);
        return opcServer;
    }

    @Override
    public OpcServer updateOpcServer(OpcServer opcServer) {
        opcServerMapper.updateByPrimaryKey(opcServer);
        return this.getOpcServer();
    }
}
