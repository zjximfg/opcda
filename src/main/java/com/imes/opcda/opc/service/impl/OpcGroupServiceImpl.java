package com.imes.opcda.opc.service.impl;

import com.imes.opcda.opc.mapper.OpcGroupMapper;
import com.imes.opcda.opc.pojo.OpcGroup;
import com.imes.opcda.opc.service.OpcConnectionService;
import com.imes.opcda.opc.service.OpcGroupService;
import com.imes.opcda.opc.service.UpdateRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpcGroupServiceImpl implements OpcGroupService {
    @Autowired
    private OpcGroupMapper opcGroupMapper;
    @Autowired
    private OpcConnectionService opcConnectionService;
    @Autowired
    private UpdateRateService updateRateService;

    @Override
    public List<OpcGroup> getOpcGroupByPid(Integer pid) {
        OpcGroup opcGroup = new OpcGroup();
        opcGroup.setOpcConnectionId(pid);
        List<OpcGroup> groups = opcGroupMapper.select(opcGroup);
        for (OpcGroup group : groups) {
            group.setUpdateRate(updateRateService.getUpdateRateById(group.getUpdateRateId()));
        }
        return groups;
    }

    @Override
    public List<OpcGroup> getAllOpcGroups() {
        OpcGroup opcGroup = new OpcGroup();
        opcGroup.setDeleted(0);
        List<OpcGroup> opcGroups = opcGroupMapper.select(opcGroup);
        opcGroups.forEach(group -> {
            group.setOpcConnection(opcConnectionService.getConnectionById(group.getOpcConnectionId()));
        });
        opcGroups.forEach(group -> {
            group.setUpdateRate(updateRateService.getUpdateRateById(group.getUpdateRateId()));
        });
        return opcGroups;
    }

    @Override
    public List<OpcGroup> insertOpcGroup(OpcGroup opcGroup) {
        opcGroup.setDeleted(0);
        opcGroupMapper.insert(opcGroup);
        return this.getAllOpcGroups();
    }

    @Override
    public List<OpcGroup> updateOpcGroup(OpcGroup opcGroup) {
        opcGroupMapper.updateByPrimaryKey(opcGroup);
        return this.getAllOpcGroups();
    }

    @Override
    public List<OpcGroup> deleteOpcGroup(OpcGroup opcGroup) {
        opcGroup.setDeleted(1);
        opcGroupMapper.updateByPrimaryKeySelective(opcGroup);
        return this.getAllOpcGroups();
    }

    @Override
    public OpcGroup getOpcGroupById(Integer groupId) {
        OpcGroup opcGroup = new OpcGroup();
        opcGroup.setGroupId(groupId);
        opcGroup.setDeleted(0);
        return opcGroupMapper.selectOne(opcGroup);
    }
}
