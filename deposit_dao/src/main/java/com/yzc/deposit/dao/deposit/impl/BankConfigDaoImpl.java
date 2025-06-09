package com.yzc.deposit.dao.deposit.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yzc.common.deposit.dto.deposit.BankConfigRespDto;
import com.yzc.common.deposit.entity.BankConfig;
import com.yzc.deposit.dao.deposit.IBankConfigDao;
import com.yzc.deposit.repository.mapper.deposit.BankConfigMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class BankConfigDaoImpl  extends ServiceImpl<BankConfigMapper, BankConfig> implements IBankConfigDao {

    @Resource
    private BankConfigMapper bankConfigMapper;

    /**
     * 根据银行类型获取银行配置信息
     *
     * @param bankTypeCode 银行类型
     * @return 银行配置
     */
    @Override
    public BankConfigRespDto getByBankTypeCode(Integer bankTypeCode) {
        if(ObjectUtil.isNull(bankTypeCode)){
            return null;
        }

        LambdaQueryWrapper<BankConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BankConfig::getBankTypeCode,bankTypeCode);

        List<BankConfig> bankConfigs = bankConfigMapper.selectList(queryWrapper);
        if(CollectionUtil.isEmpty(bankConfigs)){
            return null;
        }

        return BeanUtil.copyProperties(bankConfigs.get(0), BankConfigRespDto.class);
    }
}
