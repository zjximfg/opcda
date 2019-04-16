package com.imes.opcda.opc.controller;

import com.imes.opcda.opc.pojo.OpcServer;
import com.imes.opcda.opc.service.OpcServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("opcServer")
public class OpcServerController {

    @Autowired
    private OpcServerService opcServerService;

    @RequestMapping("getProgIds")
    public ResponseEntity<List<String>> getProgIds() {
        return ResponseEntity.ok(opcServerService.getProgIds());
    }

    @RequestMapping("getOpcServer")
    public ResponseEntity<OpcServer> getOpcServer() {
        return ResponseEntity.ok(opcServerService.getOpcServer());
    }

    @RequestMapping("updateOpcServer")
    public ResponseEntity<OpcServer> updateOpcServer(@RequestBody OpcServer opcServer) {
        return ResponseEntity.ok(opcServerService.updateOpcServer(opcServer));
    }
}
