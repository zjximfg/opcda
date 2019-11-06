package com.imes.opcda.presentation.service;

import com.imes.opcda.presentation.pojo.HoistState;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HoistStateService {

    List<HoistState> getHoistStateList();
}
