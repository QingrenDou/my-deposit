package com.yzc.deposit.service.deposit;

import com.yzc.common.api.Result;
import com.yzc.common.deposit.vo.*;

import java.util.List;

public interface IWFConfigService {

    /**
     * 获取工作流配置初始数据：子公司集合、启用子公司状态等信息
     * @return 初始数据
     */
    Result<WFConfigIndexRespVo> getWFConfigIndex();

    /**
     * 获取工作流配置列表
     * @param companyId 企业或子公司id
     * @return 工作流配置列表
     */
    Result<WFConfigListRespVo> getWFConfigList(String companyId);

    /**
     * 获取工作流配置详情
     * @param companyId 企业或子公司id
     * @param wfType 工作流类型
     * @return 工作流配置详情
     */
    Result<WFConfigDetailRespVo> getWFConfigDetail(String companyId, Integer wfType);

    /**
     * 保存工作流配置
     * @param reqVo 工作流配置详情
     * @return 保存结果
     */
    Result<String> saveWFConfig(WFConfigDetailSaveReqVo reqVo);

    /**
     * 修改工作流启用状态
     * @param wfChildCompanyEnable 工作流启用状态
     * @return 修改结果
     */
    Result<String> changeChildCompanyEnable(Integer wfChildCompanyEnable);

    /**
     * 修改工作流启用状态
     * @param reqVo 工作流启用状态
     * @return 修改结果
     */
    Result<String> changeUseStatus(WFChangeUseStatusReqVo reqVo);

    /**
     * 获取当前用户指定工作流类型的启用状态
     * @param wfType 工作流类型
     * @return 工作流启用状态
     */
    Result<String> getFlowEnabledStatus(Integer wfType);


    /**
     * 获取当前子公司指定工作流类型的流程配置
     * @param companyId 子公司id
     * @param wfType 工作流类型
     * @return 工作流配置集合
     */
    Result<List<WFInfoVo>> getFlowListByCompanyId(String companyId, Integer wfType);


    /**
     * 获取工作流审批流程url
     * @param flowId 工作流id
     * @param groupId 工作流组id
     * @return 工作流审批流程url
     */
    Result<String> getWorkFlowAuditProcessUrl(String flowId, String groupId);
}
