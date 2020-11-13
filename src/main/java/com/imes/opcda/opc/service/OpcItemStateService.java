package com.imes.opcda.opc.service;

import com.imes.opcda.opc.pojo.OpcGroup;
import com.imes.opcda.opc.pojo.OpcItemState;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public interface OpcItemStateService {

    List<OpcItemState> getOpcItemStateFromOpcGroups(OpcGroup opcGroup);

    Integer insertOpcItemStateList(List<OpcItemState> opcItemStates);

   // String getAccumValue(Timestamp startTimestamp, Timestamp endTimestamp, Integer itemId);

    List<OpcItemState> getOpcItemStateBetweenStartTimeAndEndTime(Timestamp startTime, Timestamp endTime, Integer itemId);

    String getCurrentValueFromOpcItemStateByItemId(Integer itemId);
}
