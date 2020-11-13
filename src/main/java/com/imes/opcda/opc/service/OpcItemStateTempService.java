package com.imes.opcda.opc.service;

import com.imes.opcda.opc.pojo.OpcItemState;
import org.springframework.stereotype.Service;

@Service
public interface OpcItemStateTempService {

    void updateOpcItemStateTemp(OpcItemState opcItemState);
}
