package com.imes.opcda.opc.service;

import com.imes.opcda.opc.pojo.UpdateRate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UpdateRateService {

    List<UpdateRate> getUpdateRates();

    UpdateRate getUpdateRateById(Integer id);

}
