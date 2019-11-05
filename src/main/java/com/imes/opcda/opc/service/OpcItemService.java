package com.imes.opcda.opc.service;

import com.imes.opcda.common.pojo.PageResponse;
import com.imes.opcda.opc.pojo.OpcItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OpcItemService {
    String createItemName(OpcItem opcItem);

    List<String> getDataTypes();

    List<String> getItemCatalogs();

    Integer insertOpcItem(OpcItem opcItem);

    PageResponse<OpcItem> getOpcItemsByPage(int currentPage, int pageSize, String searchKey, Integer groupId);

    List<OpcItem> getOpcItemsByPid(Integer opcGroupId);

    List<OpcItem> getOpcItemsByConnectionId(Integer opcConnectionId);

    Integer updateOpcItem(OpcItem opcItem);

    Integer deleteOpcItem(OpcItem opcItem);

}
