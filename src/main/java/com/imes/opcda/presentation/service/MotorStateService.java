package com.imes.opcda.presentation.service;

import com.imes.opcda.presentation.pojo.MotorState;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MotorStateService {
    List<MotorState> getMotorStateList();
}
