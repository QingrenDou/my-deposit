package com.yzc.deposit.dao.guar.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yzc.common.guar.entity.GuarPay;
import com.yzc.deposit.dao.guar.IGuarPayDao;
import com.yzc.deposit.repository.mapper.guar.GuarPayMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 保函支付详情 服务实现类
 * </p>
 *
 * @author DouQingRen
 * @since 2025-04-07
 */
@Service
public class GuarPayDaoImpl extends ServiceImpl<GuarPayMapper, GuarPay> implements IGuarPayDao {

}
