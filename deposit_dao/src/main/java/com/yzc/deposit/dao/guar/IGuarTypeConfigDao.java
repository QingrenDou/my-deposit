package com.yzc.deposit.dao.guar;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yzc.common.guar.entity.GuarTypeConfig;

/**
 * <p>
 * 电子保函机构配置表 服务类
 * </p>
 *
 * @author DouQingRen
 * @since 2025-04-30
 */
public interface IGuarTypeConfigDao extends IService<GuarTypeConfig> {

    /**
     * 根据保函类型获取机构信息
     * @param guarTypeCode 保函类型
     * @return 保函机构信息
     */
    GuarTypeConfig getByGuarTypeCode(Integer guarTypeCode);
}
