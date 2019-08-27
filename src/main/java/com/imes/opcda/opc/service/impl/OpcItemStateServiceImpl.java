package com.imes.opcda.opc.service.impl;

import com.imes.opcda.opc.mapper.OpcItemStateMapper;
import com.imes.opcda.opc.pojo.OpcGroup;
import com.imes.opcda.opc.pojo.OpcItem;
import com.imes.opcda.opc.pojo.OpcItemState;
import com.imes.opcda.opc.service.OpcItemStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OpcItemStateServiceImpl implements OpcItemStateService {

    @Autowired
    private OpcItemStateMapper opcItemStateMapper;

    /**
     * 根据带有实时状态的 opcGroup中读取相应的数值生成OpcItemState列表
     * @param opcGroup
     * @return
     */
    @Override
    public List<OpcItemState> getOpcItemStateFromOpcGroups(OpcGroup opcGroup) {

        List<OpcItemState> opcItemStates = new ArrayList<>();

        for (OpcItem opcItem : opcGroup.getOpcItems()) {
            OpcItemState opcItemState = new OpcItemState();
            opcItemState.setId(UUID.randomUUID().toString());
            opcItemState.setItemId(opcItem.getItemId());
            opcItemState.setItemName(opcItem.getItemName());
            opcItemState.setItemValue(opcItem.getValue()==null? "读取失败" : opcItem.getValue());
            opcItemStates.add(opcItemState);
            //opcItemState.setDatetime() 不需要，由数据库自动添加创建是的时间戳
        }
        return opcItemStates;
    }

    @Override
    public Integer insertOpcItemStateList(List<OpcItemState> opcItemStates) {
        return opcItemStateMapper.insertList(opcItemStates);
    }
}
