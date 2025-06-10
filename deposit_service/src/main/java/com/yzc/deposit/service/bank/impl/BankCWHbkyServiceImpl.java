package com.yzc.deposit.service.bank.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.yzc.common.api.Result;
import com.yzc.common.deposit.dto.bank.common.*;
import com.yzc.common.deposit.dto.bank.cwHbky.EsbBaseRespDto;
import com.yzc.common.deposit.dto.bank.cwHbky.EsbInAccReqDto;
import com.yzc.common.deposit.dto.bank.cwHbky.EsbInAccRespDto;
import com.yzc.common.deposit.dto.bank.cwHbky.EsbTokenRespDto;
import com.yzc.common.deposit.dto.deposit.BankConfigRespDto;
import com.yzc.common.deposit.dto.deposit.InAccRecordSaveReqDto;
import com.yzc.common.deposit.enums.BankMoldEnum;
import com.yzc.deposit.service.bank.IBankAdapterService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 淮北矿业-财务系统实现类
 */
@Slf4j
@Service("bankCWHbkyServiceImpl")
public class BankCWHbkyServiceImpl implements IBankAdapterService {

    @Resource
    private Environment env;

    /**
     * 申请子账号
     *
     * @param reqDto 请求参数
     * @return 申请结果
     */
    @Override
    public Result<ApplySubAccRespDto> applySubAcc(ApplySubAccReqDto reqDto, BankConfigRespDto bankConfigRespDto) {
        return Result.error("当前银行不支持");
    }

    /**
     * 申请退款
     *
     * @param reqDto 请求参数
     * @return 申请结果
     */
    @Override
    public Result<ApplyBackMoneyRespDto> applyBackMoney(ApplyBackMoneyReqDto reqDto, BankConfigRespDto bankConfigRespDto) {
        return Result.error("功能暂不支持");
    }

    /**
     * 批量申请退款
     *
     * @param reqDto 请求参数
     * @return 操作结果
     */
    @Override
    public Result<List<ApplyBackMoneyRespDto>> applyBackMoneyBatch(List<ApplyBackMoneyReqDto> reqDto, BankConfigRespDto bankConfigRespDto) {
        return Result.error("功能暂不支持");
    }

    /**
     * 根据请求号获取子账号
     *
     * @param reqDto 请求参数
     * @return 子账号信息
     */
    @Override
    public Result<ApplySubAccRespDto> getSubAccByReqNo(GetSubAccByReqNoReqDto reqDto, BankConfigRespDto bankConfigRespDto) {
        return Result.error("当前银行不支持");
    }

    /**
     * 刷新当日流水
     *
     * @param reqDto            刷新参数
     * @param bankConfigRespDto
     * @return 刷新结果
     */
    @Override
    public Result<List<InAccRecordSaveReqDto>> refreshRecordListToday(RefreshRecordListTodayReqDto reqDto, BankConfigRespDto bankConfigRespDto) {
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
        return ObjectUtil.equal(bankModelCode, BankMoldEnum.CW_HBKY.getCode());
    }


    /*******************************淮矿定制业务***************************************************************************************/

    /**
     * 获取入账列表
     * @param reqDto 入账列表请求参数
     * @return 入账列表数据集合
     */
    public EsbBaseRespDto<List<EsbInAccRespDto>> getInAccList(EsbInAccReqDto reqDto){
        if (ObjectUtil.isNull(reqDto)
                || StringUtils.isBlank(reqDto.getZhbh())
                || StringUtils.isBlank(reqDto.getSDate()) || StringUtils.isBlank(reqDto.getEDate())) {
            return EsbBaseRespDto.error("请求参数不能为空");
        }

        //获取url
        String esb_url = getESBUrl();
        String url = esb_url + "com.hbky.esb.provider.siku.api.http";
        String token = getEsbToken();

        //组装参数
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("token", token);
        paramMap.put("dType", "accountDetails");
        paramMap.put("func", "getJYMXbyZh");
        paramMap.put("zhbh", reqDto.getZhbh());
        paramMap.put("sDate", reqDto.getSDate());
        paramMap.put("eDate", reqDto.getEDate());

        try {
            String applySeqNo = DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss") +"-"+ RandomUtil.randomNumbers(4);
            log.info("获取入账数据({}),url={},请求参数：{}",applySeqNo, url, paramMap);
            String jsonResult = HttpUtil.get(url, paramMap);
            log.info("获取入账数据({}),结果：{}",applySeqNo, jsonResult);
            if(StringUtils.isEmpty(jsonResult)){
                return EsbBaseRespDto.error("获取入账数据失败,接口返回结果为空");
            }
            //将结果转为 EsbBaseRespDto<List<EsbInAccRespDto>> 对象
            TypeReference<EsbBaseRespDto<List<EsbInAccRespDto>>> type = new TypeReference<>() {};
            return JSON.parseObject(jsonResult, type.getType());
        }catch (Exception e){
            log.error("获取入账数据失败,接口异常：{}", e.getMessage());
            return EsbBaseRespDto.error("获取入账列表异常");
        }
    }

    /**
     * 将银行返回的入账数据转为入库数据
     * @param respDto 银行返回的入账数据
     * @return 入库数据
     */
    public InAccRecordSaveReqDto changeBankDtoToSaveDto(EsbInAccRespDto respDto){
        if(ObjectUtil.isNull(respDto)){
            return null;
        }
        InAccRecordSaveReqDto saveReqDto = new InAccRecordSaveReqDto();
        saveReqDto.setInAccRecordId(UUID.randomUUID().toString());
        saveReqDto.setInSubAcc(respDto.getBeszhsydf()); //收款账户
        saveReqDto.setBankSeqNo(respDto.getBesyxlshpzl()); //银行流水号
        saveReqDto.setTradeMoney(respDto.getBcdzzjejsz2()); //来款金额
        saveReqDto.setTradeDay(respDto.getBesyxjyrq()); //交易日期
        //交易时间，原文据说包含冒号
        saveReqDto.setTradeTime(StringUtils.isBlank(respDto.getBesyxjysj()) ? "" : respDto.getBesyxjysj().replace(":", ""));
        saveReqDto.setAddedMsg(respDto.getBesfyoz4m()); //来款附言
        saveReqDto.setTradeSummary(respDto.getBeszyvgoh()); //交易摘要
        saveReqDto.setFromBankName(respDto.getBesdfkhxmc()); //来款银行名称
        saveReqDto.setFromBankCode(respDto.getBeskhxlxh()); //来款银行代码
        saveReqDto.setFromAcc(respDto.getBesskzhbzr()); //来款账户
        saveReqDto.setFromAccName(respDto.getBesdfhm4ef()); //来款账户名称

        return saveReqDto;
    }

    /**
     * 将银行返回的入账数据转为入库数据
     * @param respDtoList 银行返回的入账数据集合
     * @return 入库数据集合
     */
    public List<InAccRecordSaveReqDto> changeBankDtoListToSaveDtoList(List<EsbInAccRespDto> respDtoList){
        if(CollectionUtil.isEmpty(respDtoList)){
            return null;
        }
        return respDtoList.stream().map(this::changeBankDtoToSaveDto).collect(Collectors.toList());
    }

    /**
     * 获取ESB token
     * 获取结果：-1异常，0失败，其他为token
     *
     * @return token结果
     */
    public String getEsbToken() {
        try {
            //获取url
            String esb_url = getESBUrl();
            String url = esb_url + "com.hbky.esb.provider.auth.system.getToken";

            //获取参数
            String esb_systemId = env.getProperty("bank.hbky.esb_systemId");
            String esb_password = env.getProperty("bank.hbky.esb_password");
            String esb_targetSystemId = env.getProperty("bank.hbky.esb_targetSystemId");
            if (StringUtils.isBlank(esb_systemId)
                    || StringUtils.isBlank(esb_password)
                    || StringUtils.isBlank(esb_targetSystemId)) {
                log.error("获取ESB token失败：参数配置不全，请检查");
                return "-1";
            }
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("systemId", esb_systemId);
            paramMap.put("password", esb_password);
            paramMap.put("targetSystemId", esb_targetSystemId);

            log.info("获取ESB token,url={},请求参数：{}", url, paramMap);
            String resultJson = HttpUtil.post(url, paramMap);
            log.info("获取ESB token,结果：{}", resultJson);

            if (StringUtils.isBlank(resultJson)) {
                log.error("获取ESB token失败:结果为空");
                return "0";
            }
            //将结果转为 EsbBaseRespDto<EsbTokenRespDto> 对象
            TypeReference<EsbBaseRespDto<EsbTokenRespDto>> type = new TypeReference<>() {
            };
            EsbBaseRespDto<EsbTokenRespDto> esbBaseRespDto = JSON.parseObject(resultJson, type.getType());
            if (ObjectUtil.isNull(esbBaseRespDto) || ObjectUtil.isNull(esbBaseRespDto.getData())
                    || ObjectUtil.isNull(esbBaseRespDto.getData().getToken())) {
                log.error("获取ESB token失败:内容解析失败");
                return "0";
            }
            return esbBaseRespDto.getData().getToken();
        } catch (Exception e) {
            log.error("获取ESB token失败", e);
            return "-1";
        }
    }

    /**
     * 获取ESB司库地址
     *
     * @return 司库地址
     */
    private String getESBUrl() {
        return env.getProperty("bank.hbky.esb_url");
    }


    /**
     * 测试方法，请忽略
     * @return 测试token解析
     */
    public String testToken(){
        //获取url
        String esb_url = getESBUrl();
        String url = esb_url + "com.hbky.esb.provider.auth.system.getToken";
        log.info("获取ESB token测试,url={}", url);
        String resultJson = "{\"msg\": \"系统与系统认证成功\",\"code\": 200,\"data\": {\"token\": \"27e0668c27914037bd4fc4ca472eb8c3\",\"tokenExpiry\": 300,\"refreshToken\": null}}";
        TypeReference<EsbBaseRespDto<EsbTokenRespDto>> type = new TypeReference<>() {};
        EsbBaseRespDto<EsbTokenRespDto> esbBaseRespDto =  JSON.parseObject(resultJson, type.getType());
        return esbBaseRespDto.getData().getToken();
    }
}
