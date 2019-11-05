package com.imes.opcda.menu.controller;

import com.imes.opcda.menu.pojo.MenuDataItem;
import com.imes.opcda.menu.service.MenuDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("menu")
public class MenuDataController {

    @Autowired
    private MenuDataService menuDataService;

    @GetMapping("list")
    public ResponseEntity<List<MenuDataItem>> getMenuData() {
        return ResponseEntity.ok(menuDataService.getMenuData());
    }
}
