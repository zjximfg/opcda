package com.imes.opcda.presentation.service.impl;

import com.imes.opcda.presentation.mapper.HoistStateMapper;
import com.imes.opcda.presentation.pojo.HoistState;
import com.imes.opcda.presentation.service.HoistStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HoistStateServiceImpl implements HoistStateService {

    @Autowired
    private HoistStateMapper hoistStateMapper;

    @Override
    public List<HoistState> getHoistStateList() {
        return hoistStateMapper.selectAll();
    }
}
