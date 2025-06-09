package com.yzc.deposit.dao.guar.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yzc.common.common.enums.CommonStatusEnum;
import com.yzc.common.guar.entity.GuarTypeConfig;
import com.yzc.deposit.dao.guar.IGuarTypeConfigDao;
import com.yzc.deposit.repository.mapper.guar.GuarTypeConfigMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 电子保函机构配置表 服务实现类
 * </p>
 *
 * @author DouQingRen
 * @since 2025-04-30
 */
@Service
public class GuarTypeConfigDaoImpl extends ServiceImpl<GuarTypeConfigMapper, GuarTypeConfig> implements IGuarTypeConfigDao {

    /**
     * 根据保函类型获取机构信息
     * @param guarTypeCode 保函类型
     * @return 保函机构信息
     */
    @Override
    public GuarTypeConfig getByGuarTypeCode(Integer guarTypeCode) {
        LambdaQueryWrapper<GuarTypeConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GuarTypeConfig::getGuarTypeCode, guarTypeCode);
        //未删除状态
        queryWrapper.and(wrapper -> wrapper.ne(GuarTypeConfig::getDelStatus, CommonStatusEnum.Yes.getCode())
                .or()
                .isNull(GuarTypeConfig::getDelStatus));
        return this.getOne(queryWrapper);
    }
}
