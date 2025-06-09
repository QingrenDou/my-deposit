package com.yzc.deposit.service.deposit.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.yzc.common.api.Result;
import com.yzc.common.deposit.enums.*;
import com.yzc.common.util.date.DateTimeUtil;
import com.yzc.common.deposit.dto.api.BindProjectReqDto;
import com.yzc.common.deposit.dto.api.InAccRecordQuery4BindReqDto;
import com.yzc.common.deposit.dto.api.InAccRecordQuery4BindRespDto;
import com.yzc.common.deposit.dto.deposit.CanBackMoneyBusinessReqDto;
import com.yzc.common.deposit.dto.deposit.InAccRecordRespDto;
import com.yzc.common.deposit.dto.deposit.InAccRecordSaveReqDto;
import com.yzc.common.deposit.dto.deposit.ProjectSubAccRespDto;
import com.yzc.common.deposit.entity.BindRecord;
import com.yzc.common.deposit.entity.InAccRecord;
import com.yzc.common.deposit.entity.ProjectSubAcc;
import com.yzc.common.deposit.util.DepositUtil;
import com.yzc.common.deposit.vo.InAccRecordRespVo;
import com.yzc.common.deposit.vo.TestInAccReqVo;
import com.yzc.deposit.dao.deposit.IBindRecordDao;
import com.yzc.deposit.dao.deposit.IInAccRecordDao;
import com.yzc.deposit.dao.deposit.IProjectSubAccDao;
import com.yzc.deposit.service.AbstractBaseService;
import com.yzc.deposit.service.deposit.IInAccRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class InAccRecordServiceImpl extends AbstractBaseService implements IInAccRecordService {

    @Resource
    private IInAccRecordDao inAccRecordDao;
    @Resource
    private IProjectSubAccDao projectSubAccDao;
    @Resource
    private IBindRecordDao bindRecordDao;

    @Resource
    private Environment env;

    /**
     * 根据子账号查询入账记录
     *
     * @param subAcc 子账号
     * @return 入账记录集合
     */
    @Override
    public List<InAccRecordRespVo> getApplyListBySubAcc(String subAcc) {
        List<InAccRecordRespDto> list = inAccRecordDao.getListBySubAcc(subAcc);
        List<InAccRecordRespVo> resultList = BeanUtil.copyToList(list, InAccRecordRespVo.class);
        if (CollectionUtil.isEmpty(resultList)) {
            return null;
        }

        //处理特殊数据
        resultList.forEach(item -> {
            //设置是否可以退款
            setCanBackMoney(item);

            //设置来款时间
            if (StringUtils.isNotBlank(item.getTradeDay())) {
                Date tradeDay = DateUtil.parse(item.getTradeDay(), "yyyyMMdd");
                item.setTradeDayStr(DateUtil.format(tradeDay, "yyyy-MM-dd"));
            }

            //设置申请状态显示名称
            if (ObjectUtil.equal(item.getWorkFlowAuditStatus(), WFAuditStatusEnum.Auditing.getCode())) {
                item.setApplyStatus(2); //待审核
            } else if (ObjectUtil.equal(item.getWorkFlowAuditStatus(), WFAuditStatusEnum.AuditNoPass.getCode())
                    || ObjectUtil.equal(item.getAuditStatus(), TDAuditStatusEnum.NotPass.getCode())) {
                item.setApplyStatus(-1); //审核不通过
            } else if (ObjectUtil.isNull(item.getAuditStatus()) || ObjectUtil.isNull(item.getWorkFlowAuditStatus())) {
                item.setApplyStatus(0); //未提交
            } else if (ObjectUtil.equal(item.getWorkFlowAuditStatus(), WFAuditStatusEnum.AuditPass.getCode())
                    || ObjectUtil.equal(item.getAuditStatus(), TDAuditStatusEnum.WaitAudit.getCode())) {
                item.setApplyStatus(1);//已提交
            }

        });

        return resultList;
    }

    /**
     * 根据入账id查询明细
     *
     * @param id 入账id
     * @return 入账明细
     */
    @Override
    public InAccRecordRespVo getById(String id) {
        return BeanUtil.copyProperties(inAccRecordDao.getById(id), InAccRecordRespVo.class);
    }

    /**
     * 根据入账id集合查询明细
     *
     * @param inAccRecordIdList 入账id集合
     * @return 入账明细集合
     */
    @Override
    public List<InAccRecordRespVo> getListByIds(List<String> inAccRecordIdList) {
        return BeanUtil.copyToList(inAccRecordDao.getListByIds(inAccRecordIdList), InAccRecordRespVo.class);
    }

    /**
     * 模拟入账
     *
     * @param reqVo 请求入参
     * @return 操作结果
     */
    @Override
    public Result<String> testInAcc(TestInAccReqVo reqVo) {
        String envCode = env.getProperty("deposit.envCode");
        if(!StringUtils.equals(envCode, "test")){
            return Result.error("非测试环境不支持模拟入账");
        }

        //根据保证金子账号获取项目信息
        ProjectSubAccRespDto projectSubAccRespDto = projectSubAccDao.getBySubAcc(reqVo.getInSubAcc());
        if(ObjectUtil.isNull(projectSubAccRespDto)){
            return Result.error("保证金子账号不存在");
        }

        //入账记录
        InAccRecordSaveReqDto saveReqDto = BeanUtil.copyProperties(reqVo, InAccRecordSaveReqDto.class);

        //其他属性
        saveReqDto.setFromAcc("62222235345352");
        saveReqDto.setFromBankName("优质采希望银行");
        saveReqDto.setBankSeqNo("yzc-"+DateUtil.format(new Date(), "yyyyMMddHHmmss") + RandomUtil.randomNumbers(4));
        saveReqDto.setTradeDay(DateUtil.format(new Date(), "yyyyMMdd"));
        saveReqDto.setTradeTime(DateUtil.format(new Date(), "HHmmss"));
        saveReqDto.setCreateTime(new Date());
        saveReqDto.setFromSource(FromSourceEnum.Bank.getCode());
        saveReqDto.setBalanceMoney(reqVo.getTradeMoney());

        return inAccRecordDao.save(saveReqDto) ? Result.success() : Result.error("入账失败");
    }

    /**
     * 设置是否可以退款
     *
     * @param item 入账记录
     */
    private void setCanBackMoney(InAccRecordRespVo item) {
        //先判断是否满足业务退款条件
        boolean canBackMoney = checkCanBackMoneyBusiness(BeanUtil.copyProperties(item, CanBackMoneyBusinessReqDto.class));
        if (!canBackMoney) {
            item.setIsCanBackMoney(0);
            item.setCannotBackMoneyReason("不满足业务退款条件");
            return;
        }
        //再结合当前记录审核状态
        if (ObjectUtil.equal(item.getWorkFlowAuditStatus(), WFAuditStatusEnum.Auditing.getCode())) {
            item.setIsCanBackMoney(0);
            item.setCannotBackMoneyReason("当前工作流正在审核中");
            return;
        }

        if (ObjectUtil.isNull(item.getBackOperType())
                || ObjectUtil.equal(item.getBackOperType(), TDBackOperTypeEnum.Performance.getCode())
                || ObjectUtil.equal(item.getAuditStatus(), TDAuditStatusEnum.NotPass.getCode())
        ) {
            item.setIsCanBackMoney(1);
            item.setCannotBackMoneyReason("可以退款");
            return;
        }

        item.setIsCanBackMoney(0);
        item.setCannotBackMoneyReason("不满足条件（未知）");
    }

    /******************绑定业务 start***************************************************/
    /**
     * 根据入账记录查询可绑定记录
     *
     * @param reqDto 请求入参
     * @return 绑定记录集合
     */
    @Override
    public List<InAccRecordQuery4BindRespDto> queryList4Bind(InAccRecordQuery4BindReqDto reqDto) {
        LambdaQueryWrapper<InAccRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InAccRecord::getInSubAcc, reqDto.getInSubAcc())
                .eq(InAccRecord::getTradeDay, reqDto.getTradeDay().replace("-", ""))
                .eq(InAccRecord::getFromAccName, reqDto.getBidderName())
                .notExists("select 1 from T_FNTDBindRecord bind left join T_FNTDProjectSubAcc p on bind.projectUniqueCode=p.projectUniqueCode  " +
                        "where 1=1 and isnull(p.IsAbortive,0)!=1 and bind.bindStatus={} and bind.inAccRecordId = T_FNTDInAccRecord.inAccRecordId", BindStatusEnum.Normal.getCode());

        //查询列表
        List<InAccRecord> list = inAccRecordDao.list(queryWrapper);

        return BeanUtil.copyToList(list, InAccRecordQuery4BindRespDto.class);
    }

    /**
     * 绑定项目
     *
     * @param reqDto 请求入参
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> bindProject(BindProjectReqDto reqDto) {
        //根据流水号获取来款记录
        InAccRecordRespDto inaccRecordRespDto = inAccRecordDao.getByBankSeqNo(reqDto.getInSubAcc(),reqDto.getBankSeqNo());
        if(ObjectUtil.isNull(inaccRecordRespDto)){
            return Result.error("流水号对应的来款信息不存在");
        }

        //根据项目编号+业务类型,生成项目唯一标识
        String projectUniqueCode = DepositUtil.getProjectUniqueCode(reqDto.getProjectCode(), reqDto.getBusinessType());

        //根据项目唯一标识判断项目是否已经存在，不存在则新增项目信息
        ProjectSubAccRespDto projectSubAccRespDto = projectSubAccDao.getByUniqueCode(projectUniqueCode);
        if(ObjectUtil.isNull(projectSubAccRespDto)){
            ProjectSubAcc saveProjectEntity = BeanUtil.copyProperties(reqDto, ProjectSubAcc.class);
            saveProjectEntity.setProjectSubAccId(IdUtil.simpleUUID());
            saveProjectEntity.setProjectUniqueCode(projectUniqueCode);
            saveProjectEntity.setApplyTime(new Date());

            //其他信息
            saveProjectEntity.setSubAccName(projectUniqueCode);
            saveProjectEntity.setSubAcc(reqDto.getInSubAcc());
            saveProjectEntity.setWholeSubAcc(reqDto.getInSubAcc());

            String profiles = env.getProperty("spring.profiles.active");
            if (StringUtils.isBlank(profiles) || !"hbky".equals(profiles)) {
                saveProjectEntity.setBankTypeCode(BankTypeCodeEnum.CW_HBKY.getCode()); //淮北矿业版本
            }

            //开标时间
            if(StringUtils.isNotBlank(reqDto.getOpenBidTimeStr())){
                saveProjectEntity.setOpenBidTime(DateUtil.parse(reqDto.getOpenBidTimeStr(), DateTimeUtil.LONG_TIME_FORMAT_WITH_SEC));
            }

            boolean saveProjectRst = projectSubAccDao.save(saveProjectEntity);
            if(!saveProjectRst){
                return Result.error("保存项目信息失败");
            }

        }

        //将已绑定数据，状态修改为：已换绑
        LambdaUpdateWrapper<BindRecord> inAccRecordWrapper = new LambdaUpdateWrapper<>();
        inAccRecordWrapper.eq(BindRecord::getInAccRecordId, inaccRecordRespDto.getInAccRecordId())
                .set(BindRecord::getBindStatus, BindStatusEnum.Unbind.getCode());
        bindRecordDao.update(inAccRecordWrapper);

        //新增绑定记录
        BindRecord bindRecord = new BindRecord();
        bindRecord.setBindRecordId(IdWorker.getId());
        bindRecord.setInAccRecordId(inaccRecordRespDto.getInAccRecordId());
        bindRecord.setProjectUniqueCode(projectUniqueCode);
        bindRecord.setBindStatus(BindStatusEnum.Normal.getCode());
        bindRecord.setBindUserId(reqDto.getBindUserId());
        bindRecord.setBindUserName(reqDto.getBindUserName());
        bindRecord.setBindTime(new Date());
        boolean saveBindRst = bindRecordDao.save(bindRecord);
        if(!saveBindRst){
            return Result.error("绑定失败");
        }

        //修改来款记录对应的投标人id和名称
        LambdaUpdateWrapper<InAccRecord> inAccRecordUpdateWrapper = new LambdaUpdateWrapper<>();
        inAccRecordUpdateWrapper.eq(InAccRecord::getInAccRecordId, inaccRecordRespDto.getInAccRecordId())
                .set(InAccRecord::getBidderId, reqDto.getBindBidderId())
                .set(InAccRecord::getBidderName, reqDto.getBindBidderName());
        inAccRecordDao.update(inAccRecordUpdateWrapper);

        return Result.success();
    }

    /******************绑定业务 end***************************************************/
}
