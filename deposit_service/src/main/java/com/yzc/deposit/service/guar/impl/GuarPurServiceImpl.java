package com.yzc.deposit.service.guar.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yzc.common.api.PageResult;
import com.yzc.common.api.Result;
import com.yzc.common.domain.LoginUserInfo;
import com.yzc.common.guar.dto.GuarInfoPageReqDto;
import com.yzc.common.guar.dto.GuarInfoRespDto;
import com.yzc.common.guar.dto.common.ApplyCompensationReqDto;
import com.yzc.common.guar.dto.common.GuarFileListDto;
import com.yzc.common.guar.entity.GuarInfo;
import com.yzc.common.guar.enums.GuarCloseStatusEnum;
import com.yzc.common.guar.enums.GuarCompensateStatusEnum;
import com.yzc.common.guar.enums.GuarOpenStatusEnum;
import com.yzc.common.guar.enums.GuarStatusQueryPurEnum;
import com.yzc.common.guar.util.GuarUtil;
import com.yzc.common.guar.vo.*;
import com.yzc.deposit.dao.guar.IGuarInfoDao;
import com.yzc.deposit.service.AbstractBaseService;
import com.yzc.deposit.service.bank.GuarAdapterConfig;
import com.yzc.deposit.service.guar.IGuarPurService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 电子保函-采购人端操作相关方法
 * 注释：由于 GuarInfoService方法太多，不利于阅读，所以按业务操作进行拆分
 */
@Service
@Slf4j
public class GuarPurServiceImpl extends AbstractBaseService implements IGuarPurService {

    @Resource
    private IGuarInfoDao guarInfoDao;

    @Resource
    private GuarAdapterConfig guarAdapterConfig;

    /**
     * 采购人端：获取保函列表
     *
     * @param reqVo 入参
     * @return 分页列表
     */
    @Override
    public PageResult<GuarInfoPurRespVo> pageListPur(GuarPurPageReqVo reqVo) {
        //获取当前登录人信息
        LoginUserInfo loginUserInfo = super.getLogInfo();
        //入参转换
        GuarInfoPageReqDto reqDto = BeanUtil.copyProperties(reqVo, GuarInfoPageReqDto.class);
        reqDto.setCompanyId(loginUserInfo.getCompanyId()); //仅查询当前企业的数据
        reqDto.setOpenStatus(GuarOpenStatusEnum.Success.getCode()); //采购人列表：只筛选开函成功的数据

        PageResult<GuarInfoRespDto> pageResult = guarInfoDao.pageList(reqDto);

        if (ObjectUtil.isNull(pageResult)) {
            return null;
        }
        //结果集转换
        if (CollectionUtil.isNotEmpty(pageResult.getRecords())) {
            List<GuarInfoPurRespVo> respVoList = BeanUtil.copyToList(pageResult.getRecords(), GuarInfoPurRespVo.class);

            //循环处理状态
            respVoList.forEach(respVo -> {
                //解析保函显示状态
                respVo.setPurGuarStatusStr(GuarUtil.getPurGuarStatusStr(BeanUtil.copyProperties(respVo, GuarInfo.class)));

                //已发起过索赔，即可以查看
                if(ObjectUtil.isNotNull(respVo.getCompensateStatus())
                        && ObjectUtil.notEqual(respVo.getCompensateStatus(), GuarCompensateStatusEnum.NOT_START.getCode())) {
                    respVo.setCanViewCompensate(1);
                }

                //是否可以申请索赔：状态为 已出函，且不是索赔失败的数据，均可以发起索赔
                if(StringUtils.equals(respVo.getPurGuarStatusStr(), GuarStatusQueryPurEnum.Normal.getDesc())
                    && ObjectUtil.notEqual(respVo.getCompensateStatus(), GuarCompensateStatusEnum.FAIL.getCode())){
                    respVo.setCanApplyCompensate(1); //可以索赔
                    respVo.setCanApplyCompensateDesc("‘已出函’且不是‘索赔失败’状态就可以索赔");
                }
            });

            return PageResult.toPageResult(respVoList, pageResult.getCurrent(), pageResult.getSize(), pageResult.getTotal(), pageResult.getPages());
        }

        return PageResult.toPageResult(null, pageResult.getCurrent(), pageResult.getSize(), pageResult.getTotal(), pageResult.getPages());
    }

    /**
     * 采购人端：申请索赔
     *
     * @param reqVo 索赔入参
     * @return 索赔结果
     */
    @Override
    public Result applyCompensation(ApplyCompensationReqVo reqVo) {
        //查询保函信息
        GuarInfo guarInfo = guarInfoDao.getById(reqVo.getGuarInfoId());

        if(ObjectUtil.isNull(guarInfo)){
            return Result.error("保函信息不存在");
        }
        //判断是否已经见发起索赔
        if(ObjectUtil.equals(guarInfo.getCompensateStatus(), GuarCompensateStatusEnum.START.getCode())
        || ObjectUtil.equals(guarInfo.getCompensateStatus(), GuarCompensateStatusEnum.SUCCESS.getCode())){
            return Result.error("保函已发起索赔，请勿重复提交");
        }

        //发起保函机构的索赔请求
        ApplyCompensationReqDto reqDto = new ApplyCompensationReqDto();
        reqDto.setGuarTypeCode(guarInfo.getGuarTypeCode()); //保函机构类型
        reqDto.setLgNo(guarInfo.getLgNo());
        reqDto.setContactName(reqVo.getCompensateContactName());
        reqDto.setContactPhone(reqVo.getCompensateContactPhone());
        reqDto.setTenderee(guarInfo.getTenderee());
        reqDto.setReason(reqVo.getCompensateReason());
        reqDto.setCompensateAccNo(reqVo.getCompensateAccNo());
        reqDto.setGuaranteeNumber(guarInfo.getGuaranteeNumber());
        reqDto.setBusinessType(guarInfo.getHanhuaBusiType());

        List<GuarFileListDto> fileList = new ArrayList<>();
        GuarFileListDto fileDto = new GuarFileListDto();
        fileDto.setFileCode("SP01");
        fileDto.setFileGuid(reqVo.getCompensateAttRelaId()); //索赔附件关联id
        fileList.add(fileDto);
        reqDto.setFileList(fileList);

        //调用保函机构发起索赔
        Result<String> result = guarAdapterConfig.applyCompensation(reqDto);
        if(ObjectUtil.isNull(result) || !result.isSuccess()){
            return Result.error("申请索赔失败");
        }

        //更新数据库
        LambdaUpdateWrapper<GuarInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(GuarInfo::getGuarInfoId, reqVo.getGuarInfoId());
        updateWrapper.set(GuarInfo::getCompensateStatus, GuarCompensateStatusEnum.START.getCode()) //已发起索赔
                .set(GuarInfo::getCompensateReason, reqVo.getCompensateReason())
                .set(GuarInfo::getCompensateUserId, super.getLogInfo().getUserId())
                .set(GuarInfo::getCompensateUserName, super.getLogInfo().getUserName())
                .set(GuarInfo::getCompensateAccNo, reqVo.getCompensateAccNo())
                .set(GuarInfo::getCompensateContactName, reqVo.getCompensateContactName())
                .set(GuarInfo::getCompensateContactPhone, reqVo.getCompensateContactPhone())
                .set(GuarInfo::getCompensateAttRelaId, reqVo.getCompensateAttRelaId())
                .set(GuarInfo::getCompensateMoney, reqVo.getCompensateMoney())
                .set(GuarInfo::getCompensateTime, new Date());
        boolean updateRst = guarInfoDao.update(updateWrapper);
        return updateRst ? Result.success("申请索赔成功") : Result.error("数据更新失败");
    }
}
