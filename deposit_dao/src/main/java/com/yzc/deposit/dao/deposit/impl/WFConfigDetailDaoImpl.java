package com.yzc.deposit.dao.deposit.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yzc.common.deposit.dto.deposit.WFConfigDetailRespDto;
import com.yzc.common.deposit.dto.deposit.WFConfigDetailSaveReqDto;
import com.yzc.common.deposit.entity.WFConfigDetail;
import com.yzc.deposit.dao.deposit.IWFConfigDetailDao;
import com.yzc.deposit.repository.mapper.deposit.WFConfigDetailMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author DouQingRen
 * @since 2025-03-13
 */
@Service
public class WFConfigDetailDaoImpl extends ServiceImpl<WFConfigDetailMapper, WFConfigDetail> implements IWFConfigDetailDao {


    /**
     * 根据公司id和工作流类型获取工作流详情
     *
     * @param companyId 公司id
     * @param wfType    工作流类型
     * @return 工作流详情集合
     */
    @Override
    public List<WFConfigDetailRespDto> getListByCompanyIdAndType(String companyId, Integer wfType) {
        LambdaQueryWrapper<WFConfigDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WFConfigDetail::getCompanyId, companyId)
                .eq(WFConfigDetail::getWfType, wfType)
                .orderByAsc(WFConfigDetail::getUpdateTime);
        List<WFConfigDetail> list = this.list(queryWrapper);
        return CollectionUtil.isEmpty(list) ? null : BeanUtil.copyToList(list, WFConfigDetailRespDto.class);
    }

    /**
     * 根据公司id和工作流类型删除工作流详情
     *
     * @param companyId 公司id
     * @param wfType    工作流类型
     * @return 是否删除成功
     */
    @Override
    public boolean deleteByCompanyIdAndType(String companyId, Integer wfType) {
        LambdaUpdateWrapper<WFConfigDetail> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(WFConfigDetail::getCompanyId, companyId)
                .eq(WFConfigDetail::getWfType, wfType);
        return this.remove(updateWrapper);
    }

    /**
     * 批量保存工作流详情
     *
     * @param list 工作流详情集合
     * @return 是否保存成功
     */
    @Override
    public boolean saveList(List<WFConfigDetailSaveReqDto> list) {
        if(CollectionUtil.isEmpty(list)){
            return false;
        }

        return this.saveBatch(BeanUtil.copyToList(list, WFConfigDetail.class));
    }

}
