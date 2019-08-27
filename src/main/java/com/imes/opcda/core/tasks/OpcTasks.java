package com.imes.opcda.core.tasks;

import com.imes.opcda.common.pojo.OpcConfig;
import com.imes.opcda.core.pojo.ScheduledTask;
import com.imes.opcda.core.service.OpcdaService;
import com.imes.opcda.core.utils.UtgardUtils;
import com.imes.opcda.opc.pojo.OpcServer;
import com.imes.opcda.opc.pojo.UpdateRate;
import com.imes.opcda.opc.service.UpdateRateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class OpcTasks implements CommandLineRunner {

    public static boolean initFinished; //初始化完成标志位
    public static int readUpdateRate; //读取的周期时间， 单位ms

    public static OpcServer currentOpcServer;
    public static List<UpdateRate> updateRates;
    public static List<Integer> dataAcquisitionUpdateRates;
    public static List<ScheduledTask> scheduledTasks;


    // 线程
    public static Thread threadDataAcquisition;
    public static Thread threadCommunication;

    @Autowired
    private OpcConfig opcConfig;
    @Autowired
    private OpcdaService opcdaService;
    @Autowired
    private UpdateRateService updateRateService;


    /**
     * spring boot 启动后执行任务
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        // 初始化程序
        this.initProgress();
        this.communicationProgress();
        this.dataAcquisitionProgress();
    }


    /**
     * 1. 初始数据，包括
     * - 通讯周期
     * - 内存中的opcServer
     * - 入库周期列表
     *
     * @throws Exception
     */
    private void initProgress() throws Exception {
        // 设置读取周期时间
        readUpdateRate = opcConfig.getUtgardReadUpdateRate();
        // 获取 updateRate 列表
        updateRates = updateRateService.getUpdateRates();
        log.info("updateRate列表是：" + updateRates);
        log.info("读取时间间隔为：" + readUpdateRate);
        // 初始化数据 获取所有的opcItems 放入内存
        currentOpcServer = opcdaService.initializeData();
        // 获取入库周期列表
        dataAcquisitionUpdateRates = opcdaService.getDataAcquisitionUpdateRate(currentOpcServer);
        // 整理获取 保存周期相同的opcGroups，并生成ScheduledTask
        scheduledTasks = opcdaService.getScheduledTasks(currentOpcServer, updateRates, dataAcquisitionUpdateRates);

        // TODO 获取Alarm的数据库列表

    }

    /**
     * 2. 当初始化数据完成后，对数据进行通讯读写（包括 建立服务器，连接服务器，添加组和Item，同步周期更新）
     */
    private void communicationProgress() {
        initFinished = currentOpcServer != null;
        if (initFinished) {
            threadCommunication = new Thread(new Runnable() {
                @Override
                public void run() {
                    opcdaService.communication(currentOpcServer, readUpdateRate);
                }
            });
            threadCommunication.start();
        }
    }

    private void dataAcquisitionProgress() {
        threadDataAcquisition = new Thread(()->{
            // 等待连接完成，正式开始读取数据。。。。堵塞线程
            while (!UtgardUtils.isReading) {

                try {
                    Thread.sleep(1000);
                    log.info("等待记录数据.......");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            opcdaService.dataAcquisition(scheduledTasks);
        });
        threadDataAcquisition.start();
    }



}
