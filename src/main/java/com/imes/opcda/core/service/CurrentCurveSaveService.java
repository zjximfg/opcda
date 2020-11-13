package com.imes.opcda.core.service;


import com.imes.opcda.opc.service.OpcItemStateService;
import com.imes.opcda.presentation.pojo.HoistParams;
import com.imes.opcda.presentation.vo.Chart;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

@Data
@Component
public class CurrentCurveSaveService implements Runnable {

    @Autowired
    private OpcItemStateService opcItemStateService;

    private HoistParams hoistParams;

    private ScheduledFuture scheduledFuture;

    public static List<Chart> speedCurveList;

    @Override
    public void run() {

        if (scheduledFuture.isCancelled()) {
            Thread.currentThread().interrupt();
            return;
        }
        String speed = opcItemStateService.getCurrentValueFromOpcItemStateByItemId(hoistParams.getSpeedId());
        String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Chart chart = new Chart(dateStr, speed);
        if (speedCurveList == null) {
            speedCurveList = new ArrayList<>();
        }
        if (speedCurveList.size() > 60) {
            speedCurveList.remove(0);
        }
        speedCurveList.add(chart);
    }
}
