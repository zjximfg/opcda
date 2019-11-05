package com.imes.opcda.display.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;


@Data
public class StatisticsDate {
    @KeySql(useGeneratedKeys = true)
    private Integer id;
    private String name;
    private Integer itemId;
    private Integer shiftTime; // 每天开始统计的小时数值 如8:00 24小时制

}
