package com.imes.opcda.presentation.service.impl;

import com.imes.opcda.core.service.CurrentCurveSaveService;
import com.imes.opcda.opc.pojo.OpcItemState;
import com.imes.opcda.opc.service.OpcItemStateService;
import com.imes.opcda.presentation.pojo.HoistParams;
import com.imes.opcda.presentation.pojo.Team;
import com.imes.opcda.presentation.pojo.TeamWorkers;
import com.imes.opcda.presentation.pojo.Worker;
import com.imes.opcda.presentation.service.*;
import com.imes.opcda.presentation.vo.Accum;
import com.imes.opcda.presentation.vo.Actual;
import com.imes.opcda.presentation.vo.Chart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PresentationServiceImpl implements PresentationService {

    @Autowired
    private HoistParamsService hoistParamsService;
    @Autowired
    private OpcItemStateService opcItemStateService;
    @Autowired
    private InspectionTasksService inspectionTasksService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private WorkerService workerService;

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
                frequencyShift = opcItemStateService.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getFrequencyMorningShiftId());
                // 当班产量
                yieldShift = opcItemStateService.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getYieldMorningShiftId());
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
                frequencyShift = opcItemStateService.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getFrequencyMiddleShiftId());
                // 当班产量
                yieldShift = opcItemStateService.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getYieldMiddleShiftId());
                // 当天勾数
                frequencyDate = Integer.toString(Integer.parseInt(frequencyShift) + Integer.parseInt(opcItemStateService.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getFrequencyMorningShiftId())));
                // 当天产量
                yieldDate = Integer.toString(Integer.parseInt(yieldShift) + Integer.parseInt(opcItemStateService.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getYieldMorningShiftId())));
                break;
            }
            case 3: {
                // 晚班
                // 1. 获取早班的实时值, 获得通讯内存中OpcTasks.currentOpcServer中的数据, 根据itemId
                // 当班勾数
                frequencyShift = opcItemStateService.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getFrequencyEveningShiftId());
                // 当班产量
                yieldShift = opcItemStateService.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getYieldEveningShiftId());
                // 当天勾数
                frequencyDate = Integer.toString(Integer.parseInt(frequencyShift) + Integer.parseInt(opcItemStateService.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getFrequencyMorningShiftId())) + Integer.parseInt(opcItemStateService.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getFrequencyMiddleShiftId())));
                // 当天产量
                yieldDate = Integer.toString(Integer.parseInt(yieldShift) + Integer.parseInt(opcItemStateService.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getYieldMorningShiftId())) + Integer.parseInt(opcItemStateService.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getYieldMiddleShiftId())));
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
        accums.add(new Accum("当班产量（吨数）",String.valueOf(Integer.parseInt(yieldShift) / 100  )));
        accums.add(new Accum("当天产量（吨数）", String.valueOf(Integer.parseInt(yieldDate) / 100  )));
        return accums;
    }

    /**
     * 条状图标，返回过去7天的每天的产能列表
     *
     * @param endDate 查询当天的当前日期，格式为 yyyy-MM-dd
     * @return chart 列表 7个固定的元素，没有查到的采用0 填充
     */
    @Override
    public List<Chart> getGroup2(Date endDate) {
        // 获取提升机的参数对象
        HoistParams hoistParamsOne = hoistParamsService.getHoistParamsOne();
        List<Chart> last7DaysChart = this.getLast7DaysChart(endDate, hoistParamsOne.getYieldId());
        for (Chart chart : last7DaysChart) {
            chart.setValue(String.valueOf(Double.valueOf(chart.getValue())/100));
        }
        return last7DaysChart;
    }

    /**
     * 条状图标，返回过去7天的每天的勾数列表
     *
     * @param endDate 查询当天的当前日期，格式为 yyyy-MM-dd
     * @return chart 列表 7个固定的元素，没有查到的采用0 填充
     */
    @Override
    public List<Chart> getGroup3(Date endDate) {
        // 获取提升机的参数对象
        HoistParams hoistParamsOne = hoistParamsService.getHoistParamsOne();

        return this.getLast7DaysChart(endDate, hoistParamsOne.getFrequencyId());
    }

    /**
     * 条状图标，返回过去7天的每天的 id 的数值列表
     *
     * @param endDate 查询当天的当前日期，格式为 yyyy-MM-dd
     * @param itemId  需要查询的变量名
     * @return chart 列表 7个固定的元素，没有查到的采用0 填充
     */
    private List<Chart> getLast7DaysChart(Date endDate, Integer itemId) {
        // 查询获取时间列表（前7天）
        List<Calendar> calendars = this.get7daysBefore(endDate);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");



        List<Double> maxList = new ArrayList<>();
        List<Double> minList = new ArrayList<>();
        // 查询当天的产能
        for (Calendar calendar : calendars) {

            // 查询当天的产能, 没有返回 “0”
            Map<String, Double> map = this.getAccumByCalendar(calendar, itemId);
            maxList.add(map.get("max"));
            minList.add(map.get("min"));
        }


        List<Chart> chartList = new ArrayList<>();
        for (int i = 1; i < 8; i++) {
            String value = "0";
            // 如果上一天已经清零
            if (minList.get(i - 1).equals(Double.parseDouble("0"))){
                value = String.valueOf(maxList.get(i));
            } else {
                // 如果上一天没有清零
                value = String.valueOf(maxList.get(i) - maxList.get(i - 1));
            }
            Chart chart = new Chart(simpleDateFormat.format(calendars.get(i).getTime()), value);
            chartList.add(chart);
        }
        return chartList;
    }


    /**
     * 1分钟内的实际速度值列表
     *
     * @return Chart的list对象
     */
    @Override
    public List<Chart> getGroup4() {

        // 查询 1分钟内历史数据
        // 1. 获取当前时间为startTime 秒级，1分钟前的时间为endTime 秒级
//        Timestamp startTime = new Timestamp(new Date().getTime() - 60 * 1000);
//        Timestamp endTime = new Timestamp(new Date().getTime());
        //System.out.println(startTime);
        // 2. 查询时间区间的数值OpcItemState并返回相应的时间和实际值
//        List<OpcItemState> speedCharts = opcItemStateService.getOpcItemStateBetweenStartTimeAndEndTime(startTime, endTime, hoistParamsOne.getSpeedId());
//        // 3. 整理时间和数值并创建Charts 列表
//        if (speedCharts != null && speedCharts.size() > 0) {
//            List<Chart> charts = new ArrayList<>();
//            for (OpcItemState speedChart : speedCharts) {
//                // 对时间进行格式化，转成日期时间字符串
//                String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(speedChart.getDatetime());
//                Chart chart = new Chart(dateStr, speedChart.getItemValue());
//                charts.add(chart);
//            }
//            return charts;
//        }
        List<Chart> speedCurveList = CurrentCurveSaveService.speedCurveList;
        return speedCurveList;
    }

    /**
     * 轮询播放 放大的设备展示，在设备周边展示的实时数据
     *
     * @return JSON 的实际值对象列表
     */
    @Override
    public List<Actual> getGroup5() {
        HoistParams hoistParamsOne = hoistParamsService.getHoistParamsOne();
        List<Actual> actualList = new ArrayList<>();
        // 第一组
        // 1. 速度
        String speed = opcItemStateService.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getSpeedId());
        actualList.add(new Actual(1, "实际速度", speed, "m/s"));
        // 2. 位置1
        String position_1 = opcItemStateService.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getPosition_1_id());
        actualList.add(new Actual(2, "实际位置1", position_1, "m"));
        // 3. 载重1
        String load_1 = opcItemStateService.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getLoad_1_id());
        actualList.add(new Actual(3, "实际载重1", load_1, "t"));
        // 4. 位置2
        String position_2 = opcItemStateService.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getPosition_2_id());
        actualList.add(new Actual(4, "实际位置1", position_2, "m"));

        // 第二组
        // 1. 速度
        actualList.add(new Actual(5, "实际速度", speed, "m/s"));
        // 2. 天轮1轴承1 温度
        String headSheave_1_bearing_1_T = opcItemStateService.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getHeadSheave_1_bearing_1_T_id());
        actualList.add(new Actual(6, "上天轮轴承1 温度", headSheave_1_bearing_1_T, "℃"));
        // 3. 天轮1轴承2 温度
        String headSheave_1_bearing_2_T = opcItemStateService.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getHeadSheave_1_bearing_2_T_id());
        actualList.add(new Actual(7, "上天轮轴承2 温度", headSheave_1_bearing_2_T, "℃"));
        // 4. 天轮2轴承1 温度
        String headSheave_2_bearing_1_T = opcItemStateService.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getHeadSheave_2_bearing_1_T_id());
        actualList.add(new Actual(8, "下天轮轴承1 温度", headSheave_2_bearing_1_T, "℃"));
        // 2. 天轮2轴承2 温度
        String headSheave_2_bearing_2_T = opcItemStateService.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getHeadSheave_2_bearing_2_T_id());
        actualList.add(new Actual(9, "下天轮轴承2 温度", headSheave_2_bearing_2_T, "℃"));

        // 第三组
        // 1. 励磁电流
        String excitationCurrent = opcItemStateService.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getExcitationCurrentId());
        actualList.add(new Actual(10, "励磁电流", String.valueOf((Double.valueOf(excitationCurrent)) * 1000), "A"));
        // 2. 电枢电流
        String armatureCurrent = opcItemStateService.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getArmatureCurrentId());
        actualList.add(new Actual(11, "电枢电流", String.valueOf((Double.valueOf(armatureCurrent)) * 1000), "A"));
        // 3. 滚筒轴承1温度
        String drum_1_T = opcItemStateService.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getDrum_1_T_id());
        actualList.add(new Actual(12, "滚筒轴承1 温度", drum_1_T, "℃"));
        // 4. 滚筒轴承2温度
        String drum_2_T = opcItemStateService.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getDrum_2_T_id());
        actualList.add(new Actual(13, "滚筒轴承2 温度", drum_2_T, "℃"));
        // 5. 速度
        actualList.add(new Actual(14, "实际速度", speed, "m/s"));

        return actualList;
    }

    /**
     * 每2秒请求一次数据，返回给前端当前的位置和最高点及最低点位置
     *
     * @return JSON 的实际值对象列表
     */
    @Override
    public List<Actual> getGroup6() {
        HoistParams hoistParamsOne = hoistParamsService.getHoistParamsOne();
        List<Actual> actualList = new ArrayList<>();
        // 1. 实时位置1
        String position_1 = opcItemStateService.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getPosition_1_id());
        actualList.add(new Actual(1,"实际位置1", position_1, "m"));
        // 2. 实时位置2
        String position_2 = opcItemStateService.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getPosition_2_id());
        actualList.add(new Actual(2,"实际位置2", position_2, "m"));
        // 3. 最高点位置
        actualList.add(new Actual(3, "最高点位置", hoistParamsOne.getMaxHeight().toString(), "m"));
        // 4. 最低点位置
        actualList.add(new Actual(4, "最低点位置", hoistParamsOne.getMinHeight().toString(), "m"));
        // 5. 实时速度
        String speed = opcItemStateService.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getSpeedId());
        actualList.add(new Actual(5, "实际速度", String.valueOf(Double.valueOf(speed)), "m/s"));
        return actualList;
    }

    /**
     * 获取全部的设备状态列表。
     *
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
        actualList.add(new Actual(4, "额定载荷", hoistParamsOne.getNormalLoad().toString() + "t", "t"));

        // 2. 设备状态， 1：设备准备启动；2：设备开；3：设备关；4：设备顺序开；5：设备顺序关；6：设备部分开；7：设备准备启动，部分开
        // 前端从数据库中查出所有的设备状态列表，根据实际的状态code 显示设备状态。
        String hoistStateCode = opcItemStateService.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getHoistStateId());
        actualList.add(new Actual(5, "提升机状态", hoistStateCode, ""));
        // 3. 主电机状态， 1:电机运行；2：电机报警；3：电机故障
        // 前端从数据库中查出所有的设备状态列表，根据实际的状态code 显示设备状态。
        String motorStateCode = opcItemStateService.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getMotorStateId());
        actualList.add(new Actual(6, "主电机状态", motorStateCode, ""));
        String inspectionState = inspectionTasksService.getStatus();
        actualList.add(new Actual(7, "巡检状态", inspectionState, ""));
        String inspectionNextTime = inspectionTasksService.getTime();
        actualList.add(new Actual(8, "下次巡检时间", inspectionNextTime, ""));
        String shiftMembers = this.currentWorkersName();
        actualList.add(new Actual(9, "当班人员", shiftMembers, ""));
        return actualList;
    }

    /**
     * 每2秒 请求一次，获取电枢电流的百分比
     *
     * @return json， 包装成1个Actual 对象
     */
    @Override
    public List<Actual> getGroup8() {
        // 获取电枢电流 double
        HoistParams hoistParamsOne = hoistParamsService.getHoistParamsOne();
        double armatureCurrent = Double.parseDouble(opcItemStateService.getCurrentValueFromOpcItemStateByItemId(hoistParamsOne.getArmatureCurrentId()));
        // 获取额定的电流 double
        Double current = hoistParamsOne.getCurrent();
        // 计算百分比
        double percent = 0.0;
        if (current != null && !current.equals(0.0)) {
            percent = armatureCurrent *1000 / current * 100;
        }
        List<Actual> actualList = new ArrayList<>();
        actualList.add(new Actual(1, "电枢电流百分比", String.valueOf(percent), "%"));
        return actualList;
    }

    private String currentWorkersName() {
        List<Team> teamList = teamService.findAllList();

        StringBuilder stringBuilder = new StringBuilder();

        //轮询班次表
        for (Team team : teamList) {
            Date current = new Date();
            //判断当前时间在有效期内
            if (current.before(team.getEnd()) && current.after(team.getStart())) {
                //获取班组人员列表
                team = teamService.getTeamWorkersByTeam(team);
                List<TeamWorkers> nodeTeamWorkersList = team.getTeamWorkersList();
                for(TeamWorkers teamWorkers : nodeTeamWorkersList){
                    Worker workersById = workerService.getWorkersById(teamWorkers.getWorker());
                    if (stringBuilder.length() > 0) {//该步即不会第一位有逗号，也防止最后一位拼接逗号！
                        stringBuilder.append(",");
                    }
                    if (workersById.getName()!=null) {
                        stringBuilder.append(workersById.getName());
                    }
                }
            }
        }

        return stringBuilder.toString();
    }

    /**
     * 判断并返回 当前时间为早班、中班 还是晚班
     *
     * @param hoistParams 传入的 早，中，晚 三班的起始时间小时数
     * @return 返回 1. 早班， 2. 中班， 3. 晚班
     */
    private Integer getShiftNum(HoistParams hoistParams) {
      //  System.out.println(hoistParams);
        // 获取当前的小时数
        Integer currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
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
     * 获取传入日期前7天的日历对象列表
     *
     * @param endDate 传入日期的前7天
     * @return 前7天的日历对象列表
     */
    protected List<Calendar> get7daysBefore(Date endDate) {
        List<Calendar> calendars = new ArrayList<>();
        for (int i = 8; i >= 1; i--) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(endDate);
            int currentDate = calendar.get(Calendar.DAY_OF_MONTH);
            calendar.set(Calendar.DAY_OF_MONTH, currentDate - i);
            calendars.add(calendar);

        }
        return calendars;
    }

    /**
     * 获取传入日期的产能
     *
     * @param calendar 传入的日期
     * @return 产能字符串
     */
    private Map<String, Double> getAccumByCalendar(Calendar calendar, Integer opcItemId) {

        double max = 0.0;
        double min = 0.0;
        Map<String, Double> map = new HashMap<>();
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Timestamp startTime = new Timestamp(calendar.getTimeInMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Timestamp endTime = new Timestamp(calendar.getTimeInMillis());
        List<OpcItemState> opcItemStates = opcItemStateService.getOpcItemStateBetweenStartTimeAndEndTime(startTime, endTime, opcItemId);
//        return opcItemStateService.getAccumValue(startTime, endTime, opcItemId);
        if (opcItemStates != null && opcItemStates.size() > 0) {
            // 获取最大值 和最小值
            List<Double> list = new ArrayList<>();
            for (OpcItemState opcItemState : opcItemStates) {
                if (opcItemState.getItemValue().equals("读取失败")) {
                    list.add(0.0);
                } else {
                    list.add(Double.parseDouble(opcItemState.getItemValue()));
                }
            }
            max = Collections.max(list);
            min = Collections.min(list);
            System.out.println(max);
            System.out.println(min);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(simpleDateFormat.format(calendar.getTimeInMillis()));
        map.put("max", max);
        map.put("min", min);
        return map;
    }
}
