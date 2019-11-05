package com.imes.opcda.presentation.service.impl;

import com.imes.opcda.presentation.mapper.ActualValueMapper;
import com.imes.opcda.presentation.pojo.ActualValue;
import com.imes.opcda.presentation.service.ActualValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActualValueServiceImpl implements ActualValueService {

    @Autowired
    private ActualValueMapper actualValueMapper;

    @Override
    public ActualValue getActualValueById(Integer id) {
        return actualValueMapper.selectByPrimaryKey(id);
    }
}
