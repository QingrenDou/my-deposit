package com.yzc.deposit.dao.deposit;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yzc.common.deposit.dto.deposit.BankConfigRespDto;
import com.yzc.common.deposit.entity.BankConfig;

public interface IBankConfigDao extends IService<BankConfig> {

    /**
     * 根据银行类型获取银行配置信息
     * @param bankTypeCode 银行类型
     * @return 银行配置
     */
    BankConfigRespDto getByBankTypeCode(Integer bankTypeCode);
}
