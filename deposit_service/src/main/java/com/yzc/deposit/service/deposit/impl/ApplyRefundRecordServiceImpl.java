package com.yzc.deposit.service.deposit.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.yzc.common.api.Result;
import com.yzc.common.deposit.dto.deposit.*;
import com.yzc.common.deposit.vo.*;
import com.yzc.common.domain.LoginUserInfo;
import com.yzc.common.deposit.enums.TDAuditStatusEnum;
import com.yzc.common.deposit.enums.TDBackOperTypeEnum;
import com.yzc.common.deposit.enums.WFAuditStatusEnum;
import com.yzc.deposit.dao.deposit.IApplyRefundRecordDao;
import com.yzc.deposit.dao.deposit.IApplyRefundRecordRelatedDao;
import com.yzc.deposit.dao.deposit.IInAccRecordDao;
import com.yzc.deposit.service.AbstractBaseService;
import com.yzc.deposit.service.deposit.IApplyRefundRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ApplyRefundRecordServiceImpl extends AbstractBaseService implements IApplyRefundRecordService {

    @Resource
    private IApplyRefundRecordDao applyRefundRecordDao;

    @Resource
    private IInAccRecordDao inAccRecordDao;

    @Resource
    private IApplyRefundRecordRelatedDao applyRefundRecordRelatedDao;

    /**
     * 根据子账号查询项目退款记录
     *
     * @param subAcc 子账号
     * @return 批量退款记录
     */
    @Override
    public List<ApplyRefundRecordRespVo> getListBySubAcc(String subAcc) {
        return BeanUtil.copyToList(applyRefundRecordDao.getListBySubAcc(subAcc), ApplyRefundRecordRespVo.class);
    }

    /**
     * 根据申请记录id查询入账记录集合
     *
     * @param applyRecordId 申请记录id
     * @return 入账记录集合
     */
    @Override
    public List<InAccRecordRespVo> getInAccListByApplyRecordId(String applyRecordId) {
        //先查询申请记录
        ApplyRefundRecordRespDto applyRefundRecordRespDto = applyRefundRecordDao.getById(applyRecordId);
        if(applyRefundRecordRespDto == null){
            return null;
        }

        //查询关联子表集合
        List<ApplyRefundRecordRelatedDto> applyRefundRecordRelatedDtoList = applyRefundRecordRelatedDao.getListByApplyRecordId(applyRefundRecordRespDto.getApplyRecordId());
        if(CollectionUtil.isEmpty(applyRefundRecordRelatedDtoList)){
            return null;
        }

        //lamda表达式获取入账记录id集合
        List<String> inAccRecordIdList = applyRefundRecordRelatedDtoList.stream().map(ApplyRefundRecordRelatedDto::getInAccRecordId).collect(Collectors.toList());
        //根据id集合获取入账记录集合
        List<InAccRecordRespDto>  inAccRecordRespDtoList = inAccRecordDao.getListByIds(inAccRecordIdList);

        return BeanUtil.copyToList(inAccRecordRespDtoList, InAccRecordRespVo.class);
    }

    /**
     * 根据申请记录id查询退款记录和入账记录集合
     *
     * @param applyRecordId 申请记录id
     * @return 退款记录和入账记录集合
     */
    @Override
    public ApplyRefundAndInAccListRespVo getRefundAndInAccRecordListById(String applyRecordId) {
        //先查询申请记录
        ApplyRefundRecordRespDto applyRefundRecordRespDto = applyRefundRecordDao.getById(applyRecordId);
        if(applyRefundRecordRespDto == null){
            return null;
        }
        ApplyRefundAndInAccListRespVo respVo = BeanUtil.copyProperties(applyRefundRecordRespDto, ApplyRefundAndInAccListRespVo.class);

        //获取关联子表集合
        respVo.setInAccRecordList(getInAccListByApplyRecordId(applyRecordId));
        return respVo;
    }

    /**
     * 批量申请退款
     *
     * @param applyRefundBatchReqVo 批量申请退款请求参数
     * @return 批量申请退款结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> applyRefundBatch(ApplyRefundBatchReqVo applyRefundBatchReqVo) {
        String logStr = "applyRefundBatch";
        log.info("{}=====>批量退款开始,入参:{}",logStr, JSONUtil.toJsonStr(applyRefundBatchReqVo));

        //获取当前登录用户信息
        LoginUserInfo loginUserInfo = super.getLogInfo();

        //组装申请记录
        ApplyRefundRecordSaveReqDto applySaveReqDto = new ApplyRefundRecordSaveReqDto();
        applySaveReqDto.setApplyRecordId(UUID.randomUUID().toString());
        applySaveReqDto.setInSubAcc(applyRefundBatchReqVo.getInSubAcc());
        applySaveReqDto.setDocNumber(this.getDocNumber()); //生成单号
        applySaveReqDto.setApplyUserId(loginUserInfo.getUserId());
        applySaveReqDto.setApplyUserName(loginUserInfo.getUserName());
        applySaveReqDto.setApplyTime(new Date());

        //TODO 工作流信息
        applySaveReqDto.setWorkFlowAuditStatus(WFAuditStatusEnum.AuditPass.getCode());
        applySaveReqDto.setFlowId("");
        applySaveReqDto.setGroupId("");

        //保存申请记录
        boolean saveRst = applyRefundRecordDao.save(applySaveReqDto);
        log.info("{}=====>1.保存申请记录完成,单号={},结果:{}",logStr,applySaveReqDto.getDocNumber(),saveRst);
        if(!saveRst){
            return Result.error("保存申请记录失败");
        }

        //关联子集集合
        List<ApplyRefundRecordRelatedDto> relatedList = new ArrayList<>();
        applyRefundBatchReqVo.getApplyRecordList().forEach(item -> {
            ApplyRefundRecordRelatedDto relatedDto = new ApplyRefundRecordRelatedDto();
            relatedDto.setId(UUID.randomUUID().toString());
            relatedDto.setApplyRefundRecordId(applySaveReqDto.getApplyRecordId());
            relatedDto.setInAccRecordId(item.getInAccRecordId());
            relatedList.add(relatedDto);
        });
        boolean saveRelaListRst = applyRefundRecordRelatedDao.batchSave(relatedList);
        log.info("{}=====>2.批量保存关联子表完成,单号={},结果:{}",logStr,applySaveReqDto.getDocNumber(),saveRelaListRst);
        if(!saveRelaListRst){
            return Result.error("批量保存关联子表失败");
        }

        //循环更新入账信息
        int refundCount = 0;
        for (ApplyRecordDetailReqVo item: applyRefundBatchReqVo.getApplyRecordList()) {

            //获取原入账数据
            InAccRecordRespDto inAccRecordRespDto = inAccRecordDao.getById(item.getInAccRecordId());
            if(ObjectUtil.isNull(inAccRecordRespDto)){
                log.info("{}=====>3.1 循环处理入账数据：id={},失败原因:原入账数据不存在或已被删除",logStr,item.getInAccRecordId());
                continue;
            }

            //初始化参数
            InAccRecordUpdateReqDto recordUpdateReqDto = BeanUtil.copyProperties(item, InAccRecordUpdateReqDto.class);

            //计算余额=入账金额-扣款金额-中标服务费-转履约保证金金额+利息
            BigDecimal balanceMoney = inAccRecordRespDto.getTradeMoney()
                    .subtract(ObjectUtil.isNull(item.getDeductMoney()) ? BigDecimal.ZERO : item.getDeductMoney())
                    .subtract(ObjectUtil.isNull(item.getFeeMoney())? BigDecimal.ZERO : item.getFeeMoney())
                    .subtract(ObjectUtil.isNull(item.getPerformanceMoney()) ? BigDecimal.ZERO : item.getPerformanceMoney())
                    .add(ObjectUtil.isNull(item.getInterestMoney()) ? BigDecimal.ZERO : item.getInterestMoney());

            //如果余额小于0
            if(balanceMoney.compareTo(BigDecimal.ZERO) < 0){
                log.info("{}=====>3.2 循环处理入账数据：id={},失败原因:余额小于0,余额={}",logStr,item.getInAccRecordId(),balanceMoney);
                continue;
            }
            recordUpdateReqDto.setBalanceMoney(balanceMoney); //计算应退余额
            recordUpdateReqDto.setBackOperType(TDBackOperTypeEnum.BackMoney.getCode());
            recordUpdateReqDto.setApplyUserId(loginUserInfo.getUserId());
            recordUpdateReqDto.setApplyUserName(loginUserInfo.getUserName());
            recordUpdateReqDto.setApplyTime(new Date());
            recordUpdateReqDto.setApplyDesc("批量申请退款");
            //TODO 审核状态--待定
            recordUpdateReqDto.setAuditStatus(TDAuditStatusEnum.WaitAudit.getCode());

            //工作流状态 同申请记录状态
            recordUpdateReqDto.setWorkFlowAuditStatus(applySaveReqDto.getWorkFlowAuditStatus());
            recordUpdateReqDto.setFlowId(applySaveReqDto.getFlowId());
            recordUpdateReqDto.setGroupId(applySaveReqDto.getGroupId());

            boolean updateRst = inAccRecordDao.update(recordUpdateReqDto);
            log.info("{}=====>3.1 循环处理入账数据：id={}，数据更新结果={}",logStr,item.getInAccRecordId(),updateRst);
            if(updateRst){
                refundCount++;
            }

            //TODO 调用银行接口进行退款
        }

        //批量处理入账结果
        boolean batchRefundRst = ObjectUtil.equal(refundCount, applyRefundBatchReqVo.getApplyRecordList().size());
        log.info("{}=====>3.循环处理入账数据 完成,单号={},结果:{}",logStr,applySaveReqDto.getDocNumber(),batchRefundRst);

        //判断是否全部处理成功
        if(batchRefundRst){
            return Result.error("部分记录操作失败");
        }

        return Result.success();
    }

    /**
     * 生成单号，格式：日期格式(yyyyMMddHHmmss)+4位流水号
     * @return 单号
     */
    private String getDocNumber(){
        //生成单号
        String docNumber = DateUtil.format(new Date(), "yyyyMMddHHmmss") + RandomUtil.randomNumbers(4);
        return docNumber;
    }
}
