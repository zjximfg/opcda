package com.imes.opcda.opc.controller;

import com.imes.opcda.opc.pojo.OpcGroup;
import com.imes.opcda.opc.service.OpcGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("opcGroup")
public class OpcGroupController {
    @Autowired
    private OpcGroupService opcGroupService;

    @RequestMapping("getAllOpcGroups")
    public ResponseEntity<List<OpcGroup>> getAllOpcGroups() {
        return ResponseEntity.ok(opcGroupService.getAllOpcGroups());
    }

    @RequestMapping("insertOpcGroup")
    public ResponseEntity<List<OpcGroup>> insertOpcGroup(@RequestBody OpcGroup opcGroup) {
        return ResponseEntity.ok(opcGroupService.insertOpcGroup(opcGroup));
    }

    @RequestMapping("updateOpcGroup")
    public ResponseEntity<List<OpcGroup>> updateOpcGroup(@RequestBody OpcGroup opcGroup) {
        return ResponseEntity.ok(opcGroupService.updateOpcGroup(opcGroup));
    }

    @RequestMapping("deleteOpcGroup")
    public ResponseEntity<List<OpcGroup>> deleteOpcGroup(@RequestBody OpcGroup opcGroup) {
        return ResponseEntity.ok(opcGroupService.deleteOpcGroup(opcGroup));
    }

    @RequestMapping("getOpcGroupById")
    public ResponseEntity<OpcGroup> getOpcGroupById(@RequestParam("groupId") Integer groupId) {
        return ResponseEntity.ok(opcGroupService.getOpcGroupById(groupId));
    }
}
