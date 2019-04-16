package com.imes.opcda.common.pojo;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Configuration
@Component
@ConfigurationProperties(prefix = "opc-config")
public class OpcConfig {
    //opcServer
    private Map<String, String> serverCatalog = new HashMap<>();
    //private List<String> progIds = new ArrayList<>();
    //connection
    private List<String> protocolIds = new ArrayList<>();
    //items
    private List<String> dataTypes = new ArrayList<>();
    private List<String> itemCatalogs = new ArrayList<>();

}
