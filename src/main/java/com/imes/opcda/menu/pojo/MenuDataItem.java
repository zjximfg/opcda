package com.imes.opcda.menu.pojo;

import lombok.Data;

import java.util.List;

@Data
public class MenuDataItem {
    private List<MenuDataItem> routes;
    private Boolean exact;
    private String icon;
    private String locale;
    private String name;
    private String path;
    private String component;



    /**
     * 用户界面 带图标
     * @param name
     * @param path
     * @param icon
     */
    public MenuDataItem(String name, String path, String icon) {
        this.name = name;
        this.path = path;
        this.icon = icon;
    }

    /**
     * 用户界面 带图标
     * @param name
     * @param path
     * @param icon
     * @param component
     */
    public MenuDataItem(String name, String path, String icon, String component, Boolean exact) {
        this.name = name;
        this.path = path;
        this.icon = icon;
        this.exact = exact;
        this.component = component;

    }

}
