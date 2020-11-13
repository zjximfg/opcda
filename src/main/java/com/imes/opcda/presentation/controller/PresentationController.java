package com.imes.opcda.presentation.controller;

import com.imes.opcda.core.tasks.OpcTasks;
import com.imes.opcda.presentation.pojo.HoistState;
import com.imes.opcda.presentation.pojo.MotorState;
import com.imes.opcda.presentation.service.HoistStateService;
import com.imes.opcda.presentation.service.MotorStateService;
import com.imes.opcda.presentation.service.PresentationService;
import com.imes.opcda.presentation.vo.Accum;
import com.imes.opcda.presentation.vo.Actual;
import com.imes.opcda.presentation.vo.Chart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("presentation/api")
public class PresentationController {

    @Autowired
    private PresentationService presentationService;
    @Autowired
    private HoistStateService hoistStateService;
    @Autowired
    private MotorStateService motorStateService;

    /**
     * 累计数据，当班勾数，当班产能，当天勾数，当天产能
     * @return json
     */
    @GetMapping("group/1")
    public ResponseEntity<List<Accum>> getGroupData_1() {
        List<Accum> accumList = presentationService.getGroup1();
        return ResponseEntity.ok(accumList);
    }

    /**
     * 图标，条形图，去过7天产量
     * @param endDate 请求的当前日期，格式为 yyyy-MM-dd
     * @return json 数组
     */
    @GetMapping("group/2")
    public ResponseEntity<List<Chart>> getGroupData_2(@RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        //List<Chart> charts = presentationService.getGroup2(endDate);
        return ResponseEntity.ok(OpcTasks.group2ResultList);
    }

    /**
     * 图标，条形图，去过7天勾数
     * @return json
     */
    @GetMapping("group/3")
    public ResponseEntity<List<Chart>> getGroupData_3(@RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        //List<Chart> charts = presentationService.getGroup3(endDate);
        return ResponseEntity.ok(OpcTasks.group3ResultList);
    }

    /**
     * 图标，折线图，当前的实时运行速度
     * @return json
     */
    @GetMapping("group/4")
    public ResponseEntity<List<Chart>> getGroupData_4() {
        List<Chart> charts = presentationService.getGroup4();
        return ResponseEntity.ok(charts);
    }

    /**
     * 大图旁的 实时数据反馈值
     * @return json， 包括14个数值
     */
    @GetMapping("group/5")
    public ResponseEntity<List<Actual>> getGroupData_5() {
        List<Actual> actualList = presentationService.getGroup5();
        return ResponseEntity.ok(actualList);
    }

    /**
     * 每 2 秒 返回一次数据，实时的位置值和 最高点 及 最低点位置
     * @return json， 包括4个数值 包装成4个Actual 对象
     */
    @GetMapping("group/6")
    public ResponseEntity<List<Actual>> getGroupData_6() {
        List<Actual> actualList = presentationService.getGroup6();
        return ResponseEntity.ok(actualList);
    }

    /**
     * 管理型数据，资料数据， 每小时访问一次
     * @return json， 包装成Actual 对象
     */
    @GetMapping("group/7")
    public ResponseEntity<List<Actual>> getGroupData_7() {
        List<Actual> actualList = presentationService.getGroup7();
        return ResponseEntity.ok(actualList);
    }

    /**
     * 每2秒 请求一次，获取电枢电流的百分比
     * @return json， 包装成1个Actual 对象
     */
    @GetMapping("group/8")
    public ResponseEntity<List<Actual>> getGroupData_8() {
        List<Actual> actualList = presentationService.getGroup8();
        return ResponseEntity.ok(actualList);
    }

    /**
     * 获取全部的设备状态列表。
     * @return 设备状态列表
     */
    @GetMapping("hoistState/list")
    public ResponseEntity<List<HoistState>> getHoistStateList() {
        List<HoistState> hoistStateList = hoistStateService.getHoistStateList();
        return ResponseEntity.ok(hoistStateList);
    }

    /**
     * 获取全部的主状态列表。
     * @return 主电机状态列表
     */
    @GetMapping("motorState/list")
    public ResponseEntity<List<MotorState>> getMotorStateList() {
        List<MotorState> motorStateList = motorStateService.getMotorStateList();
        return ResponseEntity.ok(motorStateList);
    }
}
