package com.imes.opcda.presentation.service;

import com.imes.opcda.presentation.pojo.ActualValue;
import org.springframework.stereotype.Service;

@Service
public interface ActualValueService {
    ActualValue getActualValueById(Integer id);
}
