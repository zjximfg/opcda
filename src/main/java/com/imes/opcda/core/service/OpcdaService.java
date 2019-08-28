package com.imes.opcda.core.service;

import com.imes.opcda.core.pojo.ScheduledTask;
import com.imes.opcda.opc.pojo.OpcServer;
import com.imes.opcda.opc.pojo.UpdateRate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OpcdaService {

    /**
     * 读取通讯数据
     * @param opcServer
     */
    void communication(OpcServer opcServer, Integer updateRate);

    /**
     * 初始化数据
     * @return 带所有字表的数据的opcServer
     */
    OpcServer initializeData() throws Exception;

    //List<Alarm> initializeAlarm();


    void dataAcquisition(List<ScheduledTask> scheduledTasks);


    List<Integer> getDataAcquisitionUpdateRate(OpcServer opcServer);

    List<ScheduledTask> getScheduledTasks(OpcServer opcServer, List<UpdateRate> updateRates, List<Integer> dataAcquisitionUpdateRates);

    void restartServer();

    String getServerState();

    //void alarmAcquisition(OpcServer opcServer, List<Alarm>);

}
