package com.imes.opcda.presentation.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@Table(name = "tbl_hoist_params")
public class HoistParams {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;
    private String name;
    private Double depth;   //提升高度 m
    private Double power;   //提升机额定功率 MW
    private Double load;    //提升机额定载荷 MW
    private Double current; //额定电枢电流 A
    private Integer startHourShift_morning;   //早班 的起始时间
    private Integer startHourShift_middle;    //中班 的起始时间
    private Integer startHourShift_evening;   //晚班 的起始时间


//    实时速度
//    箕斗1实时位置
//    箕斗2实时位置
//    实时载重吨数1
//    实时载重吨数2
//    天轮1轴承1温度
//    天轮1轴承2温度
//    天轮2轴承1温度
//    天轮2轴承2温度
//    励磁电流
//    电枢电流
//    滚筒轴承1温度
//    滚筒轴承2温度
//    设备状态

    private Integer frequencyMorningShiftId;
    private Integer frequencyMiddleShiftId;
    private Integer frequencyEveningShiftId;
    private Integer yieldMorningShiftId;
    private Integer yieldMiddleShiftId;
    private Integer yieldEveningShiftId;
    private Integer speedId;
    private Integer position_1_id;
    private Integer position_2_id;
    private Integer load_1_id;
    private Integer load_2_id;
    private Integer headSheave_1_bearing_1_T_id;
    private Integer headSheave_1_bearing_2_T_id;
    private Integer headSheave_2_bearing_1_T_id;
    private Integer headSheave_2_bearing_2_T_id;
    private Integer excitationCurrentId;
    private Integer armatureCurrentId;
    private Integer drum_1_T_id;
    private Integer drum_2_T_id;
    private Integer stateId;



    @Transient
    private ActualValue frequencyMorningShift;
    @Transient
    private ActualValue frequencyMiddleShift;
    @Transient
    private ActualValue frequencyEveningShift;
    @Transient
    private ActualValue yieldMorningShift;
    @Transient
    private ActualValue yieldMiddleShift;
    @Transient
    private ActualValue yieldEveningShift;
    @Transient
    private ActualValue speed;
    @Transient
    private ActualValue position_1;
    @Transient
    private ActualValue position_2;
    @Transient
    private ActualValue load_1;
    @Transient
    private ActualValue load_2;
    @Transient
    private ActualValue headSheave_1_bearing_1_T;
    @Transient
    private ActualValue headSheave_1_bearing_2_T;
    @Transient
    private ActualValue headSheave_2_bearing_1_T;
    @Transient
    private ActualValue headSheave_2_bearing_2_T;
    @Transient
    private ActualValue excitationCurrent;
    @Transient
    private ActualValue armatureCurrent;
    @Transient
    private ActualValue drum_1_T;
    @Transient
    private ActualValue drum_2_T;
    @Transient
    private ActualValue state;

}
