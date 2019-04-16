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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class OpcItemServiceImpl implements OpcItemService {
    @Autowired
    private OpcConnectionService opcConnectionService;
    @Autowired
    private OpcConfig opcConfig;
    @Autowired
    private OpcItemMapper opcItemMapper;

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
        opcItem.setOpcConnectionId(opcItem.getOpcConnection().getConnectionId());
        opcItem.setOpcGroupId(opcItem.getOpcGroup().getGroupId());
        opcItem.setItemName(this.createItemName(opcItem));
        return opcItemMapper.insert(opcItem);
    }

    @Override
    public PageResponse<OpcItem> getOpcItemsByPage(int currentPage, int pageSize, String searchKey) {
        PageHelper.startPage(currentPage, pageSize);
        Example example = new Example(OpcItem.class);
        Example.Criteria criteria = example.createCriteria().andEqualTo("deleted", 0);
        if (!StringUtils.isEmpty(searchKey)) {
            criteria.andLike("itemName", "%" + searchKey + "%")
                    .orLike("comment", "%" + searchKey + "%");
        }

        Page<OpcItem> page = (Page<OpcItem>) opcItemMapper.selectByExample(example);
        return new PageResponse<OpcItem>(page.getTotal(), page.getResult());
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


}
