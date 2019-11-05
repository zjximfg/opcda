package com.imes.opcda.display.service.impl;

import com.imes.opcda.display.mapper.FrequencyShiftMapper;
import com.imes.opcda.display.pojo.FrequencyShift;
import com.imes.opcda.display.service.FrequencyShiftService;
import com.imes.opcda.display.vo.Statistics;
import com.imes.opcda.opc.service.OpcItemStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FrequencyShiftServiceImpl extends StatisticsShiftBaseServiceImpl implements FrequencyShiftService {

    @Autowired
    private FrequencyShiftMapper frequencyShiftMapper;
    @Autowired
    private OpcItemStateService opcItemStateService;

    /**
     * 返回数据库中的配置
     *
     * @return 返回数据库中的配置
     */
    @Override
    public FrequencyShift getFrequencyShift() {
        return frequencyShiftMapper.selectAll().get(0);
    }

    /**
     * 返回勾数的前端数据
     *
     * @return 返回勾数的前端数据
     */
    @Override
    public Statistics getFrequencyShiftStatistics() {
        // 返回数据库中的配置参数
        FrequencyShift frequencyShift = this.getFrequencyShift();
        if (frequencyShift != null) {
//            // 获取当前的小时数
//            Calendar calendar = Calendar.getInstance();
//            Integer currentHour = calendar.get(Calendar.HOUR);
//            this.judgeStartTime(frequencyShift, currentHour);
//            // 从累计数据库中获取相应的时间段的累计值
//            // 日期为昨天
//            if (frequencyShift.getStartTime() > currentHour) {
//                // 设定日期为 昨天 否则不变（今天）
//                calendar.add(Calendar.DATE,  -1);
//            }
//            // 设定Hour为 设定并判断后的起始时间
//            calendar.set(Calendar.HOUR, frequencyShift.getStartTime());
//            // 得到起始时间戳
//            Timestamp startTimeStamp = this.getTimeStamp(calendar);
//            Timestamp endTimeStamp = this.getTimeStamp(Calendar.getInstance());
//
//            // 根据 起始时间戳 和 结束时间戳 查询 累计数据
//            String accumValue = opcItemStateService.getAccumValue(startTimeStamp, endTimeStamp, frequencyShift.getItemId());
//            // 整理成VO 返回
//            return new Statistics(frequencyShift.getName(), accumValue);
            return this.getStatistics(frequencyShift, opcItemStateService);
        }
        return null;
    }

//    /**
//     * 私有方法，判断班次的起始时间
//     * @param frequencyShift 班次勾数数据对象
//     * @param currentHour 当前的小时时间值，整数
//     */
//    private void judgeStartTime(FrequencyShift frequencyShift, Integer currentHour) {
//        if (frequencyShift.getShiftNum() == 3) {
//            if (currentHour >= frequencyShift.getShiftStartTime_1()) {
//                if (currentHour >= frequencyShift.getShiftStartTime_2()) {
//                    if (currentHour >= frequencyShift.getShiftStartTime_3()) {
//                        frequencyShift.setCurrentShift(3);
//                    } else {
//                        frequencyShift.setCurrentShift(2);
//                    }
//                } else {
//                    frequencyShift.setCurrentShift(1);
//                }
//            } else {
//                frequencyShift.setCurrentShift(3);
//            }
//        } else {
//            // 与第一个班次的起始小时时间比较，如果小于第一个班次的时间时，认为是上一个班次。
//            if (currentHour >= frequencyShift.getShiftStartTime_1()) {
//                if (currentHour >= frequencyShift.getShiftStartTime_2()) {
//                    frequencyShift.setCurrentShift(2);
//                } else {
//                    frequencyShift.setCurrentShift(1);
//                }
//            } else {
//                frequencyShift.setCurrentShift(2);
//            }
//        }
//        switch (frequencyShift.getCurrentShift()) {
//            case 1: {
//                frequencyShift.setStartTime(frequencyShift.getShiftStartTime_1());
//                break;
//            }
//            case 2: {
//                frequencyShift.setStartTime(frequencyShift.getShiftStartTime_2());
//                break;
//            }
//            case 3: {
//                frequencyShift.setStartTime(frequencyShift.getShiftStartTime_3());
//                break;
//            }
//        }
//    }
//
//
//    /**
//     * 私有方法， 获取时间戳, 将时间Calendar 进行转换, 时分秒 分别为 0
//     * @return 返回一个时间戳
//     */
//    private Timestamp getTimeStamp(Calendar calendar) {
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
//        return new Timestamp(calendar.getTimeInMillis());
//    }

}
