package com.yzc.deposit.dao.guar.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yzc.common.api.PageResult;
import com.yzc.common.common.enums.CommonStatusEnum;
import com.yzc.common.guar.dto.GuarInfoPageReqDto;
import com.yzc.common.guar.dto.GuarInfoRespDto;
import com.yzc.common.guar.entity.GuarInfo;
import com.yzc.common.guar.enums.*;
import com.yzc.deposit.dao.guar.IGuarInfoDao;
import com.yzc.deposit.repository.mapper.guar.GuarInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 保函申请信息 服务实现类
 * </p>
 *
 * @author DouQingRen
 * @since 2025-04-07
 */
@Service
public class GuarInfoDaoImpl extends ServiceImpl<GuarInfoMapper, GuarInfo> implements IGuarInfoDao {

    /**
     * 保函申请信息分页列表
     *
     * @param reqDto 请求参数
     * @return 分页列表
     */
    @Override
    public PageResult<GuarInfoRespDto> pageList(GuarInfoPageReqDto reqDto) {
        //关键字段校验：采购人id和投标人id 必传其一
        if (StringUtils.isBlank(reqDto.getCompanyId()) && StringUtils.isBlank(reqDto.getBidderId())) {
            return null;
        }

        LambdaQueryWrapper<GuarInfo> queryWrapper = new LambdaQueryWrapper<>();

        //站内项目：仅显示支付成功或审核失败退款的数据，站外项目全部显示
        queryWrapper.and(
                //优质采项目需要支付成功或退款成功
                wrapper -> wrapper.and(
                                wp -> wp.in(GuarInfo::getPayStatus, GuarPayStatusEnum.Success.getCode(),
                                        GuarPayStatusEnum.Refunding.getCode(),
                                        GuarPayStatusEnum.RefundSuccess.getCode(),
                                        GuarPayStatusEnum.RefundFail.getCode()).eq(GuarInfo::getIsYzc, CommonStatusEnum.Yes.getCode()))
                        .or()
                        .eq(GuarInfo::getIsYzc, CommonStatusEnum.No.getCode()) //非优质采项目全部展示
        );

        queryWrapper.eq(StringUtils.isNotBlank(reqDto.getCompanyId()), GuarInfo::getCompanyId, reqDto.getCompanyId()); //采购人id
        queryWrapper.eq(StringUtils.isNotBlank(reqDto.getBidderId()), GuarInfo::getBidderId, reqDto.getBidderId()); //投标人id
        queryWrapper.eq(ObjectUtil.isNotNull(reqDto.getGuarTypeCode()), GuarInfo::getGuarTypeCode, reqDto.getGuarTypeCode()); //保函类型

        //项目编号模糊查询 包含项目名称
        if (StringUtils.isNotBlank(reqDto.getProjectNoLike())) {
            queryWrapper.and(wrapper -> wrapper.like(GuarInfo::getProjectNo, reqDto.getProjectNoLike())
                    .or()
                    .like(GuarInfo::getProjectName, reqDto.getProjectNoLike()));
        }

        //标的编号、保函编号、保函代码 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(reqDto.getBidderNameLike()), GuarInfo::getBidderName, reqDto.getBidderNameLike());
        queryWrapper.like(StringUtils.isNotBlank(reqDto.getGuaranteeNumberLike()), GuarInfo::getGuaranteeNumber, reqDto.getGuaranteeNumberLike());
        queryWrapper.like(StringUtils.isNotBlank(reqDto.getGuaranteeCodeLike()), GuarInfo::getGuaranteeCode, reqDto.getGuaranteeCodeLike());
        queryWrapper.eq(ObjectUtil.isNotNull(reqDto.getOpenStatus()), GuarInfo::getOpenStatus, reqDto.getOpenStatus()); //开函状态

        //投标人查询状态解析
        if (ObjectUtil.isNotNull(reqDto.getGuarStatusBidder())) {
            GuarStatusQueryBidderEnum guarStatusBidderEnum = EnumUtil.likeValueOf(GuarStatusQueryBidderEnum.class, reqDto.getGuarStatusBidder());
            switch (guarStatusBidderEnum) {
                case Auditing:  //出函状态为空 或 审核中
                    queryWrapper.and(wrapper -> wrapper.isNull(GuarInfo::getOpenStatus).or().eq(GuarInfo::getOpenStatus, GuarOpenStatusEnum.Auditing.getCode()));
                    break;
                case OpenSuccess:
                    queryWrapper.eq(GuarInfo::getOpenStatus, GuarOpenStatusEnum.Success.getCode());
                    break;
                case OpenFail:
                    queryWrapper.eq(GuarInfo::getOpenStatus, GuarOpenStatusEnum.Fail.getCode());
                    break;
                case Close:
                    queryWrapper.eq(GuarInfo::getCloseStatus, GuarCloseStatusEnum.SUCCESS.getCode());
                    break;
                case CloseAudit:
                    queryWrapper.eq(GuarInfo::getCloseStatus, GuarCloseStatusEnum.ING.getCode());
                    break;
                default:
                    break;
            }
        }

        //采购人查询状态解析
        if (ObjectUtil.isNotNull(reqDto.getGuarStatusPur())) {
            GuarStatusQueryPurEnum guarStatusPurEnum = EnumUtil.likeValueOf(GuarStatusQueryPurEnum.class, reqDto.getGuarStatusPur());
            switch (guarStatusPurEnum) {
                case Normal: //已出函
                    queryWrapper.eq(GuarInfo::getOpenStatus, GuarOpenStatusEnum.Success.getCode())
                            .and(wrapper -> wrapper.isNull(GuarInfo::getReleaseStatus).or().ne(GuarInfo::getReleaseStatus, CommonStatusEnum.Yes.getCode()))
                            .and(wrapper -> wrapper.isNull(GuarInfo::getCloseStatus).or().ne(GuarInfo::getCloseStatus, GuarCloseStatusEnum.SUCCESS.getCode()))
                            .and(wrapper -> wrapper.isNull(GuarInfo::getCompensateStatus)
                                    .or()
                                    .in(GuarInfo::getCompensateStatus, GuarCompensateStatusEnum.FAIL.getCode(), GuarCompensateStatusEnum.NOT_START.getCode()));
                    break;
                case Close:  //已退保
                    queryWrapper.eq(GuarInfo::getCloseStatus, GuarCloseStatusEnum.SUCCESS.getCode());
                    break;
                case Complaining: //索赔中
                    queryWrapper.eq(GuarInfo::getCompensateStatus, GuarCompensateStatusEnum.START.getCode());
                    break;
                case Complained:  //已索赔
                    queryWrapper.eq(GuarInfo::getCompensateStatus, GuarCompensateStatusEnum.SUCCESS.getCode());
                    break;
                default:
                    break;
            }

        }

        //默认按项目截止时间 倒序排列
        queryWrapper.orderByDesc(GuarInfo::getApplyTime).orderByDesc(GuarInfo::getCreateTime);

        Page<GuarInfo> page = new Page<>(reqDto.getCurrent(), reqDto.getSize());
        IPage<GuarInfo> respPage = super.page(page, queryWrapper);
        return PageResult.toPageResult(page, BeanUtil.copyToList(respPage.getRecords(), GuarInfoRespDto.class));
    }

    /**
     * 根据lgNo查询保函信息
     *
     * @param lgNo 保函申请编号
     * @return 保函信息
     */
    @Override
    public GuarInfoRespDto getByLgNo(String lgNo) {
        //根据申请编号查询保函数据
        LambdaQueryWrapper<GuarInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GuarInfo::getLgNo, lgNo);
        //删除状态为空或者不等于1
        queryWrapper.and(wrapper -> wrapper.ne(GuarInfo::getDelStatus, 1).or().isNull(GuarInfo::getDelStatus));

        List<GuarInfo> guarInfoList = this.list(queryWrapper);
        if (CollectionUtil.isNotEmpty(guarInfoList)) {
            GuarInfo guarInfo = guarInfoList.get(0);
            return BeanUtil.copyProperties(guarInfo, GuarInfoRespDto.class);
        }
        return null;
    }

    /**
     * 重置为暂存状态
     *
     * @param guarInfoId 保函申请信息ID
     * @return 是否成功
     */
    @Override
    public boolean resetToTemp(Long guarInfoId) {
        LambdaUpdateWrapper<GuarInfo> updateWrapper = new LambdaUpdateWrapper<GuarInfo>()
                .eq(GuarInfo::getGuarInfoId, guarInfoId)
                .set(GuarInfo::getApplyStatus, GuarApplyStatusEnum.Temp.getCode());
        return super.update(updateWrapper);
    }
}
