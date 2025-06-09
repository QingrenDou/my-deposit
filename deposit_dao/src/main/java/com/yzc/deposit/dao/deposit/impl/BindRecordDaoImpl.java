package com.yzc.deposit.dao.deposit.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yzc.common.deposit.entity.BindRecord;
import com.yzc.deposit.dao.deposit.IBindRecordDao;
import com.yzc.deposit.repository.mapper.deposit.BindRecordMapper;
import org.springframework.stereotype.Service;

@Service
public class BindRecordDaoImpl extends ServiceImpl<BindRecordMapper, BindRecord> implements IBindRecordDao {

}
