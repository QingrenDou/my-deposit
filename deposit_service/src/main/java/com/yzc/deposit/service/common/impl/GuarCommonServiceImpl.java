package com.yzc.deposit.service.common.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.yzc.common.api.Result;
import com.yzc.common.api.service.IFileApiService;
import com.yzc.common.api.service.IOrganizationApiService;
import com.yzc.common.common.enums.CommonStatusEnum;
import com.yzc.common.common.enums.MSBusinessTypeEnum;
import com.yzc.common.domain.LoginUserInfo;
import com.yzc.common.dto.order.GetOrderProjectDto;
import com.yzc.common.dto.order.GetOrderReqDto;
import com.yzc.common.dto.subject.CredentialCompanyInfoRespDto;
import com.yzc.common.guar.dto.CertFileCopyRespDto;
import com.yzc.common.guar.entity.GuarInfo;
import com.yzc.common.guar.entity.GuarTypeConfig;
import com.yzc.common.guar.enums.GuarTypeCodeEnum;
import com.yzc.common.guar.util.GuarUtil;
import com.yzc.common.guar.vo.GuarPaySuccessReqVo;
import com.yzc.common.guar.vo.RandomGuarOrgRespVo;
import com.yzc.common.model.BiddingSubInfoModel;
import com.yzc.common.model.file.PDFFileModel;
import com.yzc.deposit.dao.guar.IGuarTypeConfigDao;
import com.yzc.deposit.service.AbstractBaseService;
import com.yzc.deposit.service.common.IGuarCommonService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * 电子保函通用方法实现类
 */
@Slf4j
@Service
public class GuarCommonServiceImpl extends AbstractBaseService implements IGuarCommonService {

    @Resource
    private Environment env;

    @Resource
    private IGuarTypeConfigDao guarTypeConfigDao;

    @Resource
    private IOrganizationApiService  organizationApiService;

    @Resource
    private IFileApiService fileServerAPI;

    /**
     * 根据保函信息，生成优质采业务订单入参
     *
     * @param guarInfo 保函信息
     * @return 优质采业务订单入参
     */
    @Override
    public GetOrderReqDto getOrderReqDto(GuarInfo guarInfo) {
        GetOrderReqDto reqDto = new GetOrderReqDto();

        //项目信息
        GetOrderProjectDto projectOrder = new GetOrderProjectDto();
        projectOrder.setProjectId(guarInfo.getProjectId());
        projectOrder.setProjectCode(guarInfo.getProjectNo());
        projectOrder.setProjectName(guarInfo.getProjectName());

        // 1,询价采购;2,企业招标;3,依法招标;4,竞价
        int projectType = 3;
        MSBusinessTypeEnum enumByCode = EnumUtil.likeValueOf(MSBusinessTypeEnum.class, guarInfo.getYzcBusiType());
        switch (enumByCode) {
            case SupplyPur:
                projectType = 1;
                break;
            case CustomBidding:
                projectType = 2;
                break;
            case Bidding:
                projectType = 4;
                break;
            default:
                projectType = 3;
                break;
        }
        projectOrder.setProjectType(projectType); //需要转换
        projectOrder.setBidderId(guarInfo.getBidderId());
        projectOrder.setBidderName(guarInfo.getBidderName());
        projectOrder.setTenderId("");
        projectOrder.setTenderName(guarInfo.getTenderee());
        projectOrder.setChargeObject(1); //投标人
        //当前日期+1 天
        projectOrder.setExpireDate(DateUtil.format(DateUtil.offsetDay(new Date(), 1), "yyyy-MM-dd HH:mm:ss"));
        projectOrder.setSectionNames(guarInfo.getProjectName());
        projectOrder.setOwnerCompanyId(guarInfo.getCompanyId());
        projectOrder.setOwnerCompanyName(guarInfo.getCompanyName());
        projectOrder.setTenderType(null); //招标方式暂时不传值
        projectOrder.setGuaranteeMoneyLimit(guarInfo.getGuaranteeAmount()); //担保金额

        //订单信息
        reqDto.setLgNo(guarInfo.getLgNo()); //关键信息
        reqDto.setFinancialCode(guarInfo.getFinancialId()); //电子保函机构
        reqDto.setOrderType(2);//项目类订单
        reqDto.setChargesType(50);
        reqDto.setOrderDesc("电子保函");
        reqDto.setServiceContent("电子保函");
        reqDto.setPaymentPlatform(2); //PC端

        //支付金额：兴泰\国控保函 要求测试环境支付订单时候，金额固定设置为0.1元
        String envCode = env.getProperty("deposit.envCode");
        if ((StringUtils.equals("test", envCode) || StringUtils.equals("pre", envCode))
                && (ObjectUtil.equals(guarInfo.getGuarTypeCode(), GuarTypeCodeEnum.XingTai.getCode())
                || ObjectUtil.equals(guarInfo.getGuarTypeCode(), GuarTypeCodeEnum.GuoKong.getCode()))
        ) {
            reqDto.setOrderMoney(BigDecimal.valueOf(0.1));
        } else {
            reqDto.setOrderMoney(guarInfo.getPayAmount()); //支付金额
        }

        reqDto.setMembershipLevel(""); //会员等级
        reqDto.setCompanyType("投标人");
        reqDto.setCompanyId(guarInfo.getBidderId());
        reqDto.setCompanyName(guarInfo.getBidderName());
        reqDto.setValidTimeMinutesCount(1440);

        //国别
        int isChina = 1; //潘总要求写死
        /*
        if(StringUtils.isBlank(guarInfo.getBidderCountry())
            || StringUtils.equals(guarInfo.getBidderCountry(),"156")
            || StringUtils.equals(guarInfo.getBidderCountry(),"CHN")){
            isChina = 1;
        }else{
            isChina = 0;
        }
        */
        reqDto.setIsChina(isChina);
        reqDto.setCreatorId(guarInfo.getApplyUserId());
        reqDto.setCreatorName(guarInfo.getApplyUserName());
        reqDto.setBusinessLicenseNo(guarInfo.getCreditCode());
        reqDto.setContact(guarInfo.getOperatorName());
        reqDto.setContactPhone(guarInfo.getOperatorPhone());
        reqDto.setSourceType(1); //来源类型：1-优质采
        reqDto.setCusid("YZC");//订单类型：YZC-优质采
        reqDto.setCallbackUrl(env.getProperty("yzc.commonGateWay") + "/deposit/api/guarOther/paySuccess");

        //业主是否非优质采用户【非优质采项目的业主为非优质采用户 值为1】
        reqDto.setUnPlatformUser(ObjectUtil.equals(guarInfo.getIsYzc(), CommonStatusEnum.No.getCode()) ? 1 : null);

        //回调入参
        GuarPaySuccessReqVo reqVo = new GuarPaySuccessReqVo();
        reqVo.setLgNo(guarInfo.getLgNo());
        reqDto.setBusinessData(JSONUtil.toJsonStr(reqVo));
        reqDto.setProjectOrder(projectOrder); //项目信息
        return reqDto;
    }

    /**
     * 随机匹配保函机构
     *
     * @return 随机匹配保函机构结果
     */
    @Override
    public Result<RandomGuarOrgRespVo> getRandomGuarOrg() {
        LoginUserInfo loginUserInfo = getLogInfo();

        BiddingSubInfoModel biddingSubInfo = getBidSubInfo(loginUserInfo.getCompanyId());

        RandomGuarOrgRespVo respVo = new RandomGuarOrgRespVo();
        respVo.setBidderId(loginUserInfo.getCompanyId());
        respVo.setBidderName(loginUserInfo.getCompanyName());
        respVo.setCreditCode(biddingSubInfo.getBusinessLicenseNo()); //统一社会信用代码---单独接口获取
        respVo.setApplyUserName(loginUserInfo.getUserName()); //当前人

        //默认为瀚华
        int guarTypeCode = GuarTypeCodeEnum.HanHua.getCode();

        /*

        //随机分配保函机构 【10021】 兴泰:国控:瀚华 的比例为3:3:4
        int ran = RandomUtil.randomInt(1, 10);  //1-10 随机生成数字

        if (ran <= 3) { //兴泰保函机构
            guarTypeCode = GuarTypeCodeEnum.XingTai.getCode();
        } else if (ran <= 6) { //国控保函机构
            guarTypeCode = GuarTypeCodeEnum.GuoKong.getCode();
        } else { //瀚华保函机构
            guarTypeCode = GuarTypeCodeEnum.HanHua.getCode();
        }

        */

        //查询保函配置信息
        GuarTypeConfig guarTypeConfig = guarTypeConfigDao.getByGuarTypeCode(guarTypeCode);
        if (guarTypeConfig == null) {
            return Result.error("匹配失败");
        }

        respVo.setGuarTypeCode(guarTypeCode);
        respVo.setGuarInfoId(IdWorker.getId());
        respVo.setFinanceOrgName(guarTypeConfig.getFinanceOrgName());
        respVo.setPayeeName(guarTypeConfig.getPayeeName());
        respVo.setLgNo(GuarUtil.genLgNo());

        return Result.success(respVo);
    }

    /**
     * 复制企业证件 并生成新的文件
     * @param companyId 企业id
     * @return 新的文件id集合
     */
    @Override
    public Result<CertFileCopyRespDto> certFileCopy(String companyId) {
        String logStr = "certFileCopy==>";
        log.info("{}开始,companyId:{}",logStr, companyId);
        Result<CredentialCompanyInfoRespDto> companyCertResult = organizationApiService.getYZCCACompanyAuthenticationInfo(companyId);
        log.info("{}获取证件信息结果:{}",logStr, JSONUtil.toJsonStr(companyCertResult));

        if(ObjectUtil.isNull(companyCertResult) || !companyCertResult.isSuccess()
                || ObjectUtil.isNull(companyCertResult.getData())){
            return Result.error("获取企业证件失败");
        }
        CredentialCompanyInfoRespDto companyCert = companyCertResult.getData();

        CertFileCopyRespDto respDto = new CertFileCopyRespDto();

        //不管是否复制成功，都新增id
        String businessLicAttIdNew = UUID.randomUUID().toString();
        String legalIdCarAttIdNew = UUID.randomUUID().toString();
        String legalIdCarBackAttIdNew = UUID.randomUUID().toString();

        //复制 营业执照
        if(ObjectUtil.isNotNull(companyCert.getBusinessLicAttrach())
                && StringUtils.isNotBlank(companyCert.getBusinessLicAttrach().getAttachUrl())
            ){

            String attName = companyCert.getBusinessLicAttrach().getAttachName(); //使用原存放文件名
            PDFFileModel pdfFileModel = new PDFFileModel();
            pdfFileModel.setFilePath(attName);
            pdfFileModel.setBaseAttachmentId(businessLicAttIdNew);
            pdfFileModel.setTendProjectId(businessLicAttIdNew);
            pdfFileModel.setRelaId(businessLicAttIdNew);
            pdfFileModel.setFileViewName(attName);
            pdfFileModel.setNewFileName(attName);

            boolean uploadTempPdfFileResult = fileServerAPI.addFile(companyCert.getBusinessLicAttrach().getAttachUrl(), pdfFileModel);
            log.info("{}营业执照复制 完成,入参={},结果:{}",logStr,JSONUtil.toJsonStr(pdfFileModel), uploadTempPdfFileResult);
        }

        //复制 法人身份证 正面
        if(ObjectUtil.isNotNull(companyCert.getLegalIdCarAttrach())
         && StringUtils.isNotBlank(companyCert.getLegalIdCarAttrach().getAttachUrl())){
            String attName = companyCert.getLegalIdCarAttrach().getAttachName();
            PDFFileModel pdfFileModel = new PDFFileModel();
            pdfFileModel.setFilePath(attName);
            pdfFileModel.setBaseAttachmentId(legalIdCarAttIdNew);
            pdfFileModel.setTendProjectId(legalIdCarAttIdNew);
            pdfFileModel.setRelaId(legalIdCarAttIdNew);
            pdfFileModel.setFileViewName(attName);
            pdfFileModel.setNewFileName(attName);
            boolean uploadTempPdfFileResult = fileServerAPI.addFile(companyCert.getLegalIdCarAttrach().getAttachUrl(), pdfFileModel);
            log.info("{}法人身份证正面复制 完成,入参={},结果:{}",logStr,JSONUtil.toJsonStr(pdfFileModel), uploadTempPdfFileResult);
        }

        //复制 法人身份证 反面
        if(ObjectUtil.isNotNull(companyCert.getLegalIdCarBackAttrach())
        && StringUtils.isNotBlank(companyCert.getLegalIdCarBackAttrach().getAttachUrl())){
            String attName = companyCert.getLegalIdCarBackAttrach().getAttachName();
            PDFFileModel pdfFileModel = new PDFFileModel();
            pdfFileModel.setFilePath(attName);
            pdfFileModel.setBaseAttachmentId(legalIdCarBackAttIdNew);
            pdfFileModel.setTendProjectId(legalIdCarBackAttIdNew);
            pdfFileModel.setRelaId(legalIdCarBackAttIdNew);
            pdfFileModel.setFileViewName(attName);
            pdfFileModel.setNewFileName(attName);
            boolean uploadTempPdfFileResult = fileServerAPI.addFile(companyCert.getLegalIdCarBackAttrach().getAttachUrl(), pdfFileModel);
            log.info("{}法人身份证反面复制 完成,入参={},结果:{}",logStr,JSONUtil.toJsonStr(pdfFileModel), uploadTempPdfFileResult);
        }

        respDto.setBusinessLicAttIdNew(businessLicAttIdNew);
        respDto.setLegalIdCarAttIdNew(legalIdCarAttIdNew);
        respDto.setLegalIdCarBackAttIdNew(legalIdCarBackAttIdNew);

        return Result.success(respDto);
    }
}
