package com.imes.opcda.opc.service.impl;

import com.imes.opcda.common.pojo.OpcConfig;
import com.imes.opcda.opc.mapper.OpcConnectionMapper;
import com.imes.opcda.opc.pojo.OpcConnection;
import com.imes.opcda.opc.pojo.OpcGroup;
import com.imes.opcda.opc.service.OpcConnectionService;
import com.imes.opcda.opc.service.OpcGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class OpcConnectionServiceImpl implements OpcConnectionService {
    @Autowired
    private OpcConfig opcConfig;
    @Autowired
    private OpcConnectionMapper opcConnectionMapper;
    @Autowired
    private OpcGroupService opcGroupService;

    @Override
    public List<String> getProtocols() {
        return opcConfig.getProtocolIds();
    }

    @Override
    public List<OpcConnection> getAllConnections(String sortType, String key) {
        List<OpcConnection> connections;
        if (sortType.equalsIgnoreCase("desc") || sortType.equalsIgnoreCase("asc")) {
            Example example = new Example(OpcConnection.class);
            example.setOrderByClause(key + " " + sortType);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("deleted", 0);
            connections = opcConnectionMapper.selectByExample(example);
        } else {
            connections = this.getAllConnections();
        }
        return connections;
    }

    @Override
    public List<OpcConnection> getAllConnections() {
        OpcConnection opcConnection = new OpcConnection();
        opcConnection.setDeleted(0);
        return opcConnectionMapper.select(opcConnection);
    }

    @Override
    public List<OpcConnection> updateConnection(OpcConnection opcConnection) {
        opcConnectionMapper.updateByPrimaryKey(opcConnection);
        return this.getAllConnections();
    }

    @Override
    public List<OpcConnection> insertConnection(OpcConnection opcConnection) {
        opcConnection.setDeleted(0);
        opcConnectionMapper.insert(opcConnection);
        return this.getAllConnections();
    }

    @Override
    public List<OpcConnection> getConnectionMenu() {
        List<OpcConnection> opcConnections = this.getAllConnections();
        opcConnections.forEach(opcConnection -> {
            List<OpcGroup> opcGroups = opcGroupService.getOpcGroupByPid(opcConnection.getConnectionId());
            opcConnection.setOpcGroups(opcGroups);
        });
        return opcConnections;
    }

    @Override
    public List<OpcConnection> deleteConnection(OpcConnection opcConnection) {
        opcConnection.setDeleted(1);
        opcConnectionMapper.updateByPrimaryKey(opcConnection);
        return this.getAllConnections();
    }

    @Override
    public OpcConnection getConnectionById(Integer id) {
        return opcConnectionMapper.selectByPrimaryKey(id);
    }
}
