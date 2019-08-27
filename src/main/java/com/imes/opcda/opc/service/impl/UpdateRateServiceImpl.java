package com.imes.opcda.opc.service.impl;

import com.imes.opcda.opc.mapper.UpdateRateMapper;
import com.imes.opcda.opc.pojo.UpdateRate;
import com.imes.opcda.opc.service.UpdateRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UpdateRateServiceImpl implements UpdateRateService {
    @Autowired
    private UpdateRateMapper updateRateMapper;

    @Override
    public List<UpdateRate> getUpdateRates() {
        return updateRateMapper.selectAll();
    }

    @Override
    public UpdateRate getUpdateRateById(Integer id) {
        return updateRateMapper.selectByPrimaryKey(id);
    }
}
