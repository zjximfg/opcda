package com.imes.opcda.display.service.impl;

import com.imes.opcda.display.mapper.YieldShiftMapper;
import com.imes.opcda.display.pojo.YieldShift;
import com.imes.opcda.display.service.YieldShiftService;
import com.imes.opcda.display.vo.Statistics;
import com.imes.opcda.opc.service.OpcItemStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class YieldShiftServiceImpl extends StatisticsShiftBaseServiceImpl implements YieldShiftService {

    @Autowired
    private YieldShiftMapper yieldShiftMapper;
    @Autowired
    private OpcItemStateService opcItemStateService;

    /**
     * 返回数据库中的配置
     *
     * @return 返回数据库中的配置
     */
    @Override
    public YieldShift getYieldShift() {
        return yieldShiftMapper.selectAll().get(0);
    }

    /**
     * 返回勾数的前端数据
     *
     * @return 返回勾数的前端数据
     */
    @Override
    public Statistics getYieldShiftStatistics() {
        // 返回数据库中的配置参数
        YieldShift yieldShift = this.getYieldShift();
        if (yieldShift != null) {
            return this.getStatistics(yieldShift, opcItemStateService);
        }
        return null;
    }


}
