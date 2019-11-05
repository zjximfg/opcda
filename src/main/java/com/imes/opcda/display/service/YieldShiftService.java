package com.imes.opcda.display.service;

import com.imes.opcda.display.pojo.YieldShift;
import com.imes.opcda.display.vo.Statistics;
import org.springframework.stereotype.Service;

@Service
public interface YieldShiftService {

    YieldShift getYieldShift();

    Statistics getYieldShiftStatistics();

}
