package com.imes.opcda.display.service.impl;

import com.imes.opcda.display.pojo.StatisticsDate;
import com.imes.opcda.display.uitls.TimestampUtils;
import com.imes.opcda.display.vo.Statistics;
import com.imes.opcda.opc.service.OpcItemStateService;

import java.util.Calendar;

public class StatisticsDateBaseServiceImpl {

    Statistics getStatistics(StatisticsDate statisticsDate, OpcItemStateService opcItemStateService) {
        // 获取当前的小时数
        Calendar calendar = Calendar.getInstance();
        Integer currentHour = calendar.get(Calendar.HOUR);
        // 从累计数据库中获取相应的时间段的累计值
        // 日期为昨天
        if (statisticsDate.getShiftTime() > currentHour) {
            // 设定日期为 昨天 否则不变（今天）
            calendar.add(Calendar.DATE, -1);
        }
        // 设定Hour为 设定并判断后的起始时间
        calendar.set(Calendar.HOUR, statisticsDate.getShiftTime());
        // 得到起始时间戳
        // 根据 起始时间戳 和 结束时间戳 查询 累计数据
        String accumValue = opcItemStateService.getAccumValue(TimestampUtils.getTimeStamp(calendar), TimestampUtils.getTimeStamp(Calendar.getInstance()), statisticsDate.getItemId());
        // 整理成VO 返回
        return new Statistics(statisticsDate.getName(), accumValue);
    }

    Statistics getStatisticsByDate(Calendar calendar, StatisticsDate statisticsDate, OpcItemStateService opcItemStateService) {

    }

}
