package com.imes.opcda.opc.service;

import com.imes.opcda.opc.pojo.OpcGroup;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OpcGroupService {
    List<OpcGroup> getOpcGroupByPid(Integer pid);

    List<OpcGroup> getAllOpcGroups();

    List<OpcGroup> insertOpcGroup(OpcGroup opcGroup);

    List<OpcGroup> updateOpcGroup(OpcGroup opcGroup);

    List<OpcGroup> deleteOpcGroup(OpcGroup opcGroup);

    OpcGroup getOpcGroupById(Integer groupId);
}
