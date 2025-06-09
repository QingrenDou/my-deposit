package com.yzc.deposit.controller.deposit.api;

import com.yzc.common.api.Result;
import com.yzc.common.deposit.dto.api.*;
import com.yzc.deposit.service.deposit.IProjectSubAccService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 保证金系统公开业务接口
 */
@RestController
@RequestMapping("api/tendDepositApi")
public class TendDepositApiController {

    @Resource
    private IProjectSubAccService projectSubAccService;

    /**
     * 【YZC-TD-API-001】更新项目状态
     * @param reqDto 入参
     * @return 更新结果
     */
    @PostMapping("updateProjectStatus")
    public Result<String> updateProjectStatus(@RequestBody @Validated UpdateInfoBySubAccReqDto reqDto){
        return projectSubAccService.updateProjectStatus(reqDto);
    }

    /**
     * 【YZC-TD-API-002】批量更新项目状态
     * 注意：只要有一个失败，就会中断执行，且之前的更新会生效
     * @param reqDto 入参
     * @return 更新结果
     */
    @PostMapping("batchUpdateProjectStatus")
    public Result<String> batchUpdateProjectStatus(@RequestBody @Validated BatchUpdateProjectReqDto reqDto){
        return projectSubAccService.batchUpdateProjectStatus(reqDto);
    }

    /**
     * 【HBKY-TD-API-001】更新中标人服务费
     * @param reqDto 入参
     * @return 更新结果
     */
    @PostMapping("updateWinBidServiceFee")
    public Result<String> updateWinBidServiceFee(@RequestBody @Validated UpdateWinBidServiceFeeReqDto reqDto){
        return projectSubAccService.updateWinBidServiceFee(reqDto);
    }

    /****************************根据项目编号 更新数据**********************************************************/

    /**
     * 【TD-API-001】更新项目信息
     * 说明：用于项目管理系统对接
     * @param reqDto 入参
     * @return 更新结果
     */
    @PostMapping("updateProjectByPCode")
    public Result<String> updateProjectByPCode(@RequestBody @Validated UpdateProjectByPCodeReqDto reqDto){
        return projectSubAccService.updateProjectByPCode(reqDto);
    }

    /**
     * 【TD-API-004】更新合同状态
     * 说明：用于项目管理系统对接
     * @param reqDto 入参
     * @return 更新结果
     */
    @PostMapping("updateContractStatus")
    public Result<String> updateContractStatus(@RequestBody @Validated UpdateContractByPCodeReqDto reqDto){
        return projectSubAccService.updateContractStatus(reqDto);
    }
}
