package com.imes.opcda.presentation.service;

import com.imes.opcda.presentation.pojo.TeamWorkers;
import com.imes.opcda.presentation.vo.Accum;
import com.imes.opcda.presentation.vo.Actual;
import com.imes.opcda.presentation.vo.Chart;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface PresentationService {
    List<Accum> getGroup1();

    List<Chart> getGroup2(Date endDate);

    List<Chart> getGroup3(Date endDate);

    List<Chart> getGroup4();

    List<Actual> getGroup5();

    List<Actual> getGroup6();

    List<Actual> getGroup7();

    List<Actual> getGroup8();

}

