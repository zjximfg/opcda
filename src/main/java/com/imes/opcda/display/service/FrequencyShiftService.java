package com.imes.opcda.display.service;

import com.imes.opcda.display.pojo.FrequencyShift;
import com.imes.opcda.display.vo.Statistics;
import org.springframework.stereotype.Service;

@Service
public interface FrequencyShiftService {

    FrequencyShift getFrequencyShift();

    Statistics getFrequencyShiftStatistics();

}
