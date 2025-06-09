package com.yzc.deposit.dao.deposit.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yzc.common.deposit.entity.BankKey;
import com.yzc.deposit.dao.deposit.IBankKeyDao;
import com.yzc.deposit.repository.mapper.deposit.BankKeyMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 银行秘钥表 服务实现类
 * </p>
 *
 * @author DouQingRen
 * @since 2025-06-04
 */
@Service
public class BankKeyDaoImpl extends ServiceImpl<BankKeyMapper, BankKey> implements IBankKeyDao {

    /**
     * 根据银行类型查询秘钥配置
     *
     * @param bankTypeCode 银行类型
     * @return 秘钥配置
     */
    @Override
    public BankKey getByBankTypeCode(Integer bankTypeCode) {
        LambdaQueryWrapper<BankKey> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BankKey::getBankTypeCode, bankTypeCode);

        List<BankKey> list = this.list(queryWrapper);
        return CollectionUtil.isNotEmpty(list) ?  list.get(0) : null;
    }
}
