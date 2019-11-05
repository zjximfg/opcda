package com.imes.opcda.presentation.controller;

import com.imes.opcda.presentation.service.PresentationService;
import com.imes.opcda.presentation.vo.Accum;
import com.imes.opcda.presentation.vo.Chart;
import com.sun.xml.internal.bind.v2.TODO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("presentation/api")
public class PresentationController {

    @Autowired
    private PresentationService presentationService;

    /**
     * 累计数据，当班勾数，当班产能，当天勾数，当天产能
     * @return json
     */
    @GetMapping("group/1")
    public ResponseEntity<List<Accum>> getGroup1Data_1() {
        List<Accum> accums = presentationService.getGroup1();
        return ResponseEntity.ok(accums);
    }

    /**
     * 图标，条形图，去过7天产量
     * @return json
     */
    @GetMapping("group/2")
    public ResponseEntity<List<Chart>> getGroupData_2() {
        //TODO
        List<Chart> charts = new ArrayList<>(7);
        return ResponseEntity.ok(charts);
    }

    /**
     * 图标，条形图，去过7天勾数
     * @return json
     */
    @GetMapping("group/2")
    public ResponseEntity<List<Chart>> getGroupData_3() {
        //TODO
        List<Chart> charts = new ArrayList<>(7);
        return ResponseEntity.ok(charts);
    }

    /**
     * 图标，折线图，当前的实时运行速度
     * @return json
     */
    @GetMapping("group/2")
    public ResponseEntity<List<Chart>> getGroupData_4() {
        List<Chart> charts = new ArrayList<>(7);
        return ResponseEntity.ok(charts);
    }
}
