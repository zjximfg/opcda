package com.imes.opcda.core.pojo;

import com.imes.opcda.opc.pojo.OpcGroup;
import com.imes.opcda.opc.pojo.OpcItemState;
import com.imes.opcda.opc.pojo.UpdateRate;
import com.imes.opcda.opc.service.OpcItemStateService;
import com.imes.opcda.opc.service.impl.OpcItemStateServiceImpl;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ScheduledFuture;

@Data
@Component
@Slf4j
public class ScheduledTask implements Runnable {

    private OpcItemStateService opcItemStateService;

    private UpdateRate updateRate;
    private List<OpcGroup> opcGroupList;
    private ScheduledFuture scheduledFuture;

    @Override
    public void run() {
        for (OpcGroup opcGroup : opcGroupList) {
            System.out.println(opcGroup);
            if (opcGroup.getOpcItems().size() > 0) {
                List<OpcItemState> opcItemStates = opcItemStateService.getOpcItemStateFromOpcGroups(opcGroup);
                log.info(opcItemStates.toString());
                Integer resultCount = opcItemStateService.insertOpcItemStateList(opcItemStates);
                if (resultCount < 1) {
                    log.error("相数据库中存储组：" + opcGroup.getGroupName() + " 失败！");
                }
            }
        }
    }
}
