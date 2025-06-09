package com.yzc.deposit.repository.mapper.guar;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yzc.common.guar.entity.GuarPay;

/**
 * <p>
 * 保函支付详情 Mapper 接口
 * </p>
 *
 * @author DouQingRen
 * @since 2025-04-07
 */
@DS("guar")
public interface GuarPayMapper extends BaseMapper<GuarPay> {

}
