package com.yzc.deposit.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import com.yzc.common.api.service.IAuthInfoService;
import com.yzc.common.api.service.IOrganizationApiService;
import com.yzc.common.common.util.CommonUtil;
import com.yzc.common.domain.LoginUserInfo;
import com.yzc.common.exception.BusinessException;
import com.yzc.common.model.BiddingSubInfoModel;
import com.yzc.common.util.date.DateTimeUtil;
import com.yzc.common.deposit.dto.deposit.CanBackMoneyBusinessReqDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author liuzhongxiang
 * @version 1.0
 * @title AbstractBaseService
 * @description
 * @create 2025/3/7 9:22
 */
@Slf4j
@Service
public abstract class AbstractBaseService {

    @Resource
    private IAuthInfoService authInfoService;
    @Resource
    private IOrganizationApiService organizationApiService;

    @Resource
    private Environment env;

    /**
     * 校验是否满足业务退款条件
     * @param reqDto 请求参数
     * @return 结果
     */
    public boolean checkCanBackMoneyBusiness(CanBackMoneyBusinessReqDto reqDto){
        //项目异常
        if(ObjectUtil.equal(reqDto.getIsAbortive(),1)){
            return true;
        }

        //中标人确定已超过三天
        if(ObjectUtil.equal(reqDto.getIsConfirmBidder(),1) && reqDto.getConfirmBidderTime() != null
                && DateUtil.compare(DateTimeUtil.dateAddDay(reqDto.getConfirmBidderTime(),3),new Date()) <= 0){
            return true;
        }

        //中标人已确定，所有非中标人可以退款
        if (ObjectUtil.equal(reqDto.getIsConfirmBidder(), 1)
                && reqDto.getConfirmBidderTime() != null
                && !ObjectUtil.equal(reqDto.getIsWinBidder(), 1)) {  //所有未中标
            return true;
        }

        //候选中标人确定已超过三天,所有非候选人可退款
        if (ObjectUtil.equal(reqDto.getIsConfirmCandidate(), 1)
                && reqDto.getConfirmCandidateTime() != null
                && DateUtil.compare(DateTimeUtil.dateAddDay(reqDto.getConfirmCandidateTime(), 3), new Date()) <= 0
                && !ObjectUtil.equal(reqDto.getIsCandidate(), 1)
                && !ObjectUtil.equal(reqDto.getIsWinBidder(), 1)) { //增加非中标人条件---最大限度防止业务漏传
            return true;
        }

        return false;
    }

    /**
     * 获取当前登录用户信息
     * @return 用户信息
     */
    public LoginUserInfo getLogInfo(){
        String envCode = env.getProperty("deposit.envCode");
        if(StringUtils.equals(envCode, "dev")){
            return authInfoService.getAuthInfoByUserId("6FD1194B-CCFD-41FF-8E14-28D9385487C6"); //xtt01
        }else{
            return authInfoService.getAuthInfo();

        }
    }

    /**
     * 获取当前用户企业信息明细
     * @param userId 用户id
     * @return 用户信息
     */
    public LoginUserInfo getAuthInfoByUserId(String userId){
        return authInfoService.getAuthInfoByUserId(userId);
    }

    /**
     * 获取企业信息明细
     * @param companyId 企业id
     * @return 企业信息明细
     */
    public BiddingSubInfoModel getBidSubInfo(String companyId){
        return organizationApiService.GetBidSubInfo(companyId);
    }

    /**
     * 检查当前用户是否有指定的权限
     *
     * @param rindex 权限索引
     * @return 是否有权限
     */
    public Boolean checkAuthor(Integer rindex) {
        LoginUserInfo loginUserInfo = this.getLogInfo();
        String userRightBin = loginUserInfo.getPermissionStr();
        if (StrUtil.isEmpty(userRightBin)) {
            return false;
        }

        if (userRightBin.length() < rindex + 1) {
            return false;
        } else {
            return "1".equals(userRightBin.substring(rindex, rindex + 1));
        }
    }

    /**
     * word转pdf
     */
    public void convertFileWord2Pdf(String wordFilePath, String pdfFilePath) {
        try{
            if (CommonUtil.getAsposeLicense()) {
                log.error("Aspose.Words 许可证应用失败");
                throw new BusinessException("Aspose.Words 许可证应用失败");
            }

            Document document_Report = new Document(wordFilePath);
            document_Report.save(pdfFilePath, SaveFormat.PDF);
        }
        catch (Exception ex){
            log.error("word转pdf失败===>",ex.toString());
        }
    }
}
