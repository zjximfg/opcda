package com.imes.opcda.presentation.service.impl;

import com.imes.opcda.core.tasks.OpcTasks;
import com.imes.opcda.opc.pojo.OpcConnection;
import com.imes.opcda.opc.pojo.OpcGroup;
import com.imes.opcda.opc.pojo.OpcItem;
import com.imes.opcda.opc.pojo.OpcItemState;
import com.imes.opcda.opc.service.OpcItemStateService;
import com.imes.opcda.presentation.pojo.HoistParams;
import com.imes.opcda.presentation.service.HoistParamsService;
import com.imes.opcda.presentation.service.PresentationService;
import com.imes.opcda.presentation.vo.Accum;
import com.imes.opcda.presentation.vo.Chart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class PresentationServiceImpl implements PresentationService {

    @Autowired
    private HoistParamsService hoistParamsService;
    @Autowired
    private OpcItemStateService opcItemStateService;

    /**
     * 累计数据，当班勾数，当班产能，当天勾数，当天产能
     *
     * @return LIST 对象
     */
    @Override
    public List<Accum> getGroup1() {
        HoistParams hoistParamsOne = hoistParamsService.getHoistParamsOne();
        // 判断当前班次 1. 早班， 2. 中班， 3. 晚班
        Integer shiftNum = this.getShiftNum(hoistParamsOne);
        String frequencyShift;
        String yieldShift;
        String frequencyDate;
        String yieldDate;
        switch (shiftNum) {
            case 1: {
                // 早班
                // 1. 获取早班的实时值, 获得通讯内存中OpcTasks.currentOpcServer中的数据, 根据itemId
                // 当班勾数
                frequencyShift = this.getCurrentOpcItemStateByItemId(hoistParamsOne.getFrequencyMorningShiftId());
                // 当班产量
                yieldShift = this.getCurrentOpcItemStateByItemId(hoistParamsOne.getYieldMorningShiftId());
                // 当天勾数
                frequencyDate = frequencyShift;
                // 当天产量
                yieldDate = yieldShift;
                break;
            }
            case 2: {
                // 中班班
                // 1. 获取早班的实时值, 获得通讯内存中OpcTasks.currentOpcServer中的数据, 根据itemId
                // 当班勾数
                frequencyShift = this.getCurrentOpcItemStateByItemId(hoistParamsOne.getFrequencyMiddleShiftId());
                // 当班产量
                yieldShift = this.getCurrentOpcItemStateByItemId(hoistParamsOne.getYieldMiddleShiftId());
                // 当天勾数
                frequencyDate = Integer.toString(Integer.parseInt(frequencyShift) + Integer.parseInt(this.getCurrentOpcItemStateByItemId(hoistParamsOne.getFrequencyMorningShiftId())));
                // 当天产量
                yieldDate = Integer.toString(Integer.parseInt(yieldShift) + Integer.parseInt(this.getCurrentOpcItemStateByItemId(hoistParamsOne.getYieldMorningShiftId())));
                break;
            }
            case 3: {
                // 晚班
                // 1. 获取早班的实时值, 获得通讯内存中OpcTasks.currentOpcServer中的数据, 根据itemId
                // 当班勾数
                frequencyShift = this.getCurrentOpcItemStateByItemId(hoistParamsOne.getFrequencyEveningShiftId());
                // 当班产量
                yieldShift = this.getCurrentOpcItemStateByItemId(hoistParamsOne.getYieldEveningShiftId());
                // 当天勾数
                frequencyDate = Integer.toString(Integer.parseInt(frequencyShift) + Integer.parseInt(this.getCurrentOpcItemStateByItemId(hoistParamsOne.getFrequencyMorningShiftId())) + Integer.parseInt(this.getCurrentOpcItemStateByItemId(hoistParamsOne.getFrequencyMiddleShiftId())));
                // 当天产量
                yieldDate = Integer.toString(Integer.parseInt(yieldShift) + Integer.parseInt(this.getCurrentOpcItemStateByItemId(hoistParamsOne.getYieldMorningShiftId())) + Integer.parseInt(this.getCurrentOpcItemStateByItemId(hoistParamsOne.getYieldMiddleShiftId())));
                break;
            }
            default: {
                frequencyShift = "0";
                yieldShift = "0";
                frequencyDate = "0";
                yieldDate = "0";
            }
        }
        // 生成 vo 对象
        List<Accum> accums = new ArrayList<>();
        accums.add(new Accum("当班提升勾数", frequencyShift));
        accums.add(new Accum("当天提升勾数", frequencyDate));
        accums.add(new Accum("当班产量（吨数）", yieldShift));
        accums.add(new Accum("当天产量（吨数）", yieldDate));
        return accums;
    }


    /**
     * 1分钟内的
     *
     * @return Chart的list对象
     */
    @Override
    public List<Chart> getGroup4() {
        HoistParams hoistParamsOne = hoistParamsService.getHoistParamsOne();

        // 查询 1分钟内历史数据
        // 1. 获取当前时间为startTime 秒级，1分钟前的时间为endTime 秒级
        Timestamp startTime = new Timestamp(new Date().getTime() - 60 * 1000);
        Timestamp endTime = new Timestamp(new Date().getTime());
        // 2. 查询时间区间的数值OpcItemState并返回相应的时间和实际值
        List<OpcItemState> speedCharts = opcItemStateService.getOpcItemStateBetweenStartTimeAndEndTime(startTime, endTime, hoistParamsOne.getSpeedId());
        // 3. 整理时间和数值并创建Charts 列表
        if (speedCharts != null && speedCharts.size() > 0) {
            List<Chart> charts = new ArrayList<>();
            for (OpcItemState speedChart : speedCharts) {
                // 对时间进行格式化，转成日期时间字符串
                String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(speedChart.getDatetime());
                Chart chart = new Chart(dateStr, speedChart.getItemValue());
                charts.add(chart);
            }
            return charts;
        }
        return null;
    }

    /**
     * 判断并返回 当前时间为早班、中班 还是晚班
     *
     * @param hoistParams 传入的 早，中，晚 三班的起始时间小时数
     * @return 返回 1. 早班， 2. 中班， 3. 晚班
     */
    private Integer getShiftNum(HoistParams hoistParams) {
        // 获取当前的小时数
        Integer currentHour = Calendar.getInstance().get(Calendar.HOUR);
        // 判断 ShiftNum
        int shiftNum;
        if (currentHour >= hoistParams.getStartHourShift_morning()) {
            if (currentHour >= hoistParams.getStartHourShift_middle()) {
                if (currentHour >= hoistParams.getStartHourShift_evening()) {
                    shiftNum = 3;
                } else {
                    shiftNum = 2;
                }
            } else {
                shiftNum = 1;
            }
        } else {
            shiftNum = 3;
        }
        return shiftNum;
    }

    /**
     * 根据传入的itemId 查找内存中通讯并读取到的实时数值
     *
     * @param itemId 传入的itemId值
     * @return 返回同时数据的值，以字符串的形式返回
     */
    private String getCurrentOpcItemStateByItemId(Integer itemId) {
        for (OpcConnection opcConnection : OpcTasks.currentOpcServer.getOpcConnections()) {
            for (OpcGroup opcGroup : opcConnection.getOpcGroups()) {
                for (OpcItem opcItem : opcGroup.getOpcItems()) {
                    if (opcItem.getItemId().equals(itemId)) {
                        return opcItem.getValue();
                    }
                }
            }
        }
        return "0";
    }

}
