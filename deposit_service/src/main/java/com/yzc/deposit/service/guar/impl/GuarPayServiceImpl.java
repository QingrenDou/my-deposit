package com.yzc.deposit.service.guar.impl;

import com.yzc.deposit.service.guar.IGuarPayService;
import com.yzc.deposit.dao.guar.IGuarPayDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class GuarPayServiceImpl implements IGuarPayService {

    @Resource
    private IGuarPayDao guarPayDao;


}
