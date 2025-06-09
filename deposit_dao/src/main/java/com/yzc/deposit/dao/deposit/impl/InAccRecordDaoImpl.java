package com.yzc.deposit.dao.deposit.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.yzc.common.api.Result;
import com.yzc.common.deposit.dto.api.BidderInfoDto;
import com.yzc.common.deposit.dto.api.UpdateInfoBySubAccReqDto;
import com.yzc.common.deposit.dto.api.WinBidderWithFeeDto;
import com.yzc.common.deposit.dto.deposit.InAccRecordCountRespDto;
import com.yzc.common.deposit.dto.deposit.InAccRecordRespDto;
import com.yzc.common.deposit.dto.deposit.InAccRecordSaveReqDto;
import com.yzc.common.deposit.dto.deposit.InAccRecordUpdateReqDto;
import com.yzc.common.deposit.entity.InAccRecord;
import com.yzc.common.deposit.entity.ProjectSubAcc;
import com.yzc.common.deposit.enums.TDAuditStatusEnum;
import com.yzc.common.deposit.enums.TDBackOperTypeEnum;
import com.yzc.common.deposit.enums.UpdateProjectTypeAllEnum;
import com.yzc.common.deposit.enums.WFAuditStatusEnum;
import com.yzc.deposit.dao.deposit.IInAccRecordDao;
import com.yzc.deposit.repository.mapper.deposit.InAccRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class InAccRecordDaoImpl  extends ServiceImpl<InAccRecordMapper, InAccRecord> implements IInAccRecordDao {

    @Resource
    private InAccRecordMapper inAccRecordMapper;

    /**
     * 根据子账号，统计数量
     *
     * @param subAcc 子账号
     * @return 统计结果
     */
    @Override
    public InAccRecordCountRespDto countBySubAcc(String subAcc) {
        if (StringUtils.isBlank(subAcc)) {
            return null;
        }
        InAccRecordCountRespDto respDto = new InAccRecordCountRespDto();

        LambdaQueryWrapper<InAccRecord> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(InAccRecord::getInSubAcc, subAcc);
        queryWrapper.and(wrapper -> wrapper.isNull(InAccRecord::getDelStatus).or().ne(InAccRecord::getDelStatus, 1));

        //入账记录总数
        respDto.setInRecordCount(inAccRecordMapper.selectCount(queryWrapper).intValue());

        //已发起退款记录总数
        queryWrapper.and(wrapper -> wrapper.eq(InAccRecord::getWorkFlowAuditStatus, WFAuditStatusEnum.Auditing.getCode())
                .or().and(wrapper2 -> wrapper2.eq(InAccRecord::getBackOperType, TDBackOperTypeEnum.BackMoney.getCode())
                        .or().in(InAccRecord::getAuditStatus, TDAuditStatusEnum.WaitAudit.getCode(), TDAuditStatusEnum.Pass.getCode())));

        respDto.setRefundRecordCount(inAccRecordMapper.selectCount(queryWrapper).intValue());
        return respDto;
    }

    /**
     * 根据子账号，查询入账列表
     *
     * @param subAcc 子账号
     * @return 入账列表
     */
    @Override
    public List<InAccRecordRespDto> getListBySubAcc(String subAcc) {
        if (StringUtils.isBlank(subAcc)) {
            return null;
        }

        //查询
        MPJLambdaWrapper<InAccRecord> queryWrapper = new MPJLambdaWrapper<InAccRecord>()
                .selectAll(InAccRecord.class)
                .selectAs(ProjectSubAcc::getIsConfirmBidder, InAccRecordRespDto::getIsConfirmBidder)
                .selectAs(ProjectSubAcc::getConfirmBidderTime, InAccRecordRespDto::getConfirmBidderTime)
                .selectAs(ProjectSubAcc::getIsConfirmCandidate, InAccRecordRespDto::getIsConfirmCandidate)
                .selectAs(ProjectSubAcc::getConfirmCandidateTime, InAccRecordRespDto::getConfirmCandidateTime)
                .selectAs(ProjectSubAcc::getIsAbortive, InAccRecordRespDto::getIsAbortive)

                .leftJoin(ProjectSubAcc.class, ProjectSubAcc::getWholeSubAcc, InAccRecord::getInSubAcc)
                .eq(InAccRecord::getInSubAcc, subAcc)
                .and(wrapper -> wrapper.isNull(InAccRecord::getDelStatus).or().ne(InAccRecord::getDelStatus, 1))
                .orderByDesc(InAccRecord::getCreateTime);

        return inAccRecordMapper.selectJoinList(InAccRecordRespDto.class, queryWrapper);
    }

    /**
     * 根据id，查询入账记录
     *
     * @param id 入账记录id
     * @return 入账记录
     */
    @Override
    public InAccRecordRespDto getById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }

        return BeanUtil.copyProperties(inAccRecordMapper.selectById(id), InAccRecordRespDto.class);
    }

    /**
     * 根据银行流水号，查询入账记录
     *
     * @param subAcc 保证金账号
     * @param bankSeqNo 银行流水号
     * @return 入账记录
     */
    @Override
    public InAccRecordRespDto getByBankSeqNo(String subAcc,String bankSeqNo) {
        if(StringUtils.isBlank(bankSeqNo)){
            return null;
        }

        //流水号获取
        LambdaQueryWrapper<InAccRecord> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(InAccRecord::getInSubAcc, subAcc);
        queryWrapper.eq(InAccRecord::getBankSeqNo, bankSeqNo); //流水号
        queryWrapper.and(wrapper -> wrapper.isNull(InAccRecord::getDelStatus).or().ne(InAccRecord::getDelStatus, 1));
        List<InAccRecord> inAccRecordList = inAccRecordMapper.selectList(queryWrapper);

        return CollectionUtil.isEmpty(inAccRecordList) ? null : BeanUtil.copyProperties(inAccRecordList.get(0), InAccRecordRespDto.class);
    }

    /**
     * 根据id列表，查询入账记录
     *
     * @param inAccRecordIdList 入账记录id列表
     * @return 入账记录列表
     */
    @Override
    public List<InAccRecordRespDto> getListByIds(List<String> inAccRecordIdList) {
        if (CollectionUtil.isEmpty(inAccRecordIdList)) {
            return null;
        }
        LambdaQueryWrapper<InAccRecord> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(InAccRecord::getInAccRecordId, inAccRecordIdList);
        queryWrapper.and(wrapper -> wrapper.isNull(InAccRecord::getDelStatus).or().ne(InAccRecord::getDelStatus, 1));
        queryWrapper.orderByDesc(InAccRecord::getCreateTime);
        return BeanUtil.copyToList(inAccRecordMapper.selectList(queryWrapper), InAccRecordRespDto.class);
    }

    /**
     * 更新入账记录
     *
     * @param recordUpdateReqDto 入账记录更新请求
     * @return 操作结果
     */
    @Override
    public boolean update(InAccRecordUpdateReqDto recordUpdateReqDto) {
        if (recordUpdateReqDto == null) {
            return false;
        }
        return inAccRecordMapper.updateById(BeanUtil.copyProperties(recordUpdateReqDto, InAccRecord.class)) > 0;
    }

    /**
     * 保存入账记录
     *
     * @param saveReqDto 入账记录保存请求
     * @return 操作结果
     */
    @Override
    public boolean save(InAccRecordSaveReqDto saveReqDto) {
        if (saveReqDto == null) {
            return false;
        }

        //判断流水号是否已存在，已存在，不重复增加数据
        if(ObjectUtil.isNotNull(this.getByBankSeqNo(saveReqDto.getInSubAcc(),saveReqDto.getBankSeqNo()))){
           return true;
        }

        //主键id默认
        if(StringUtils.isBlank(saveReqDto.getInAccRecordId())){
            saveReqDto.setInAccRecordId(UUID.randomUUID().toString());
        }

        //创建日期默认
        if(ObjectUtil.isNull(saveReqDto.getCreateTime())){
            saveReqDto.setCreateTime(new Date());
        }

        //余额默认
        if(ObjectUtil.isNull(saveReqDto.getBalanceMoney())){
            saveReqDto.setBalanceMoney(saveReqDto.getTradeMoney());
        }

        return inAccRecordMapper.insert(BeanUtil.copyProperties(saveReqDto, InAccRecord.class)) > 0;
    }

    /**
     * 批量保存入账记录
     *
     * @param saveReqDtoList 入账记录保存请求列表
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatch(List<InAccRecordSaveReqDto> saveReqDtoList) {
        if(CollectionUtil.isEmpty(saveReqDtoList)){
            return false;
        }
        //循环入库
        for (InAccRecordSaveReqDto saveReqDto : saveReqDtoList) {
            this.save(saveReqDto);
        }
        return true;
    }

    /**
     * 清除中标人和候选人状态
     *
     * @param subAcc 子账号
     * @return 操作结果
     */
    @Override
    public boolean clearWinStatusBySubAcc(String subAcc) {
        if (StringUtils.isBlank(subAcc)) {
            return false;
        }
        LambdaUpdateWrapper<InAccRecord> updateWrapper = new LambdaUpdateWrapper();
        updateWrapper.set(InAccRecord::getInSubAcc, subAcc);
        updateWrapper.set(InAccRecord::getIsWinBidder, 0);
        updateWrapper.set(InAccRecord::getIsCandidate, 0);
        return inAccRecordMapper.update(null, updateWrapper) > 0;
    }

    /**
     * 更新入账记录业务状态
     *
     * @param reqDto 入账记录更新请求
     * @return 操作结果
     */
    @Override
    public Result<String> updateRecordStatus(UpdateInfoBySubAccReqDto reqDto) {
        if (StringUtils.isBlank(reqDto.getSubAcc()) || reqDto.getUpdateType() == null) {
            return Result.error("保证金账号和更新类型不能为空");
        }

        //还原sql
        LambdaUpdateWrapper<InAccRecord> resetWrapper = new LambdaUpdateWrapper();
        resetWrapper.eq(InAccRecord::getInSubAcc, reqDto.getSubAcc());

        //修改sql
        LambdaUpdateWrapper<InAccRecord> updateWrapper = new LambdaUpdateWrapper();
        updateWrapper.eq(InAccRecord::getInSubAcc, reqDto.getSubAcc());

        //修改总数
        int count = 0;

        //枚举解析
        UpdateProjectTypeAllEnum updateProjectTypeAllEnum = EnumUtil.likeValueOf(UpdateProjectTypeAllEnum.class, reqDto.getUpdateType());
        switch (updateProjectTypeAllEnum) {
            case WinBidderList:
                if(CollectionUtil.isEmpty(reqDto.getBankSeqNoList())){
                    return Result.error("中标人流水号不能为空");
                }
                //先还原所有中标人数据
                resetWrapper.eq(InAccRecord::getIsWinBidder,null);
                inAccRecordMapper.update(null, resetWrapper);

                //再设置中标人数据
                updateWrapper.in(InAccRecord::getBankSeqNo,reqDto.getBankSeqNoList());
                updateWrapper.set(InAccRecord::getIsWinBidder,1);
                return inAccRecordMapper.update(null, updateWrapper) > 0 ? Result.success("修改成功") : Result.error("修改失败");
            case WinBidderWithFeeList:
                if(CollectionUtil.isEmpty(reqDto.getBidderWithFeeList())){
                    return Result.error("中标人费用列表不能为空");
                }
                //先还原所有中标人数据
                resetWrapper.eq(InAccRecord::getIsWinBidder,null);
                inAccRecordMapper.update(null, resetWrapper);

                //循环修改数据
                for (WinBidderWithFeeDto winBidderWithFeeDto : reqDto.getBidderWithFeeList()) {
                    updateWrapper.set(InAccRecord::getIsWinBidder,1);
                    updateWrapper.set(InAccRecord::getWinAmount,winBidderWithFeeDto.getWinAmount());
                    updateWrapper.eq(InAccRecord::getBankSeqNo,winBidderWithFeeDto.getBankSeqNo());
                    count += inAccRecordMapper.update(null, updateWrapper);
                }
                return count > 0 ? Result.success("修改成功") : Result.error("修改失败");

            case WinCandidateList:
                if(CollectionUtil.isEmpty(reqDto.getBankSeqNoList())){
                    return Result.error("候选人流水号不能为空");
                }
                //先还原所有候选人数据
                resetWrapper.eq(InAccRecord::getIsCandidate,null);
                inAccRecordMapper.update(null, resetWrapper);

                //再设置候选人数据
                updateWrapper.in(InAccRecord::getBankSeqNo,reqDto.getBankSeqNoList());
                updateWrapper.set(InAccRecord::getIsCandidate,1);
                return inAccRecordMapper.update(null, updateWrapper) > 0 ? Result.success("修改成功") : Result.error("修改失败");
            case SyncBidderList:
                if(CollectionUtil.isEmpty(reqDto.getBidderList())){
                    return Result.error("投标人列表不能为空");
                }
                for (BidderInfoDto bidderInfoDto : reqDto.getBidderList()) {
                    updateWrapper.set(InAccRecord::getBidderId,bidderInfoDto.getBidderId());
                    updateWrapper.set(InAccRecord::getBidderName,bidderInfoDto.getBidderName());
                    updateWrapper.set(InAccRecord::getSignUpUserId,bidderInfoDto.getSignUpUserId());
                    updateWrapper.set(InAccRecord::getSignUpUserName,bidderInfoDto.getSignUpUserName());
                    updateWrapper.set(InAccRecord::getSignUpUserPhone,bidderInfoDto.getSignUpUserPhone());
                    updateWrapper.eq(InAccRecord::getBankSeqNo,bidderInfoDto.getBankSeqNo());
                    count += inAccRecordMapper.update(null, updateWrapper);
                }
                return count > 0 ? Result.success("修改成功") : Result.error("修改失败");
            default:
                return Result.error("更新项目状态失败：未知更新类型②");
        }
    }


}
