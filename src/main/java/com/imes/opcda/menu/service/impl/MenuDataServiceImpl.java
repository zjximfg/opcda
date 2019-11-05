package com.imes.opcda.menu.service.impl;

import com.imes.opcda.menu.pojo.MenuDataItem;
import com.imes.opcda.menu.service.MenuDataService;
import com.imes.opcda.opc.pojo.OpcConnection;
import com.imes.opcda.opc.pojo.OpcGroup;
import com.imes.opcda.opc.service.OpcConnectionService;
import com.imes.opcda.opc.service.OpcGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class MenuDataServiceImpl implements MenuDataService {
    @Autowired
    private OpcConnectionService connectionService;
    @Autowired
    private OpcGroupService opcGroupService;
    
    @Override
    public List<MenuDataItem> getMenuData() {
        // 主界面
        //MenuDataItem welcome = new MenuDataItem("welcome", "/welcome", "smile", "./Welcome", true);
        MenuDataItem server = new MenuDataItem("服务器与连接", "/server", "edit", "./server", true);
        List<OpcConnection> allConnections = connectionService.getAllConnections();
        List<MenuDataItem> connectionMenu = this.getConnectionMenu(allConnections);
        List<MenuDataItem> menuDataItems = new ArrayList<>();
        //menuDataItems.add(welcome);
        menuDataItems.add(server);
        menuDataItems.addAll(connectionMenu);
        return menuDataItems;
    }
    
    private List<MenuDataItem> getConnectionMenu(List<OpcConnection> connections) {
        List<MenuDataItem> connectionMenus = new ArrayList<>();
        for (OpcConnection connection : connections) {
            // 获取每个connection下的 group
            List<OpcGroup> opcGroupByPid = opcGroupService.getOpcGroupByPid(connection.getConnectionId());
            List<MenuDataItem> groupMenus = new ArrayList<>();
            for (OpcGroup opcGroup : opcGroupByPid) {
                MenuDataItem menuDataItem = new MenuDataItem(opcGroup.getGroupName(),
                        "/connection/" + connection.getConnectionId() + "/group/" + opcGroup.getGroupId(),
                        "edit",
                        "./group/id",
                        true);
                groupMenus.add(menuDataItem);
            }
            MenuDataItem menu = new MenuDataItem(connection.getConnectionName(), "/connection/" + connection.getConnectionId(), "edit");
            menu.setRoutes(groupMenus);
            connectionMenus.add(menu);
        }
        return connectionMenus;
    }
}
