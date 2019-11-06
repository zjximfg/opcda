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
import com.imes.opcda.presentation.vo.Actual;
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
                frequencyShift = this.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getFrequencyMorningShiftId());
                // 当班产量
                yieldShift = this.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getYieldMorningShiftId());
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
                frequencyShift = this.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getFrequencyMiddleShiftId());
                // 当班产量
                yieldShift = this.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getYieldMiddleShiftId());
                // 当天勾数
                frequencyDate = Integer.toString(Integer.parseInt(frequencyShift) + Integer.parseInt(this.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getFrequencyMorningShiftId())));
                // 当天产量
                yieldDate = Integer.toString(Integer.parseInt(yieldShift) + Integer.parseInt(this.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getYieldMorningShiftId())));
                break;
            }
            case 3: {
                // 晚班
                // 1. 获取早班的实时值, 获得通讯内存中OpcTasks.currentOpcServer中的数据, 根据itemId
                // 当班勾数
                frequencyShift = this.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getFrequencyEveningShiftId());
                // 当班产量
                yieldShift = this.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getYieldEveningShiftId());
                // 当天勾数
                frequencyDate = Integer.toString(Integer.parseInt(frequencyShift) + Integer.parseInt(this.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getFrequencyMorningShiftId())) + Integer.parseInt(this.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getFrequencyMiddleShiftId())));
                // 当天产量
                yieldDate = Integer.toString(Integer.parseInt(yieldShift) + Integer.parseInt(this.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getYieldMorningShiftId())) + Integer.parseInt(this.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getYieldMiddleShiftId())));
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
     * 1分钟内的实际速度值列表
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
     * 轮询播放 放大的设备展示，在设备周边展示的实时数据
     * @return JSON 的实际值对象列表
     */
    @Override
    public List<Actual> getGroup5() {
        HoistParams hoistParamsOne = hoistParamsService.getHoistParamsOne();
        List<Actual> actualList = new ArrayList<>();
        // 第一组
        // 1. 速度
        String speed = this.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getSpeedId());
        actualList.add(new Actual(1, hoistParamsOne.getSpeed().getName(), speed, "m/s"));
        // 2. 位置1
        String position_1 = this.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getPosition_1_id());
        actualList.add(new Actual(2, hoistParamsOne.getPosition_1().getName(), position_1, "m"));
        // 3. 载重1
        String load_1 = this.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getLoad_1_id());
        actualList.add(new Actual(3, hoistParamsOne.getLoad_1().getName(), load_1, "t"));
        // 4. 位置2
        String position_2 = this.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getPosition_2_id());
        actualList.add(new Actual(4, hoistParamsOne.getPosition_2().getName(), position_2, "m"));

        // 第二组
        // 1. 速度
        actualList.add(new Actual(5, hoistParamsOne.getSpeed().getName(), speed, "m/s"));
        // 2. 天轮1轴承1 温度
        String headSheave_1_bearing_1_T = this.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getHeadSheave_1_bearing_1_T_id());
        actualList.add(new Actual(6, hoistParamsOne.getHeadSheave_1_bearing_1_T().getName(), headSheave_1_bearing_1_T, "℃"));
        // 3. 天轮1轴承2 温度
        String headSheave_1_bearing_2_T = this.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getHeadSheave_1_bearing_2_T_id());
        actualList.add(new Actual(7, hoistParamsOne.getHeadSheave_1_bearing_2_T().getName(), headSheave_1_bearing_2_T, "℃"));
        // 4. 天轮2轴承1 温度
        String headSheave_2_bearing_1_T = this.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getHeadSheave_2_bearing_1_T_id());
        actualList.add(new Actual(8, hoistParamsOne.getHeadSheave_2_bearing_1_T().getName(), headSheave_2_bearing_1_T, "℃"));
        // 2. 天轮2轴承2 温度
        String headSheave_2_bearing_2_T = this.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getHeadSheave_2_bearing_2_T_id());
        actualList.add(new Actual(9, hoistParamsOne.getHeadSheave_2_bearing_2_T().getName(), headSheave_2_bearing_2_T, "℃"));

        // 第三组
        // 1. 励磁电流
        String excitationCurrent = this.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getExcitationCurrentId());
        actualList.add(new Actual(10, hoistParamsOne.getExcitationCurrent().getName(), excitationCurrent, "A"));
        // 2. 电枢电流
        String armatureCurrent = this.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getArmatureCurrentId());
        actualList.add(new Actual(11, hoistParamsOne.getArmatureCurrent().getName(), armatureCurrent, "A"));
        // 3. 滚筒轴承1温度
        String drum_1_T = this.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getDrum_1_T_id());
        actualList.add(new Actual(12, hoistParamsOne.getDrum_1_T().getName(), drum_1_T, "℃"));
        // 4. 滚筒轴承2温度
        String drum_2_T = this.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getDrum_2_T_id());
        actualList.add(new Actual(13, hoistParamsOne.getDrum_2_T().getName(), drum_2_T, "℃"));
        // 5. 速度
        actualList.add(new Actual(14, hoistParamsOne.getSpeed().getName(), speed, "m/s"));

        return actualList;
    }

    /**
     * 每2秒请求一次数据，返回给前端当前的位置和最高点及最低点位置
     * @return JSON 的实际值对象列表
     */
    @Override
    public List<Actual> getGroup6() {
        HoistParams hoistParamsOne = hoistParamsService.getHoistParamsOne();
        List<Actual> actualList = new ArrayList<>();
        // 1. 实时位置1
        String position_1 = this.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getPosition_1_id());
        actualList.add(new Actual(1, hoistParamsOne.getPosition_1().getName(), position_1, "m"));
        // 2. 实时位置2
        String position_2 = this.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getPosition_2_id());
        actualList.add(new Actual(2, hoistParamsOne.getPosition_2().getName(), position_2, "m"));
        // 3. 最高点位置
        actualList.add(new Actual(3, "最高点位置", hoistParamsOne.getMaxHeight().toString(), "m"));
        // 4. 最低点位置
        actualList.add(new Actual(4, "最低点位置", hoistParamsOne.getMinHeight().toString(), "m"));
        return actualList;
    }

    /**
     * 获取全部的设备状态列表。
     * @return 设备状态列表
     */
    @Override
    public List<Actual> getGroup7() {
        HoistParams hoistParamsOne = hoistParamsService.getHoistParamsOne();
        List<Actual> actualList = new ArrayList<>();
        // 1. 设备信息
        actualList.add(new Actual(1, "设备名称", hoistParamsOne.getName(), ""));
        actualList.add(new Actual(2, "提升深度", hoistParamsOne.getDepth() + "m", "m"));
        actualList.add(new Actual(3, "额定功率", hoistParamsOne.getPower().toString() + "MW", "MW"));
        actualList.add(new Actual(4, "额定载荷", hoistParamsOne.getLoad().toString() + "t", "t"));

        // 2. 设备状态， 1：设备准备启动；2：设备开；3：设备关；4：设备顺序开；5：设备顺序关；6：设备部分开；7：设备准备启动，部分开
        // 前端从数据库中查出所有的设备状态列表，根据实际的状态code 显示设备状态。
        String hoistStateCode = this.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getHoistStateId());
        actualList.add(new Actual(5, hoistParamsOne.getHoistState().getName(), hoistStateCode, ""));
        // 3. 主电机状态， 1:电机运行；2：电机报警；3：电机故障
        // 前端从数据库中查出所有的设备状态列表，根据实际的状态code 显示设备状态。
        String motorStateCode = this.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getMotorStateId());
        actualList.add(new Actual(6, hoistParamsOne.getMotorState().getName(), motorStateCode, ""));
        String inspectionState = ""; //TODO 巡检状态应从后端管理系统中查出返回给前端
        actualList.add(new Actual(7, "巡检状态", inspectionState, ""));
        String inspectionNextTime = ""; //TODO 巡检时间应从后端管理系统中查出返回给前端
        actualList.add(new Actual(8, "下次巡检时间", inspectionNextTime, ""));
        String shiftMembers = ""; //TODO 当班人员名称应从后端管理系统中查出返回给前端 ， 以“，”隔开。如“张三， 李四， 王五”
        actualList.add(new Actual(9, "当班人员", shiftMembers, ""));
        return actualList;
    }

    /**
     * 每2秒 请求一次，获取电枢电流的百分比
     * @return json， 包装成1个Actual 对象
     */
    @Override
    public List<Actual> getGroup8() {
        // 获取电枢电流 double
        HoistParams hoistParamsOne = hoistParamsService.getHoistParamsOne();
        double armatureCurrent = Double.parseDouble(this.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getArmatureCurrentId()));
        // 获取额定的电流 double
        Double current = hoistParamsOne.getCurrent();
        // 计算百分比
        double percent = 0.0;
        if (current != null && !current.equals(0.0)) {
            percent = armatureCurrent / current;
        }
        List<Actual> actualList = new ArrayList<>();
        actualList.add(new Actual(1, "电枢电流百分比", String.valueOf(percent), "%"));
        return actualList;
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
    private String getCurrentValueFromOpcItemStateByItemId(Integer itemId) {
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
