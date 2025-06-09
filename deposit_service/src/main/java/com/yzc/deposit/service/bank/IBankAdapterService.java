package com.yzc.deposit.service.bank;

import com.yzc.common.api.Result;
import com.yzc.common.deposit.dto.bank.common.*;
import com.yzc.common.deposit.dto.deposit.BankConfigRespDto;

import java.util.List;

/**
 * 银行适配器服务
 * 注：这里只适配通用的一些功能,个别银行特有的自定义功能，不包含在内
 */
public interface IBankAdapterService {

    /**
     * 申请子账号
     * @param reqDto 请求参数
     * @return 申请结果
     */
    Result<ApplySubAccRespDto> applySubAcc(ApplySubAccReqDto reqDto, BankConfigRespDto bankConfigRespDto);


    /**
     * 申请退款
     * @param reqDto 请求参数
     * @return 申请结果
     */
    Result<ApplyBackMoneyRespDto> applyBackMoney(ApplyBackMoneyReqDto reqDto, BankConfigRespDto bankConfigRespDto);

    /**
     * 批量申请退款
     * @param reqDto 请求参数
     * @return 操作结果
     */
    Result<List<ApplyBackMoneyRespDto>> applyBackMoneyBatch(List<ApplyBackMoneyReqDto> reqDto, BankConfigRespDto bankConfigRespDto);

    /**
     * 根据请求号获取子账号
     * @param reqDto 请求参数
     * @return 子账号信息
     */
    Result<ApplySubAccRespDto> getSubAccByReqNo(GetSubAccByReqNoReqDto reqDto, BankConfigRespDto bankConfigRespDto);

    /**
     * 刷新当日流水
     * @param reqDto 刷新参数
     * @return 刷新结果
     */
    Result refreshRecordListToday(RefreshRecordListTodayReqDto reqDto, BankConfigRespDto bankConfigRespDto);

    /**
     * 根据银行类型适配当前银行
     * @param bankModelCode 银行类型
     * @return 适配成功
     */
    boolean isCurrentBank(Integer bankModelCode);
}
