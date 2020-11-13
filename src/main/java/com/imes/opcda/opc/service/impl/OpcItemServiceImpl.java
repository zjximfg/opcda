package com.imes.opcda.opc.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.imes.opcda.common.pojo.OpcConfig;
import com.imes.opcda.common.pojo.PageResponse;
import com.imes.opcda.opc.mapper.OpcItemMapper;
import com.imes.opcda.opc.pojo.OpcConnection;
import com.imes.opcda.opc.pojo.OpcItem;
import com.imes.opcda.opc.service.OpcConnectionService;
import com.imes.opcda.opc.service.OpcItemService;
import com.imes.opcda.opc.service.OpcItemStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Service
public class OpcItemServiceImpl implements OpcItemService {
    @Autowired
    private OpcConnectionService opcConnectionService;
    @Autowired
    private OpcConfig opcConfig;
    @Autowired
    private OpcItemMapper opcItemMapper;
    @Autowired
    private OpcItemStateService opcItemStateService;

    @Override
    public String createItemName(OpcItem opcItem) {
        StringBuffer stringBuffer = new StringBuffer();
        OpcConnection opcConnection;
        if (opcItem.getOpcConnectionId() != null) {
            opcConnection = opcConnectionService.getConnectionById(opcItem.getOpcConnectionId());
        } else {
            if (opcItem.getOpcConnection() != null) {
                opcConnection = opcConnectionService.getConnectionById(opcItem.getOpcConnection().getConnectionId());
            } else {
                return "";
            }
        }

        if (opcConnection == null || opcItem.getItemCatalog() == null) {
            return "";
        }
        stringBuffer.append(opcConnection.getProtocolId()).append(":").append("[")
                .append(opcConnection.getConnectionName()).append("]");
        //如果是DB,添加DB号和“，”
        if (opcItem.getItemCatalog().equals("DB")) {
            stringBuffer.append(opcItem.getItemCatalog());
            if (opcItem.getDbNum() != null) {
                stringBuffer.append(opcItem.getDbNum());
            }
            if (opcItem.getDataType() != null) {
                stringBuffer.append(",").append(opcItem.getDataType());
            }
            if (opcItem.getDataAddressNum() != null) {
                stringBuffer.append(opcItem.getDataAddressNum());
            }
        } else {
            stringBuffer.append(opcItem.getItemCatalog());
            if (opcItem.getDataType() != null) {
                stringBuffer.append(opcItem.getDataType());
            }
            if (opcItem.getDataAddressNum() != null) {
                stringBuffer.append(opcItem.getDataAddressNum());
            }
        }
        // 如果是X有位存在，添加.bitNum
        if (opcItem.getDataType() != null && opcItem.getDataType().equals("X") && opcItem.getDataBitOffset() != null) {
            stringBuffer.append(".").append(opcItem.getDataBitOffset());
        }

        return stringBuffer.toString();
    }

    @Override
    public List<String> getDataTypes() {
        return opcConfig.getDataTypes();
    }

    @Override
    public List<String> getItemCatalogs() {
        return opcConfig.getItemCatalogs();
    }

    @Override
    public Integer insertOpcItem(OpcItem opcItem) {
        opcItem.setItemName(this.createItemName(opcItem));
        return opcItemMapper.insert(opcItem);
    }

    @Override
    public PageResponse<OpcItem> getOpcItemsByPage(int currentPage, int pageSize, String searchKey, Integer groupId) {
        PageHelper.startPage(currentPage, pageSize);
        Example example = new Example(OpcItem.class);
        Example.Criteria criteria = example.createCriteria().andEqualTo("deleted", 0).andEqualTo("opcGroupId", groupId);
        if (!StringUtils.isEmpty(searchKey)) {
            criteria.andLike("itemName", "%" + searchKey + "%")
                    .orLike("comment", "%" + searchKey + "%");
        }

        Page<OpcItem> page = (Page<OpcItem>) opcItemMapper.selectByExample(example);
        return new PageResponse<>(page.getTotal(), page.getResult());
    }

    @Override
    public List<OpcItem> getOpcItemsByPid(Integer opcGroupId) {
        OpcItem opcItem = new OpcItem();
        opcItem.setOpcGroupId(opcGroupId);
        opcItem.setDeleted(0);
        return opcItemMapper.select(opcItem);
    }

    @Override
    public OpcItem getOpcItemById(Integer id) {
        OpcItem opcItem = new OpcItem();
        opcItem.setDeleted(0);
        opcItem.setId(id);
        return opcItemMapper.selectOne(opcItem);
    }

    @Override
    public List<OpcItem> getOpcItemsByConnectionId(Integer opcConnectionId) {
        OpcItem opcItem = new OpcItem();
        opcItem.setOpcConnectionId(opcConnectionId);
        opcItem.setDeleted(0);
        return opcItemMapper.select(opcItem);
    }

    @Override
    public Integer updateOpcItem(OpcItem opcItem) {
        opcItem.setItemName(this.createItemName(opcItem));
        return opcItemMapper.updateByPrimaryKey(opcItem);
    }

    @Override
    public Integer deleteOpcItem(OpcItem opcItem) {
        opcItem.setDeleted(1);
        return opcItemMapper.updateByPrimaryKey(opcItem);
    }

    /**
     * 根据传入的 opcitem ID 获取内存中的通过通讯过来的当前value，并整理成opcItem LIST
     * @param itemIdList 传入的 opcitem ID LIST
     * @return 带有当前value 的opcItem LIST
     */
    @Override
    public List<OpcItem> getCurrentByItemList(List<Integer> itemIdList) {
        List<OpcItem> opcItemList = new ArrayList<>();
        for (Integer id : itemIdList) {
            OpcItem opcItemById = this.getOpcItemById(id);
            String currentValueFromOpcItemStateByItemId = opcItemStateService.getCurrentValueFromOpcItemStateByItemId(id);
            opcItemById.setValue(currentValueFromOpcItemStateByItemId);
            opcItemList.add(opcItemById);
        }
        return opcItemList;
    }


}
