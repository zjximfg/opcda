package com.imes.opcda.opc.controller;

import com.imes.opcda.common.pojo.PageResponse;
import com.imes.opcda.opc.pojo.OpcItem;
import com.imes.opcda.opc.service.OpcItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("opcItem")
public class OpcItemController {
    @Autowired
    private OpcItemService opcItemService;

    @RequestMapping("getDataTypes")
    public ResponseEntity<List<String>> getDataTypes() {
        return ResponseEntity.ok(opcItemService.getDataTypes());
    }

    @RequestMapping("getItemCatalogs")
    public ResponseEntity<List<String>> getItemCatalogs() {
        return ResponseEntity.ok(opcItemService.getItemCatalogs());
    }

    @RequestMapping("getItemName")
    public ResponseEntity<String> getItemName(@RequestBody OpcItem opcItem) {
        return ResponseEntity.ok(opcItemService.createItemName(opcItem));
    }

    @RequestMapping("insertOpcItem")
    public ResponseEntity insertOpcItem(@RequestBody OpcItem opcItem) {
        Integer result = opcItemService.insertOpcItem(opcItem);
        if (result == 1) {
            return new ResponseEntity(HttpStatus.OK);
        }else
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
    }

    @RequestMapping("getOpcItemsByPage")
    public ResponseEntity<PageResponse<OpcItem>> getOpcItemsByPage(@RequestParam("currentPage") int currentPage,
                                                                   @RequestParam("pageSize") int pageSize,
                                                                   @RequestParam("searchKey") String searchKey,
                                                                   @RequestParam("groupId") Integer groupId) {
        PageResponse<OpcItem> pageResponse = opcItemService.getOpcItemsByPage(currentPage, pageSize, searchKey, groupId);
        return ResponseEntity.ok(pageResponse);
    }

    @RequestMapping("updateOpcItem")
    public ResponseEntity updateOpcItem(@RequestBody OpcItem opcItem) {
        Integer result = opcItemService.updateOpcItem(opcItem);
        if (result == 1) {
            return new ResponseEntity(HttpStatus.OK);
        }else
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
    }

    @RequestMapping("deleteOpcItem")
    public ResponseEntity deleteOpcItem(@RequestBody OpcItem opcItem) {
        Integer result = opcItemService.deleteOpcItem(opcItem);
        if (result == 1) {
            return new ResponseEntity(HttpStatus.OK);
        }else
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
    }
}
