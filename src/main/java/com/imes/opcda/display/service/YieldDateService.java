package com.imes.opcda.display.service;

import com.imes.opcda.display.pojo.YieldDate;
import com.imes.opcda.display.vo.Statistics;
import org.springframework.stereotype.Service;

@Service
public interface YieldDateService {

    YieldDate getYieldDate();

    Statistics getYieldDateStatistics();

}
