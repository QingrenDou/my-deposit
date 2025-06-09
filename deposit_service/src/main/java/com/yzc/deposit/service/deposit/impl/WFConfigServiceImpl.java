package com.yzc.deposit.service.deposit.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.yzc.common.api.Result;
import com.yzc.common.api.service.IOrganizationApiService;
import com.yzc.common.api.service.IWorkFlowService;
import com.yzc.common.deposit.dto.deposit.*;
import com.yzc.common.deposit.vo.*;
import com.yzc.common.domain.LoginUserInfo;
import com.yzc.common.dto.workflow.WorkFlowApiDto;
import com.yzc.common.model.OrganizeModel;
import com.yzc.common.util.StringUtil;
import com.yzc.common.deposit.enums.ConfigKeyEnum;
import com.yzc.common.deposit.enums.UseStatusEnum;
import com.yzc.deposit.dao.deposit.ICommonConfigDao;
import com.yzc.deposit.dao.deposit.IWFConfigDao;
import com.yzc.deposit.dao.deposit.IWFConfigDetailDao;
import com.yzc.deposit.dao.deposit.IWFTypeDao;
import com.yzc.deposit.service.AbstractBaseService;
import com.yzc.deposit.service.deposit.IWFConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 工作流配置
 */
@Slf4j
@Service
public class WFConfigServiceImpl extends AbstractBaseService implements IWFConfigService {

    @Resource
    private IWFConfigDao wfConfigDao;

    @Resource
    private IWFConfigDetailDao wfConfigDetailDao;

    @Resource
    private IWFTypeDao wfTypeDao;

    @Resource
    private ICommonConfigDao commonConfigDao;

    @Resource
    private IOrganizationApiService organizationApiService;

    @Resource
    @Qualifier(value = "workFlowEdgeService")
    private IWorkFlowService workFlowService;

    /**
     * 获取工作流配置初始数据：子公司集合、启用子公司状态等信息
     *
     * @return 初始数据
     */
    @Override
    public Result<WFConfigIndexRespVo> getWFConfigIndex() {
        WFConfigIndexRespVo respVo = new WFConfigIndexRespVo();

        //获取当前登录用户信息
        LoginUserInfo loginUserInfo = super.getLogInfo();

        //是否启用 子公司工作流
        String configKey = ConfigKeyEnum.WFChildCompanyEnable.toString() + "-" + loginUserInfo.getCompanyId().toLowerCase();
        String configValue = commonConfigDao.getCommonConfigByKey(configKey);
        //已经配置了1，则启用子公司工作流
        if (StringUtils.equals(configValue, "1")) {
            respVo.setWfChildCompanyEnable(1);
        }

        //定义公司集合
        List<WFChildCompanyVo> childCompanyList = new ArrayList<>();

        // 根据公司Id获取包含（总公司，子公司）的集合信息
        List<OrganizeModel> list = organizationApiService.getCompanyListByMainCompanyId(loginUserInfo.getCompanyId());
        if (CollUtil.isNotEmpty(list)) {
            list.forEach(item -> {
                WFChildCompanyVo childCompany = new WFChildCompanyVo();
                childCompany.setName(item.getName());

                // 判断当前用户是否是子公司
                if (!StringUtil.GUID_EMPTY.equals(item.getParentID())) {
                    //子公司--取id字段
                    childCompany.setId(item.getId());
                } else { //总公司--取companyId字段
                    childCompany.setId(item.getCompanyID());
                }
                childCompanyList.add(childCompany);
            });
        }
        respVo.setChildCompanyList(childCompanyList);
        return Result.success(respVo);
    }

    /**
     * 获取工作流配置列表
     *
     * @param companyId 企业或子公司id
     * @return 工作流配置列表
     */
    @Override
    public Result<WFConfigListRespVo> getWFConfigList(String companyId) {
        //获取所有流程集合
        List<WFTypeRespDto> wfTypeList = wfTypeDao.getAllList();

        //获取当前公司所有配置的流程集合
        List<WFConfigRespDto> wfConfigList = wfConfigDao.getListByCompanyId(companyId);
        if (CollectionUtil.isEmpty(wfTypeList)) {
            return Result.error("暂无流程");
        }

        //定义结果集
        List<WFConfigVo> configVoList = new ArrayList<>();

        //循环类型，设置绑定数据
        wfTypeList.forEach(item -> {
            WFConfigVo vo = new WFConfigVo();
            vo.setWfType(item.getWfType());
            vo.setWfTypeName(item.getWfTypeName());

            //绑定信息
            if (CollectionUtil.isNotEmpty(wfConfigList)) {
                wfConfigList.stream().filter(config -> ObjectUtil.equal(config.getWfType(),item.getWfType()))
                        .findFirst()
                        .ifPresent(config -> {
                            vo.setWfConfigId(config.getWfConfigId());
                            vo.setStartStatus(config.getStartStatus());
                            vo.setCompanyId(config.getCompanyId());
                            vo.setUpdateUserId(config.getUpdateUserId());
                            vo.setUpdateUserName(config.getUpdateUserName());
                            vo.setUpdateTime(config.getUpdateTime());
                        });
            }
            configVoList.add(vo);
        });


        return Result.success(configVoList);
    }

    /**
     * 获取工作流配置详情
     *
     * @param companyId 企业或子公司id
     * @param wfType    工作流类型
     * @return 工作流配置详情
     */
    @Override
    public Result<WFConfigDetailRespVo> getWFConfigDetail(String companyId, Integer wfType) {
        WFConfigDetailRespVo respVo = new WFConfigDetailRespVo();

        //获取当前登录用户信息
        LoginUserInfo loginUserInfo = super.getLogInfo();

        //获取配置信息，启用状态
        WFConfigRespDto wfConfigRespDto = wfConfigDao.getByCompanyIdAndWfType(companyId, wfType);
        if(ObjectUtil.isNotNull(wfConfigRespDto)){
            respVo.setStartStatus(wfConfigRespDto.getStartStatus());
        }

        //获取已配置流程集合
        List<WFConfigDetailRespDto> configDetailList = wfConfigDetailDao.getListByCompanyIdAndType(companyId, wfType);
        if(CollectionUtil.isNotEmpty(configDetailList)){
            respVo.setConfigFlowInfoList(BeanUtil.copyToList(configDetailList, WFInfoVo.class));
        }

        //所有已配置流程集合
        List<WFInfoVo> allFlowInfoList = new ArrayList<>();
        //获取当前公司所有流程集合
        List<WorkFlowApiDto> allFlowList = workFlowService.getWorkflowByUserId(loginUserInfo.getCompanyId());
        if(CollectionUtil.isNotEmpty(allFlowList)){

            allFlowList.stream().forEach(item -> {
                WFInfoVo vo = new WFInfoVo();
                vo.setFlowId(item.getId());
                vo.setFlowName(item.getName());
                allFlowInfoList.add(vo);
            });
        }
        respVo.setAllFlowInfoList(allFlowInfoList);

        return Result.success(respVo);
    }

    /**
     * 保存工作流配置
     *
     * @param reqVo 工作流配置详情
     * @return 保存结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> saveWFConfig(WFConfigDetailSaveReqVo reqVo) {
        String logStr = "保存工作流配置";
        log.info("{}=====>1.工作流提交,入参:{}",logStr, JSONUtil.toJsonStr(reqVo));
        //获取当前登录用户信息
        LoginUserInfo loginUserInfo = super.getLogInfo();

        //先查询是否已经配置
        WFConfigRespDto wfConfigRespDto = wfConfigDao.getByCompanyIdAndWfType(reqVo.getCompanyId(), reqVo.getWfType());

        //已存在，则修改
        if(ObjectUtil.isNotNull(wfConfigRespDto)){
            WFConfigSaveOrUpdateReqDto updateConfigReqDto = BeanUtil.copyProperties(wfConfigRespDto, WFConfigSaveOrUpdateReqDto.class);
            updateConfigReqDto.setStartStatus(reqVo.getStartStatus());
            updateConfigReqDto.setUpdateUserId(loginUserInfo.getUserId());
            updateConfigReqDto.setUpdateUserName(loginUserInfo.getUserName());
            updateConfigReqDto.setUpdateTime(new Date());
            boolean updateRst = wfConfigDao.saveOrUpdate(updateConfigReqDto);
            log.info("{}=====>2.1.工作流配置已存在,企业id={},流程={},操作结果={}",logStr,reqVo.getCompanyId(), reqVo.getWfType(), updateRst);
            if(!updateRst){
                return Result.error("修改流程配置失败");
            }
        }else{ //不存在，则新增
            WFConfigSaveOrUpdateReqDto saveConfigReqDto = new WFConfigSaveOrUpdateReqDto();
            //采用雪花算法id
            saveConfigReqDto.setWfConfigId(new IdWorker().getId());
            saveConfigReqDto.setCompanyId(reqVo.getCompanyId());
            saveConfigReqDto.setWfType(reqVo.getWfType());
            saveConfigReqDto.setStartStatus(reqVo.getStartStatus());
            saveConfigReqDto.setUpdateUserId(loginUserInfo.getUserId());
            saveConfigReqDto.setUpdateUserName(loginUserInfo.getUserName());
            saveConfigReqDto.setUpdateTime(new Date());
            boolean saveRst = wfConfigDao.saveOrUpdate(saveConfigReqDto);
            log.info("{}=====>2.2.工作流配置不存在,企业id={},流程={},操作结果:{}",logStr,reqVo.getCompanyId(), reqVo.getWfType(), saveRst);
            if(!saveRst){
                return Result.error("保存流程配置失败");
            }
        }

        //删除已配置流程
        boolean deleteDetailRst = wfConfigDetailDao.deleteByCompanyIdAndType(reqVo.getCompanyId(), reqVo.getWfType());
        log.info("{}=====>3.删除已配置流程完成,企业id={},流程={},操作结果:{},注：删除失败可能是原来没有数据",logStr,reqVo.getCompanyId(), reqVo.getWfType(), deleteDetailRst);

        //已选流程转为保存对象
        List<WFConfigDetailSaveReqDto> detailSaveList = new ArrayList<>();
        reqVo.getChooseFlowInfoList().forEach(item -> {
            WFConfigDetailSaveReqDto saveReqDto = new WFConfigDetailSaveReqDto();
            saveReqDto.setWfConfigDetailId(new IdWorker().getId());
            saveReqDto.setCompanyId(reqVo.getCompanyId());
            saveReqDto.setWfType(reqVo.getWfType());
            saveReqDto.setFlowId(item.getFlowId());
            saveReqDto.setFlowName(item.getFlowName());
            saveReqDto.setUpdateTime(new Date());
            detailSaveList.add(saveReqDto);
        });

        //重新保存已配置流程
        boolean saveDetailRst = wfConfigDetailDao.saveList(detailSaveList);
        log.info("{}=====>4.批量保存已配置流程完成,企业id={},流程={},操作结果:{}",logStr,reqVo.getCompanyId(), reqVo.getWfType(), saveDetailRst);
        if(!saveDetailRst){
            return Result.error("更新流程设置失败");
        }

        return Result.success("保存成功");
    }

    /**
     * 修改工作流启用状态
     *
     * @param wfChildCompanyEnable 是否启用子公司工作流状态
     * @return 修改结果
     */
    @Override
    public Result<String> changeChildCompanyEnable(Integer wfChildCompanyEnable) {
        //获取当前登录用户信息
        LoginUserInfo loginUserInfo = super.getLogInfo();
        String key = ConfigKeyEnum.WFChildCompanyEnable.toString() + "-" + loginUserInfo.getCompanyId().toLowerCase();
        boolean rst = commonConfigDao.saveOrUpdateCommonConfigByKey(key, wfChildCompanyEnable.toString());
        return rst ? Result.success("修改成功") : Result.error("修改失败");
    }

    /**
     * 修改工作流启用状态
     *
     * @param reqVo 工作流启用状态
     * @return 修改结果
     */
    @Override
    public Result<String> changeUseStatus(WFChangeUseStatusReqVo reqVo) {
        //获取已配置流程
        WFConfigRespDto configRespDto = wfConfigDao.getByCompanyIdAndWfType(reqVo.getCompanyId(), reqVo.getWfType());
        if(ObjectUtil.isNull(configRespDto)){
            return Result.error("请先配置流程");
        }
        //转为修改对象
        WFConfigSaveOrUpdateReqDto updateReqDto = BeanUtil.copyProperties(configRespDto, WFConfigSaveOrUpdateReqDto.class);
        updateReqDto.setStartStatus(reqVo.getStartStatus());
        return wfConfigDao.saveOrUpdate(updateReqDto) ? Result.success("修改成功") : Result.error("修改失败");
    }

    /**
     * 获取当前用户指定工作流类型的启用状态
     *
     * @param wfType 工作流类型
     * @return 工作流启用状态
     */
    @Override
    public Result<String> getFlowEnabledStatus(Integer wfType) {
        //获取当前登录用户信息
        LoginUserInfo loginUserInfo = super.getLogInfo();

        //是否启用子公司工作流
        String configKey = ConfigKeyEnum.WFChildCompanyEnable.toString() + "-" + loginUserInfo.getCompanyId().toLowerCase();
        String configValue = commonConfigDao.getCommonConfigByKey(configKey);

        List<String> childCompanyIdList = new ArrayList<>();

        //已经配置了1，启用子公司工作流
        if (StringUtils.equals(configValue, "1")) {
            // 根据用户ID获取用户所属子公司对象
            List<OrganizeModel> organizeModelList = organizationApiService.getCompanyDepListByUserId(loginUserInfo.getUserId(), 1);

            //如果未获取到子公司信息，则返回错误信息
            if(CollectionUtil.isEmpty(organizeModelList)){
                log.info("{}=====>获取子公司信息失败,用户id={},用户名称={},子公司为空,改用企业id查询","getFlowEnabledStatus",loginUserInfo.getUserId(),loginUserInfo.getUserName());
                childCompanyIdList.add(loginUserInfo.getCompanyId());
            }

            //所有子公司集合
            childCompanyIdList.addAll(organizeModelList.stream().map(item -> item.getId()).collect(Collectors.toList()));
        }else{
            childCompanyIdList.add(loginUserInfo.getCompanyId());
        }

        //查询已启用配置集合
        List<WFConfigRespDto> configList = wfConfigDao.getListByCompanyIdList(childCompanyIdList,wfType, UseStatusEnum.Normal.getCode());

        //有数据即为启用
        return Result.success("操作成功",CollectionUtil.isNotEmpty(configList) && configList.size() > 0 ? "1" : "0");
    }

    /**
     * 获取当前子公司指定工作流类型的流程配置
     *
     * @param companyId 子公司id
     * @param wfType    工作流类型
     * @return 工作流配置集合
     */
    @Override
    public Result<List<WFInfoVo>> getFlowListByCompanyId(String companyId, Integer wfType) {
        //获取当前子公司id工作流配置信息
        WFConfigRespDto configRespDto = wfConfigDao.getByCompanyIdAndWfType(companyId, wfType);
        //未配置 或未启用
        if(ObjectUtil.isNull(configRespDto) || !ObjectUtil.equal(configRespDto.getStartStatus(),UseStatusEnum.Normal.getCode())){
            return Result.success(new ArrayList<>());
        }

        //子公司工作流已启动，开始获取已配置流程
        List<WFConfigDetailRespDto> detailList = wfConfigDetailDao.getListByCompanyIdAndType(companyId,wfType);

        return Result.success(BeanUtil.copyToList(detailList,WFInfoVo.class));
    }

    /**
     * 获取工作流审批流程url
     *
     * @param flowId  工作流id
     * @param groupId 工作流组id
     * @return 工作流审批流程url
     */
    @Override
    public Result<String> getWorkFlowAuditProcessUrl(String flowId, String groupId) {
        //获取当前登录用户信息
        LoginUserInfo loginUserInfo = super.getLogInfo();
        String auditProcessHtml = workFlowService.getAuditProcessHtml(flowId, groupId, loginUserInfo.getUserId());
        if (StrUtil.isNotBlank(auditProcessHtml)) {
            return Result.success("获取工作流审核进度成功", auditProcessHtml);
        }
        return Result.error("获取工作流审核进度失败");
    }
}
