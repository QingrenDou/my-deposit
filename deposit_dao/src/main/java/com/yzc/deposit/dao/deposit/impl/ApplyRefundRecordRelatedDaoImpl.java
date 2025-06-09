package com.yzc.deposit.dao.deposit.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yzc.common.deposit.dto.deposit.ApplyRefundRecordRelatedDto;
import com.yzc.common.deposit.entity.ApplyRefundRecordRelated;
import com.yzc.deposit.dao.deposit.IApplyRefundRecordRelatedDao;
import com.yzc.deposit.repository.mapper.deposit.ApplyRefundRecordRelatedMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class ApplyRefundRecordRelatedDaoImpl  extends ServiceImpl<ApplyRefundRecordRelatedMapper, ApplyRefundRecordRelated> implements IApplyRefundRecordRelatedDao {

    @Resource
    private ApplyRefundRecordRelatedMapper applyRefundRecordRelatedMapper;


    /**
     * 根据申请记录id查询退款记录关联子表集合
     *
     * @param applyRecordId 申请记录id
     * @return 退款记录关联子表集合
     */
    @Override
    public List<ApplyRefundRecordRelatedDto> getListByApplyRecordId(String applyRecordId) {
        if(StringUtils.isBlank(applyRecordId)){
            return null;
        }
        LambdaQueryWrapper<ApplyRefundRecordRelated> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApplyRefundRecordRelated::getApplyRefundRecordId, applyRecordId);
        return BeanUtil.copyToList(applyRefundRecordRelatedMapper.selectList(queryWrapper), ApplyRefundRecordRelatedDto.class);
    }

    /**
     * 批量保存
     *
     * @param relatedList 关联子表集合
     * @return 操作结果
     */
    @Override
    public boolean batchSave(List<ApplyRefundRecordRelatedDto> relatedList) {
        if(CollectionUtil.isEmpty(relatedList)){
            return false;
        }

        // 遍历集合，保存到数据库，并计数
        int count = 0;
        for(ApplyRefundRecordRelatedDto item : relatedList){
            ApplyRefundRecordRelated recordRelated = BeanUtil.copyProperties(item, ApplyRefundRecordRelated.class);
            count += applyRefundRecordRelatedMapper.insert(recordRelated);
        }

        return ObjectUtil.equals(count, relatedList.size());
    }
}
