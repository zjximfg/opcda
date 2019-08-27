package com.imes.opcda.opc.controller;

import com.imes.opcda.opc.pojo.UpdateRate;
import com.imes.opcda.opc.service.UpdateRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("updateRate")
public class UpdateRateController {

    @Autowired
    private UpdateRateService updateRateService;

    @RequestMapping("getUpdateRates")
    public ResponseEntity<List<UpdateRate>> getUpdateRates() {
        return ResponseEntity.ok(updateRateService.getUpdateRates());
    }
}
