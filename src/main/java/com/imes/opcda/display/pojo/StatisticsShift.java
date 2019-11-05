package com.imes.opcda.display.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Transient;

@Data
public class StatisticsShift {
    @KeySql(useGeneratedKeys = true)
    private Integer id;
    private String name;
    private Integer itemId;
    private Integer shiftNum;  // 一天几班？ 仅支持2 or 3
    private Integer shiftStartTime_1; // 第一班的起始小时数
    private Integer shiftStartTime_2; // 第二班的起始小时数
    private Integer shiftStartTime_3; // 第三班的起始小时数， 当shiftNum为2时，不显示

    @Transient
    private Integer currentShift;  //当前班次数， 1， 2， 3
    private Integer startTime;
}
