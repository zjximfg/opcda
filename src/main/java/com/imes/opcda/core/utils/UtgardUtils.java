package com.imes.opcda.core.utils;

import com.imes.opcda.opc.pojo.OpcItem;
import com.imes.opcda.opc.pojo.OpcServer;
import lombok.extern.slf4j.Slf4j;
import org.jinterop.dcom.common.JIException;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.common.NotConnectedException;
import org.openscada.opc.lib.da.*;

import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

@Slf4j
public class UtgardUtils {

    public static Server server;
    public static boolean isConnected; // =true 已经连接
    public static AutoReconnectController autoReconnectController;
    public static AccessBase syncAccessBase; //同步连接
    public static AccessBase asyncAccessBase; //异步连接
    public static boolean isReading;
    public static String connectState;  // CONNECTED 连接上

    /**
     * 建立服务器
     * @param opcServer 从数据库中查询的opcServer对象
     * @param scheduledExecutorService 线程池
     */
    public static void createServer(OpcServer opcServer, ScheduledExecutorService scheduledExecutorService) {

        if (opcServer == null) {
            System.out.println("opcServer == null");
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
        System.out.println(server.toString());
        log.info("1. opc服务器创建完成！");
    }

    /**
     * 建立连接，如果异常表示连接失败（与opc服务器）----- 暂时不用
     */
//    public static void connect() throws Exception {
//        if (server == null) {
//            log.error("没有建立opc服务器！");
//            throw new Exception("没有初始化server");
//        }
//
//        server.connect();
//        isConnected = true;
//        log.info("服务器连接成功！");
//    }


    /**
     * 与服务器建立连接 （异步模式，自动重连）
     * @param updateRate
     */
    public static void autoReconnectionSync(Integer updateRate) {
        autoReconnectController.connect();
        autoReconnectController.addListener(new AutoReconnectListener() {
            @Override
            public void stateChanged(AutoReconnectState state) {
                connectState = state.toString();
            }
        });

        try {
            syncAccessBase = new SyncAccess(server, updateRate);
        } catch (UnknownHostException e) {
            log.error("未知的服务器！");
        } catch (NotConnectedException e) {
            log.error("没有连接成功！");
        } catch (JIException e) {
            log.error("！");
        } catch (DuplicateGroupException e) {
            log.error("出现了重复的组！");
        }
    }

    /**
     * 异步添加, 同时定义读取规则
     * @param opcItems
     */
    public static void syncAdd(List<OpcItem> opcItems) {

        opcItems.forEach(opcItem -> {
            // 添加变量
            try {
                syncAccessBase.addItem(opcItem.getItemName(), new DataCallback(){
                    // 定义变量读取规则，不调用（使用syncAccessBase.bind()调用）
                    @Override
                    public void changed(Item item, ItemState itemState) {
                        opcItem.setItemState(itemState);
                        // 获取当前的实时值
                        try {
                            getValue(opcItem, itemState);
                            opcItem.setOnline(true);
                        } catch (JIException e) {
                            opcItem.setValue(null);
                            opcItem.setOnline(false);
                            e.printStackTrace();
                        }
                    }
                });
                log.info("3. 变量添加完成！");
            } catch (JIException e) {
                e.printStackTrace();
            } catch (AddFailedException e) {
                log.error("变量添加失败！");
            }
        });
    }

    public static void syncRead() {

        // 开始同步读取数据
        syncAccessBase.bind();
        // 赋值标志位
        isReading = true;
        log.info("4. 开始同步读取变量的实时值........");

        // 堵塞线程
        while (isReading) {
            if (Thread.currentThread().isInterrupted()){
                break;
            }
            try {
                Thread.sleep(10*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 当读取结束时 （标志位 isReading == false 时 取消读取状态）
        try {
            syncAccessBase.unbind();
        } catch (JIException e) {
            e.printStackTrace();
        }
        syncAccessBase.clear();
        // 断开连接
        autoReconnectController.disconnect();

    }


    private static void getValue(OpcItem opcItem, ItemState itemState) throws JIException {
        switch (opcItem.getDataType()) {
            case "X": // ok
                opcItem.setValue(String.valueOf(itemState.getValue().getObjectAsBoolean()));
                break;
            case "B": // ????????
                opcItem.setValue(String.valueOf(itemState.getValue().getObjectAsUnsigned().getValue()));
                break;
            case "W": // ??????????
                opcItem.setValue(String.valueOf(itemState.getValue().getObjectAsUnsigned()));
                break;
            case "D": // ok
                //System.out.println(itemState.getValue());
                opcItem.setValue(String.valueOf(itemState.getValue().getObjectAsUnsigned()));
                break;
            case "INT": // ok
                opcItem.setValue(String.valueOf(itemState.getValue().getObjectAsShort()));
                break;
            case "DINT": // ok
                opcItem.setValue(String.valueOf(itemState.getValue().getObjectAsInt()));
                break;
            case "REAL": // ok
                opcItem.setValue(String.valueOf(itemState.getValue().getObjectAsFloat()));
                break;
            default:
                opcItem.setValue(itemState.getValue().getObjectAsString().toString());
                break;
        }
    }


}
