package com.yzc.deposit.service.bank.impl;

import cn.hutool.core.util.ObjectUtil;
import com.yzc.common.api.Result;
import com.yzc.common.deposit.dto.bank.common.*;
import com.yzc.common.deposit.dto.deposit.BankConfigRespDto;
import com.yzc.common.deposit.enums.BankMoldEnum;
import com.yzc.deposit.service.bank.IBankAdapterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 平安开放银行-实现类
 */
@Slf4j
@Service
public class BankPingAnOpenApiServiceImpl implements IBankAdapterService {
    /**
     * 申请子账号
     *
     * @param reqDto 请求参数
     * @return 申请结果
     */
    @Override
    public Result<ApplySubAccRespDto> applySubAcc(ApplySubAccReqDto reqDto, BankConfigRespDto bankConfigRespDto) {
        ApplySubAccRespDto respDto = new ApplySubAccRespDto();
        respDto.setSubAcc("888999");
        respDto.setWholeSubAcc("30205149888999");
        return Result.success(respDto);
    }

    /**
     * 申请退款
     *
     * @param reqDto 请求参数
     * @return 申请结果
     */
    @Override
    public Result<ApplyBackMoneyRespDto> applyBackMoney(ApplyBackMoneyReqDto reqDto, BankConfigRespDto bankConfigRespDto) {
        return Result.success();
    }

    /**
     * 批量申请退款
     *
     * @param reqDto 请求参数
     * @return 操作结果
     */
    @Override
    public Result<List<ApplyBackMoneyRespDto>> applyBackMoneyBatch(List<ApplyBackMoneyReqDto> reqDto, BankConfigRespDto bankConfigRespDto) {
        return Result.success();
    }

    /**
     * 根据请求号获取子账号
     *
     * @param reqDto 请求参数
     * @return 子账号信息
     */
    @Override
    public Result<ApplySubAccRespDto> getSubAccByReqNo(GetSubAccByReqNoReqDto reqDto, BankConfigRespDto bankConfigRespDto) {
        return Result.success();
    }

    /**
     * 刷新当日流水
     *
     * @param reqDto            刷新参数
     * @param bankConfigRespDto
     * @return 刷新结果
     */
    @Override
    public Result refreshRecordListToday(RefreshRecordListTodayReqDto reqDto, BankConfigRespDto bankConfigRespDto) {
        return Result.success();
    }

    /**
     * 根据银行类型适配当前银行
     *
     * @param bankModelCode 银行类型
     * @return 适配成功
     */
    @Override
    public boolean isCurrentBank(Integer bankModelCode) {
        return ObjectUtil.equal(bankModelCode, BankMoldEnum.PingAnOpenApi.getCode());
    }
}
