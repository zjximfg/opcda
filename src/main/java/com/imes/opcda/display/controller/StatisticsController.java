package com.imes.opcda.display.controller;

import com.imes.opcda.display.service.FrequencyDateService;
import com.imes.opcda.display.service.FrequencyShiftService;
import com.imes.opcda.display.service.YieldDateService;
import com.imes.opcda.display.service.YieldShiftService;
import com.imes.opcda.display.vo.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("statistics")
public class StatisticsController {

    @Autowired
    private FrequencyShiftService frequencyShiftService;
    @Autowired
    private YieldShiftService yieldShiftService;
    @Autowired
    private FrequencyDateService frequencyDateService;
    @Autowired
    private YieldDateService yieldDateService;


    @GetMapping
    public ResponseEntity<List<Statistics>> getStatisticsList() {
        List<Statistics> statistics = new ArrayList<>();
        // 创建班次勾数显示对象
        Statistics frequencyShiftStatistics = frequencyShiftService.getFrequencyShiftStatistics();
        Statistics yieldShiftStatistics = yieldShiftService.getYieldShiftStatistics();
        Statistics frequencyDateStatistics = frequencyDateService.getFrequencyDateStatistics();
        Statistics yieldDateStatistics = yieldDateService.getYieldDateStatistics();

        // 将数据添加进list
        statistics.add(frequencyShiftStatistics);
        statistics.add(yieldShiftStatistics);
        statistics.add(frequencyDateStatistics);
        statistics.add(yieldDateStatistics);

        return ResponseEntity.ok(statistics);
    }
}
