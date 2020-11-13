package com.imes.opcda.display.service.impl;

import com.imes.opcda.display.pojo.StatisticsShift;
import com.imes.opcda.display.uitls.TimestampUtils;
import com.imes.opcda.display.vo.Statistics;
import com.imes.opcda.opc.service.OpcItemStateService;

import java.sql.Timestamp;
import java.util.Calendar;

public class StatisticsShiftBaseServiceImpl {

    Statistics getStatistics(StatisticsShift statisticsShift, OpcItemStateService opcItemStateService) {
        // 获取当前的小时数
        Calendar calendar = Calendar.getInstance();
        Integer currentHour = calendar.get(Calendar.HOUR);
        this.judgeStartTime(statisticsShift, currentHour);
        // 从累计数据库中获取相应的时间段的累计值
        // 日期为昨天
        if (statisticsShift.getStartTime() > currentHour) {
            // 设定日期为 昨天 否则不变（今天）
            calendar.add(Calendar.DATE,  -1);
        }
        // 设定Hour为 设定并判断后的起始时间
        calendar.set(Calendar.HOUR, statisticsShift.getStartTime());
        // 得到起始时间戳
        Timestamp startTimeStamp = TimestampUtils.getTimeStamp(calendar);
        Timestamp endTimeStamp = TimestampUtils.getTimeStamp(Calendar.getInstance());

        // 根据 起始时间戳 和 结束时间戳 查询 累计数据
        //String accumValue = opcItemStateService.getAccumValue(startTimeStamp, endTimeStamp, statisticsShift.getItemId());
        // 整理成VO 返回
        return new Statistics(statisticsShift.getName(), "");
    }


    /**
     * 私有方法，判断班次的起始时间
     * @param statisticsShift 班次勾数数据对象
     * @param currentHour 当前的小时时间值，整数
     */
    private void judgeStartTime(StatisticsShift statisticsShift, Integer currentHour) {
        if (statisticsShift.getShiftNum() == 3) {
            if (currentHour >= statisticsShift.getShiftStartTime_1()) {
                if (currentHour >= statisticsShift.getShiftStartTime_2()) {
                    if (currentHour >= statisticsShift.getShiftStartTime_3()) {
                        statisticsShift.setCurrentShift(3);
                    } else {
                        statisticsShift.setCurrentShift(2);
                    }
                } else {
                    statisticsShift.setCurrentShift(1);
                }
            } else {
                statisticsShift.setCurrentShift(3);
            }
        } else {
            // 与第一个班次的起始小时时间比较，如果小于第一个班次的时间时，认为是上一个班次。
            if (currentHour >= statisticsShift.getShiftStartTime_1()) {
                if (currentHour >= statisticsShift.getShiftStartTime_2()) {
                    statisticsShift.setCurrentShift(2);
                } else {
                    statisticsShift.setCurrentShift(1);
                }
            } else {
                statisticsShift.setCurrentShift(2);
            }
        }
        switch (statisticsShift.getCurrentShift()) {
            case 1: {
                statisticsShift.setStartTime(statisticsShift.getShiftStartTime_1());
                break;
            }
            case 2: {
                statisticsShift.setStartTime(statisticsShift.getShiftStartTime_2());
                break;
            }
            case 3: {
                statisticsShift.setStartTime(statisticsShift.getShiftStartTime_3());
                break;
            }
        }
    }



}
