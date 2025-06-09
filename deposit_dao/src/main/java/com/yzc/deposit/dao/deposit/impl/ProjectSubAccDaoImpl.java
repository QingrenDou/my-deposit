package com.yzc.deposit.dao.deposit.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.EnumUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yzc.common.api.PageResult;
import com.yzc.common.api.Result;
import com.yzc.common.util.date.DateTimeUtil;
import com.yzc.common.deposit.dto.api.UpdateInfoBySubAccReqDto;
import com.yzc.common.deposit.dto.deposit.ProjectSubAccPageReqDto;
import com.yzc.common.deposit.dto.deposit.ProjectSubAccRespDto;
import com.yzc.common.deposit.entity.ProjectSubAcc;
import com.yzc.common.deposit.enums.QueryPermissionTypeEnum;
import com.yzc.common.deposit.enums.UpdateProjectTypeAllEnum;
import com.yzc.deposit.dao.deposit.IProjectSubAccDao;
import com.yzc.deposit.repository.mapper.deposit.ProjectSubAccMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ProjectSubAccDaoImpl extends ServiceImpl<ProjectSubAccMapper, ProjectSubAcc> implements IProjectSubAccDao {

    @Resource
    private ProjectSubAccMapper projectSubAccMapper;


    /**
     * 分页查询
     *
     * @param reqDto 请求参数
     * @return 查询结果
     */
    @Override
    public PageResult<ProjectSubAccRespDto> pageList(ProjectSubAccPageReqDto reqDto) {
        LambdaQueryWrapper<ProjectSubAcc> queryWrapper = new LambdaQueryWrapper<>();

        //公司id ---必填项
        queryWrapper.eq(StringUtils.isNotBlank(reqDto.getCompanyId()), ProjectSubAcc::getCompanyId, reqDto.getCompanyId());
        queryWrapper.ge(ProjectSubAcc::getOpenBidTime, new Date()); //开标时间已过

        //企业额名称
        if (StringUtils.isNotBlank(reqDto.getProjectNameLike())) {
            queryWrapper.and(wrapper -> wrapper.like(ProjectSubAcc::getProjectName, reqDto.getProjectNameLike())
                    .or().like(ProjectSubAcc::getProjectCode, reqDto.getProjectNameLike()));
        }

        //开标时间_起
        if (StringUtils.isNotBlank(reqDto.getOpenBidTimeStart())) {
            queryWrapper.ge(ProjectSubAcc::getOpenBidTime, DateUtil.parse(reqDto.getOpenBidTimeStart() + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
        }

        //开标时间_止
        if (StringUtils.isNotBlank(reqDto.getOpenBidTimeEnd())) {
            queryWrapper.le(ProjectSubAcc::getOpenBidTime, DateUtil.parse(reqDto.getOpenBidTimeEnd() + " 23:59:59", "yyyy-MM-dd HH:mm:ss"));
        }

        //删除状态为空或者不等于1
        queryWrapper.and(wrapper -> wrapper.ne(ProjectSubAcc::getDelStatus, 1).or().isNull(ProjectSubAcc::getDelStatus));

        //权限
        QueryPermissionTypeEnum queryPermissionTypeEnum = EnumUtil.likeValueOf(QueryPermissionTypeEnum.class, reqDto.getQueryPermissionType());
        switch (queryPermissionTypeEnum) {
            case Dept:
                queryWrapper.in(ProjectSubAcc::getPurchaserId,reqDto.getDeptUserList());
                break;
            case User:
                queryWrapper.eq(ProjectSubAcc::getPurchaserId, reqDto.getCurUserId());
            default:
                break;
        }

        //最后排序
        queryWrapper.orderByDesc(ProjectSubAcc::getApplyTime);

        Page<ProjectSubAcc> page = new Page<>(reqDto.getCurrent(), reqDto.getSize());
        IPage<ProjectSubAcc> respPage = projectSubAccMapper.selectPage(page, queryWrapper);
        return PageResult.toPageResult(page, BeanUtil.copyToList(respPage.getRecords(), ProjectSubAccRespDto.class));
    }

    /**
     * 根据子账号查询
     *
     * @param inSubAcc 子账号
     * @return 查询结果
     */
    @Override
    public ProjectSubAccRespDto getBySubAcc(String inSubAcc) {
        if (StringUtils.isBlank(inSubAcc)) {
            return null;
        }
        LambdaQueryWrapper<ProjectSubAcc> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProjectSubAcc::getWholeSubAcc, inSubAcc);
        List<ProjectSubAcc> subAccs = projectSubAccMapper.selectList(queryWrapper);
        if (CollectionUtil.isEmpty(subAccs)) {
            return null;
        }

        return BeanUtil.copyProperties(subAccs.get(0), ProjectSubAccRespDto.class);
    }

    /**
     * 根据项目唯一码查询
     *
     * @param uniqueCode 项目唯一识别码
     * @return 查询结果
     */
    @Override
    public ProjectSubAccRespDto getByUniqueCode(String uniqueCode) {
        if (StringUtils.isBlank(uniqueCode)) {
            return null;
        }
        LambdaQueryWrapper<ProjectSubAcc> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProjectSubAcc::getProjectUniqueCode, uniqueCode);
        List<ProjectSubAcc> subAccs = projectSubAccMapper.selectList(queryWrapper);
        if (CollectionUtil.isEmpty(subAccs)) {
            return null;
        }

        return BeanUtil.copyProperties(subAccs.get(0), ProjectSubAccRespDto.class);
    }

    /**
     * 修改项目状态数据
     *
     * @param reqDto 入参
     * @return 操作结果
     */
    @Override
    public Result<String> updateProjectStatus(UpdateInfoBySubAccReqDto reqDto) {
        //使用hutool工具，将类型转为枚举
        if (StringUtils.isBlank(reqDto.getSubAcc()) || reqDto.getUpdateType() == null) {
            return Result.error("保证金账号和更新类型不能为空");
        }
        UpdateProjectTypeAllEnum updateProjectTypeAllEnum = EnumUtil.likeValueOf(UpdateProjectTypeAllEnum.class, reqDto.getUpdateType());

        LambdaUpdateWrapper<ProjectSubAcc> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ProjectSubAcc::getWholeSubAcc, reqDto.getSubAcc());
        updateWrapper.and(wrapper->wrapper.isNull(ProjectSubAcc::getDelStatus).or().ne(ProjectSubAcc::getDelStatus, 1));

        //按类型修改
        switch (updateProjectTypeAllEnum) {
            case OpenBidTime:
                if (StringUtils.isBlank(reqDto.getOpenBidTimeStr())) {
                    return Result.error("开标时间格式不正确");
                }
                Date openBidTime = DateUtil.parse(reqDto.getOpenBidTimeStr(), DateTimeUtil.LONG_TIME_FORMAT_WITH_SEC);
                updateWrapper.set(ProjectSubAcc::getOpenBidTime, openBidTime);
                break;
            case ProjectName:
                if (StringUtils.isBlank(reqDto.getProjectName())) {
                    return Result.error("项目名称不能为空");
                }
                updateWrapper.set(ProjectSubAcc::getProjectName, reqDto.getProjectName());
                break;
            case IsAbortive:
                if (reqDto.getIsAbortive() == null) {
                    return Result.error("异常状态不能为空");
                }
                updateWrapper.set(ProjectSubAcc::getIsAbortive, reqDto.getIsAbortive());
                break;
            case AutoBackMoney:
                if (reqDto.getIsAutoBackMoney() == null) {
                    return Result.error("自动退款状态不能为空");
                }
                updateWrapper.set(ProjectSubAcc::getIsAutoBackMoney, reqDto.getIsAutoBackMoney());
                break;
            case ConfirmBidder:
                Date confirmBidderTime = new Date();
                if (StringUtils.isNotBlank(reqDto.getConfirmBidderTimeStr())) {
                    confirmBidderTime = DateUtil.parse(reqDto.getConfirmBidderTimeStr(), DateTimeUtil.LONG_TIME_FORMAT_WITH_SEC);
                }
                updateWrapper.set(ProjectSubAcc::getConfirmBidderTime, confirmBidderTime);
                updateWrapper.set(ProjectSubAcc::getIsConfirmBidder, 1);
                break;
            case IsConfirmCandidate:
                Date confirmCandidateTime = new Date();
                if (StringUtils.isNotBlank(reqDto.getConfirmCandidateTimeStr())) {
                    confirmCandidateTime = DateUtil.parse(reqDto.getConfirmCandidateTimeStr(), DateTimeUtil.LONG_TIME_FORMAT_WITH_SEC);
                }
                updateWrapper.set(ProjectSubAcc::getConfirmCandidateTime, confirmCandidateTime);
                updateWrapper.set(ProjectSubAcc::getIsConfirmCandidate, 1);
                break;
            default:
                return Result.error("不支持的更新类型");
        }
        boolean updateRst = projectSubAccMapper.update(null, updateWrapper) > 0;
        return updateRst ? Result.success("修改成功") : Result.error("修改失败");
    }

    /**
     * 根据项目唯一编号修改合同状态
     *
     * @param projectUniqueCode 项目唯一编号
     * @param contractStatus    合同状态
     * @return 操作结果
     */
    @Override
    public boolean updateContractStatusByUniqueCode(String projectUniqueCode, Integer contractStatus) {
        LambdaUpdateWrapper<ProjectSubAcc> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ProjectSubAcc::getProjectUniqueCode, projectUniqueCode);
        updateWrapper.and(wrapper->wrapper.isNull(ProjectSubAcc::getDelStatus).or().ne(ProjectSubAcc::getDelStatus, 1));
        updateWrapper.set(ProjectSubAcc::getIsSignContract, contractStatus);
        return projectSubAccMapper.update(null, updateWrapper) > 0;
    }
}
