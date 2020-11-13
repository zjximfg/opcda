package com.imes.opcda.presentation.service.impl;

import com.imes.opcda.presentation.mapper.MotorStateMapper;
import com.imes.opcda.presentation.pojo.MotorState;
import com.imes.opcda.presentation.service.MotorStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MotorStateServiceImpl implements MotorStateService {

    @Autowired
    private MotorStateMapper motorStateMapper;

    @Override
    public List<MotorState> getMotorStateList() {
        return motorStateMapper.selectAll();
    }
}
