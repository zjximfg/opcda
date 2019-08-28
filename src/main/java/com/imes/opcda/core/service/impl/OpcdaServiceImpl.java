package com.imes.opcda.core.service.impl;

import com.imes.opcda.core.pojo.ScheduledTask;
import com.imes.opcda.core.service.OpcdaService;
import com.imes.opcda.core.tasks.OpcTasks;
import com.imes.opcda.core.utils.UtgardUtils;
import com.imes.opcda.opc.pojo.*;
import com.imes.opcda.opc.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class OpcdaServiceImpl implements OpcdaService {

    @Autowired
    private OpcServerService opcServerService;
    @Autowired
    private OpcConnectionService opcConnectionService;
    @Autowired
    private OpcGroupService opcGroupService;
    @Autowired
    private OpcItemService opcItemService;
    @Autowired
    private OpcItemStateService opcItemStateService;
    @Autowired
    private OpcTasks opcTasks;


    /**
     * 初始化所有的opcItems 数据放入到内存中，用于通讯读取数据
     *
     * @return 所有的带子类的 opcServer
     * @throws Exception 没有找到配备的服务器数据
     */
    @Override
    public OpcServer initializeData() throws Exception {

        // 读取数据库中的配置， 返回一个opcServer
        OpcServer opcServer = opcServerService.getOpcServer();

        if (opcServer == null) {
            log.error("未找到配置的opc服务器");
            throw new Exception("未找到配置的opc服务器");
        }

        // 添加所有的连接
        List<OpcConnection> allConnections = opcConnectionService.getAllConnections();

        allConnections.forEach(opcConnection -> {
            List<OpcGroup> opcGroups = opcGroupService.getOpcGroupByPid(opcConnection.getConnectionId());
            opcGroups.forEach(opcGroup -> {
                List<OpcItem> opcItems = opcItemService.getOpcItemsByPid(opcGroup.getGroupId());
                opcGroup.setOpcItems(opcItems);
            });
            opcConnection.setOpcGroups(opcGroups);
        });
        opcServer.setOpcConnections(allConnections);
        System.out.println(opcServer);
        return opcServer;
    }


    /**
     * 通讯过程，包括建立连接，添加 组，item，最后周期同步读取数据等工作
     * 需要放入单独的线程调用
     *
     * @param opcServer 传入初始化后的opcServer， 必须带有所有子类数据
     */
    @Override
    public void communication(OpcServer opcServer, Integer updateRate) {
        // 断开连接
        UtgardUtils.isReading = false;
        //1. 创建服务器
        UtgardUtils.createServer(opcServer, Executors.newSingleThreadScheduledExecutor());
        //2. 连接 - 自动重连
        UtgardUtils.autoReconnectionSync(updateRate);

        while (!UtgardUtils.isConnected) {
            if (Thread.currentThread().isInterrupted()) {
                break;
            }
            try {
                log.info("正在尝试与服务器进行连接.........");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            UtgardUtils.isConnected = StringUtils.equals(UtgardUtils.connectState, "CONNECTED");
        }

        log.info("2. 与opc服务器连接完成！");

        //3. 添加组 items
        if (UtgardUtils.isConnected) {
            opcServer.getOpcConnections().forEach(opcConnection -> {
                opcConnection.getOpcGroups().forEach(opcGroup -> {
                    UtgardUtils.syncAdd(opcGroup.getOpcItems());
                });
            });
        }

        //4. 同步读取实时数据
        UtgardUtils.syncRead();
    }

    /**
     * 创建线程连接池，使用多线程来保存不同周期的值
     *
     * @param scheduledTasks
     */
    @Override
    public void dataAcquisition(List<ScheduledTask> scheduledTasks) {
        log.info("开始记录数据..... ");
        //建立任务的连接池
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(scheduledTasks.size() + 2);
        for (ScheduledTask scheduledTask : scheduledTasks) {
            if (!scheduledTask.getScheduledFuture().isCancelled()) {
                scheduledTask.getScheduledFuture().cancel(true);
            }
            scheduledTask.setScheduledFuture(scheduledExecutorService.scheduleAtFixedRate(scheduledTask, 5, Long.valueOf(scheduledTask.getUpdateRate().getRate()), TimeUnit.MILLISECONDS));
        }
    }

    /**
     * 整理，获取所有的入库时间，（每个时间对应一个周期函数），根据组的刷新时间去生成，去除重复项
     *
     * @param opcServer 带所有子类的opcServer
     * @return 所有的入库周期列表
     */
    @Override
    public List<Integer> getDataAcquisitionUpdateRate(OpcServer opcServer) {
        List<Integer> rates = new ArrayList<>();
        opcServer.getOpcConnections().forEach(opcConnection -> {
            opcConnection.getOpcGroups().forEach(opcGroup -> {
                rates.add(opcGroup.getUpdateRate().getRate());
            });
        });
        // 去除重复项并返回
        return removeDuplicate(rates);
    }


    /**
     * 去除重复项的方法
     *
     * @param list 要去重的列表
     * @return 去重后的列表
     */
    private List<Integer> removeDuplicate(List<Integer> list) {
        HashSet<Integer> hashSet = new HashSet<>(list);
        list.clear();
        list.addAll(hashSet);
        return list;
    }

    /**
     * 整理获取 保存周期相同的opcGroups，并生成ScheduledTask
     *
     * @param opcServer
     * @param updateRates
     * @param dataAcquisitionUpdateRates
     * @return
     */
    @Override
    public List<ScheduledTask> getScheduledTasks(OpcServer opcServer, List<UpdateRate> updateRates, List<Integer> dataAcquisitionUpdateRates) {
        List<ScheduledTask> scheduledTasks = new ArrayList<>();
        dataAcquisitionUpdateRates.forEach(rate -> {
            // 每一个dataAcquisitionUpdateRates 获取添加对应的 UpdateRate
            ScheduledTask scheduledTask = new ScheduledTask();
            scheduledTask.setOpcItemStateService(opcItemStateService);
            scheduledTask.setUpdateRate(getUpdateRateByRate(rate, updateRates));
            // 获取相同保存周期的 opcGroups
            List<OpcGroup> opcGroups = new ArrayList<>();
            opcServer.getOpcConnections().forEach(opcConnection -> {
                opcConnection.getOpcGroups().forEach(opcGroup -> {
                    if (opcGroup.getUpdateRate().getRate().equals(rate)) {
                        opcGroups.add(opcGroup);
                    }
                });
            });
            scheduledTask.setOpcGroupList(opcGroups);
            scheduledTasks.add(scheduledTask);
        });
        System.out.println(scheduledTasks.size());
        return scheduledTasks;
    }

    @Override
    public void restartServer() {
        try {
            opcTasks.initProgress();
            opcTasks.communicationProgress();
            opcTasks.dataAcquisitionProgress();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getServerState() {
        if (UtgardUtils.isReading) {
            return "Server is reading variable!";
        } else if (UtgardUtils.isConnected) {
            return "Server is connected!";
        } else if (OpcTasks.initFinished) {
            return "Server initialization completed!";
        } else {
            return "Server IS ERROR!";
        }
    }

    /**
     * 通过 rate 获取相对应的updateRate对象
     *
     * @param rate
     * @param updateRates
     * @return
     */
    private UpdateRate getUpdateRateByRate(Integer rate, List<UpdateRate> updateRates) {
        for (UpdateRate updateRate : updateRates) {
            if (updateRate.getRate().equals(rate)) {
                return updateRate;
            }
        }
        // 如果没有 返回初始值
        UpdateRate updateRate = new UpdateRate();
        updateRate.setId(1);
        updateRate.setRate(1000);
        updateRate.setCron("*/1 * * * * ?");
        updateRate.setName("1s");
        return updateRate;
    }
}
