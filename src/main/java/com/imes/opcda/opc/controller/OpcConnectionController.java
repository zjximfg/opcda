package com.imes.opcda.opc.controller;

import com.imes.opcda.opc.pojo.OpcConnection;
import com.imes.opcda.opc.service.OpcConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("connection")
public class OpcConnectionController {
    @Autowired
    private OpcConnectionService opcConnectionService;

    @RequestMapping("getProtocols")
    public ResponseEntity<List<String>> getProtocols() {
        return ResponseEntity.ok(opcConnectionService.getProtocols());
    }

    @RequestMapping("getAllConnections")
    public ResponseEntity<List<OpcConnection>> getAllConnections(@RequestParam(value = "sortType", required = false) String sortType,
                                                                 @RequestParam(value = "key", required = false) String key) {
        if (sortType == null) {
            sortType = "default";
        }
        List<OpcConnection> opcConnections = opcConnectionService.getAllConnections(sortType, key);
        return ResponseEntity.ok(opcConnections);
    }

    @RequestMapping("updateConnection")
    public ResponseEntity<List<OpcConnection>> updateConnection(@RequestBody OpcConnection opcConnection) {
        return ResponseEntity.ok(opcConnectionService.updateConnection(opcConnection));
    }

    @RequestMapping("insertConnection")
    public ResponseEntity<List<OpcConnection>> insertConnection(@RequestBody OpcConnection opcConnection) {
        return ResponseEntity.ok(opcConnectionService.insertConnection(opcConnection));
    }

    @RequestMapping("getConnectionMenu")
    public ResponseEntity<List<OpcConnection>> getConnectionMenu() {
        return ResponseEntity.ok(opcConnectionService.getConnectionMenu());
    }

    @RequestMapping("deleteConnection")
    public ResponseEntity<List<OpcConnection>> deleteConnection(@RequestBody OpcConnection opcConnection) {
        return ResponseEntity.ok(opcConnectionService.deleteConnection(opcConnection));
    }

    @RequestMapping("getOpcConnectionById")
    public ResponseEntity<OpcConnection> getOpcConnectionById(@RequestParam Integer connectionId) {
        return ResponseEntity.ok(opcConnectionService.getConnectionById(connectionId));
    }
}
