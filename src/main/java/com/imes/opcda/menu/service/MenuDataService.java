package com.imes.opcda.menu.service;

import com.imes.opcda.menu.pojo.MenuDataItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MenuDataService {
    List<MenuDataItem> getMenuData();
}
