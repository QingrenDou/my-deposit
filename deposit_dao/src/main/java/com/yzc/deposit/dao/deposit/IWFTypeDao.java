package com.yzc.deposit.dao.deposit;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yzc.common.deposit.dto.deposit.WFTypeRespDto;
import com.yzc.common.deposit.entity.WFType;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author DouQingRen
 * @since 2025-03-13
 */
public interface IWFTypeDao extends IService<WFType> {

    /**
     * 获取所有工作流类型集合
     * @return 工作流类型结婚
     */
    List<WFTypeRespDto> getAllList();
}
