package com.imes.opcda.opc.service.impl;

import com.imes.opcda.opc.mapper.OpcItemStateTempMapper;
import com.imes.opcda.opc.pojo.OpcItemState;
import com.imes.opcda.opc.pojo.OpcItemStateTemp;
import com.imes.opcda.opc.service.OpcItemStateTempService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;


@Service
public class OpcItemStateTempServiceImpl implements OpcItemStateTempService {

    @Autowired
    private OpcItemStateTempMapper opcItemStateTempMapper;

    @Override
    public void updateOpcItemStateTemp(OpcItemState opcItemState) {
        // 先确认是否有该值
        OpcItemStateTemp opcItemStateTemp1 = opcItemStateTempMapper.selectByPrimaryKey(opcItemState.getItemId().toString());
        // 封装对象
        OpcItemStateTemp opcItemStateTemp = new OpcItemStateTemp();
        opcItemStateTemp.setId(opcItemState.getItemId().toString());
        opcItemStateTemp.setItemId(opcItemState.getItemId());
        opcItemStateTemp.setItemName(opcItemState.getItemName());
        opcItemStateTemp.setItemValue(opcItemState.getItemValue());
        opcItemStateTemp.setDatetime(new Timestamp(System.currentTimeMillis()));
        if (opcItemStateTemp1 == null) {
            // 插入
            opcItemStateTempMapper.insert(opcItemStateTemp);
        } else {
            //  更新
            opcItemStateTempMapper.updateByPrimaryKey(opcItemStateTemp);
        }
    }
}
