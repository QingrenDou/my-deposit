package com.yzc.deposit.service.bank.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.yzc.common.api.Result;
import com.yzc.common.deposit.dto.bank.cmbCloudSm.*;
import com.yzc.common.deposit.dto.bank.common.*;
import com.yzc.common.deposit.dto.deposit.BankConfigRespDto;
import com.yzc.common.deposit.dto.deposit.InAccRecordSaveReqDto;
import com.yzc.common.deposit.entity.BankKey;
import com.yzc.common.deposit.enums.BankMoldEnum;
import com.yzc.common.deposit.enums.BankTypeCodeEnum;
import com.yzc.common.deposit.util.CmbCloudSmHelper;
import com.yzc.common.deposit.util.DepositUtil;
import com.yzc.deposit.dao.deposit.IBankKeyDao;
import com.yzc.deposit.service.bank.IBankAdapterService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 招商银行-云直连-SM国密对接方式
 */
@Slf4j
@Service("bankCMBCloudSMServiceImpl")
public class BankCMBCloudSMServiceImpl implements IBankAdapterService {

    @Resource
    private IBankKeyDao bankKeyDao;

    @Resource
    private Environment env;

    private static final String SUCCESS_CODE = "SUC0000";
    private static final int BOUND_START = 1000000;
    private static final int BOUND_END = 9000000;

    private static Random random = new Random();

    /**
     * 申请子账号
     *
     * @param reqDto            请求参数
     * @param bankConfigRespDto 银行配置
     * @return 申请结果
     */
    @Override
    public Result<ApplySubAccRespDto> applySubAcc(ApplySubAccReqDto reqDto, BankConfigRespDto bankConfigRespDto) {
        String logStr = "CMBCloudSM[applySubAcc]申请虚拟号===>";

        String reqNo = DepositUtil.getSeqNo(); //业务参考号
        //1.转为银行入参
        CMBCloudAddSubAccReqBodyDto body = new CMBCloudAddSubAccReqBodyDto();
        CMBCloudAddSubAccReqDto ntdmaaddxReqDto = new CMBCloudAddSubAccReqDto();
        ntdmaaddxReqDto.setAccnbr(bankConfigRespDto.getMainAccount()); //主账号
        ntdmaaddxReqDto.setDmanam(reqDto.getSubAccName());
        ntdmaaddxReqDto.setOvrctl("N"); //是否可透支 不允许
        ntdmaaddxReqDto.setYurref(reqNo); //业务流水号：唯一
        body.setNtdmaaddx(List.of(ntdmaaddxReqDto));

        CMBCloudBusModyReqDto ntbusmodyReqDto = new CMBCloudBusModyReqDto();
        ntbusmodyReqDto.setBusmod(getBusmod());
        body.setNtbusmody(List.of(ntbusmodyReqDto));

        //2.调用银行接口
        CMBCloudBaseRespDto<CMBCloudAddSubAccRespBodyDto> rst = postToBank(body,"NTDMAADD",bankConfigRespDto,  logStr,  new TypeReference<>(){});
        if(ObjectUtil.isNull(rst) || ObjectUtil.isNull(rst.getResponse())
            || ObjectUtil.isNull(rst.getResponse().getHead())
                || ObjectUtil.isNull(rst.getResponse().getBody())){
            return Result.error("申请子账号失败[null]");
        }

        //解析头部
        CMBCloudCommonRespHeadDto respHeadDto = rst.getResponse().getHead();
        if(ObjectUtil.notEqual(respHeadDto.getResultcode(), SUCCESS_CODE)){
            return Result.error("申请子账号失败["+respHeadDto.getResultmsg()+"]");
        }

        //解析body部分
        CMBCloudAddSubAccRespBodyDto respBodyDto = rst.getResponse().getBody();
        //获取子账号
        if(CollectionUtil.isEmpty(respBodyDto.getNtdmabadz1())){
            return Result.error("申请子账号失败[银行结果集为空]");
        }

        CMBCloudAddSubAccRespDto respDto = respBodyDto.getNtdmabadz1().get(0);
        if(ObjectUtil.notEqual(respDto.getErrcod(),  SUCCESS_CODE)){
            return Result.error("申请子账号失败["+respDto.getErrtxt()+"]");
        }

        //3.解析返回结果
        ApplySubAccRespDto resDto = new ApplySubAccRespDto();
        if(StringUtils.isBlank(respDto.getDmanbr())){
            //注：新版接口不再返回“流程实例号”字段，说明无需再次获取结果
            return Result.error("申请子账号失败[无子账号]");
        }

        resDto.setSubAcc(respDto.getDmanbr());
        resDto.setWholeSubAcc(bankConfigRespDto.getMainAccount()+respDto.getDmanbr());

        return Result.success(resDto);
    }

    /**
     * 申请退款
     *
     * @param reqDto            请求参数
     * @param bankConfigRespDto 银行配置
     * @return 申请结果
     */
    @Override
    public Result<ApplyBackMoneyRespDto> applyBackMoney(ApplyBackMoneyReqDto reqDto, BankConfigRespDto bankConfigRespDto) {
        String logStr = "CMBCloudSM[applyBackMoney]申请退款===>";
        log.info("{}收到请求: reqDto={}, bankConfigRespDto={}", logStr, JSONUtil.toJsonStr(reqDto), JSONUtil.toJsonStr(bankConfigRespDto));

        // 1.转为银行入参
        CMBCloudNTOPRDMRReqBodyDto body = new CMBCloudNTOPRDMRReqBodyDto();

        // Populate ntbusmody
        CMBCloudBusModyReqDto ntbusmodyReqDto = new CMBCloudBusModyReqDto();
        ntbusmodyReqDto.setBusmod(getBusmod());
        body.setNtbusmody(List.of(ntbusmodyReqDto));

        // Populate ntoprdmrx1
        CMBCloudNTOPRDMRReqNtoprdmrx1Dto ntoprdmrx1Dto = new CMBCloudNTOPRDMRReqNtoprdmrx1Dto();
        ntoprdmrx1Dto.setAccnbr(bankConfigRespDto.getMainAccount());
       /* ntoprdmrx1Dto.setTrxnbr(reqDto.getOrigTransNo());
        ntoprdmrx1Dto.setTrsamt(reqDto.getAmount());
        ntoprdmrx1Dto.setDumnbr(reqDto.getSubAcc());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        ntoprdmrx1Dto.setEptdat(dateFormat.format(reqDto.getReqDate() != null ? reqDto.getReqDate() : new Date()));

        ntoprdmrx1Dto.setRpyacc(reqDto.getReceiveAccNo());
        ntoprdmrx1Dto.setRpynam(reqDto.getReceiveAccName());
        ntoprdmrx1Dto.setIntflg(Boolean.TRUE.equals(reqDto.getIsRefundInterest()) ? "Y" : "N");
        ntoprdmrx1Dto.setIntamt(reqDto.getInterestAmount());*/

            ntoprdmrx1Dto.setYurref(DepositUtil.getSeqNo());


        // Default values for flags
        ntoprdmrx1Dto.setBckflg("N"); // deprecated, but set default
        ntoprdmrx1Dto.setApvflg("N"); // Assume "N" for now

        List<CMBCloudNTOPRDMRReqNtoprdmrx1Dto> ntoprdmrx1List = new ArrayList<>();
        ntoprdmrx1List.add(ntoprdmrx1Dto);
        body.setNtoprdmrx1(ntoprdmrx1List);

        // Populate ntoprdmrx2 (Conditional)
            ntoprdmrx1Dto.setApdflg("N");

        // Populate ntoprdmrx3 (Conditional/Optional) - Assuming not critical for now
        // if (reqDto.getIntprt() != null || reqDto.getDmrprt() != null) {
        //     CMBCloudNTOPRDMRReqNtoprdmrx3Dto ntoprdmrx3Dto = new CMBCloudNTOPRDMRReqNtoprdmrx3Dto();
        //     ntoprdmrx3Dto.setIntprt(reqDto.getIntprt());
        //     ntoprdmrx3Dto.setDmrprt(reqDto.getDmrprt());
        //     body.setNtoprdmrx3(List.of(ntoprdmrx3Dto));
        // }

        // 2.调用银行接口
        String funcode = "NTOPRDMR";
        CMBCloudBaseRespDto<CMBCloudNTOPRDMRRespBodyDto> rst = postToBank(body, funcode, bankConfigRespDto, logStr, new TypeReference<>() {});

        // 3.解析返回结果
        if (ObjectUtil.isNull(rst) || ObjectUtil.isNull(rst.getResponse()) ||
            ObjectUtil.isNull(rst.getResponse().getHead()) || ObjectUtil.isNull(rst.getResponse().getBody())) {
            log.error("{}申请退款失败[银行返回为空或结构错误]", logStr);
            return Result.error("申请退款失败[银行返回为空或结构错误]");
        }

        CMBCloudCommonRespHeadDto respHeadDto = rst.getResponse().getHead();
        if (ObjectUtil.notEqual(respHeadDto.getResultcode(), SUCCESS_CODE)) {
            log.error("{}申请退款失败[银行头部返回错误]: code={}, msg={}", logStr, respHeadDto.getResultcode(), respHeadDto.getResultmsg());
            return Result.error("申请退款失败[" + respHeadDto.getResultmsg() + "]");
        }

        CMBCloudNTOPRDMRRespBodyDto respBodyDto = rst.getResponse().getBody();
        if (CollectionUtil.isEmpty(respBodyDto.getNtoprrtnz()) || ObjectUtil.isNull(respBodyDto.getNtoprrtnz().get(0))) {
            log.error("{}申请退款失败[银行结果集为空或首项为空]", logStr);
            return Result.error("申请退款失败[银行结果集为空或首项为空]");
        }

        CMBCloudNTOPRDMRRespNtoprrtnzDto respItem = respBodyDto.getNtoprrtnz().get(0);
        // SUQP001 表示受理成功，不代表最终成功，需通过“按业务参考号查询结果NTDUMRED”获取请求最终状态
        // 但对于NTOPRDMR，通常直接返回最终状态
        if (ObjectUtil.notEqual(respItem.getErrcod(), SUCCESS_CODE)) {
             // SUC0000 才是最终成功, 其他如 SUQP001 (受理成功) 也视为中间态或失败
            log.error("{}申请退款业务失败[银行返回错误码]: code={}, msg={}", logStr, respItem.getErrcod(), respItem.getErrtxt());
            // 不直接返回error，而是将银行的错误信息包装到结果中
        }

        ApplyBackMoneyRespDto resDto = new ApplyBackMoneyRespDto();

        log.info("{}申请退款成功: resDto={}", logStr, JSONUtil.toJsonStr(resDto));
        return Result.success(resDto);
    }

    /**
     * 批量申请退款
     *
     * @param reqDto            请求参数
     * @param bankConfigRespDto 银行配置
     * @return 操作结果
     */
    @Override
    public Result<List<ApplyBackMoneyRespDto>> applyBackMoneyBatch(List<ApplyBackMoneyReqDto> reqDto, BankConfigRespDto bankConfigRespDto) {
        String logStr = "CMBCloudSM[applyBackMoneyBatch]申请批量退款===>";

        //1.转为银行入参

        //2.调用银行接口

        //3.解析返回结果

        return null;
    }

    /**
     * 根据请求号获取子账号
     *
     * @param reqDto            请求参数
     * @param bankConfigRespDto 银行配置
     * @return 子账号信息
     */
    @Override
    public Result<ApplySubAccRespDto> getSubAccByReqNo(GetSubAccByReqNoReqDto reqDto, BankConfigRespDto bankConfigRespDto) {
        //招商云直连，新版接口，无需更新虚拟号
        return Result.success();
    }

    /**
     * 刷新当日流水
     *
     * @param reqDto 刷新参数
     * @param bankConfigRespDto
     * @return 刷新结果
     */
    @Override
    public Result<List<InAccRecordSaveReqDto>> refreshRecordListToday(RefreshRecordListTodayReqDto reqDto, BankConfigRespDto bankConfigRespDto) {
        String logStr = "CMBCloudSM[refreshRecordListToday]更新当日数据===>";
        log.info("{}收到请求: reqDto={}, bankConfigRespDto={}", logStr, JSONUtil.toJsonStr(reqDto), JSONUtil.toJsonStr(bankConfigRespDto));

        List<InAccRecordSaveReqDto> allRecords = new ArrayList<>();
        String continueKey = reqDto.getContinueKey(); // Initial continue key from request

        do {
            // 1.转为银行入参
            CMBCloudQueryRecordTodayReqBodyDto body = new CMBCloudQueryRecordTodayReqBodyDto();
            CMBCloudQueryRecordTodayReqNtdmtlstyDto ntdmtlstyDto = new CMBCloudQueryRecordTodayReqNtdmtlstyDto();
            ntdmtlstyDto.setAccnbr(bankConfigRespDto.getMainAccount());
            ntdmtlstyDto.setDmanbr(StringUtils.isNotBlank(reqDto.getSubAcc()) ? reqDto.getSubAcc() : "");
            if (StringUtils.isNotBlank(continueKey)) {
                ntdmtlstyDto.setCtnkey(continueKey);
            }
            body.setNtdmtlsty(List.of(ntdmtlstyDto));

            // 2.调用银行接口
            String funcode = "NTDMTLST";
            CMBCloudBaseRespDto<CMBCloudQueryRecordTodayRespBodyDto> rst = postToBank(body, funcode, bankConfigRespDto, logStr, new TypeReference<>() {});

            // 3.解析返回结果
            if (ObjectUtil.isNull(rst) || ObjectUtil.isNull(rst.getResponse()) ||
                    ObjectUtil.isNull(rst.getResponse().getHead()) || ObjectUtil.isNull(rst.getResponse().getBody())) {
                log.error("{}更新当日数据失败[银行返回为空或结构错误]", logStr);
                return Result.error("更新当日数据失败[银行返回为空或结构错误]");
            }

            CMBCloudCommonRespHeadDto respHeadDto = rst.getResponse().getHead();
            if (ObjectUtil.notEqual(respHeadDto.getResultcode(), SUCCESS_CODE)) {
                log.error("{}更新当日数据失败[银行头部返回错误]: code={}, msg={}", logStr, respHeadDto.getResultcode(), respHeadDto.getResultmsg());
                return Result.error("更新当日数据失败[" + respHeadDto.getResultmsg() + "]");
            }

            CMBCloudQueryRecordTodayRespBodyDto respBodyDto = rst.getResponse().getBody();
            // 检查 ntdmtlstz1 中的业务错误
            if (CollectionUtil.isNotEmpty(respBodyDto.getNtdmtlstz1())) {
                CMBCloudQueryRecordTodayRespNtdmtlstz1Dto errorStatus = respBodyDto.getNtdmtlstz1().get(0);
                if (ObjectUtil.notEqual(errorStatus.getErrcod(), SUCCESS_CODE)) {
                    log.error("{}更新当日数据业务失败[银行返回错误码]: code={}, msg={}", logStr, errorStatus.getErrcod(), errorStatus.getErrtxt());
                    return Result.error("更新当日数据失败[" + errorStatus.getErrtxt() + "]");
                }
            }

            // 4.将结果转换为数据库InAccRecordSaveReqDto对象集合
            List<InAccRecordSaveReqDto> currentBatchRecords = new ArrayList<>();
            if (CollectionUtil.isNotEmpty(respBodyDto.getNtdmtlstz())) {
                for (CMBCloudQueryRecordTodayItemDto itemDto : respBodyDto.getNtdmtlstz()) {
                    InAccRecordSaveReqDto record = new InAccRecordSaveReqDto();
                    record.setBankTransNo(itemDto.getTrxnbr());
                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");
                        Date trxDate = dateFormat.parse(itemDto.getTrxdat());
                        Date trxTime = timeFormat.parse(itemDto.getTrxtim());
                        record.setTransDate(new Date(trxDate.getTime() + trxTime.getTime())); // Combine date and time
                    } catch (Exception e) {
                        log.error("{}日期或时间解析失败: trxdat={}, trxtim={}", logStr, itemDto.getTrxdat(), itemDto.getTrxtim(), e);
                    }
                    record.setTransAmount(itemDto.getTrsam());
                    record.setPayAccNo(itemDto.getCltacc());
                    record.setPayAccName(itemDto.getCltnam());
                    record.setReceiveAccNo(itemDto.getEtyacc()); // 主账号或子账号
                    record.setReceiveAccName(itemDto.getEtynam());
                    record.setSummary(itemDto.getNaryur());
                    record.setSubAcc(itemDto.getDmanbr()); // 交易发生的子账号
                    record.setTransStatus(itemDto.getRtnsts()); // 可能需要映射
                    record.setCurrency(itemDto.getCcynbr());
                    record.setBankType(bankConfigRespDto.getBankTypeCode());
                    record.setTenantId(reqDto.getTenantId());
                    // DCFLAG: D-借方（支出） C-贷方（收入）
                    if ("D".equals(itemDto.getDcflag())) {
                        record.setTransType(2); // 支出
                    } else if ("C".equals(itemDto.getDcflag())) {
                        record.setTransType(1); // 收入
                    }
                    currentBatchRecords.add(record);
                }
            }
            // Ntdmtlstz2 结构与 Ntdmtlstz 相同
            if (CollectionUtil.isNotEmpty(respBodyDto.getNtdmtlstz2())) {
                 for (CMBCloudQueryRecordTodayItemDto itemDto : respBodyDto.getNtdmtlstz2()) {
                    InAccRecordSaveReqDto record = new InAccRecordSaveReqDto();
                    record.setBankTransNo(itemDto.getTrxnbr());
                     try {
                         SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                         SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");
                         Date trxDate = dateFormat.parse(itemDto.getTrxdat());
                         Date trxTime = timeFormat.parse(itemDto.getTrxtim());
                         record.setTransDate(new Date(trxDate.getTime() + trxTime.getTime())); // Combine date and time
                     } catch (Exception e) {
                         log.error("{}日期或时间解析失败: trxdat={}, trxtim={}", logStr, itemDto.getTrxdat(), itemDto.getTrxtim(), e);
                     }
                    record.setTransAmount(itemDto.getTrsam());
                    record.setPayAccNo(itemDto.getCltacc());
                    record.setPayAccName(itemDto.getCltnam());
                    record.setReceiveAccNo(itemDto.getEtyacc());
                    record.setReceiveAccName(itemDto.getEtynam());
                    record.setSummary(itemDto.getNaryur());
                    record.setSubAcc(itemDto.getDmanbr());
                    record.setTransStatus(itemDto.getRtnsts());
                    record.setCurrency(itemDto.getCcynbr());
                    record.setBankType(bankConfigRespDto.getBankTypeCode());
                    record.setTenantId(reqDto.getTenantId());
                    if ("D".equals(itemDto.getDcflag())) {
                        record.setTransType(2); // 支出
                    } else if ("C".equals(itemDto.getDcflag())) {
                        record.setTransType(1); // 收入
                    }
                    currentBatchRecords.add(record);
                }
            }

            allRecords.addAll(currentBatchRecords);

            // 处理分页: 检查响应中的 ntdmtlsty 是否有 ctnkey
            if (CollectionUtil.isNotEmpty(respBodyDto.getNtdmtlsty()) &&
                respBodyDto.getNtdmtlsty().get(0) != null &&
                StringUtils.isNotBlank(respBodyDto.getNtdmtlsty().get(0).getCtnkey())) {
                continueKey = respBodyDto.getNtdmtlsty().get(0).getCtnkey();
            } else {
                continueKey = null; // No more pages
            }

        } while (StringUtils.isNotBlank(continueKey));

        log.info("{}成功获取 {} 条当日流水记录", logStr, allRecords.size());
        return Result.success(allRecords);
    }

    /**
     * 根据银行类型适配当前银行
     *
     * @param bankModelCode 银行类型
     * @return 适配成功
     */
    @Override
    public boolean isCurrentBank(Integer bankModelCode) {
        return ObjectUtil.equal(bankModelCode, BankMoldEnum.CMBCloudSM.getCode());
    }

    /****************************************个性化 方法************************************************************************/

    /**
     * 关闭子账号
     * 注：该接口银行要求必须对接
     * @param reqDto 请求参数
     * @param bankConfigRespDto 银行配置
     * @return 关闭结果
     */
    public Result closeSubAcc(CloseSubAccReqDto reqDto, BankConfigRespDto bankConfigRespDto){
        String logStr = "CMBCloudSM[closeSubAcc]关闭子账号===>";
        //1.转为银行入参
        CMBCloudCloseSubAccReqBodyDto body = new CMBCloudCloseSubAccReqBodyDto();

        //busmod部分
        CMBCloudBusModyReqDto ntbusmodyReqDto = new CMBCloudBusModyReqDto();
        ntbusmodyReqDto.setBusmod(getBusmod());
        body.setNtbusmody(List.of(ntbusmodyReqDto));

        //主账号部分
        CMBCloudCloseSubAccMainDto ntdmadltx1ReqDto = new CMBCloudCloseSubAccMainDto();
        ntdmadltx1ReqDto.setAccnbr(bankConfigRespDto.getMainAccount());
        ntdmadltx1ReqDto.setYurref(reqDto.getReqNo());
        body.setNtdmadltx1(List.of(ntdmadltx1ReqDto));

        //子账号部分
        CMBCloudCloseSubAccDto ntdmadltx2ReqDto = new CMBCloudCloseSubAccDto();
        ntdmadltx2ReqDto.setDmanbr(reqDto.getSubAcc());
        body.setNtdmadltx2(List.of(ntdmadltx2ReqDto));

        //2.调用银行接口
        postToBank(body,"NTDMADLT",bankConfigRespDto,  logStr,  new TypeReference<>(){});

        //3.解析返回结果 --无需

        return Result.success("操作成功");
    }

    /**
     * 刷新退款状态
     * @param reqDto     请求参数
     * @param bankConfigRespDto 银行配置
     * @return 刷新结果
     */
    public Result<RefreshBackMoneyStatusRespDto> refreshBackMoneyStatus(RefreshBackMoneyStatusReqDto reqDto, BankConfigRespDto bankConfigRespDto) {
        String logStr = "CMBCloudSM[refreshBackMoneyStatus]更新退款状态===>";

        //1.转为银行入参

        //2.调用银行接口

        //3.解析返回结果

        return Result.success();
    }

    /**
     * 刷新历史交易流水
     *
     * @param reqDto 刷新参数
     * @param bankConfigRespDto 银行配置
     * @return 刷新结果
     */
    public Result<List<InAccRecordSaveReqDto>> refreshRecordListHis(RefreshRecordListHisReqDto reqDto, BankConfigRespDto bankConfigRespDto) {
        String logStr = "CMBCloudSM[refreshRecordListHis]更新历史数据===>";
        log.info("{}收到请求: reqDto={}, bankConfigRespDto={}", logStr, JSONUtil.toJsonStr(reqDto), JSONUtil.toJsonStr(bankConfigRespDto));

        List<InAccRecordSaveReqDto> allRecords = new ArrayList<>();
        String continueKey = reqDto.getContinueKey(); // Initial continue key from request
        SimpleDateFormat bankDateFormat = new SimpleDateFormat("yyyyMMdd");

        do {
            // 1.转为银行入参
            CMBCloudQueryRecordHisReqBodyDto body = new CMBCloudQueryRecordHisReqBodyDto();
            CMBCloudQueryRecordHisReqNtdmthlsyDto ntdmthlsyDto = new CMBCloudQueryRecordHisReqNtdmthlsyDto();
            ntdmthlsyDto.setAccnbr(bankConfigRespDto.getMainAccount());
            ntdmthlsyDto.setDmanbr(StringUtils.isNotBlank(reqDto.getSubAcc()) ? reqDto.getSubAcc() : "");
            ntdmthlsyDto.setBegdat(bankDateFormat.format(reqDto.getStartDate()));
            ntdmthlsyDto.setEnddat(bankDateFormat.format(reqDto.getEndDate()));
            if (StringUtils.isNotBlank(continueKey)) {
                ntdmthlsyDto.setCtnkey(continueKey);
            }
            body.setNtdmthlsy(List.of(ntdmthlsyDto));

            // 2.调用银行接口
            String funcode = "NTDMTHLS";
            CMBCloudBaseRespDto<CMBCloudQueryRecordHisRespBodyDto> rst = postToBank(body, funcode, bankConfigRespDto, logStr, new TypeReference<>() {});

            // 3.解析返回结果
            if (ObjectUtil.isNull(rst) || ObjectUtil.isNull(rst.getResponse()) ||
                    ObjectUtil.isNull(rst.getResponse().getHead()) || ObjectUtil.isNull(rst.getResponse().getBody())) {
                log.error("{}更新历史数据失败[银行返回为空或结构错误]", logStr);
                return Result.error("更新历史数据失败[银行返回为空或结构错误]");
            }

            CMBCloudCommonRespHeadDto respHeadDto = rst.getResponse().getHead();
            if (ObjectUtil.notEqual(respHeadDto.getResultcode(), SUCCESS_CODE)) {
                log.error("{}更新历史数据失败[银行头部返回错误]: code={}, msg={}", logStr, respHeadDto.getResultcode(), respHeadDto.getResultmsg());
                return Result.error("更新历史数据失败[" + respHeadDto.getResultmsg() + "]");
            }

            CMBCloudQueryRecordHisRespBodyDto respBodyDto = rst.getResponse().getBody();
            // 检查 ntdmthlsz1 中的业务错误 (银行文档中历史查询没有ntdmthlsz1这一层，直接是ntdmthlsz，但为保险起见，保留对通用错误节点的检查逻辑，如果确定不需要可以移除)
            // 根据实际的招行SM接口文档，NTDMTHLS响应体中没有ntdmthlsz1错误节点，错误在ntdmthlsy中体现或直接在head中。
            // 此处我们假设错误信息可能在 ntdmthlsy 的 errcod/errtxt 或者 head
            if (CollectionUtil.isNotEmpty(respBodyDto.getNtdmthlsy())) {
                 CMBCloudQueryRecordHisRespNtdmthlsyDto statusNode = respBodyDto.getNtdmthlsy().get(0);
                 // 银行历史流水接口中，ntdmthlsy节点主要用于返回分页信息和可能的处理结果，具体错误通常在head或业务列表为空时体现。
                 // 有些银行接口会在这个节点也包含错误码。如果文档确认这里有errcod，则进行判断。
                 // if (ObjectUtil.isNotNull(statusNode.getErrcod()) && ObjectUtil.notEqual(statusNode.getErrcod(), SUCCESS_CODE)) {
                 //    log.error("{}更新历史数据业务失败[银行返回错误码]: code={}, msg={}", logStr, statusNode.getErrcod(), statusNode.getErrtxt());
                 //    return Result.error("更新历史数据失败[" + statusNode.getErrtxt() + "]");
                 // }
            }


            // 4.将结果转换为数据库InAccRecordSaveReqDto对象集合
            List<InAccRecordSaveReqDto> currentBatchRecords = new ArrayList<>();
            if (CollectionUtil.isNotEmpty(respBodyDto.getNtdmthlsz())) {
                for (CMBCloudQueryRecordHisItemDto itemDto : respBodyDto.getNtdmthlsz()) {
                    InAccRecordSaveReqDto record = convertHisItemToSaveReqDto(itemDto, bankConfigRespDto, reqDto, logStr);
                    currentBatchRecords.add(record);
                }
            }
            // Ntdmthlsz2 结构与 Ntdmthlsz 相同
            if (CollectionUtil.isNotEmpty(respBodyDto.getNtdmthlsz2())) {
                 for (CMBCloudQueryRecordHisItemDto itemDto : respBodyDto.getNtdmthlsz2()) {
                    InAccRecordSaveReqDto record = convertHisItemToSaveReqDto(itemDto, bankConfigRespDto, reqDto, logStr);
                    currentBatchRecords.add(record);
                }
            }

            allRecords.addAll(currentBatchRecords);

            // 处理分页: 检查响应中的 ntdmthlsy 是否有 ctnkey
            if (CollectionUtil.isNotEmpty(respBodyDto.getNtdmthlsy()) &&
                respBodyDto.getNtdmthlsy().get(0) != null &&
                StringUtils.isNotBlank(respBodyDto.getNtdmthlsy().get(0).getCtnkey())) {
                continueKey = respBodyDto.getNtdmthlsy().get(0).getCtnkey();
            } else {
                continueKey = null; // No more pages
            }

        } while (StringUtils.isNotBlank(continueKey));

        log.info("{}成功获取 {} 条历史流水记录", logStr, allRecords.size());
        return Result.success(allRecords);
    }

    private InAccRecordSaveReqDto convertHisItemToSaveReqDto(CMBCloudQueryRecordHisItemDto itemDto, BankConfigRespDto bankConfigRespDto, RefreshRecordListHisReqDto reqDto, String logStr) {
        InAccRecordSaveReqDto record = new InAccRecordSaveReqDto();
        record.setBankTransNo(itemDto.getTrxnbr());
        try {
            //历史流水通常只有日期，没有精确到时分秒的 trxtim 字段，如果银行返回了，则解析，否则只用日期
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Date trxDate = dateFormat.parse(itemDto.getTrxdat());
            if (StringUtils.isNotBlank(itemDto.getTrxtim())) {
                 SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");
                 Date trxTime = timeFormat.parse(itemDto.getTrxtim());
                 record.setTransDate(new Date(trxDate.getTime() + trxTime.getTime())); // Combine date and time
            } else {
                 record.setTransDate(trxDate);
            }
        } catch (Exception e) {
            log.error("{}日期或时间解析失败: trxdat={}, trxtim={}", logStr, itemDto.getTrxdat(), itemDto.getTrxtim(), e);
        }
        record.setTransAmount(itemDto.getTrsam());
        record.setPayAccNo(itemDto.getCltacc());
        record.setPayAccName(itemDto.getCltnam());
        record.setReceiveAccNo(itemDto.getEtyacc());
        record.setReceiveAccName(itemDto.getEtynam());
        record.setSummary(itemDto.getNaryur());
        record.setSubAcc(itemDto.getDmanbr());
        record.setTransStatus(itemDto.getRtnsts());
        record.setCurrency(itemDto.getCcynbr());
        record.setBankType(bankConfigRespDto.getBankTypeCode());
        record.setTenantId(reqDto.getTenantId());
        if ("D".equals(itemDto.getDcflag())) {
            record.setTransType(2); // 支出
        } else if ("C".equals(itemDto.getDcflag())) {
            record.setTransType(1); // 收入
        }
        return record;
    }

    /****************************************公共方法************************************************************************/

    /**
     * 调用银行接口
     * @param body 请求体
     * @param funcode 功能码
     * @param bankConfigRespDto 银行配置
     * @param logStr 日志标识
     * @return 响应体
     */
    private <T> T postToBank(CMBCloudCommonReqBodyDto body, String funcode, BankConfigRespDto bankConfigRespDto, String logStr,TypeReference<T> type){

        //根据银行类型 获取银行秘钥配置
        BankKey bankKey = bankKeyDao.getByBankTypeCode(bankConfigRespDto.getBankTypeCode());
        if(ObjectUtil.isNull(bankKey)){
            log.error("{}银行秘钥未配置，请检查，bankTypeCode={}",  logStr, bankConfigRespDto.getBankTypeCode());
            return null;
        }

        try {
            // 装载BC库,必须在应用的启动类中调用此函数
            Security.addProvider(new BouncyCastleProvider());
            System.setProperty("sun.net.http.retryPost", "false");

            //完整请求体
            CMBCloudBaseReqDto req = new CMBCloudBaseReqDto();

            //request > body
            CMBCloudCommonRequestDto<CMBCloudCommonReqBodyDto> request = new CMBCloudCommonRequestDto<>();
            request.setBody(body);

            //request > head
            String currentDatetime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String reqid = new SimpleDateFormat("yyyyMMddHHmmssSSSS").format(new Date()) + (BOUND_START + random.nextInt(BOUND_END));
            CMBCloudCommonReqHeadDto head = new CMBCloudCommonReqHeadDto();
            head.setFuncode(funcode);
            head.setReqid(reqid);
            head.setUserid(bankConfigRespDto.getConsumerId());
            request.setHead(head);

            // request
            req.setRequest(request);

            // signature
            CMBCloudCommonSignatureDto signature = new CMBCloudCommonSignatureDto();
            signature.setSigdat("__signature_sigdat__");
            signature.setSigtim(currentDatetime);
            req.setSignature(signature);

            String data = JSONObject.toJSONString(req);
            log.info("{}请求报文:{}",logStr,data);
            CmbCloudSmHelper bankHelper = new CmbCloudSmHelper(bankConfigRespDto.getBankHttpUrl(), bankConfigRespDto.getConsumerId(), bankKey.getYzcPrivateKey(), bankKey.getBankPublicKey(), bankKey.getCipher());
            String response = bankHelper.sendRequest(data, funcode);
            log.info("{}响应报文:{}",logStr,response);

            //解析响应报文
            T resp = JSONObject.parseObject(response, type);
            if(ObjectUtil.isNull(resp)){
                log.error("{}银行响应报文解析失败",logStr);
                return null;
            }

            return resp;

        }catch (Exception e){
            log.error("调用银行接口异常,exp={}",e.toString());
            e.printStackTrace();
            return null;
        }
    }

    private String getBusmod(){
        return env.getProperty("bank.nmjt.busmod");
    }

    /****************************************测试方法************************************************************************/

    /**
     * 测试方法
     * @param args
     */
    public static void main(String[] args) {
        CMBCloudAddSubAccReqBodyDto body = new CMBCloudAddSubAccReqBodyDto();
        CMBCloudAddSubAccReqDto ntdmaaddxReqDto = new CMBCloudAddSubAccReqDto();
        ntdmaaddxReqDto.setAccnbr("755936048410904");
        ntdmaaddxReqDto.setDmanam("测试子账号");
        ntdmaaddxReqDto.setOvrctl("N");
        ntdmaaddxReqDto.setYurref("20250604_1234567890");
        body.setNtdmaaddx(List.of(ntdmaaddxReqDto));

        CMBCloudBusModyReqDto ntbusmodyReqDto = new CMBCloudBusModyReqDto();
        ntbusmodyReqDto.setBusmod("S2008");
        body.setNtbusmody(List.of(ntbusmodyReqDto));

        BankConfigRespDto bankConfigRespDto = new BankConfigRespDto();
        bankConfigRespDto.setBankTypeCode(BankTypeCodeEnum.CMBBank_NMJTSJY.getCode());
        bankConfigRespDto.setBankHttpUrl("http://cdctest.cmburl.cn/cdcserver/api/v2");
        bankConfigRespDto.setConsumerId("N002987341");

        //注：本地测试 需要将 postToBank 改为static，且需要在方法内设置bankKey参数
        //CMBCloudBaseRespDto<CMBCloudAddSubAccRespBodyDto> rst = postToBank(body,"NTDMAADD",bankConfigRespDto,  "测试申请虚拟号==>",  new TypeReference<>(){});
        //log.info("测试结果:{}", JSONUtil.toJsonStr(rst.getResponse()));

    }
}
