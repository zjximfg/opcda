package com.imes.opcda.core.controller;

import com.imes.opcda.core.service.OpcdaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("opcda")
public class OpcdaController {

    @Autowired
    private OpcdaService opcdaService;

    @GetMapping("restartServer")
    public ResponseEntity<Void> restartServer() {
        opcdaService.restartServer();
        return ResponseEntity.ok().build();
    }

    @GetMapping("getServerState")
    public ResponseEntity<String> getServerState() {
        String state = opcdaService.getServerState();
        return ResponseEntity.ok(state);
    }
}
