package com.yzc.deposit.service.guar.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.yzc.common.api.PageResult;
import com.yzc.common.api.Result;
import com.yzc.common.api.service.IExtAPIService;
import com.yzc.common.api.service.IFileApiService;
import com.yzc.common.api.service.IOrderCenterService;
import com.yzc.common.common.enums.CommonStatusEnum;
import com.yzc.common.common.enums.MSBusinessTypeEnum;
import com.yzc.common.common.vo.FilePreviewReqVo;
import com.yzc.common.common.vo.FilePreviewRespVo;
import com.yzc.common.domain.LoginUserInfo;
import com.yzc.common.dto.SignatureDto;
import com.yzc.common.dto.order.GetOrderReqDto;
import com.yzc.common.dto.order.GetOrderRespDto;
import com.yzc.common.dto.order.MarkCancelGuaranteeReqDto;
import com.yzc.common.guar.dto.CertFileCopyRespDto;
import com.yzc.common.guar.dto.GuarInfoPageReqDto;
import com.yzc.common.guar.dto.GuarInfoRespDto;
import com.yzc.common.guar.dto.common.*;
import com.yzc.common.guar.dto.hanhua.HanHuaCloseResultSyncReqDto;
import com.yzc.common.guar.dto.hanhua.HanHuaOpenResultSyncReqDto;
import com.yzc.common.guar.dto.hanhua.HanHuaSyncResultBaseReqDto;
import com.yzc.common.guar.entity.GuarInfo;
import com.yzc.common.guar.entity.GuarTypeConfig;
import com.yzc.common.guar.enums.*;
import com.yzc.common.guar.util.GuarUtil;
import com.yzc.common.guar.vo.*;
import com.yzc.common.model.BiddingSubInfoModel;
import com.yzc.common.model.file.PDFFileModel;
import com.yzc.common.model.infoRemind.CompanyPhoneDto;
import com.yzc.common.model.infoRemind.PhoneMsgDto;
import com.yzc.common.util.date.DateTimeUtil;
import com.yzc.deposit.dao.guar.IGuarInfoDao;
import com.yzc.deposit.dao.guar.IGuarLogDao;
import com.yzc.deposit.dao.guar.IGuarTypeConfigDao;
import com.yzc.deposit.service.AbstractBaseService;
import com.yzc.deposit.service.bank.GuarAdapterConfig;
import com.yzc.deposit.service.common.IFileService;
import com.yzc.deposit.service.common.IGuarCommonService;
import com.yzc.deposit.service.common.IMsgService;
import com.yzc.deposit.service.guar.IGuarInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class GuarInfoServiceImpl extends AbstractBaseService implements IGuarInfoService {

    @Resource
    private IGuarInfoDao guarInfoDao;

    @Resource
    private IOrderCenterService orderCenterService;

    @Resource
    private GuarAdapterConfig guarAdapterConfig;

    @Resource
    private IGuarCommonService guarCommonService;

    @Resource
    private IFileService fileService;

    @Resource
    private IGuarLogDao guarLogDao;

    /**
     * 消息发送，并保存日志
     */
    @Resource
    private IMsgService msgService;

    @Resource
    private Environment env;

    @Resource
    private IGuarTypeConfigDao guarTypeConfigDao;

    @Resource
    private IExtAPIService extAPIService;

    /**
     * minio文件服务
     */
    @Resource
    private IFileApiService fileServerAPI;

    /**
     * 担保申请
     *
     * @param reqVo 请求参数
     * @return 处理结果
     */
    @Override
    public Result<GuarApplyRespVo> applyGuar(GuarApplyReqVo reqVo) {
        String logStr = "applyGuar(" + RandomUtil.randomNumbers(4) + ")";
        GuarApplyRespVo resVo = new GuarApplyRespVo();

        log.info("{}---->接收保函申请，入参={}", logStr, JSONUtil.toJsonStr(reqVo));
        //业务判重（唯一条件：项目类型+项目id+投标人id+未删除状态 判断是否已经存在）
        LambdaQueryWrapper<GuarInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GuarInfo::getYzcBusiType, reqVo.getYzcBusiType())
                .eq(GuarInfo::getProjectId, reqVo.getProjectId())
                .eq(GuarInfo::getBidderId, reqVo.getBidderId())
                .and(wrapper -> wrapper.isNull(GuarInfo::getDelStatus).or().ne(GuarInfo::getDelStatus, 1));
        List<GuarInfo> guarInfoList = guarInfoDao.list(queryWrapper);

        if (CollectionUtil.isNotEmpty(guarInfoList)) {
            GuarInfo guarInfo = guarInfoList.get(0);
            resVo.setGuarInfoId(guarInfo.getGuarInfoId());
            resVo.setLgNo(guarInfo.getLgNo());
            log.info("{}---->保函申请已存在，返回结果={}", logStr, JSONUtil.toJsonStr(resVo));
            return Result.success(resVo);
        }

        //准备新增数据
        GuarInfo guarInfo = BeanUtil.copyProperties(reqVo, GuarInfo.class);
        guarInfo.setGuarInfoId(IdWorker.getId()); //主键id
        guarInfo.setLgNo(GuarUtil.genLgNo()); //生成保函编号[格式为年月日时分秒+八位随机码]
        guarInfo.setHanhuaBusiType("01"); //业务类型：01 投标保函
//        guarInfo.setFinancialId(1); //瀚华保函 ---方案调整：暂存之前刷新页面就会随机匹配保函机构
        guarInfo.setOperatorName(reqVo.getApplyUserName()); //经办人姓名：默认当前操作人
        //投标截止时间：格式为yyyy-MM-dd HH:mm:ss
        guarInfo.setSignupEndTime(DateUtil.parse(reqVo.getSignupEndTime(), "yyyy-MM-dd HH:mm:ss"));
        guarInfo.setGuaranteeAmount(reqVo.getProjectDeposit()); //担保金额：默认同项目保证金金额

        guarInfo.setApplyTime(new Date()); //申请时间：当前时间
        guarInfo.setCreateUserId(reqVo.getApplyUserId()); //创建人id：当前操作人
        guarInfo.setCreateUserName(reqVo.getApplyUserName()); //创建人姓名：当前操作人
        guarInfo.setIsYzc(CommonStatusEnum.Yes.getCode()); //优质采类型

        //默认为：加密函
        if (ObjectUtil.isNull(guarInfo.getIsEncrypt())) {
            guarInfo.setIsEncrypt(CommonStatusEnum.Yes.getCode()); //默认为加密函,目前只有竞价是明文保函
        }

        //开标时间处理
        if (StringUtils.isNotBlank(reqVo.getOpenBidTime())) {
            guarInfo.setOpenBidTime(DateUtil.parse(reqVo.getOpenBidTime(), "yyyy-MM-dd HH:mm:ss"));
        }

        //投标人证件信息复制
        Result<CertFileCopyRespDto> certFileCopyRst =guarCommonService.certFileCopy(reqVo.getBidderId());
        if(ObjectUtil.isNotNull(certFileCopyRst) && certFileCopyRst.isSuccess()
            && ObjectUtil.isNotNull(certFileCopyRst.getData())){
            //复制成功--修改id
            guarInfo.setBusiLicenseAttRelaId(certFileCopyRst.getData().getBusinessLicAttIdNew());
            guarInfo.setIdcardFrontAttRelaId(certFileCopyRst.getData().getLegalIdCarAttIdNew());
            guarInfo.setIdcardBackAttRelaId(certFileCopyRst.getData().getLegalIdCarBackAttIdNew());
        }

        //新增数据
        boolean isSuccess = guarInfoDao.save(guarInfo);
        log.info("{}---->保函申请数据保存结果={}", logStr, isSuccess);

        if (!isSuccess) {
            return Result.error("保函数据保存失败");
        }

        //返回信息
        resVo.setGuarInfoId(guarInfo.getGuarInfoId());
        resVo.setLgNo(guarInfo.getLgNo());
        return Result.success(resVo);
    }

    /**
     * 根据id查询电子保函明细
     *
     * @param guarInfoId 电子保函id
     * @return 电子保函明细
     */
    @Override
    public Result<GuarInfoRespVo> getGuarInfo(Long guarInfoId) {
        String logStr = "[getGuarInfo]获取保函信息===>";
        GuarInfo guarInfo = guarInfoDao.getById(guarInfoId);

        //判断已提交的数据，是否有优质采订单号，没有优质采订单号的数据说明是旧版提交的，需要重新生成优质采订单
        if (ObjectUtil.equals(guarInfo.getApplyStatus(), GuarApplyStatusEnum.Apply.getCode())
                && StringUtils.isBlank(guarInfo.getOrderNo())) {

            //生成优质采订单
            GetOrderReqDto getOrderReqDto = guarCommonService.getOrderReqDto(guarInfo);
            GetOrderRespDto getOrderRespDto = orderCenterService.getOrder(getOrderReqDto);
            log.info("{}，生成优质采业务订单完成，结果={}", logStr, JSONUtil.toJsonStr(getOrderRespDto));

            //创建订单失败
            if (ObjectUtil.isNull(getOrderRespDto) || StringUtils.isBlank(getOrderRespDto.getOrderNo())) {
                return Result.error("创建订单失败");
            }

            //更新保函优质采订单号
            LambdaUpdateWrapper<GuarInfo> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(GuarInfo::getGuarInfoId, guarInfoId)
                    .set(GuarInfo::getOrderNo, getOrderRespDto.getOrderNo());
            boolean updateOrderNoRst = guarInfoDao.update(updateWrapper);
            log.info("{}，更新优质采订单号完成，结果={}", logStr, updateOrderNoRst);

            //补充订单号
            guarInfo.setOrderNo(getOrderRespDto.getOrderNo());
        }

        //结果
        GuarInfoRespVo respVo = BeanUtil.copyProperties(guarInfo, GuarInfoRespVo.class);
        respVo.setChargesType(50);

        //解析保函显示状态
        respVo.setGuarStatusStr(GuarUtil.getBidderGuarStatusStr(guarInfo));

        //如果未保存，则随机分配保函机构
        if (ObjectUtil.isNull(guarInfo.getApplyStatus())) {
            //随机分配
            Result<RandomGuarOrgRespVo> randomGuarOrgRst = guarCommonService.getRandomGuarOrg();

            //默认为瀚华
            int guarTypeCode = GuarTypeCodeEnum.HanHua.getCode();
            //分配成功，则改为随机分配的结果
            if (ObjectUtil.isNotNull(randomGuarOrgRst) && randomGuarOrgRst.isSuccess()
                    && ObjectUtil.isNotNull(randomGuarOrgRst.getData())) {
                guarTypeCode = randomGuarOrgRst.getData().getGuarTypeCode();
            }

            //获取保函机构配置信息
            GuarTypeConfig guarTypeConfig = guarTypeConfigDao.getByGuarTypeCode(guarTypeCode);
            if (guarTypeConfig == null) {
                return Result.error("保函机构分配失败");
            }

            respVo.setGuarTypeCode(guarTypeCode);
            respVo.setFinancialId(GuarUtil.getFinancialIdByGuarType(guarTypeCode)); //瀚华保函 ---方案调整：暂存之前刷新页面就会随机匹配保函机构
            respVo.setFinanceOrgName(guarTypeConfig.getFinanceOrgName()); //金融机构
            respVo.setPayeeName(guarTypeConfig.getPayeeName()); //收款方
        }

        /* 保函文件预览url赋值 */
        //保函营业执照预览url  ---前端需要 下载地址，非预览地址
        Result<FilePreviewRespVo> busiLicenseResult = fileService.previewFile(FilePreviewReqVo.builder().attRelaId(respVo.getBusiLicenseAttRelaId()).build());
        if (ObjectUtil.isNotNull(busiLicenseResult) && busiLicenseResult.isSuccess()
                && ObjectUtil.isNotNull(busiLicenseResult.getData())
                && StringUtils.isNotBlank(busiLicenseResult.getData().getDownLoadUrl())) {
            respVo.setBusiLicensePreviewUrl(busiLicenseResult.getData().getDownLoadUrl());
        }

        //保函身份证正面预览url
        Result<FilePreviewRespVo> idcardFrontResult = fileService.previewFile(FilePreviewReqVo.builder().attRelaId(respVo.getIdcardFrontAttRelaId()).build());
        if (ObjectUtil.isNotNull(idcardFrontResult) && idcardFrontResult.isSuccess()
                && ObjectUtil.isNotNull(idcardFrontResult.getData())
                && StringUtils.isNotBlank(idcardFrontResult.getData().getDownLoadUrl())) {
            respVo.setIdcardFrontPreviewUrl(idcardFrontResult.getData().getDownLoadUrl());
        }

        //保函身份证反面预览url
        Result<FilePreviewRespVo> idcardBackResult = fileService.previewFile(FilePreviewReqVo.builder().attRelaId(respVo.getIdcardBackAttRelaId()).build());
        if (ObjectUtil.isNotNull(idcardBackResult) && idcardBackResult.isSuccess()
                && ObjectUtil.isNotNull(idcardBackResult.getData())
                && StringUtils.isNotBlank(idcardBackResult.getData().getDownLoadUrl())) {
            respVo.setIdcardBackPreviewUrl(idcardBackResult.getData().getDownLoadUrl());
        }


        return Result.success(respVo);
    }

    /**
     * 保存或提交电子保函信息
     *
     * @param reqVo 保存参数
     * @return 处理结果
     */
    @Override
    public Result<GuarInfoSaveRespVo> saveGuarInfo(GuarInfoSaveReqVo reqVo) {
        String logStr = "[saveGuarInfo]电子保函提交===>id=" + reqVo.getGuarInfoId();
        //获取当前用户
        LoginUserInfo loginUserInfo = super.getLogInfo();

        //判断数据状态,是否已经提交
        GuarInfo guarInfo = guarInfoDao.getById(reqVo.getGuarInfoId());
        if (ObjectUtil.isNull(guarInfo)) {
            return Result.error("保函信息不存在");
        }

        //已经提交
        if (ObjectUtil.equals(guarInfo.getApplyStatus(), GuarApplyStatusEnum.Apply.getCode())) {
            return Result.error("保函信息已提交,请勿重复操作");
        }

        //更新数据库数据
        GuarInfo updateInfo = BeanUtil.copyProperties(reqVo, GuarInfo.class);

        //test环境支付金额设置为 0.01
        String envCode = env.getProperty("deposit.envCode");

        //瀚华提交的时候，按0.01处理，兴泰在提交订单的时候，才改为0.1
        if ((StringUtils.equals("test", envCode) || StringUtils.equals("pre", envCode))
                && ObjectUtil.equals(reqVo.getGuarTypeCode(), GuarTypeCodeEnum.HanHua.getCode()) //根据页面的保函机构类型判断
        ) {
            updateInfo.setPayAmount(BigDecimal.valueOf(0.01));
        } else {
            updateInfo.setPayAmount(GuarUtil.getPayMoney(reqVo.getGuaranteeAmount())); //根据保函金额计算
        }

        //测试环境无法CA签章，设置电子保函协议附件id
        if (StringUtils.equals("test", envCode)) {
            updateInfo.setAgreementAttRelaId("46d2297d-4b92-4c9a-8922-cdc214888266");
        }

        updateInfo.setUpdateTime(new Date());
        updateInfo.setApplyUserId(loginUserInfo.getUserId());
        updateInfo.setApplyUserName(loginUserInfo.getUserName());
        updateInfo.setPayFrom("2"); //优质采页面支付

        //获取出函机构等信息
        GuarTypeConfig guarTypeConfig = guarTypeConfigDao.getByGuarTypeCode(reqVo.getGuarTypeCode());
        if (ObjectUtil.isNull(guarTypeConfig)) {
            return Result.error("保函机构不存在");
        }

        //出函机构、收款方等信息
        updateInfo.setFinancialId(GuarUtil.getFinancialIdByGuarType(reqVo.getGuarTypeCode()));
        updateInfo.setFinanceOrgName(guarTypeConfig.getFinanceOrgName());
        updateInfo.setPayeeName(guarTypeConfig.getPayeeName());

        boolean updateRst = guarInfoDao.updateById(updateInfo);
        log.info("{}，保函信息更新完成，结果={}", logStr, updateRst);
        if (!updateRst) {
            return Result.error("保函信息更新失败");
        }

        //查询最新数据对象
        guarInfo = guarInfoDao.getById(reqVo.getGuarInfoId());

        //暂存，流程到此结束
        if (!ObjectUtil.equals(guarInfo.getApplyStatus(), GuarApplyStatusEnum.Apply.getCode())) {
            return Result.success();
        }

        //由前端提交的保函机构类型，进行适配申请
        ApplyGuarReqDto reqDto = BeanUtil.copyProperties(guarInfo, ApplyGuarReqDto.class);

        //其他信息
        reqDto.setBusinessType(StringUtils.isBlank(guarInfo.getHanhuaBusiType()) ? "01" : guarInfo.getHanhuaBusiType());  //保函类型默认为 01 投标保函

        //保函申请
        Result<ApplyGuarRespDto> guarApplyRespDto = guarAdapterConfig.open(reqDto);
        if (ObjectUtil.isNull(guarApplyRespDto)) {
            guarInfoDao.resetToTemp(guarInfo.getGuarInfoId());//还原状态至暂存
            return Result.error("保函申请失败[null]");
        }

        if (!guarApplyRespDto.isSuccess()
                || ObjectUtil.isNull(guarApplyRespDto.getData())) {

            guarInfoDao.resetToTemp(guarInfo.getGuarInfoId());//还原状态至暂存
            return Result.error(guarApplyRespDto.getMsg());
        }


        //生成优质采订单
        GetOrderReqDto getOrderReqDto = guarCommonService.getOrderReqDto(guarInfo);
        GetOrderRespDto getOrderRespDto = orderCenterService.getOrder(getOrderReqDto);
        log.info("{}，生成优质采业务订单完成，结果={}", logStr, JSONUtil.toJsonStr(getOrderRespDto));

        //创建订单失败
        if (ObjectUtil.isNull(getOrderRespDto) || StringUtils.isBlank(getOrderRespDto.getOrderNo())) {
            return Result.error("创建订单失败");
        }

        //更新保函优质采订单号
        LambdaUpdateWrapper<GuarInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(GuarInfo::getGuarInfoId, reqVo.getGuarInfoId())
                .set(GuarInfo::getOrderNo, getOrderRespDto.getOrderNo())
        ;
        boolean updateOrderNoRst = guarInfoDao.update(updateWrapper);
        log.info("{}，更新保函订单号出函机构等信息完成，结果={}", logStr, updateOrderNoRst);

        GuarInfoSaveRespVo respVo = new GuarInfoSaveRespVo();
        respVo.setChargesType(50);
        respVo.setOrderNo(getOrderRespDto.getOrderNo());

        return Result.success(respVo);
    }

    /**
     * 保函支付成功回调
     *
     * @param reqVo 回调参数
     * @return 处理结果
     */
    @Override
    public Result paySuccess(GuarPaySuccessReqVo reqVo) {
        String logStr = "[paySuccess]电子保函支付成功回调===>";
        //根据保函编号 更新数据
        LambdaUpdateWrapper<GuarInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(GuarInfo::getLgNo, reqVo.getLgNo())
                .set(GuarInfo::getPayStatus, GuarPayStatusEnum.Success.getCode())
                .set(GuarInfo::getPayTime, new Date());
        //更新数据库
        boolean updateRst = guarInfoDao.update(updateWrapper);
        log.info("{}接收入参={},操作结果={}", logStr, JSONUtil.toJsonStr(reqVo), updateRst);
        return updateRst ? Result.success() : Result.error("数据更新失败");
    }

    /**
     * 投标人端：获取保函列表
     *
     * @param reqVo 入参
     * @return 分页列表
     */
    @Override
    public PageResult<GuarInfoRespVo> pageListBidder(GuarBidderPageReqVo reqVo) {
        //获取当前登录人信息
        LoginUserInfo loginUserInfo = super.getLogInfo();
        //入参转换
        GuarInfoPageReqDto reqDto = BeanUtil.copyProperties(reqVo, GuarInfoPageReqDto.class);
        reqDto.setBidderId(loginUserInfo.getCompanyId()); //仅查询当前企业数据

        PageResult<GuarInfoRespDto> pageResult = guarInfoDao.pageList(reqDto);

        if (ObjectUtil.isNull(pageResult)) {
            return null;
        }
        //结果集转换
        if (CollectionUtil.isNotEmpty(pageResult.getRecords())) {
            List<GuarInfoRespVo> respVoList = BeanUtil.copyToList(pageResult.getRecords(), GuarInfoRespVo.class);

            //循环处理状态
            respVoList.forEach(respVo -> {
                //解析保函显示状态
                respVo.setGuarStatusStr(GuarUtil.getBidderGuarStatusStr(BeanUtil.copyProperties(respVo, GuarInfo.class)));

                //是否可以申请退保,可以申请条件：已开函 && （项目异常 || 项目报名截止时间未到） && 未退保
                if (ObjectUtil.equals(respVo.getOpenStatus(), GuarOpenStatusEnum.Success.getCode())
                        && (ObjectUtil.equals(respVo.getAbortiveStatus(), 1) || DateUtil.compare(respVo.getSignupEndTime(), new Date()) > 0)
                        && (ObjectUtil.equals(respVo.getCloseStatus(), GuarCloseStatusEnum.None.getCode()) || ObjectUtil.isNull(respVo.getCloseStatus()))
                ) {
                    respVo.setCanApplyBack(1);
                }
                respVo.setCanApplyBackDesc(String.format("开函状态=%s,项目异常状态=%s,报名截止时间=%s,退保状态=%s", respVo.getOpenStatus()
                        , respVo.getAbortiveStatus(), DateUtil.format(respVo.getSignupEndTime(), "yyyy-MM-dd HH:mm:ss"), respVo.getCloseStatus()));

                if (StringUtils.isBlank(respVo.getFinanceOrgName())) {
                    respVo.setFinanceOrgName("瀚华融资担保股份有限公司"); //TODO 金融机构
                }

                if (StringUtils.isBlank(respVo.getPayeeName())) {
                    respVo.setPayeeName("瀚华融资担保股份有限公司"); //TODO 收款方
                }
            });

            return PageResult.toPageResult(respVoList, pageResult.getCurrent(), pageResult.getSize(), pageResult.getTotal(), pageResult.getPages());
        }

        return PageResult.toPageResult(null, pageResult.getCurrent(), pageResult.getSize(), pageResult.getTotal(), pageResult.getPages());
    }

    /**
     * 发起退保
     *
     * @param reqVo 入参
     * @return 处理结果
     */
    @Override
    public Result<String> closeGuar(GuarCloseReqVo reqVo) {

        //当前登录人
        LoginUserInfo loginUserInfo = super.getLogInfo();

        //查询数据
        GuarInfo guarInfo = guarInfoDao.getById(reqVo.getGuarInfoId());
        if (ObjectUtil.isNull(guarInfo)) {
            return Result.error("保函信息不存在");
        }

        //判断状态是否满足，以下条件均不满足：未开函 ||  已退保 || (报名截止时间已截止 && 项目未异常)
        if (!ObjectUtil.equals(guarInfo.getOpenStatus(), GuarOpenStatusEnum.Success.getCode()) //未开函
                || (DateUtil.compare(guarInfo.getSignupEndTime(), new Date()) < 0 //报名截止时间已截止
                && !ObjectUtil.equals(guarInfo.getAbortiveStatus(), 1))) //项目未异常
        {
            return Result.error("电子保函已生效，不支持申请退保");
        }

        //退保状态不为空,且不为初始状态
        if (ObjectUtil.isNotNull(guarInfo.getCloseStatus()) &&
                !ObjectUtil.equals(guarInfo.getCloseStatus(), GuarCloseStatusEnum.None.getCode())) {
            return Result.error("已发起退保，请勿重复操作");
        }

        // 组装退保参数
        ApplyCloseReqDto reqDto = new ApplyCloseReqDto();
        //关键字段
        reqDto.setGuarTypeCode(guarInfo.getGuarTypeCode()); //保函机构类型
        reqDto.setLgNo(guarInfo.getLgNo());
        reqDto.setMemo(reqVo.getCloseDesc());

        //其他字段
        reqDto.setBidNo(guarInfo.getProjectNo());
        reqDto.setBidName(guarInfo.getProjectName());
        reqDto.setGuaranteeNumber(guarInfo.getGuaranteeNumber());
        reqDto.setOperatorPhone(guarInfo.getOperatorPhone());

        Result closeResult = guarAdapterConfig.close(reqDto);

        //申请异常
        if (ObjectUtil.isNull(closeResult)) {
            return Result.error("申请退保异常(null)");
        }

        //申请失败
        if (!closeResult.isSuccess()) {
            return Result.error(closeResult.getMsg());
        }

        //更新业务数据
        LambdaUpdateWrapper<GuarInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(GuarInfo::getGuarInfoId, reqVo.getGuarInfoId())
                .set(GuarInfo::getCloseStatus, GuarCloseStatusEnum.ING.getCode()) //退保中
                .set(GuarInfo::getCloseDesc, reqVo.getCloseDesc())
                .set(GuarInfo::getCloseUserId, loginUserInfo.getUserId())
                .set(GuarInfo::getCloseUserName, loginUserInfo.getUserName())
                .set(GuarInfo::getCloseTime, new Date())
        ;
        boolean updateRst = guarInfoDao.update(updateWrapper);

        return updateRst ? Result.success("退保成功") : Result.error("退保状态更新失败");
    }

    /**
     * （瀚华）同步保函退保结果通知
     *
     * @param reqDto 入参
     * @return 处理结果
     */
    @Override
    public Result syncCloseResult(HanHuaSyncResultBaseReqDto<HanHuaCloseResultSyncReqDto> reqDto) {
        String logStr = "[syncCloseResult]接收退保结果通知===>";
        int seqNo = RandomUtil.randomInt(1000, 9999);
        guarLogDao.saveLog("syncCloseResult", seqNo + "入参：" + JSONUtil.toJsonStr(reqDto));
        log.info("{}入参={}", logStr, JSONUtil.toJsonStr(reqDto));

        if (ObjectUtil.isNull(reqDto.getData())) {
            return Result.error("入参不能为空");
        }
        HanHuaCloseResultSyncReqDto hanHuaCloseResultSyncReqDto = reqDto.getData();

        if (StringUtils.isEmpty(hanHuaCloseResultSyncReqDto.getLgNo())) {
            return Result.error("保函编号不能为空");
        }

        String lgNo = hanHuaCloseResultSyncReqDto.getLgNo();
        String logSeq = lgNo + "[" + seqNo + "]";

        //退保成功-后续处理
        if (StringUtils.equals(hanHuaCloseResultSyncReqDto.getStatus(), "2")) {
            syncCloseSuccessAfter(hanHuaCloseResultSyncReqDto.getLgNo(), seqNo);
        }

        //更新数据
        LambdaUpdateWrapper<GuarInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(GuarInfo::getLgNo, hanHuaCloseResultSyncReqDto.getLgNo())
                .set(GuarInfo::getCloseStatus, StringUtils.equals(hanHuaCloseResultSyncReqDto.getStatus(), "2") ? GuarCloseStatusEnum.SUCCESS.getCode() : GuarCloseStatusEnum.FAIL.getCode())
                .set(GuarInfo::getCloseResult, hanHuaCloseResultSyncReqDto.getAuditOpinion());
        boolean updateRst = guarInfoDao.update(updateWrapper);
        guarLogDao.saveLog("syncCloseResult", logSeq + "数据库更新结果：" + updateRst);
        log.info("{}数据库更新完成,申请编号={},结果={}", logStr, hanHuaCloseResultSyncReqDto.getLgNo(), updateRst);

        return updateRst ? Result.success("同步成功") : Result.error("同步失败");
    }

    /**
     * 修改投标截止时间
     *
     * @param reqVo 入参
     * @return 处理结果
     */
    @Override
    public Result updateSignUpEndTime(SignUpEndTimeUpdateReqVo reqVo) {

        //日期转换为date格式
        Date signupEndTime = DateUtil.parse(reqVo.getSignupEndTime(), "yyyy-MM-dd HH:mm:ss");
        LambdaUpdateWrapper<GuarInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(GuarInfo::getProjectId, reqVo.getProjectId())
                .set(GuarInfo::getSignupEndTime, signupEndTime);
        Boolean rst = guarInfoDao.update(updateWrapper);
        log.info("修改项目报名截止时间，入参={},结果={}", JSONUtil.toJsonStr(reqVo), rst);
        guarLogDao.saveLog("updateSignUpEndTime", "入参=" + JSONUtil.toJsonStr(reqVo) + ",结果=" + rst);

        return rst ? Result.success("修改成功") : Result.error("修改失败");
    }

    /**
     * 修改保函终止状态
     *
     * @param reqDto 入参
     * @return 处理结果
     */
    @Override
    public Result updateAbortive(AbortiveStatusUpdateReqVo reqDto) {
        if (ObjectUtil.isNull(reqDto.getAbortiveStatus())) {
            reqDto.setAbortiveStatus(1); //默认为 1已终止
        }

        //修改数据
        LambdaUpdateWrapper<GuarInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(GuarInfo::getProjectId, reqDto.getProjectId())
                .set(GuarInfo::getAbortiveStatus, reqDto.getAbortiveStatus());
        Boolean rst = guarInfoDao.update(updateWrapper);
        log.info("修改项目终止状态，入参={},结果={}", JSONUtil.toJsonStr(reqDto), rst);
        guarLogDao.saveLog("updateAbortive", "入参=" + JSONUtil.toJsonStr(reqDto) + ",结果=" + rst);

        return rst ? Result.success("修改成功") : Result.error("修改失败");
    }

    /**
     * （瀚华）同步保函开函结果通知
     *
     * @param reqDto 入参
     * @return 处理结果
     */
    @Override
    public Result syncOpenResult(HanHuaSyncResultBaseReqDto<HanHuaOpenResultSyncReqDto> reqDto) {
        String logStr = "[syncOpenResult]接收开函结果通知===>";
        int seqNo = RandomUtil.randomInt(1000, 9999);
        log.info("{}入参={}", logStr, JSONUtil.toJsonStr(reqDto));
        guarLogDao.saveLog("syncOpenResult", seqNo + "入参：" + JSONUtil.toJsonStr(reqDto));

        if (ObjectUtil.isNull(reqDto.getData())) {
            return Result.error("入参不能为空");
        }
        HanHuaOpenResultSyncReqDto hanHuaOpenResultSyncReqDto = reqDto.getData();

        if (StringUtils.isEmpty(hanHuaOpenResultSyncReqDto.getLgNo())) {
            return Result.error("保函编号不能为空");
        }

        String lgNo = hanHuaOpenResultSyncReqDto.getLgNo();
        String logSeq = lgNo + "[" + seqNo + "]";

        LambdaUpdateWrapper<GuarInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(GuarInfo::getLgNo, hanHuaOpenResultSyncReqDto.getLgNo())
                .set(GuarInfo::getOpenStatus, hanHuaOpenResultSyncReqDto.getStatus())
                .set(GuarInfo::getAuditOpinion, hanHuaOpenResultSyncReqDto.getAuditOpinion())
                .set(GuarInfo::getGuaranteeFileUrl, hanHuaOpenResultSyncReqDto.getGuaranteeFileUrl())
                .set(GuarInfo::getGuaranteeNumber, hanHuaOpenResultSyncReqDto.getGuaranteeNumber())
                .set(GuarInfo::getGuaranteeCode, hanHuaOpenResultSyncReqDto.getGuaranteeCode())
                .set(GuarInfo::getOpenTime, hanHuaOpenResultSyncReqDto.getCreateTime())
                .set(GuarInfo::getLetterExpireStartTime, hanHuaOpenResultSyncReqDto.getLetterExpireStartTime())
                .set(GuarInfo::getLetterExpireEndTime, hanHuaOpenResultSyncReqDto.getLetterExpireEndTime())
                .set(GuarInfo::getRate, StringUtils.isEmpty(hanHuaOpenResultSyncReqDto.getRate()) ? null : new BigDecimal(hanHuaOpenResultSyncReqDto.getRate()))
                .set(GuarInfo::getPayAmount, StringUtils.isEmpty(hanHuaOpenResultSyncReqDto.getPayAmount()) ? null : new BigDecimal(hanHuaOpenResultSyncReqDto.getPayAmount()))
                .set(GuarInfo::getEnsureType, hanHuaOpenResultSyncReqDto.getEnsureType())
                .set(GuarInfo::getOpenUpdateTime, new Date());//通知接收时间
        ;


        boolean updateRst = guarInfoDao.update(updateWrapper);
        guarLogDao.saveLog("syncOpenResult", logSeq + "数据库更新结果：" + updateRst);
        log.info("{}更新数据完成,申请编号={},结果={}", logStr, hanHuaOpenResultSyncReqDto.getLgNo(), updateRst);

        //数据库更新成功后，相关逻辑
        if (updateRst) {
            syncOpenGuarAfter(hanHuaOpenResultSyncReqDto.getLgNo(), seqNo);
        }

        return updateRst ? Result.success("同步成功") : Result.error("同步失败");
    }

    /**
     * 通用-同步保函开函结果通知
     * 注：lgNo 为唯一标识
     *
     * @param reqDto 入参
     * @return 处理结果
     */
    @Override
    public Result syncOpenResultCommon(OpenResultSyncReqDto reqDto) {
        String logStr = "[syncOpenResultCommon]接收开函结果通知(通用)===>";
        int seqNo = RandomUtil.randomInt(1000, 9999);
        log.info("{}入参={}", logStr, JSONUtil.toJsonStr(reqDto));
        guarLogDao.saveLog("syncOpenResultCommon", seqNo + "入参：" + JSONUtil.toJsonStr(reqDto));

        if (StringUtils.isEmpty(reqDto.getLgNo())) {
            return Result.error("保函编号不能为空");
        }

        String lgNo = reqDto.getLgNo();
        String logSeq = lgNo + "[" + seqNo + "]";

        //先根据lgNo查询保函信息
        GuarInfoRespDto guarInfoOld = guarInfoDao.getByLgNo(lgNo);
        if(ObjectUtil.isNull(guarInfoOld)){
            return Result.error("保函信息不存在");
        }

        //更新数据
        LambdaUpdateWrapper<GuarInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(GuarInfo::getLgNo, reqDto.getLgNo())
                .set(GuarInfo::getOpenStatus, reqDto.getStatus())
                .set(GuarInfo::getAuditOpinion, reqDto.getAuditOpinion())
                .set(GuarInfo::getGuaranteeFileUrl, reqDto.getGuaranteeFileUrl())
                .set(GuarInfo::getGuaranteeNumber, reqDto.getGuaranteeNumber())
                .set(GuarInfo::getGuaranteeCode, reqDto.getGuaranteeCode())
                .set(GuarInfo::getOpenTime, reqDto.getCreateTime())
                .set(GuarInfo::getLetterExpireStartTime, reqDto.getLetterExpireStartTime())
                .set(GuarInfo::getLetterExpireEndTime, reqDto.getLetterExpireEndTime())
                .set(GuarInfo::getRate, StringUtils.isEmpty(reqDto.getRate()) ? null : new BigDecimal(reqDto.getRate()))
                .set(GuarInfo::getOpenUpdateTime, new Date());//通知接收时间
        ;

        //增加逻辑：如果原记录支付状态为空，则设置为支付成功（原因：兴泰保函风控异常的申请，不会进行支付回调，但会收到开函结果通知）
        if(StringUtils.isBlank(guarInfoOld.getPayStatus())){
            updateWrapper.set(GuarInfo::getPayStatus, GuarPayStatusEnum.Success.getCode()); //支付成功
        }

        boolean updateRst = guarInfoDao.update(updateWrapper);
        guarLogDao.saveLog("syncOpenResultCommon", logSeq + "数据库更新结果：" + updateRst);
        log.info("{}更新数据完成,申请编号={},结果={}", logStr, reqDto.getLgNo(), updateRst);

        //数据库更新成功后，相关逻辑
        if (updateRst) {
            syncOpenGuarAfter(reqDto.getLgNo(), seqNo);
        }

        return updateRst ? Result.success("同步成功") : Result.error("同步失败");
    }

    /**
     * 通用-同步保函退保结果通知
     * 注：lgNo 为唯一标识
     *
     * @param reqDto 入参
     * @return 处理结果
     */
    @Override
    public Result syncCloseResultCommon(CloseResultSyncReqDto reqDto) {
        String logStr = "[syncCloseResultCommon]接收退保结果通知===>";
        int seqNo = RandomUtil.randomInt(1000, 9999);
        guarLogDao.saveLog("syncCloseResultCommon", seqNo + "入参：" + JSONUtil.toJsonStr(reqDto));
        log.info("{}入参={}", logStr, JSONUtil.toJsonStr(reqDto));


        if (StringUtils.isEmpty(reqDto.getLgNo())) {
            return Result.error("保函编号不能为空");
        }

        String lgNo = reqDto.getLgNo();
        String logSeq = lgNo + "[" + seqNo + "]";

        //退保成功-后续处理
        if (StringUtils.equals(reqDto.getStatus(), "2")) {
            syncCloseSuccessAfter(reqDto.getLgNo(), seqNo);
        }

        //更新数据
        LambdaUpdateWrapper<GuarInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(GuarInfo::getLgNo, reqDto.getLgNo())
                .set(GuarInfo::getCloseStatus, StringUtils.equals(reqDto.getStatus(), "2") ? GuarCloseStatusEnum.SUCCESS.getCode() : GuarCloseStatusEnum.FAIL.getCode())
                .set(GuarInfo::getCloseResult, reqDto.getAuditOpinion());
        boolean updateRst = guarInfoDao.update(updateWrapper);
        guarLogDao.saveLog("syncCloseResultCommon", logSeq + "数据库更新结果：" + updateRst);
        log.info("{}数据库更新完成,申请编号={},结果={}", logStr, reqDto.getLgNo(), updateRst);


        return updateRst ? Result.success("同步成功") : Result.error("同步失败");
    }

    /**
     * 通用-同步保函补偿结果通知
     * 注：lgNo 为唯一标识
     *
     * @param reqDto 入参
     * @return 处理结果
     */
    @Override
    public Result syncCompensationResultCommon(CompensationResultSyncReqDto reqDto) {
        String logStr = "[syncCompensationResultCommon]接收理赔结果通知(通用)===>";
        int seqNo = RandomUtil.randomInt(1000, 9999);
        log.info("{}入参={}", logStr, JSONUtil.toJsonStr(reqDto));
        guarLogDao.saveLog("syncCompensationResultCommon", seqNo + "入参：" + JSONUtil.toJsonStr(reqDto));

        if (StringUtils.isEmpty(reqDto.getLgNo())) {
            return Result.error("保函编号不能为空");
        }

        String lgNo = reqDto.getLgNo();
        String logSeq = lgNo + "[" + seqNo + "]";

        //更新数据
        LambdaUpdateWrapper<GuarInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(GuarInfo::getLgNo, reqDto.getLgNo())
                .set(GuarInfo::getCompensateStatus, reqDto.getStatus())
                .set(GuarInfo::getCompensateRecName, reqDto.getReceiverName())
                .set(GuarInfo::getCompensateRecPhone, reqDto.getReceiverPhone())
                .set(GuarInfo::getCompensateRecRemark, reqDto.getRemark())
                .set(GuarInfo::getUpdateTime, new Date());//最后更新时间
        ;

        boolean updateRst = guarInfoDao.update(updateWrapper);
        guarLogDao.saveLog("syncCompensationResultCommon", logSeq + "数据库更新结果：" + updateRst);
        log.info("{}更新数据完成,申请编号={},结果={}", logStr, reqDto.getLgNo(), updateRst);

        return updateRst ? Result.success("同步成功") : Result.error("同步失败");
    }

    /**
     * 接收保函开函通知后，需要执行的后续操作
     *
     * @param lgNo  保函编号
     * @param seqNo 操作流水号
     * @return 操作结果
     */
    private boolean syncOpenGuarAfter(String lgNo, int seqNo) {
        String logStr = "开函通知后续操作====>";
        String logSeq = lgNo + "[" + seqNo + "]";

        //根据申请编号查询保函数据
        LambdaQueryWrapper<GuarInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GuarInfo::getLgNo, lgNo);
        //删除状态为空或者不等于1
        queryWrapper.and(wrapper -> wrapper.ne(GuarInfo::getDelStatus, 1).or().isNull(GuarInfo::getDelStatus));

        List<GuarInfo> guarInfoList = guarInfoDao.list(queryWrapper);
        if (CollectionUtil.isNotEmpty(guarInfoList)) {
            GuarInfo guarInfo = guarInfoList.get(0);

            /*** 同步至订单中心 ****/
            //出函失败，通知订单中心：已退款
            if (ObjectUtil.equals(guarInfo.getOpenStatus(), GuarOpenStatusEnum.Fail.getCode())) {
                log.info("{}出函失败,准备同步至订单中心,seqNo={},lgNo={}", logStr, seqNo, guarInfo.getLgNo());
                MarkCancelGuaranteeReqDto markCancelGuaranteeReqDto = new MarkCancelGuaranteeReqDto();
                markCancelGuaranteeReqDto.setLgNo(guarInfo.getLgNo());

                //只做记录，不会中断主流程
                Result<Boolean> syncOrderResult = orderCenterService.markCancelGuarantee(markCancelGuaranteeReqDto);
                guarLogDao.saveLog("syncOpenGuarAfter", logSeq + "出函失败,同步至订单中心,结果：" + JSONUtil.toJsonStr(syncOrderResult));
                log.info("{}出函失败,同步至订单中心 完成,seqNo={},结果={}", logStr, seqNo, JSONUtil.toJsonStr(syncOrderResult));
            }

            /*** 发送短信通知 ****/
            PhoneMsgDto phoneMsgDto = new PhoneMsgDto();
            phoneMsgDto.setProjectId(guarInfo.getProjectId());
            phoneMsgDto.setProjectName(guarInfo.getProjectName());
            phoneMsgDto.setSendCompanyId(guarInfo.getCompanyId());

            //发送对象
            CompanyPhoneDto companyPhoneDto = new CompanyPhoneDto();
            companyPhoneDto.setCompanyId(guarInfo.getBidderId());
            companyPhoneDto.setPhoneNo(guarInfo.getOperatorPhone());
            phoneMsgDto.setCompanyPhoneList(List.of(companyPhoneDto));

            String aliYunTemplateCode = "";//模板编号
            String msgContent = "";//模板内容

            //根据出函状态判断模板编号
            if (ObjectUtil.equals(guarInfo.getOpenStatus(), GuarOpenStatusEnum.Success.getCode())) {
                //出函成功[您在项目编号为${projectcode}提交的电子保函申请，已成功开函，请登录平台在我的保函列表下载保函凭证。][SMS_483960173]
                aliYunTemplateCode = "SMS_483960173";
                msgContent = String.format("您在项目编号为%s提交的电子保函申请，已成功开函，请登录平台在我的保函列表下载保函凭证。", guarInfo.getProjectNo());
            } else {
                //出函失败[您在项目编号为${projectcode}提交的电子保函申请未通过风控审核，请尽快登录平台重新选择保证金支付方式。][SMS_484015166]
                aliYunTemplateCode = "SMS_484015166";
                msgContent = String.format("您在项目编号为%s提交的电子保函申请未通过风控审核，请尽快登录平台重新选择保证金支付方式。", guarInfo.getProjectNo());
            }
            phoneMsgDto.setAliYunTemplateCode(aliYunTemplateCode);
            phoneMsgDto.setAliYunTemplateContent(JSONUtil.toJsonStr(Map.of("projectcode", guarInfo.getProjectNo())));
            phoneMsgDto.setModuleCode("TB_000000");
            phoneMsgDto.setMsgContent(msgContent);

            boolean sendMsgRst = msgService.sendPhoneMsg(phoneMsgDto);
            guarLogDao.saveLog("syncOpenGuarAfter", logSeq + "手机号码：" + guarInfo.getOperatorPhone() + ",发送内容：" + msgContent + ",发送短信结果：" + sendMsgRst);
            log.info("{}发送短信通知完成,申请编号={},手机号码={},发送结果={}", logStr, lgNo, guarInfo.getOperatorPhone(), sendMsgRst);

            /*** 瀚华非优质采项目，自动解密 ****/
            //瀚华 && 非优质采项目
            /*if (ObjectUtil.equals(guarInfo.getGuarTypeCode(), GuarTypeCodeEnum.HanHua.getCode())
                    && ObjectUtil.equals(guarInfo.getIsYzc(), CommonStatusEnum.No.getCode())) {
                //直接解密
                try {
                    DecGuarSingleReqDto decReqDto = GuarUtil.changeToDecSingleDto(guarInfo);

                    Result<DecGuarRespDto> decRst = guarAdapterConfig.decSingle(decReqDto);
                    log.info("{}自动解密完成，入参={}，结果={}", logStr, JSONUtil.toJsonStr(decReqDto), JSONUtil.toJsonStr(decRst));
                } catch (Exception e) {
                    log.error("{}自动解密异常,申请编号={},异常={}", logStr, guarInfo.getLgNo(), e);
                }
            }*/

            return true;
        }
        log.info("{}保函记录不存在,seqNo={},lgNo={}", logStr, seqNo, lgNo);
        return false;
    }

    /**
     * 退保成功通知后续操作
     *
     * @param lgNo  保函编号
     * @param seqNo 操作流水号
     * @return 处理结果
     */
    private boolean syncCloseSuccessAfter(String lgNo, int seqNo) {
        String logStr = "接收退保成功通知后续操作====>";
        String logSeq = lgNo + "[" + seqNo + "]";

        /*** 同步至订单中心 ****/
        MarkCancelGuaranteeReqDto markCancelGuaranteeReqDto = new MarkCancelGuaranteeReqDto();
        markCancelGuaranteeReqDto.setLgNo(lgNo);

        //只做记录，不会中断主流程
        Result<Boolean> syncOrderResult = orderCenterService.markCancelGuarantee(markCancelGuaranteeReqDto);
        guarLogDao.saveLog("syncCloseSuccessAfter", logSeq + "同步至订单中心,结果：" + JSONUtil.toJsonStr(syncOrderResult));
        log.info("{}同步至订单中心,结果={}", logStr, JSONUtil.toJsonStr(syncOrderResult));

        /*** 发送短信通知 ****/
        //根据申请编号查询保函数据
        LambdaQueryWrapper<GuarInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GuarInfo::getLgNo, lgNo);
        //删除状态为空或者不等于1
        queryWrapper.and(wrapper -> wrapper.ne(GuarInfo::getDelStatus, 1).or().isNull(GuarInfo::getDelStatus));

        List<GuarInfo> guarInfoList = guarInfoDao.list(queryWrapper);
        if (CollectionUtil.isNotEmpty(guarInfoList)) {
            GuarInfo guarInfo = guarInfoList.get(0);
            PhoneMsgDto phoneMsgDto = new PhoneMsgDto();
            phoneMsgDto.setProjectId(guarInfo.getProjectId());
            phoneMsgDto.setProjectName(guarInfo.getProjectName());
            phoneMsgDto.setSendCompanyId(guarInfo.getCompanyId());

            //发送对象
            CompanyPhoneDto companyPhoneDto = new CompanyPhoneDto();
            companyPhoneDto.setCompanyId(guarInfo.getBidderId());
            companyPhoneDto.setPhoneNo(guarInfo.getOperatorPhone());
            phoneMsgDto.setCompanyPhoneList(List.of(companyPhoneDto));

            String aliYunTemplateCode = "SMS_483925156";//模板编号
            phoneMsgDto.setAliYunTemplateCode(aliYunTemplateCode); //项目编号为${projectcode}的电子保函已成功退保，请留意退款通知。
            phoneMsgDto.setAliYunTemplateContent(JSONUtil.toJsonStr(Map.of("projectcode", guarInfo.getProjectNo())));

            phoneMsgDto.setModuleCode("TB_000000");
            phoneMsgDto.setMsgContent(String.format("项目编号为%s的电子保函已成功退保，请留意退款通知。", guarInfo.getProjectNo()));
            boolean sendMsgRst = msgService.sendPhoneMsg(phoneMsgDto);
            guarLogDao.saveLog("syncCloseSuccessAfter", logSeq + "手机号码：" + guarInfo.getOperatorPhone() + ",发送内容：" + phoneMsgDto.getMsgContent() + ",发送短信结果：" + sendMsgRst);
            log.info("{}发送短信通知完成,申请编号={},手机号码={},发送结果={}", logStr, guarInfo.getLgNo(), guarInfo.getOperatorPhone(), sendMsgRst);
            return true;
        }

        return false;
    }

    /**
     * 刷新保函
     *
     * @param reqVo 入参
     * @return 处理结果
     */
    @Override
    public Result<GuarApiRespVo> refreshGuar(RefreshGuarReqVo reqVo) {
        //查询保函信息
        LambdaQueryWrapper<GuarInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GuarInfo::getProjectId, reqVo.getProjectId());
        queryWrapper.eq(GuarInfo::getBidderId, reqVo.getBidderId());
        queryWrapper.and(wrapper -> wrapper.ne(GuarInfo::getDelStatus, 1).or().isNull(GuarInfo::getDelStatus));//未删除
        List<GuarInfo> guarInfoList = guarInfoDao.list(queryWrapper);
        if (CollectionUtil.isEmpty(guarInfoList)) {
            return Result.error("保函信息不存在");
        }

        GuarInfo oldGuarInfo = guarInfoList.get(0);

        //未申请
        if(ObjectUtil.notEqual(oldGuarInfo.getApplyStatus(),GuarApplyStatusEnum.Apply.getCode())){
            return Result.error("保函未申请,不予刷新");
        }

        //调用保函机构接口 刷新保函数据
        QueryGuarReqDto queryGuarReqDto = new QueryGuarReqDto();
        queryGuarReqDto.setGuarTypeCode(oldGuarInfo.getGuarTypeCode());
        queryGuarReqDto.setLgNo(oldGuarInfo.getLgNo());

        Result<QueryGuarRespDto> queryRst = guarAdapterConfig.query(queryGuarReqDto);
        if (ObjectUtil.isNull(queryRst) || !queryRst.isSuccess()) {
            return Result.error("保函刷新失败");
        }

        QueryGuarRespDto queryRstData = queryRst.getData();
        //返回的数据不为空，则需要更新数据（兴泰保函 暂时没有实现）
        if (ObjectUtil.isNotNull(queryRst.getData())) {
            //更新数据
            LambdaUpdateWrapper<GuarInfo> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(GuarInfo::getGuarInfoId, oldGuarInfo.getGuarInfoId()); //根据主键刷新
            updateWrapper.set(GuarInfo::getOpenStatus, queryRstData.getStatus());
            updateWrapper.set(GuarInfo::getPayStatus, queryRstData.getPayStatus());
            updateWrapper.set(GuarInfo::getAuditOpinion, queryRstData.getAuditOpinion());
            updateWrapper.set(GuarInfo::getGuaranteeFileUrl, queryRstData.getGuaranteeFileUrl());
            updateWrapper.set(GuarInfo::getGuaranteeNumber, queryRstData.getGuaranteeNumber());
            updateWrapper.set(GuarInfo::getGuaranteeCode, queryRstData.getGuaranteeCode());
            updateWrapper.set(GuarInfo::getOpenTime, queryRstData.getCreateTime());
            updateWrapper.set(GuarInfo::getLetterExpireStartTime, queryRstData.getLetterExpireStartTime());
            updateWrapper.set(GuarInfo::getLetterExpireEndTime, queryRstData.getLetterExpireEndTime());
            updateWrapper.set(GuarInfo::getRate, StringUtils.isEmpty(queryRstData.getRate()) ? null : new BigDecimal(queryRstData.getRate()));

            //更新数据
            boolean updateRst = guarInfoDao.update(null, updateWrapper);
            if (!updateRst) {
                return Result.error("保函信息更新失败");
            }
        }

        //再次查询，并返回结果
        GuarInfo newGuarInfo = guarInfoDao.getById(oldGuarInfo.getGuarInfoId());
        GuarApiRespVo guarApiRespVo = BeanUtil.copyProperties(newGuarInfo, GuarApiRespVo.class);
        return Result.success(guarApiRespVo);
    }

    /**
     * (业务接口)批量解密
     *
     * @param reqVo 入参
     * @return 处理结果
     */
    @Override
    public Result decBatch(ApplyDecReqVo reqVo) {
        //根据项目查询所有保函信息
        List<GuarInfo> guarInfoList = guarInfoDao.list(new LambdaQueryWrapper<GuarInfo>()
                .eq(GuarInfo::getProjectId, reqVo.getProjectId())
                .eq(StringUtils.isNotBlank(reqVo.getBidderId()), GuarInfo::getBidderId, reqVo.getBidderId()) //投标人id
        );

        if (CollectionUtil.isEmpty(guarInfoList)) {
            return Result.error("当前项目无保函信息");
        }

        //开标时间如果为空，则默认为当前时间
        if (StringUtils.isBlank(reqVo.getOpenBidTime())) {
            reqVo.setOpenBidTime(DateUtil.format(new Date(), DatePattern.NORM_DATETIME_FORMAT));
        }

        /*******************************1.组装参数*******************************/
        //统一取项目信息
        GuarInfo guarInfoFirst = guarInfoList.get(0);

        //统一的入参基本参数
        DecGuarBatchReqDto baseReqDto = BeanUtil.copyProperties(guarInfoFirst, DecGuarBatchReqDto.class);

        //特殊字段处理
        baseReqDto.setBidNo(guarInfoFirst.getProjectNo());
        baseReqDto.setBidName(guarInfoFirst.getProjectName());
        if (ObjectUtil.isNull(guarInfoFirst.getGuarTypeCode())) {
            //默认为 瀚华保函
            baseReqDto.setGuarTypeCode(GuarTypeCodeEnum.HanHua.getCode());
        }
        baseReqDto.setOpenBidTime(reqVo.getOpenBidTime());

        //由于保函信息可能来自多个 保函机构，所以需要循环解密，目前瀚华和兴泰支持批量解密（瀚华是网关接口内部实现），所以为了减少网络请求次数，这里直接循环调用保函机构接口
        DecGuarBatchReqDto hanhuaDecReqDto = BeanUtil.copyProperties(baseReqDto, DecGuarBatchReqDto.class); //瀚华类型
        List<DecGuarInfoDto> hanhuaInfoList = new ArrayList<>(); //瀚华子项

        DecGuarBatchReqDto xingtaiDecReqDto = BeanUtil.copyProperties(baseReqDto, DecGuarBatchReqDto.class); //兴泰类型
        List<DecGuarInfoDto> xingtaiInfoList = new ArrayList<>(); //兴泰子项

        List<DecGuarSingleReqDto> otherGuarDecReqDtoList = new ArrayList<>(); //其他类型保函

        for (GuarInfo guarInfo : guarInfoList) {
            //如果已经解密，则无需处理
            if (ObjectUtil.isNotNull(guarInfo.getDecStatus())
                    && ObjectUtil.equal(guarInfo.getDecStatus(), CommonStatusEnum.Yes.getCode())) {
                continue;
            }
            //瀚华类型
            if (ObjectUtil.isNull(guarInfo.getGuarTypeCode())
                    || ObjectUtil.equal(guarInfo.getGuarTypeCode(), GuarTypeCodeEnum.HanHua.getCode())) {
                DecGuarInfoDto decGuarInfoDto = BeanUtil.copyProperties(guarInfo, DecGuarInfoDto.class);
                decGuarInfoDto.setEnterpriseName(guarInfo.getBidderName());
                hanhuaInfoList.add(decGuarInfoDto);
            }

            //兴泰或国控
            if (ObjectUtil.equal(guarInfo.getGuarTypeCode(), GuarTypeCodeEnum.XingTai.getCode())
                    || ObjectUtil.equal(guarInfo.getGuarTypeCode(), GuarTypeCodeEnum.GuoKong.getCode())) {
                DecGuarInfoDto decGuarInfoDto = BeanUtil.copyProperties(guarInfo, DecGuarInfoDto.class);
                decGuarInfoDto.setEnterpriseName(guarInfo.getBidderName());
                xingtaiInfoList.add(decGuarInfoDto);
            }

            //其他类型保函：转为保函机构 请求参数
            DecGuarSingleReqDto decReqDto = GuarUtil.changeToDecSingleDto(guarInfo);
            decReqDto.setOpenBidTime(reqVo.getOpenBidTime());
            otherGuarDecReqDtoList.add(decReqDto);
        }

        hanhuaDecReqDto.setGuaranteeInfoList(hanhuaInfoList);
        xingtaiDecReqDto.setGuaranteeInfoList(xingtaiInfoList);

        /*******************************执行解密*******************************/
        //瀚华批量
        if (ObjectUtil.isNotNull(hanhuaDecReqDto) && CollectionUtil.isNotEmpty(hanhuaDecReqDto.getGuaranteeInfoList())) {
            Result<List<DecGuarRespDto>> decBatchRst = guarAdapterConfig.decBatch(hanhuaDecReqDto);
            if (ObjectUtil.isNull(decBatchRst) || !decBatchRst.isSuccess()) {
                log.info("瀚华批量解密失败,入参={},结果={}", JSONUtil.toJsonStr(xingtaiDecReqDto), JSONUtil.toJsonStr(decBatchRst));
            }
        }

        //兴泰批量
        if (ObjectUtil.isNotNull(xingtaiDecReqDto) && CollectionUtil.isNotEmpty(xingtaiDecReqDto.getGuaranteeInfoList())) {
            Result<List<DecGuarRespDto>> decBatchRst = guarAdapterConfig.decBatch(xingtaiDecReqDto);
            if (ObjectUtil.isNull(decBatchRst) || !decBatchRst.isSuccess()) {
                log.info("兴泰批量解密失败,入参={},结果={}", JSONUtil.toJsonStr(xingtaiDecReqDto), JSONUtil.toJsonStr(decBatchRst));
            }
        }

        //其他单个执行
        if (CollectionUtil.isNotEmpty(otherGuarDecReqDtoList)) {
            for (DecGuarSingleReqDto decReqDto : otherGuarDecReqDtoList) {
                Result<DecGuarRespDto> decSingleRst = guarAdapterConfig.decSingle(decReqDto);
                if (ObjectUtil.isNull(decSingleRst) || !decSingleRst.isSuccess()) {
                    log.info("其他保函批量解密失败,入参={},结果={}", JSONUtil.toJsonStr(decReqDto), JSONUtil.toJsonStr(decSingleRst));
                }
            }
        }

        /*******************************1.更新解密*******************************/
        LambdaUpdateWrapper<GuarInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(GuarInfo::getProjectId, reqVo.getProjectId())
                .eq(StringUtils.isNotBlank(reqVo.getBidderId()), GuarInfo::getBidderId, reqVo.getBidderId()) //指定了投标人
                .eq(GuarInfo::getOpenStatus,GuarOpenStatusEnum.Success.getCode())  //仅修改已经开函成功的 数据(防止类似竞价业务，申请后直接调用解密接口)
                .set(GuarInfo::getOpenBidTime, DateUtil.parse(reqVo.getOpenBidTime(), DateTimeUtil.LONG_TIME_FORMAT_WITH_SEC))
                .set(GuarInfo::getDecStatus, CommonStatusEnum.Yes.getCode())
                .set(GuarInfo::getDecTime, new Date());
        boolean rst = guarInfoDao.update(updateWrapper);

        return rst ? Result.success() : Result.error("更新保函解密状态失败");
    }

    /**
     * (业务接口)单笔解密
     *
     * @param reqVo 入参
     * @return 处理结果
     */
    @Override
    public Result decSingle(ApplyDecReqVo reqVo) {

        if(StringUtils.isBlank(reqVo.getBidderId())){
            return Result.error("投标人id不能为空");
        }

        List<GuarInfo> guarInfoList = guarInfoDao.list(new LambdaQueryWrapper<GuarInfo>()
                .eq(GuarInfo::getProjectId, reqVo.getProjectId())
                .eq(GuarInfo::getBidderId, reqVo.getBidderId()));

        if (CollectionUtil.isEmpty(guarInfoList)) {
            return Result.error("未查询到保函信息");
        }

        GuarInfo guarInfo = guarInfoList.get(0);

        //判断是否已经开函
        if(ObjectUtil.notEqual(guarInfo.getOpenStatus(), GuarOpenStatusEnum.Success.getCode())){
            return Result.error("保函未开函,不支持解密");
        }

        //开标时间如果为空，则默认为当前时间
        if (StringUtils.isBlank(reqVo.getOpenBidTime())) {
            reqVo.setOpenBidTime(DateUtil.format(new Date(), DatePattern.NORM_DATETIME_FORMAT));
        }

        DecGuarSingleReqDto decReqDto = GuarUtil.changeToDecSingleDto(guarInfo);
        //开标时间
        decReqDto.setOpenBidTime(reqVo.getOpenBidTime());


        Result<DecGuarRespDto> decSingleRst = guarAdapterConfig.decSingle(decReqDto);

        //解密失败，直接返回
        if (ObjectUtil.isNull(decSingleRst) || !decSingleRst.isSuccess()) {
            return Result.error("保函解密失败");
        }

        //解密成功，更新数据
        LambdaUpdateWrapper<GuarInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(GuarInfo::getGuarInfoId, guarInfo.getGuarInfoId())
                .set(GuarInfo::getOpenBidTime, DateUtil.parse(reqVo.getOpenBidTime(), DateTimeUtil.LONG_TIME_FORMAT_WITH_SEC))
                .set(GuarInfo::getDecStatus, CommonStatusEnum.Yes.getCode())
                .set(GuarInfo::getDecTime, new Date());
        boolean rst = guarInfoDao.update(updateWrapper);

        return rst ? Result.success() : Result.error("更新保函解密状态失败");
    }

    /**
     * (业务接口)查询保函列表
     *
     * @param reqVo 入参
     * @return 处理结果
     */
    @Override
    public Result<List<GuarApiRespVo>> queryListApi(QueryListApiReqVo reqVo) {
        if (CollectionUtil.isEmpty(reqVo.getProjectIdList())) {
            return Result.error("项目id集合不能为空");
        }

        LambdaQueryWrapper<GuarInfo> queryWrapper = new LambdaQueryWrapper<>();
        if (CollectionUtil.isNotEmpty(reqVo.getProjectIdList())) {
            queryWrapper.in(GuarInfo::getProjectId, reqVo.getProjectIdList());
        }
        queryWrapper.eq(StringUtils.isNotBlank(reqVo.getBidderId()), GuarInfo::getBidderId, reqVo.getBidderId());
        queryWrapper.eq(ObjectUtil.isNotNull(reqVo.getOpenStatus()), GuarInfo::getOpenStatus, reqVo.getOpenStatus());
        queryWrapper.eq(ObjectUtil.isNotNull(reqVo.getReleaseStatus()), GuarInfo::getReleaseStatus, reqVo.getReleaseStatus());
        queryWrapper.eq(ObjectUtil.isNotNull(reqVo.getCloseStatus()), GuarInfo::getCloseStatus, reqVo.getCloseStatus());
        queryWrapper.orderByDesc(GuarInfo::getApplyTime);

        return Result.success(BeanUtil.copyToList(guarInfoDao.list(queryWrapper), GuarApiRespVo.class));
    }

    /**
     * (前端接口)保存非优质采保函
     *
     * @param reqVo 入参
     * @return 处理结果
     */
    @Override
    public Result<GuarInfoSaveRespVo> saveNotYzcGuar(NotYzcGuarSaveReqVo reqVo) {
        String logStr = "[saveNotYzcGuar]非优质采电子保函提交===>lgNo=" + reqVo.getLgNo();

        //获取当前用户
        LoginUserInfo loginUserInfo = super.getLogInfo();

        BiddingSubInfoModel biddingSubInfo = getBidSubInfo(loginUserInfo.getCompanyId());

        //判断数据状态,是否已经提交
        GuarInfo guarInfo = guarInfoDao.getById(reqVo.getGuarInfoId());

        //已经提交
        if (ObjectUtil.isNotNull(guarInfo) && ObjectUtil.equals(guarInfo.getApplyStatus(), GuarApplyStatusEnum.Apply.getCode())) {
            return Result.error("保函信息已提交,请勿重复操作");
        }

        //准备更新数据库数据参数
        GuarInfo updateInfo = BeanUtil.copyProperties(reqVo, GuarInfo.class);

        //test环境支付金额设置为 0.01
        String envCode = env.getProperty("deposit.envCode");

        //瀚华保函，测试环境申请金额为0.01元
        if ((StringUtils.equals("test", envCode) || StringUtils.equals("pre", envCode))
                && ObjectUtil.equals(reqVo.getGuarTypeCode(), GuarTypeCodeEnum.HanHua.getCode())
        ) {
            updateInfo.setPayAmount(new BigDecimal(0.01));
        } else {
            updateInfo.setPayAmount(GuarUtil.getPayMoney(reqVo.getGuaranteeAmount())); //根据保函金额计算
        }

        //由于测试环境 无法进行CA签章，所以申请书附件id 写死
        if (StringUtils.equals("test", envCode)) {
            updateInfo.setAgreementAttRelaId("46d2297d-4b92-4c9a-8922-cdc214888266");
        }

        updateInfo.setHanhuaBusiType("01"); //投标保函
        updateInfo.setYzcBusiType(MSBusinessTypeEnum.Other.getCode()); //站外项目
        updateInfo.setUpdateTime(new Date());
        updateInfo.setApplyUserId(loginUserInfo.getUserId());
        updateInfo.setApplyUserName(loginUserInfo.getUserName());
        updateInfo.setApplyTime(new Date());
        updateInfo.setPayFrom("2"); //优质采页面支付

        //非优质采项目：其他默认参数
        //项目id
        String projectId = UUID.randomUUID().toString();
        //如果已经有项目id，则直接用，因为CA签章的时候，会自动生成
        if (ObjectUtil.isNotNull(guarInfo) && StringUtils.isNotEmpty(guarInfo.getProjectId())) {
            projectId = guarInfo.getProjectId();
        }
        updateInfo.setProjectId(projectId); //项目id随机生成一个
        updateInfo.setIsYzc(CommonStatusEnum.No.getCode()); //非优质采类型
        updateInfo.setBidderId(loginUserInfo.getCompanyId());
        updateInfo.setBidderName(loginUserInfo.getCompanyName());
        updateInfo.setCreditCode(biddingSubInfo.getBusinessLicenseNo()); //登录信息里面为空,所以单独获取一次
        updateInfo.setProjectDeposit(reqVo.getGuaranteeAmount()); //非优质采项目保证金金额默认为申请金额
        updateInfo.setBidderCountry("CHN");//国别默认为中国
        updateInfo.setOpenBidTime(reqVo.getSignupEndTime()); //站外项目开标时间默认为报名截止时间，因为是明文，兴泰保函又需要开标时间

        //获取出函机构等信息
        GuarTypeConfig guarTypeConfig = guarTypeConfigDao.getByGuarTypeCode(reqVo.getGuarTypeCode());
        if (ObjectUtil.isNull(guarTypeConfig)) {
            return Result.error("保函机构不存在");
        }

        //出函机构、收款方等信息
        updateInfo.setFinancialId(GuarUtil.getFinancialIdByGuarType(reqVo.getGuarTypeCode()));
        updateInfo.setFinanceOrgName(guarTypeConfig.getFinanceOrgName());
        updateInfo.setPayeeName(guarTypeConfig.getPayeeName());

        boolean saveOrUpdateRst = false;
        if (ObjectUtil.isNotNull(guarInfo)) {
            //更新数据库
            saveOrUpdateRst = guarInfoDao.updateById(updateInfo);
        } else {
            //新增数据
            saveOrUpdateRst = guarInfoDao.save(updateInfo);
        }

        log.info("{}，保函信息新增或更新完成，结果={}", logStr, saveOrUpdateRst);
        if (!saveOrUpdateRst) {
            return Result.error("保函信息更新失败");
        }

        //查询最新数据对象
        guarInfo = guarInfoDao.getById(reqVo.getGuarInfoId());

        //暂存，流程到此结束
        if (!ObjectUtil.equals(guarInfo.getApplyStatus(), GuarApplyStatusEnum.Apply.getCode())) {
            return Result.success();
        }

        //由前端提交的保函机构类型，进行适配申请
        ApplyGuarReqDto reqDto = BeanUtil.copyProperties(guarInfo, ApplyGuarReqDto.class);

        //其他信息
        reqDto.setIsEncrypt(CommonStatusEnum.No.getCode()); //默认为不加密函

        //保函申请
        Result<ApplyGuarRespDto> guarApplyRespDto = guarAdapterConfig.open(reqDto);
        if (ObjectUtil.isNull(guarApplyRespDto)) {
            guarInfoDao.resetToTemp(guarInfo.getGuarInfoId());//还原状态至暂存
            return Result.error("保函申请失败[null]");
        }

        if (!guarApplyRespDto.isSuccess()
                || ObjectUtil.isNull(guarApplyRespDto.getData())) {
            guarInfoDao.resetToTemp(guarInfo.getGuarInfoId());//还原状态至暂存
            return Result.error("保函申请失败[" + guarApplyRespDto.getMsg() + "]");
        }

        //生成优质采订单
        GetOrderReqDto getOrderReqDto = guarCommonService.getOrderReqDto(guarInfo);
        GetOrderRespDto getOrderRespDto = orderCenterService.getOrder(getOrderReqDto);
        log.info("{}，生成优质采业务订单完成，结果={}", logStr, JSONUtil.toJsonStr(getOrderRespDto));

        //创建订单失败
        if (ObjectUtil.isNull(getOrderRespDto) || StringUtils.isBlank(getOrderRespDto.getOrderNo())) {
            return Result.error("创建订单失败");
        }

        //更新保函优质采订单号
        LambdaUpdateWrapper<GuarInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(GuarInfo::getGuarInfoId, reqVo.getGuarInfoId())
                .set(GuarInfo::getOrderNo, getOrderRespDto.getOrderNo())
        ;
        boolean updateOrderNoRst = guarInfoDao.update(updateWrapper);
        log.info("{}，更新保函订单号出函机构等信息完成，结果={}", logStr, updateOrderNoRst);

        GuarInfoSaveRespVo respVo = new GuarInfoSaveRespVo();
        respVo.setChargesType(50);
        respVo.setOrderNo(getOrderRespDto.getOrderNo());

        return Result.success(respVo);
    }

    /**
     * (前端接口)获取CA签章url
     *
     * @param reqVo
     * @return 签章url
     */
    @Override
    public Result<String> getESignUrl(GuarESignReqVo reqVo) throws IOException {
        String logStr = "获取CA签章url===>";
        //非优质采项目必填项校验
        if (ObjectUtil.equals(reqVo.getIsYzc(), CommonStatusEnum.No.getCode())
                && (StringUtils.isBlank(reqVo.getProjectNo())
                || StringUtils.isBlank(reqVo.getProjectName())
                || StringUtils.isBlank(reqVo.getTenderee()))) {
            return Result.error("项目编号、项目名称、招标人等信息不能为空");
        }
        //获取当前用户
        LoginUserInfo loginUserInfo = super.getLogInfo();

        //判断是否已存在
        GuarInfo guarInfo = guarInfoDao.getById(reqVo.getGuarInfoId());

        //准备更新数据库
        GuarInfo guarInfoUpdate = BeanUtil.copyProperties(reqVo, GuarInfo.class);

        //非优质采
        if (ObjectUtil.equals(reqVo.getIsYzc(), CommonStatusEnum.No.getCode())) {
            guarInfoUpdate.setIsYzc(CommonStatusEnum.No.getCode());
            guarInfoUpdate.setBidderId(loginUserInfo.getCompanyId());
            guarInfoUpdate.setBidderName(loginUserInfo.getCompanyName());
        }

        //获取出函机构等信息
        GuarTypeConfig guarTypeConfig = guarTypeConfigDao.getByGuarTypeCode(reqVo.getGuarTypeCode());
        if (ObjectUtil.isNull(guarTypeConfig)) {
            return Result.error("保函机构不存在");
        }

        //出函机构、收款方等信息
        guarInfoUpdate.setFinancialId(GuarUtil.getFinancialIdByGuarType(reqVo.getGuarTypeCode()));
        guarInfoUpdate.setFinanceOrgName(guarTypeConfig.getFinanceOrgName());
        guarInfoUpdate.setPayeeName(guarTypeConfig.getPayeeName());

        boolean saveOrUpdateRst = false;

        if (ObjectUtil.isNull(guarInfo)) {
            saveOrUpdateRst = guarInfoDao.save(guarInfoUpdate);
        } else {
            saveOrUpdateRst = guarInfoDao.updateById(guarInfoUpdate);
        }
        log.info("{}，保函信息新增或更新完成，入参={},结果={}", logStr, JSONUtil.toJsonStr(guarInfoUpdate), saveOrUpdateRst);

        if (!saveOrUpdateRst) {
            return Result.error("保函信息更新失败");
        }

        //重新获取数据对象
        guarInfo = guarInfoDao.getById(reqVo.getGuarInfoId());

        //匹配模板
        String filePath = "";
        if (ObjectUtil.equals(guarInfo.getGuarTypeCode(), GuarTypeCodeEnum.HanHua.getCode())) {
            filePath = "template/瀚华保函申请书.docx";
        } else {
            filePath = "template/兴泰保函申请书.docx";
        }

        FileOutputStream tempWordFileOutStream = null;
        FileInputStream tempPdfFileInputStream = null;
        File tempWordFile = null;
        File tempPdfFile = null;
        try {
            //1.根据模板转换word
            Map<String, String> map = new HashMap<>();
            map.put("LgNo", guarInfo.getLgNo());
            map.put("FinanceOrgName", guarInfo.getFinanceOrgName());
            map.put("BidderName", guarInfo.getBidderName());
            map.put("ProjectNo", guarInfo.getProjectNo());
            map.put("ProjectName", guarInfo.getProjectName());
            map.put("Tenderee", guarInfo.getTenderee());
            map.put("ProjectDeposit", guarInfo.getGuaranteeAmount().toString()); //项目担保金额

            // 打开 Word 文档
            XWPFDocument docx = null;
            try {
                InputStream is = new ClassPathResource(filePath).getStream();
                docx = new XWPFDocument(is);
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //获取表格
            assert docx != null;
            List<XWPFParagraph> paragraphs = docx.getParagraphs();
            for (XWPFParagraph paragraph : paragraphs) {
                List<XWPFRun> runs = paragraph.getRuns();
                for (XWPFRun run : runs) {
                    //遍历该段里的所有文本
                    String str = run.toString();
                    //如果该段文本包含map中的key，则替换为map中的value值。
                    Set<String> keySet = map.keySet();
                    for (String key : keySet) {
                        if (str.trim().equals(key)) {
                            //替换该文本0位置的数据。
                            run.setText(map.get(key), 0);
                        }
                    }
                }
            }

            //本地启动，会指定文件夹
            String tempFilePath = "E:\\tempFile\\guarTest";
            File directory = new File(tempFilePath);
            //将word文件存储为临时文件，名为：tempWord+时间戳+随机数.docx
            String wordTempPath = "tempWord" + DateUtil.format(new Date(), "yyyyMMddHHmmss") + RandomUtil.randomInt(100, 999);
            // pdf创建临时 word文件转pdf
            String tempPdfFileName = "tempPdf" + DateUtil.format(new Date(), "yyyyMMddHHmmss") + RandomUtil.randomInt(100, 999);

            String envCode = env.getProperty("deposit.envCode");
            if (StringUtils.equals(envCode, "dev")) {
                tempWordFile = File.createTempFile(wordTempPath, ".docx", directory);
                tempPdfFile = File.createTempFile(tempPdfFileName, ".pdf", directory);
            } else {
                tempWordFile = File.createTempFile(wordTempPath, ".docx");
                tempPdfFile = File.createTempFile(tempPdfFileName, ".pdf");
            }

            log.info("word临时文件路径：" + tempWordFile.getPath());
            log.info("pdf临时文件路径：" + tempPdfFile.getPath());

            //写入临时word文件
            tempWordFileOutStream = new FileOutputStream(tempWordFile);
            docx.write(tempWordFileOutStream);

            //2.根据word生成pdf
//            FileUtil.word2Pdf(tempWordFile.getPath(), tempPdfFile.getPath()); //测试失败---centos cmd执行报错
            convertFileWord2Pdf(tempWordFile.getPath(), tempPdfFile.getPath()); //测试成功

            //3.将临时pdf文件存入minio文件
            String attId = UUID.randomUUID().toString();
            String attName = guarInfo.getLgNo() + ".pdf";

            PDFFileModel pdfFileModel = new PDFFileModel();
            pdfFileModel.setFilePath(attName);
            pdfFileModel.setBaseAttachmentId(attId);
            pdfFileModel.setTendProjectId(attId);
            pdfFileModel.setRelaId(attId);
            pdfFileModel.setFileViewName(attName);
            pdfFileModel.setNewFileName(attName);

            log.info("attId：" + attId);
            log.info("fileName：" + attName);
            boolean uploadTempPdfFileResult = fileServerAPI.addFile(tempPdfFile.getPath(), pdfFileModel);

           /* FileModel fileDto = new FileModel();
            tempPdfFileInputStream = new FileInputStream(tempPdfFile);
            fileDto.setBaseAttachmentId(attId);
            fileDto.setTendProjectId(attId);
            fileDto.setRelaId(attId);
            fileDto.setFileViewName(attName);
            fileDto.setAttViewName(attName);
            fileDto.setAttPath(attName);
            fileDto.setFile(tempPdfFileInputStream.readAllBytes());

            Boolean uploadTempPdfFileResult = fileServerAPI.uploadFile(fileDto);*/
            log.info("{}上传待签章pdf文件至服务器,入参={},结果={}", logStr, JSONUtil.toJsonStr(pdfFileModel), uploadTempPdfFileResult);
            if (!uploadTempPdfFileResult) {
                return Result.error("上传pdf文件至文件服务器失败");
            }

            //4.调用CA签章接口
            String businessId = reqVo.getLgNo();

            SignatureDto signatureDto = new SignatureDto();

//            signatureDto.setBucket(env.getProperty("minio.file.bucketName"));
            signatureDto.setNetFolderName("fileupload/" + attId);
            signatureDto.setFileName(attName);
            signatureDto.setFileViewName(attName);

            signatureDto.setBusinessType("GUAR"); //投标工具、询比、竞价、企业招标、合同业务类型
            signatureDto.setBusinessName("电子保函");

            /**
             * 业务Id：由业务传入，用于记录扫码记录。
             * 例如：项目Id或者合同Id
             */
            signatureDto.setBusinessId(businessId);

            /**
             * 操作完成后的动作类型1.跨域通知 2.直接页面跳转 3.无动作
             */
            signatureDto.setFinishOperateType(reqVo.getFinishOperateType());

            /**
             * 操作完成后业务网址 1.跨域通知iframe通知地址2.直接页面跳转网址,用于新标签页打开的情况,
             * 签署完成后跳转到具体业务页面3.无动作时为空
             */
            signatureDto.setFinishOperateUrl(reqVo.getFinishOperateUrl());

            signatureDto.setButtonStatus("1-1,2-1,3-1,4-1,5-0,6-1,7-1,8-0");
            signatureDto.setBackUrl(env.getProperty("yzc.commonGateWay") + "/deposit/api/guarOther/saveESignResult");

            /**
             * 印章来源默认为0 0/UKey  2/小程序端
             */
            signatureDto.setSealSource(reqVo.getSealSource());

            /**
             * 证件类型 0个人，1事件，2企业
             */
            signatureDto.setCertType("2");
            signatureDto.setUnitNo(loginUserInfo.getBusinessLicenseNo());
            signatureDto.setUnit(loginUserInfo.getCompanyName());

            signatureDto.setOperatingPlatform("YOUZC");

            /**
             * 业务唯一标识（各业务自己定义，能找到唯一的就行）
             */
            signatureDto.setBusinessRelationCode(businessId);

            log.info("{}申请签章，入参={}", logStr, JSONUtil.toJsonStr(signatureDto));
            String signUrl = extAPIService.getTzSignUrl(signatureDto);

            log.info("签章链接：" + signUrl);
            return Result.success("测试成功", signUrl);

        } catch (Exception e) {
            log.error("saveFrAuthorization error msg :{}", e.getMessage());
            e.printStackTrace();
        } finally {
            //关闭流
            if (tempWordFileOutStream != null) {
                tempWordFileOutStream.close();
                tempWordFileOutStream.flush();
            }
            if (tempPdfFileInputStream != null) {
                tempPdfFileInputStream.close();
            }
            //5.删除临时pdf文件
            if (tempWordFile != null && tempWordFile.exists()) {
                tempWordFile.delete();
            }

            if (tempPdfFile != null && tempPdfFile.exists()) {
                tempPdfFile.delete();
            }
        }

        return Result.success("测试异常");

    }

    /**
     * CA签章结果保存
     *
     * @param json json对象
     * @return 操作结果
     */
    @Override
    public Boolean saveSignResult(JSONObject json) {
        String logStr = "接收CA签章结果回推===>";
        //签章状态：1成功
        Integer signResult = json.getInteger("signResult");

        //附件关联id
        String businessRelationCode = json.getString("businessRelationCode");

        //签章后的附件id【minio的附件id】
        String attRelaId = json.getString("attRelaId");

        //签章后文件访问url
        String fileDownloadUrl = json.getString("fileDownloadUrl");

        log.info("{}入参：businessRelationCode={},signResult={},attRelaId={}", logStr, businessRelationCode,  signResult, attRelaId);

        //根据保函编号，更新附件关联id
        LambdaUpdateWrapper<GuarInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(GuarInfo::getLgNo, businessRelationCode)
                .set(GuarInfo::getSignResult,  signResult)  //签署状态
                .set(GuarInfo::getAgreementAttRelaId, attRelaId);

        return guarInfoDao.update(updateWrapper);
    }

    /**
     * (前端接口)获取CA签章结果
     *
     * @param lgNo lgNo
     * @return 签章结果
     */
    @Override
    public GuarSignResultRespVo getSignResult(String lgNo) {
        //根据保函编号 获取保函信息
        GuarInfoRespDto guarInfoRespDto = guarInfoDao.getByLgNo(lgNo);

        if(ObjectUtil.isNull(guarInfoRespDto)){
            return null;
        }

        return BeanUtil.copyProperties(guarInfoRespDto, GuarSignResultRespVo.class);
    }
}
