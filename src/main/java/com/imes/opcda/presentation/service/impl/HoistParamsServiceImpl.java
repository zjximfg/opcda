package com.imes.opcda.presentation.service.impl;

import com.imes.opcda.presentation.mapper.HoistParamsMapper;
import com.imes.opcda.presentation.pojo.HoistParams;
import com.imes.opcda.presentation.service.ActualValueService;
import com.imes.opcda.presentation.service.HoistParamsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HoistParamsServiceImpl implements HoistParamsService {

    @Autowired
    private HoistParamsMapper hoistParamsMapper;
    @Autowired
    private ActualValueService actualValueService;

    @Override
    public HoistParams getHoistParamsOne() {
        HoistParams hoistParams = hoistParamsMapper.selectByPrimaryKey(1);
        hoistParams.setFrequencyMorningShift(actualValueService.getActualValueById(hoistParams.getFrequencyMorningShiftId()));
        hoistParams.setFrequencyMiddleShift(actualValueService.getActualValueById(hoistParams.getFrequencyMiddleShiftId()));
        hoistParams.setFrequencyEveningShift(actualValueService.getActualValueById(hoistParams.getFrequencyEveningShiftId()));
        hoistParams.setYieldMorningShift(actualValueService.getActualValueById(hoistParams.getYieldMorningShiftId()));
        hoistParams.setYieldMiddleShift(actualValueService.getActualValueById(hoistParams.getYieldMiddleShiftId()));
        hoistParams.setYieldEveningShift(actualValueService.getActualValueById(hoistParams.getFrequencyEveningShiftId()));
        hoistParams.setSpeed(actualValueService.getActualValueById(hoistParams.getSpeedId()));
        hoistParams.setPosition_1(actualValueService.getActualValueById(hoistParams.getPosition_1_id()));
        hoistParams.setPosition_2(actualValueService.getActualValueById(hoistParams.getPosition_2_id()));
        hoistParams.setLoad_1(actualValueService.getActualValueById(hoistParams.getLoad_1_id()));
        hoistParams.setLoad_2(actualValueService.getActualValueById(hoistParams.getLoad_2_id()));
        hoistParams.setHeadSheave_1_bearing_1_T(actualValueService.getActualValueById(hoistParams.getHeadSheave_1_bearing_1_T_id()));
        hoistParams.setHeadSheave_1_bearing_2_T(actualValueService.getActualValueById(hoistParams.getHeadSheave_1_bearing_2_T_id()));
        hoistParams.setHeadSheave_2_bearing_1_T(actualValueService.getActualValueById(hoistParams.getHeadSheave_2_bearing_1_T_id()));
        hoistParams.setHeadSheave_2_bearing_2_T(actualValueService.getActualValueById(hoistParams.getHeadSheave_2_bearing_2_T_id()));
        hoistParams.setExcitationCurrent(actualValueService.getActualValueById(hoistParams.getExcitationCurrentId()));
        hoistParams.setArmatureCurrent(actualValueService.getActualValueById(hoistParams.getArmatureCurrentId()));
        hoistParams.setDrum_1_T(actualValueService.getActualValueById(hoistParams.getDrum_1_T_id()));
        hoistParams.setDrum_2_T(actualValueService.getActualValueById(hoistParams.getDrum_2_T_id()));
        hoistParams.setHoistState(actualValueService.getActualValueById(hoistParams.getHoistStateId()));
        return null;
    }
}
