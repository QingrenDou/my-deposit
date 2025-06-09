package com.yzc.deposit.dao.deposit;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yzc.common.deposit.entity.BankKey;

/**
 * <p>
 * 银行秘钥表 服务类
 * </p>
 *
 * @author DouQingRen
 * @since 2025-06-04
 */
public interface IBankKeyDao extends IService<BankKey> {
    /**
     * 根据银行类型查询秘钥配置
     * @param bankTypeCode 银行类型
     * @return 秘钥配置
     */
    BankKey getByBankTypeCode(Integer bankTypeCode);
}
