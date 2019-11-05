package com.imes.opcda.opc.service.impl;

import com.imes.opcda.opc.mapper.OpcItemStateMapper;
import com.imes.opcda.opc.pojo.OpcGroup;
import com.imes.opcda.opc.pojo.OpcItem;
import com.imes.opcda.opc.pojo.OpcItemState;
import com.imes.opcda.opc.service.OpcItemStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OpcItemStateServiceImpl implements OpcItemStateService {

    @Autowired
    private OpcItemStateMapper opcItemStateMapper;

    /**
     * 根据带有实时状态的 opcGroup中读取相应的数值生成OpcItemState列表
     *
     * @param opcGroup 父类Id
     * @return
     */
    @Override
    public List<OpcItemState> getOpcItemStateFromOpcGroups(OpcGroup opcGroup) {

        List<OpcItemState> opcItemStates = new ArrayList<>();

        for (OpcItem opcItem : opcGroup.getOpcItems()) {
            OpcItemState opcItemState = new OpcItemState();
            opcItemState.setId(UUID.randomUUID().toString());
            opcItemState.setItemId(opcItem.getItemId());
            opcItemState.setItemName(opcItem.getItemName());
            opcItemState.setItemValue(opcItem.getValue() == null ? "读取失败" : opcItem.getValue());
            opcItemStates.add(opcItemState);
            //opcItemState.setDatetime() 不需要，由数据库自动添加创建是的时间戳
        }
        return opcItemStates;
    }

    @Override
    public Integer insertOpcItemStateList(List<OpcItemState> opcItemStates) {
        return opcItemStateMapper.insertList(opcItemStates);
    }

    /**
     * 根据起始时间戳， 结束时间戳，查询opcItemState中的值，返回List中的第一个值与最后一个值的差值。
     *
     * @param startTimeStamp 查询的起始时间戳
     * @param endTimeStamp   查询的结束时间戳
     * @param itemId         变量的itemId
     * @return 函数的返回值是list 中的第一个值与最后一个值的差值。
     */
    @Override
    public String getAccumValue(Timestamp startTimeStamp, Timestamp endTimeStamp, Integer itemId) {
        // 获取 list
        List<OpcItemState> opcItemStates = this.getOpcItemStateBetweenStartTimeAndEndTime(startTimeStamp, endTimeStamp, itemId);
        // 判断返回值是否为空？ 如果为空， 函数返回 ”0“
        if (opcItemStates != null && opcItemStates.size() > 0) {
            // 获取第一个值和最后一个值
            double first = Double.parseDouble(opcItemStates.get(0).getItemValue());
            double last = Double.parseDouble(opcItemStates.get(opcItemStates.size() - 1).getItemValue());
            // 获取差值并取绝对值 并取整
            double value = Math.round(Math.abs(first - last));
            return String.valueOf(value);
        }
        return "0";
    }

    /**
     * 根据起始时间戳， 结束时间戳，查询opcItemState中的值，返回List
     * @param startTime 查询的起始时间戳
     * @param endTime 查询的结束时间戳
     * @param itemId 变量的itemId
     * @return 查询opcItemState中的值
     */
    @Override
    public List<OpcItemState> getOpcItemStateBetweenStartTimeAndEndTime(Timestamp startTime, Timestamp endTime, Integer itemId) {
        // 获取 list
        Example example = new Example(OpcItemState.class);
        // 排序
        example.setOrderByClause("datetime DESC");
        Example.Criteria criteria = example.createCriteria();
        // 查询条件
        criteria.andEqualTo("itemId", itemId).andBetween("datetime", startTime, endTime);
        return opcItemStateMapper.selectByExample(example);

    }
}
