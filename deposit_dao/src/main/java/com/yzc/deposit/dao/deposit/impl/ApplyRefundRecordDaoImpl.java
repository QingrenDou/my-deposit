package com.yzc.deposit.dao.deposit.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yzc.common.deposit.dto.deposit.ApplyRefundRecordRespDto;
import com.yzc.common.deposit.dto.deposit.ApplyRefundRecordSaveReqDto;
import com.yzc.common.deposit.entity.ApplyRefundRecord;
import com.yzc.deposit.dao.deposit.IApplyRefundRecordDao;
import com.yzc.deposit.repository.mapper.deposit.ApplyRefundRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class ApplyRefundRecordDaoImpl extends ServiceImpl<ApplyRefundRecordMapper, ApplyRefundRecord> implements IApplyRefundRecordDao {
    @Resource
    private ApplyRefundRecordMapper applyRefundRecordMapper;

    /**
     * 根据子账号查询羡慕退款记录
     *
     * @param subAcc 子账号
     * @return 结果集合
     */
    @Override
    public List<ApplyRefundRecordRespDto> getListBySubAcc(String subAcc) {
        if(StringUtils.isBlank(subAcc)){
            return null;
        }
        //查询
        LambdaQueryWrapper<ApplyRefundRecord> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(ApplyRefundRecord::getInSubAcc, subAcc);
        queryWrapper.orderByDesc(ApplyRefundRecord::getApplyTime);
        return BeanUtil.copyToList(applyRefundRecordMapper.selectList(queryWrapper),ApplyRefundRecordRespDto.class);
    }

    /**
     * 根据申请记录id查询退款记录
     *
     * @param applyRecordId 申请记录id
     * @return 结果集合
     */
    @Override
    public ApplyRefundRecordRespDto getById(String applyRecordId) {
        if(StringUtils.isBlank(applyRecordId)){
            return null;
        }

        return BeanUtil.copyProperties(applyRefundRecordMapper.selectById(applyRecordId),ApplyRefundRecordRespDto.class);
    }

    /**
     * 保存退款记录
     *
     * @param reqDto 请求参数
     * @return 操作结果
     */
    @Override
    public boolean save(ApplyRefundRecordSaveReqDto reqDto) {
        return applyRefundRecordMapper.insert(BeanUtil.copyProperties(reqDto,ApplyRefundRecord.class)) > 0;
    }
}
