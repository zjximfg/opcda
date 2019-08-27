package com.imes.opcda.opc.service;

import com.imes.opcda.opc.pojo.OpcServer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OpcServerService {

    List<String> getProgIds();

    List<String> getClsids();

    OpcServer getOpcServer();

    OpcServer updateOpcServer(OpcServer opcServer);
}
