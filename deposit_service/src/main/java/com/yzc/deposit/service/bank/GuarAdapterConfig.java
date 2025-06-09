package com.yzc.deposit.service.bank;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yzc.common.api.Result;
import com.yzc.common.common.enums.CommonStatusEnum;
import com.yzc.common.guar.dto.common.*;
import com.yzc.common.guar.entity.GuarTypeConfig;
import com.yzc.common.guar.util.GuarMD5Util;
import com.yzc.deposit.dao.guar.IGuarTypeConfigDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 电子保函结构适配器
 */
@Component
public class GuarAdapterConfig {

    @Resource
    private IGuarTypeConfigDao guarTypeConfigDao;

    @Resource
    private List<IGuarAdapterService> guarAdapterServiceList;

    /**
     * 申请开函-
     *
     * @param reqDto 入参
     * @return 结果
     */
    public Result<ApplyGuarRespDto> open(ApplyGuarReqDto reqDto) {

        GuarTypeConfig guarTypeConfig = guarTypeConfigDao.getByGuarTypeCode(reqDto.getGuarTypeCode());

        //保函机构不存在
        if (ObjectUtil.isNull(guarTypeConfig)) {
            return Result.error("无效的保函机构");
        }

        for (IGuarAdapterService guarAdapterService : guarAdapterServiceList) {
            if (guarAdapterService.isCurrentGuar(guarTypeConfig.getFinanceMode())) {

                //预处理加密信息
                if(ObjectUtil.equal(reqDto.getIsEncrypt(), CommonStatusEnum.Yes.getCode())){ //需要加密
                    /* 1.需要加密的字段
                     * 2.暂时没有的字段：招标人/受益人统一社会信用代码、招标人地址、项目发布时间等
                     * 3.加密方法沿用之前网关的MD5加密方法
                     */
                    String projectId = GuarMD5Util.encryption(reqDto.getProjectId());
                    String projectNo = GuarMD5Util.encryption(reqDto.getProjectNo());
                    String projectName = GuarMD5Util.encryption(reqDto.getProjectName());
                    String tenderee = GuarMD5Util.encryption(reqDto.getTenderee());

                    reqDto.setProjectId(projectId);
                    reqDto.setProjectNo(projectNo);
                    reqDto.setProjectName(projectName);
                    reqDto.setTenderee(tenderee);
                }

                //业务类型为空，默认为01
                if(StringUtils.isBlank(reqDto.getBusinessType())){
                    reqDto.setBusinessType("01");
                }
                return guarAdapterService.open(reqDto,guarTypeConfig);
            }
        }
        return Result.error("当前保函机构暂不支持");
    }

    /**
     * 申请退保
     * @param reqDto 入参
     * @return 操作结果
     */
    public Result close(ApplyCloseReqDto reqDto) {

        GuarTypeConfig guarTypeConfig = guarTypeConfigDao.getByGuarTypeCode(reqDto.getGuarTypeCode());

        //保函机构不存在
        if (ObjectUtil.isNull(guarTypeConfig)) {
            return Result.error("无效的保函机构");
        }

        for (IGuarAdapterService guarAdapterService : guarAdapterServiceList) {
            if (guarAdapterService.isCurrentGuar(guarTypeConfig.getFinanceMode())) {
                return guarAdapterService.close(reqDto,guarTypeConfig);
            }
        }

        return Result.error("当前保函机构暂不支持");
    }

    /**
     * 批量解密
     * @param reqDto 入参
     * @return 操作结果
     */
    public Result<List<DecGuarRespDto>> decBatch(DecGuarBatchReqDto reqDto) {

        GuarTypeConfig guarTypeConfig = guarTypeConfigDao.getByGuarTypeCode(reqDto.getGuarTypeCode());

        //保函机构不存在
        if (ObjectUtil.isNull(guarTypeConfig)) {
            return Result.error("无效的保函机构");
        }

        for (IGuarAdapterService guarAdapterService : guarAdapterServiceList) {
            if (guarAdapterService.isCurrentGuar(guarTypeConfig.getFinanceMode())) {
                return guarAdapterService.decBatch(reqDto,guarTypeConfig);
            }
        }
        return Result.error("当前保函机构暂不支持");
    }

    /**
     * 申请赔付
     * @param reqDto 入参
     * @return 操作结果
     */
    public Result applyCompensation(ApplyCompensationReqDto reqDto) {
        GuarTypeConfig guarTypeConfig = guarTypeConfigDao.getByGuarTypeCode(reqDto.getGuarTypeCode());
        //保函机构不存在
        if (ObjectUtil.isNull(guarTypeConfig)) {
            return Result.error("无效的保函机构");
        }
        for (IGuarAdapterService guarAdapterService : guarAdapterServiceList) {
            if (guarAdapterService.isCurrentGuar(guarTypeConfig.getFinanceMode())) {
                return guarAdapterService.applyCompensation(reqDto,guarTypeConfig);
            }
        }
        return Result.error("当前保函机构暂不支持");
    }

    /**
     * 查询保函信息
     * @param reqDto 入参
     * @return 操作结果
     */
    public Result<QueryGuarRespDto> query(QueryGuarReqDto reqDto) {
        GuarTypeConfig guarTypeConfig = guarTypeConfigDao.getByGuarTypeCode(reqDto.getGuarTypeCode());
        //保函机构不存在
        if (ObjectUtil.isNull(guarTypeConfig)) {
            return Result.error("无效的保函机构");
        }
        for (IGuarAdapterService guarAdapterService : guarAdapterServiceList) {
            if (guarAdapterService.isCurrentGuar(guarTypeConfig.getFinanceMode())) {
                return guarAdapterService.query(reqDto,guarTypeConfig);
            }
        }
        return Result.error("当前保函机构暂不支持");
    }

    /**
     * 单个解密
     * 说明：由于每个项目内的可能存在多个保函机构，所以解密可能逐笔解密更合适
     * @param reqDto 入参
     * @return 操作结果
     */
    public Result<DecGuarRespDto> decSingle(DecGuarSingleReqDto reqDto) {
        GuarTypeConfig guarTypeConfig = guarTypeConfigDao.getByGuarTypeCode(reqDto.getGuarTypeCode());
        //保函机构不存在
        if (ObjectUtil.isNull(guarTypeConfig)) {
            return Result.error("无效的保函机构");
        }
        for (IGuarAdapterService guarAdapterService : guarAdapterServiceList) {
            if (guarAdapterService.isCurrentGuar(guarTypeConfig.getFinanceMode())) {
                return guarAdapterService.decSingle(reqDto,guarTypeConfig);
            }
        }

        return Result.error("当前保函机构暂不支持");
    }
}
