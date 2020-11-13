package com.imes.opcda.presentation.service.interface_test_impl;

import com.imes.opcda.presentation.pojo.HoistParams;
import com.imes.opcda.presentation.service.HoistParamsService;
import com.imes.opcda.presentation.service.impl.PresentationServiceImpl;
import com.imes.opcda.presentation.vo.Accum;
import com.imes.opcda.presentation.vo.Actual;
import com.imes.opcda.presentation.vo.Chart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
//@Service
//
public class PresentationServiceTestImpl extends PresentationServiceImpl {

    @Autowired
    private HoistParamsService hoistParamsService;

    @Override
    public List<Accum> getGroup1() {
        // 随机 100 - 200
        Accum accum_1 = new Accum("当班提升勾数", String.valueOf(this.getRandomInt(100, 200)));
        Accum accum_2 = new Accum("当天提升勾数", String.valueOf(this.getRandomInt(100, 200)));
        // 随机 1000 - 2000
        Accum accum_3 = new Accum("当班产量（吨数）", String.valueOf(this.getRandomDoubleStr(1000, 2000)));
        Accum accum_4 = new Accum("当天产量（吨数）", String.valueOf(this.getRandomDoubleStr(1000, 2000)));

        List<Accum> list = new ArrayList<>();
        list.add(accum_1);
        list.add(accum_2);
        list.add(accum_3);
        list.add(accum_4);
        return list;
    }

    @Override
    public List<Chart> getGroup2(Date endDate) {
        // 获取前7天的列表
        List<Calendar> daysBefore = this.get7daysBefore(endDate);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List<Chart> chartList = new ArrayList<>();
        for (Calendar calendar : daysBefore) {
            // 生成随机的累计吨数
            String randomDoubleStr = this.getRandomDoubleStr(10000, 15000);
            Chart chart = new Chart(simpleDateFormat.format(calendar.getTime()), randomDoubleStr);
            chartList.add(chart);
        }
        return chartList;
    }

    @Override
    public List<Chart> getGroup3(Date endDate) {
        return getGroup2(endDate);
    }

    @Override
    public List<Chart> getGroup4() {
        int randomInt = this.getRandomInt(50, 60);
        List<Chart> charts = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            // 生成数据
            // 时间
            Timestamp startTime = new Timestamp(new Date().getTime() - i * 1000);
            String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime);
            // 值
            Chart chart = new Chart(dateStr, this.getRandomDoubleStr(60, 300));
            charts.add(chart);
        }
        return charts;
    }

    @Override
    public List<Actual> getGroup5() {
        List<Actual> actualList = new ArrayList<>();
        // 第一组
        // 1. 速度
        actualList.add(new Actual(1, "实际速度", this.getRandomDoubleStr(2, 10), "m/s"));
        // 2. 位置1
        actualList.add(new Actual(2, "实际位置1", this.getRandomDoubleStr(-200, 100), "m"));
        // 3. 载重1
        actualList.add(new Actual(3, "实际载重1", this.getRandomDoubleStr(5, 20), "t"));
        // 4. 位置2
        actualList.add(new Actual(4, "实际位置1", this.getRandomDoubleStr(-200, 100), "m"));

        // 第二组
        // 1. 速度
        actualList.add(new Actual(5, "实际速度", this.getRandomDoubleStr(2, 10), "m/s"));
        // 2. 天轮1轴承1 温度
        actualList.add(new Actual(6, "上天轮轴承1 温度", this.getRandomDoubleStr(23, 40), "℃"));
        // 3. 天轮1轴承2 温度
        actualList.add(new Actual(7, "上天轮轴承2 温度", this.getRandomDoubleStr(23, 40), "℃"));
        // 4. 天轮2轴承1 温度
        actualList.add(new Actual(8, "下天轮轴承1 温度", this.getRandomDoubleStr(23, 40), "℃"));
        // 2. 天轮2轴承2 温度
        actualList.add(new Actual(9, "下天轮轴承2 温度", this.getRandomDoubleStr(23, 40), "℃"));

        // 第三组
        // 1. 励磁电流
        actualList.add(new Actual(10, "励磁电流", this.getRandomDoubleStr(60, 100), "A"));
        // 2. 电枢电流
        actualList.add(new Actual(11, "电枢电流", this.getRandomDoubleStr(60, 300), "A"));
        // 3. 滚筒轴承1温度
        actualList.add(new Actual(12, "滚筒轴承1 温度", this.getRandomDoubleStr(23, 40), "℃"));
        // 4. 滚筒轴承2温度
        actualList.add(new Actual(13, "滚筒轴承2 温度", this.getRandomDoubleStr(23, 40), "℃"));
        // 5. 速度
        actualList.add(new Actual(14, "实际速度", this.getRandomDoubleStr(2, 10), "m/s"));

        return actualList;
    }

    @Override
    public List<Actual> getGroup6() {
        HoistParams hoistParamsOne = hoistParamsService.getHoistParamsOne();
        List<Actual> actualList = new ArrayList<>();
        // 1. 实时位置1
        actualList.add(new Actual(1, "实际位置1", this.getRandomDoubleStr(-200, 100), "m"));
        // 2. 实时位置2
        actualList.add(new Actual(2, "实际位置2", this.getRandomDoubleStr(-200, 100), "m"));
        // 3. 最高点位置
        actualList.add(new Actual(3, "最高点位置", "500", "m"));
        // 4. 最低点位置
        actualList.add(new Actual(4, "最低点位置", "-200", "m"));
        return actualList;
    }

    /**
     * 获取全部的设备状态列表。
     *
     * @return 设备状态列表
     */
    @Override
    public List<Actual> getGroup7() {
        List<Actual> actualList = new ArrayList<>();
        // 1. 设备信息
        actualList.add(new Actual(1, "设备名称", "口孜东主井提升机（东）", ""));
        actualList.add(new Actual(2, "提升深度", 800 + "m", "m"));
        actualList.add(new Actual(3, "额定功率", 4 + "MW", "MW"));
        actualList.add(new Actual(4, "额定载荷", 18 + "t", "t"));

        // 2. 设备状态， 1：设备准备启动；2：设备开；3：设备关；4：设备顺序开；5：设备顺序关；6：设备部分开；7：设备准备启动，部分开
        // 前端从数据库中查出所有的设备状态列表，根据实际的状态code 显示设备状态。
        actualList.add(new Actual(5, "提升机状态", String.valueOf(this.getRandomInt(1, 7)), ""));
        // 3. 主电机状态， 1:电机运行；2：电机报警；3：电机故障
        // 前端从数据库中查出所有的设备状态列表，根据实际的状态code 显示设备状态。
        actualList.add(new Actual(6, "主电机状态", String.valueOf(this.getRandomInt(1,3)), ""));
        String inspectionState = "巡检中"; //TODO 巡检状态应从后端管理系统中查出返回给前端
        actualList.add(new Actual(7, "巡检状态", inspectionState, ""));
        String inspectionNextTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date().getTime() + 3600 * 1000); //TODO 巡检时间应从后端管理系统中查出返回给前端
        actualList.add(new Actual(8, "下次巡检时间", inspectionNextTime, ""));
        String shiftMembers = "张三， 李四， 王五"; //TODO 当班人员名称应从后端管理系统中查出返回给前端 ， 以“，”隔开。如“张三， 李四， 王五”
        actualList.add(new Actual(9, "当班人员", shiftMembers, ""));
        return actualList;
    }
    @Override
    public List<Actual> getGroup8() {
        List<Actual> actualList = new ArrayList<>();
        actualList.add(new Actual(1, "电枢电流百分比", String.valueOf(this.getRandomDoubleStr(1, 170)), "%"));
        return actualList;
    }

    /**
     * 获取 传入的两个值中间的随机数
     *
     * @param start 范围内的起始值
     * @param end   范围内的结束值
     * @return 随机生成的范围内的整数
     */
    private int getRandomInt(int start, int end) {
        double result = Math.random() * end + start;
        return (int) result;
    }

    /**
     * 获取 两位小数的随机小数字符串
     *
     * @param start 范围内的起始值
     * @param end   范围内的结束值
     * @return 随机生成的范围内的两位小数的随机小数字符串
     */
    private String getRandomDoubleStr(int start, int end) {
        double result = Math.random() * end + start;
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(result);
    }
}
