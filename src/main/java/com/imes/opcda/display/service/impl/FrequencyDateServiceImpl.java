package com.imes.opcda.display.service.impl;

import com.imes.opcda.display.mapper.FrequencyDateMapper;
import com.imes.opcda.display.pojo.FrequencyDate;
import com.imes.opcda.display.service.FrequencyDateService;
import com.imes.opcda.display.vo.Statistics;
import com.imes.opcda.opc.service.OpcItemStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FrequencyDateServiceImpl extends StatisticsDateBaseServiceImpl implements FrequencyDateService {

    @Autowired
    private FrequencyDateMapper frequencyDateMapper;
    @Autowired
    private OpcItemStateService opcItemStateService;

    /**
     * 返回数据库中的配置
     *
     * @return 返回数据库中的配置
     */
    @Override
    public FrequencyDate getFrequencyDate() {
        return frequencyDateMapper.selectAll().get(0);
    }

    /**
     * 返回勾数的前端数据
     *
     * @return 返回勾数的前端数据
     */
    @Override
    public Statistics getFrequencyDateStatistics() {
        // 返回数据库中的配置参数
        FrequencyDate frequencyDate = this.getFrequencyDate();
        if (frequencyDate != null) {
            return this.getStatistics(frequencyDate, opcItemStateService);
        }
        return null;
    }
}