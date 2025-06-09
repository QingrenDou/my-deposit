package com.yzc.deposit.controller.deposit.business;

import com.yzc.common.api.Result;
import com.yzc.common.deposit.vo.*;
import com.yzc.deposit.service.deposit.IWFConfigService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 工作流配置
 */
@RestController
@RequestMapping(value = "/api/wfConfig")
public class WFConfigController {

    @Resource
    private IWFConfigService wfConfigService;

    /**
     * 【TD-Web-020】工作流配置-首页子公司信息接口
     *
     * @return 结果
     */
    @GetMapping(value = "getWFConfigIndex")
    public Result<WFConfigIndexRespVo> getWFConfigIndex() {
        return wfConfigService.getWFConfigIndex();
    }

    /**
     * 【TD-Web-021】工作流配置-获取工作流配置列表接口
     *
     * @param companyId 企业或子公司id
     * @return 结果
     */
    @GetMapping(value = "getWFConfigList")
    public Result<WFConfigListRespVo> getWFConfigList(@RequestParam @NotBlank String companyId) {
        return wfConfigService.getWFConfigList(companyId);
    }

    /**
     * 【TD-Web-022】工作流配置-获取工作流配置详情接口
     *
     * @param companyId 企业或子公司id
     * @param wfType    工作流类型
     * @return 结果
     */
    @GetMapping(value = "getWFConfigDetail")
    public Result<WFConfigDetailRespVo> getWFConfigDetail(@RequestParam @NotBlank String companyId,
                                                          @RequestParam @NotBlank Integer wfType) {
        return wfConfigService.getWFConfigDetail(companyId, wfType);
    }

    /**
     * 【TD-Web-023】工作流配置-保存工作流配置接口
     *
     * @param reqVo 入参
     * @return 结果
     */
    @PostMapping(value = "saveWFConfig")
    public Result<String> saveWFConfig(@RequestBody WFConfigDetailSaveReqVo reqVo) {
        return wfConfigService.saveWFConfig(reqVo);
    }

    /**
     * 【TD-Web-024】工作流配置-“是否启用子公司工作”流状态切换接口
     *
     * @param wfChildCompanyEnable 启用状态【1启用 0不启用】
     * @return 结果
     */
    @GetMapping(value = "changeChildCompanyEnable")
    public Result<String> changeChildCompanyEnable(@RequestParam @NotNull Integer wfChildCompanyEnable){
        return wfConfigService.changeChildCompanyEnable(wfChildCompanyEnable);
    }

    /**
     * 【TD-Web-025】工作流配置-启用、停用状态切换接口
     * @param reqVo
     * @return
     */
    @PostMapping(value = "changeWFUseStatus")
    public Result<String> changeWFUseStatus(@RequestBody WFChangeUseStatusReqVo reqVo) {
        return wfConfigService.changeUseStatus(reqVo);
    }

    /**
     * 【TD-Web-026】工作流业务集成-获取当前用户流程启用状态接口
     * @param wfType 工作流类型
     * @return Result<String> 1.启用 0.停用
     */
    @GetMapping("getFlowEnabledStatus")
    public Result<String> getFlowEnabledStatus(@RequestParam(value = "wfType",required = true) Integer wfType) {
        return wfConfigService.getFlowEnabledStatus(wfType);
    }

    /**
     * 【TD-Web-027】工作流业务集成-根据子公司加载待选流程列表
     * @param companyId 企业或子公司id
     * @param wfType 工作流类型
     * @return Result<List<WFInfoVo>>
     */
    @GetMapping("getFlowListByCompanyId")
    public Result<List<WFInfoVo>> getFlowListByCompanyId(@RequestParam(value = "companyId",required = true) String companyId
        , @RequestParam(value = "wfType",required = true) Integer wfType) {
        return wfConfigService.getFlowListByCompanyId(companyId,wfType);
    }

    /**
     * 【TD-Web-028】获取工作流审核步骤Url(淮矿版暂时用不上)
     * @param flowId 工作流id
     * @param groupId 工作流组id
     * @return Result<String>
     */
    @GetMapping("getWorkFlowAuditProcessUrl")
    public Result<String> getWorkFlowAuditProcessUrl(@RequestParam(value = "flowId") String flowId,
                                                     @RequestParam(value = "groupId") String groupId) {
        return wfConfigService.getWorkFlowAuditProcessUrl(flowId, groupId);
    }
}
