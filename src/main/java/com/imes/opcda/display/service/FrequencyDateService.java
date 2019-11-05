package com.imes.opcda.display.service;

import com.imes.opcda.display.pojo.FrequencyDate;
import com.imes.opcda.display.vo.Statistics;
import org.springframework.stereotype.Service;

@Service
public interface FrequencyDateService {

    FrequencyDate getFrequencyDate();

    Statistics getFrequencyDateStatistics();

}
