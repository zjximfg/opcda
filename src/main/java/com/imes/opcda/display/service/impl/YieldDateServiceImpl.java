package com.imes.opcda.display.service.impl;

import com.imes.opcda.display.mapper.YieldDateMapper;
import com.imes.opcda.display.pojo.YieldDate;
import com.imes.opcda.display.service.YieldDateService;
import com.imes.opcda.display.vo.Statistics;
import com.imes.opcda.opc.service.OpcItemStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class YieldDateServiceImpl extends StatisticsDateBaseServiceImpl implements YieldDateService {

    @Autowired
    private YieldDateMapper yieldDateMapper;
    @Autowired
    private OpcItemStateService opcItemStateService;

    /**
     * 返回数据库中的配置
     *
     * @return 返回数据库中的配置
     */
    @Override
    public YieldDate getYieldDate() {
        return yieldDateMapper.selectAll().get(0);
    }

    /**
     * 返回勾数的前端数据
     *
     * @return 返回勾数的前端数据
     */
    @Override
    public Statistics getYieldDateStatistics() {
        // 返回数据库中的配置参数
        YieldDate yieldDate = this.getYieldDate();
        if (yieldDate != null) {
            return this.getStatistics(yieldDate, opcItemStateService);
        }
        return null;
    }
}