package com.yzc.deposit.service.deposit.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yzc.common.api.PageResult;
import com.yzc.common.api.Result;
import com.yzc.common.api.service.IOrganizationApiService;
import com.yzc.common.common.enums.MSBusinessTypeEnum;
import com.yzc.common.deposit.dto.api.*;
import com.yzc.common.deposit.enums.*;
import com.yzc.common.domain.LoginUserInfo;
import com.yzc.common.model.UfUnifiedUserModel;
import com.yzc.common.deposit.dto.bank.common.ApplySubAccReqDto;
import com.yzc.common.deposit.dto.bank.common.ApplySubAccRespDto;
import com.yzc.common.deposit.dto.deposit.InAccRecordCountRespDto;
import com.yzc.common.deposit.dto.deposit.InAccRecordRespDto;
import com.yzc.common.deposit.dto.deposit.ProjectSubAccPageReqDto;
import com.yzc.common.deposit.dto.deposit.ProjectSubAccRespDto;
import com.yzc.common.deposit.entity.BindRecord;
import com.yzc.common.deposit.entity.InAccRecord;
import com.yzc.common.deposit.entity.ProjectSubAcc;
import com.yzc.common.deposit.util.DepositUtil;
import com.yzc.common.deposit.vo.ProjectSubAccPageReqVo;
import com.yzc.common.deposit.vo.ProjectSubAccPageRespVo;
import com.yzc.deposit.dao.deposit.IBindRecordDao;
import com.yzc.deposit.dao.deposit.IInAccRecordDao;
import com.yzc.deposit.dao.deposit.ILogDao;
import com.yzc.deposit.dao.deposit.IProjectSubAccDao;
import com.yzc.deposit.service.AbstractBaseService;
import com.yzc.deposit.service.bank.BankAdapterConfig;
import com.yzc.deposit.service.deposit.IProjectSubAccService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProjectSubAccServiceImpl extends AbstractBaseService implements IProjectSubAccService {

    @Resource
    private IProjectSubAccDao projectSubAccDao;
    @Resource
    private IInAccRecordDao inAccRecordDao;
    @Resource
    private IBindRecordDao bindRecordDao;

    @Resource
    private BankAdapterConfig bankAdapterConfig;

    @Resource
    private ILogDao logDao;

    @Resource
    private Environment env;

    @Resource
    private IOrganizationApiService organizationApiService;

    /**
     * 项目分页查询
     *
     * @param reqVo 请求参数
     * @return 查询结果
     */
    @Override
    public PageResult<ProjectSubAccPageRespVo> pageList(ProjectSubAccPageReqVo reqVo) {
        ProjectSubAccPageReqDto reqDto = BeanUtil.copyProperties(reqVo, ProjectSubAccPageReqDto.class);

        //获取当前登录用户信息
        LoginUserInfo loginUserInfo = super.getLogInfo();
        reqDto.setCompanyId(loginUserInfo.getCompanyId()); //当前企业id
        reqDto.setCurUserId(loginUserInfo.getUserId()); //当前用户id

        String deptAuthCode = env.getProperty("deposit.deptAuthCode"); //查看本部门（权限）
        String companyAuthCode = env.getProperty("deposit.companyAuthCode"); //查看本公司（权限）

        //设置查询权限类型
        if(StringUtils.isNotBlank(companyAuthCode) && super.checkAuthor(Integer.parseInt(companyAuthCode))){
            //拥有本公司权限
            reqDto.setQueryPermissionType(QueryPermissionTypeEnum.Company.getCode());
        }else if(StringUtils.isBlank(deptAuthCode) && super.checkAuthor(Integer.parseInt(deptAuthCode))){
            //拥有部门权限
            reqDto.setQueryPermissionType(QueryPermissionTypeEnum.Dept.getCode());

            //获取当前用户部门下的所有用户id
            List<String> deptUserList = new ArrayList<>();
            //查询当前用户部门下的所有用户id
            List<UfUnifiedUserModel> userList = organizationApiService.getDeptAllUserByUserId(loginUserInfo.getUserId());
            if(CollectionUtil.isEmpty(userList)){ //如果部门为空，仅查询当前用户id
                deptUserList.add(loginUserInfo.getUserId());
            }else{
                //循环用户id集合
                for (UfUnifiedUserModel user : userList) {
                    deptUserList.add(user.getId());
                }
            }
            reqDto.setDeptUserList(deptUserList);
        }else{
            //默认个人权限
            reqDto.setQueryPermissionType(QueryPermissionTypeEnum.User.getCode());
        }
        log.info("保证金项目查询pageList权限处理结果===>：当前用户名={},查询参数={}",loginUserInfo.getUserName(), JSONUtil.toJsonStr(reqDto));
        PageResult<ProjectSubAccRespDto> pageDtoResult = projectSubAccDao.pageList(reqDto);
        if (pageDtoResult == null) {
            return null;
        }

        //结果对象转换
        PageResult<ProjectSubAccPageRespVo> pageVoResult = BeanUtil.copyProperties(pageDtoResult, PageResult.class);

        //特殊数据处理
        if (CollectionUtil.isNotEmpty(pageVoResult.getRecords())) {
            for (ProjectSubAccPageRespVo respVo : pageVoResult.getRecords()) {

                //统计数量
                InAccRecordCountRespDto countRespDto = inAccRecordDao.countBySubAcc(respVo.getWholeSubAcc());
                if (countRespDto != null) {
                    respVo.setInRecordCount(countRespDto.getInRecordCount());
                    respVo.setRefundRecordCount(countRespDto.getRefundRecordCount());
                }

                //业务类型翻译,依据枚举：MSBusinessTypeEnum
                respVo.setBusinessTypeStr(MSBusinessTypeEnum.getTextByCode(respVo.getBusinessType()));

            }
        }
        return pageVoResult;
    }

    /**
     * 测试申请子账户
     *
     * @param reqDto 请求参数
     * @return 查询结果
     */
    @Override
    public Result<ApplySubAccRespDto> testApplySubAcc(ApplySubAccReqDto reqDto) {
        return bankAdapterConfig.applySubAcc(reqDto);
    }

    /**
     * 更新项目状态
     *
     * @param reqDto 请求参数
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> updateProjectStatus(UpdateInfoBySubAccReqDto reqDto) {
        Result updateResult;

        UpdateProjectTypeAllEnum updateProjectTypeAllEnum = EnumUtil.likeValueOf(UpdateProjectTypeAllEnum.class, reqDto.getUpdateType());
        switch (updateProjectTypeAllEnum) {
            case OpenBidTime:
            case ConfirmBidder:
            case ProjectName:
            case AutoBackMoney:
            case IsAbortive:
            case IsConfirmCandidate:
                updateResult = projectSubAccDao.updateProjectStatus(reqDto); //更新项目类状态
                break;
            case WinBidderList:
            case WinBidderWithFeeList:
            case WinCandidateList:
            case SyncBidderList:
                updateResult = inAccRecordDao.updateRecordStatus(reqDto); //更新来款记录类状态
                break;
            default:
                updateResult = Result.error("更新项目状态失败：未知更新类型");
                break;
        }

        //记入日志
        logDao.saveLog(reqDto.getSubAcc(), "入参：" + JSONUtil.toJsonStr(reqDto) + "；结果：" + updateResult.isSuccess());

        if (!updateResult.isSuccess()) {
            return updateResult;
        }

        /************************特殊处理***************************/

        //项目异常时，重置中标人和候选人状态
        if (ObjectUtil.equal(reqDto.getUpdateType(), UpdateProjectTypeAllEnum.IsAbortive.getCode())
                && ObjectUtil.equal(reqDto.getIsAbortive(), 1)) {
            //项目异常时，清除中标人和候选人状态
            inAccRecordDao.clearWinStatusBySubAcc(reqDto.getSubAcc());
        }

        return updateResult;
    }

    /**
     * 批量更新项目状态
     *
     * @param reqDto 请求参数
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> batchUpdateProjectStatus(BatchUpdateProjectReqDto reqDto) {
        if (CollectionUtil.isEmpty(reqDto.getUpdateList())) {
            return Result.error("更新数据集合不能为空");
        }

        //只要有一个失败，就会中断执行，且之前的更新会生效
        for (UpdateInfoBySubAccReqDto updateInfoBySubAccReqDto : reqDto.getUpdateList()) {
            Result<String> updateResult = updateProjectStatus(updateInfoBySubAccReqDto);
            if (!updateResult.isSuccess()) {
                return updateResult;
            }
        }

        return Result.success();
    }

    /**********************按项目编号更新数据****************************************************************/

    /**
     * 更新合同状态
     *
     * @param reqDto 请求参数
     * @return 操作结果
     */
    @Override
    public Result<String> updateContractStatus(UpdateContractByPCodeReqDto reqDto) {
        //生成项目唯一标识
        String projectUniqueCode = DepositUtil.getProjectUniqueCode(reqDto.getProjectCode(), reqDto.getBusinessType());
        Integer contractStatus = ObjectUtil.isNull(reqDto.getContractStatus()) ? 1 : reqDto.getContractStatus();

        //更新合同状态
        boolean updateRst = projectSubAccDao.updateContractStatusByUniqueCode(projectUniqueCode, contractStatus);;

        logDao.saveLog(projectUniqueCode,"修改合同信息,入参="+JSONUtil.toJsonStr(reqDto)+",结果="+updateRst);
        return Result.success();
    }

    /**
     * 按项目编号更新项目信息
     *
     * @param reqDto 请求参数
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> updateProjectByPCode(UpdateProjectByPCodeReqDto reqDto) {
        //生成项目唯一标识
        String projectUniqueCode = DepositUtil.getProjectUniqueCode(reqDto.getProjectCode(), reqDto.getBusinessType());

        //定义最终结果
        boolean updateRst = false;

        //更新项目信息
        LambdaUpdateWrapper<ProjectSubAcc> updateWrapper = new LambdaUpdateWrapper<>();

        //获取已绑定的来款记录集合
        LambdaQueryWrapper<BindRecord> bindRecordQueryWrapper = new LambdaQueryWrapper<>();
        bindRecordQueryWrapper.eq(BindRecord::getProjectUniqueCode, projectUniqueCode);
        bindRecordQueryWrapper.eq(BindRecord::getBindStatus, BindStatusEnum.Normal.getCode());
        List<BindRecord> bindRecordList = bindRecordDao.list(bindRecordQueryWrapper);

        //来款记录id集合
        List<String> inAccRecordIdList = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(bindRecordList)) {
            inAccRecordIdList = bindRecordList.stream().map(BindRecord::getInAccRecordId).collect(Collectors.toList());
        }

        UpdateProjectTypePCodeEnum updateProjectTypePCodeEnum = EnumUtil.likeValueOf(UpdateProjectTypePCodeEnum.class, reqDto.getUpdateType());
        switch (updateProjectTypePCodeEnum) {
            case OpenBidTime:
                if(StringUtils.isBlank(reqDto.getOpenBidTimeStr())){
                    return Result.error("开标时间不能为空");
                }

                updateWrapper.eq(ProjectSubAcc::getProjectUniqueCode, projectUniqueCode)
                        .set(ProjectSubAcc::getOpenBidTime, DateUtil.parse(reqDto.getOpenBidTimeStr(), "yyyy-MM-dd HH:mm:ss"));
                updateRst = projectSubAccDao.update(updateWrapper);
                break;
            case IsAbortive:
                if(ObjectUtil.isNull(reqDto.getIsAbortive())){
                    return Result.error("异常状态不能为空");
                }
                updateWrapper.eq(ProjectSubAcc::getProjectUniqueCode, projectUniqueCode)
                        .set(ProjectSubAcc::getIsAbortive, reqDto.getIsAbortive());
                updateRst = projectSubAccDao.update(updateWrapper);

                //项目异常时，清除中标人和候选人状态
                if(ObjectUtil.equals(reqDto.getIsAbortive(),1) && updateRst){

                    //批量修改来款记录对应的中标人、候选人状态
                    LambdaUpdateWrapper<InAccRecord> inAccRecordUpdateWrapper = new LambdaUpdateWrapper<>();
                    inAccRecordUpdateWrapper.in(InAccRecord::getInAccRecordId, inAccRecordIdList)
                            .set(InAccRecord::getIsWinBidder, null)
                            .set(InAccRecord::getIsCandidate, null);
                    inAccRecordDao.update(inAccRecordUpdateWrapper);
                }
                break;
            case ConfirmBidder:
                //条件判断
                if(CollectionUtil.isEmpty(reqDto.getBidderWithFeeList())){
                    return Result.error("中标人信息不能为空");
                }

                //确定时间
                Date confirmBidderTime;
                if(StringUtils.isBlank(reqDto.getConfirmTimeStr())){
                    confirmBidderTime = new Date();
                }else{
                    confirmBidderTime = DateUtil.parse(reqDto.getConfirmTimeStr(), "yyyy-MM-dd HH:mm:ss");
                }

                //修改项目成交状态和时间
                updateWrapper.eq(ProjectSubAcc::getProjectUniqueCode, projectUniqueCode)
                        .set(ProjectSubAcc::getIsConfirmBidder, 1)
                        .set(ProjectSubAcc::getConfirmBidderTime, confirmBidderTime);
                updateRst = projectSubAccDao.update(updateWrapper);

                if(updateRst){
                    //还原项目绑定的来款记录对应的中标人状态
                    LambdaUpdateWrapper<InAccRecord> resetWrapper = new LambdaUpdateWrapper<>();
                    resetWrapper.in(InAccRecord::getInAccRecordId, inAccRecordIdList)
                            //仅还原未退款数据
                            .and(wrapper->wrapper.isNull(InAccRecord::getAuditStatus).or().ne(InAccRecord::getAuditStatus, TDAuditStatusEnum.Pass.getCode()))
                            .set(InAccRecord::getWinAmount,null)
                            .set(InAccRecord::getIsWinBidder, null);
                    inAccRecordDao.update(resetWrapper);

                    //修改项目绑定的来款记录对应的中标人
                    reqDto.getBidderWithFeeList().stream().forEach(item->{
                        LambdaUpdateWrapper<InAccRecord> updateInAccRecordWrapper = new LambdaUpdateWrapper<>();
                        updateInAccRecordWrapper.eq(InAccRecord::getBankSeqNo, item.getBankSeqNo())
                                .set(InAccRecord::getIsWinBidder, 1)
                                .set(InAccRecord::getWinAmount, item.getWinAmount());
                        inAccRecordDao.update(updateInAccRecordWrapper);
                    });
                }

                break;
            case ConfirmCandidate:
                //条件判断
                if(CollectionUtil.isEmpty(reqDto.getBidderWithFeeList())){
                    return Result.error("候选人信息不能为空");
                }

                //确定候选人时间
                Date confirmCandidateTime;
                if(StringUtils.isBlank(reqDto.getConfirmTimeStr())){
                    confirmCandidateTime = new Date();
                }else{
                    confirmCandidateTime = DateUtil.parse(reqDto.getConfirmTimeStr(), "yyyy-MM-dd HH:mm:ss");
                }

                //修改项目候选人状态和时间
                updateWrapper.eq(ProjectSubAcc::getProjectUniqueCode, projectUniqueCode)
                        .set(ProjectSubAcc::getIsConfirmCandidate, 1)
                        .set(ProjectSubAcc::getConfirmCandidateTime, confirmCandidateTime);
                updateRst = projectSubAccDao.update(updateWrapper);

                if(updateRst){
                    //还原项目绑定的来款记录对应的候选人状态
                    LambdaUpdateWrapper<InAccRecord> resetWrapper = new LambdaUpdateWrapper<>();
                    resetWrapper.in(InAccRecord::getInAccRecordId, inAccRecordIdList)
                            //仅还原未退款数据
                            .and(wrapper->wrapper.isNull(InAccRecord::getAuditStatus).or().ne(InAccRecord::getAuditStatus, TDAuditStatusEnum.Pass.getCode()))
                            .set(InAccRecord::getIsCandidate, null);
                    inAccRecordDao.update(resetWrapper);

                    //新的候选人流水号集合
                    List<String> bankSeqNoList = reqDto.getBidderWithFeeList().stream().map(item->item.getBankSeqNo()).collect(Collectors.toList());

                    //根据流水号 批量更新新的候选人
                    LambdaUpdateWrapper<InAccRecord> updateInAccRecordWrapper = new LambdaUpdateWrapper<>();
                    updateInAccRecordWrapper.in(InAccRecord::getBankSeqNo, bankSeqNoList)
                            .set(InAccRecord::getIsCandidate, 1);
                    inAccRecordDao.update(updateInAccRecordWrapper);
                }

                break;
            default:
                break;
        }

        logDao.saveLog(projectUniqueCode,"按编号更新信息,入参="+JSONUtil.toJsonStr(reqDto)+",结果="+updateRst);
        return updateRst ? Result.success() : Result.error("更新失败");
    }

    /**
     * 按流水号更新中标人服务费
     *
     * @param reqDto 请求参数
     * @return 操作结果
     */
    @Override
    public Result<String> updateWinBidServiceFee(UpdateWinBidServiceFeeReqDto reqDto) {
        String logCode = "更新中标服务费===>";
        log.info("{}准备处理，流水号={}", logCode, reqDto.getBankSeqNo());
        //根据流水号获取来款记录
        InAccRecordRespDto inAccRecord = inAccRecordDao.getByBankSeqNo(reqDto.getSubAcc(),reqDto.getBankSeqNo());
        if(ObjectUtil.isNull(inAccRecord)){
            log.info("{}流水号对应的来款信息不存在,不予处理,流水号={}", logCode, reqDto.getBankSeqNo());
            return Result.error("流水号对应的来款信息不存在");
        }

        //判断是否已经发起退款，已发起退款则不予处理
        if(ObjectUtil.isNotNull(inAccRecord.getBackOperType())){
            log.info("{}流水号对应的来款信息已发起过退款,不予处理,流水号={}", logCode, reqDto.getBankSeqNo());
            return Result.error("流水号对应的来款信息已发起退款");
        }

        //根据流水号 修改中标服务费
        LambdaUpdateWrapper<InAccRecord> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(InAccRecord::getInAccRecordId, inAccRecord.getInAccRecordId())
                .set(InAccRecord::getFeeMoneyStr, reqDto.getFeeMoneyStr());

        boolean rst = inAccRecordDao.update(updateWrapper);
        log.info("{}处理完毕，流水号={}，结果={}", logCode, reqDto.getBankSeqNo(), rst);
        return rst ? Result.success() : Result.error("更新失败");
    }
}
