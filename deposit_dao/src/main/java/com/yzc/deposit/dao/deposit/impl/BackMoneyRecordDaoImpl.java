package com.yzc.deposit.dao.deposit.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yzc.common.deposit.entity.BackMoneyRecord;
import com.yzc.deposit.dao.deposit.IBackMoneyRecordDao;
import com.yzc.deposit.repository.mapper.deposit.BackMoneyRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class BackMoneyRecordDaoImpl  extends ServiceImpl<BackMoneyRecordMapper, BackMoneyRecord> implements IBackMoneyRecordDao {

    @Resource
    private BackMoneyRecordMapper backMoneyRecordMapper;


}
