package com.imes.opcda.opc.service;

import com.imes.opcda.opc.pojo.OpcConnection;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OpcConnectionService {
    List<String> getProtocols();

    List<OpcConnection> getAllConnections(String sortType, String key);

    List<OpcConnection> getAllConnections();

    List<OpcConnection> updateConnection(OpcConnection opcConnection);

    List<OpcConnection> insertConnection(OpcConnection opcConnection);

    List<OpcConnection> getConnectionMenu();

    List<OpcConnection> deleteConnection(OpcConnection opcConnection);

    OpcConnection getConnectionById(Integer id);
}
