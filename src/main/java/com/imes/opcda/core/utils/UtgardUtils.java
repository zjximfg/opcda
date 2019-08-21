package com.imes.opcda.core.utils;

import com.imes.opcda.opc.pojo.OpcGroup;
import com.imes.opcda.opc.pojo.OpcItem;
import com.imes.opcda.opc.pojo.OpcServer;
import lombok.extern.slf4j.Slf4j;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.da.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

@Slf4j
public class UtgardUtils {

    public static Server server;
    public static boolean isConnected; // =true 已经连接
    public static boolean isSuccessAddGroup; // =true 添加组完成
    public static List<Group> groups;
    public static List<Integer> updateRates;
    public static List<Item> items;
    public static List<ItemState> itemStates;
    public static AutoReconnectController autoReconnectController;
    public static AccessBase syncAccessBase; //同步连接
    public static AccessBase asyncAccessBase; //异步连接
    public static boolean isReading;

    /**
     * 建立服务器
     * @param opcServer 从数据库中查询的opcServer对象
     * @param scheduledExecutorService 线程池
     */
    public static void createServer(OpcServer opcServer, ScheduledExecutorService scheduledExecutorService) {

        if (opcServer == null) {
            return;
        }

        // 建立连接
        ConnectionInformation connectionInformation = new ConnectionInformation();
        // 设置连接服务器
        connectionInformation.setHost(opcServer.getIpAddress());
        connectionInformation.setDomain(opcServer.getDomain());
        connectionInformation.setUser(opcServer.getServerUser());
        connectionInformation.setPassword(opcServer.getServerPassword());
        // connectionInformation.setProgId(opcServer.getProgId());
        connectionInformation.setClsid(opcServer.getClsid());

        server = new Server(connectionInformation, scheduledExecutorService);
        // 自动重连
        autoReconnectController = new AutoReconnectController(server);
    }

    /**
     * 建立连接，如果异常表示连接失败（与opc服务器）
     */
    public static void connect() throws Exception {
        if (server == null) {
            log.error("没有建立opc服务器！");
            throw new Exception("没有初始化server");
        }

        server.connect();
        isConnected = true;
        log.info("服务器连接成功！");
    }

    /**
     * 将所有的连接中的opcGroups 全部加载到opc server中
     * @param opcGroups 所有连接中的opcGroups
     */
    public static void addGroups(List<OpcGroup> opcGroups) {
        List<Group> tempGroups = new ArrayList<>();

    }

    /**
     * 添加opcItem
     * @param opcItems 数据库中存储的opcItem
     */
    public static void addItems(List<OpcItem> opcItems) {
        List<Item> tempIems = new ArrayList<>();

    }


}
