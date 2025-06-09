package com.yzc.deposit.dao.deposit.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yzc.common.deposit.dto.deposit.WFTypeRespDto;
import com.yzc.common.deposit.entity.WFType;
import com.yzc.deposit.dao.deposit.IWFTypeDao;
import com.yzc.deposit.repository.mapper.deposit.WFTypeMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author DouQingRen
 * @since 2025-03-13
 */
@Service
public class WFTypeDaoImpl extends ServiceImpl<WFTypeMapper, WFType> implements IWFTypeDao {

    /**
     * 获取所有工作流类型集合
     *
     * @return 工作流类型结婚
     */
    @Override
    public List<WFTypeRespDto> getAllList() {
        LambdaQueryWrapper<WFType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(WFType::getWfType);
        return BeanUtil.copyToList(this.list(queryWrapper), WFTypeRespDto.class);
    }
}
