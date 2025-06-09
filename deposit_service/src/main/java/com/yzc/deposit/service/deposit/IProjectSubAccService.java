package com.yzc.deposit.service.deposit;

import com.yzc.common.api.PageResult;
import com.yzc.common.api.Result;
import com.yzc.common.deposit.dto.api.*;
import com.yzc.common.deposit.dto.bank.common.ApplySubAccReqDto;
import com.yzc.common.deposit.dto.bank.common.ApplySubAccRespDto;
import com.yzc.common.deposit.vo.ProjectSubAccPageReqVo;
import com.yzc.common.deposit.vo.ProjectSubAccPageRespVo;

public interface IProjectSubAccService {

    /**
     * 项目分页查询
     * @param reqVo 请求参数
     * @return 查询结果
     */
    PageResult<ProjectSubAccPageRespVo> pageList(ProjectSubAccPageReqVo reqVo);

    /**
     * 测试申请子账户
     * @param reqDto 请求参数
     * @return 查询结果
     */
    Result<ApplySubAccRespDto> testApplySubAcc(ApplySubAccReqDto reqDto);

    /**
     * 更新项目状态
     * @param reqDto 请求参数
     * @return 操作结果
     */
    Result<String> updateProjectStatus(UpdateInfoBySubAccReqDto reqDto);

    /**
     * 批量更新项目状态
     * @param reqDto 请求参数
     * @return 操作结果
     */
    Result<String> batchUpdateProjectStatus(BatchUpdateProjectReqDto reqDto);

    /**********************按项目编号更新数据****************************************************************/

    /**
     * 更新合同状态
     * @param reqDto 请求参数
     * @return 操作结果
     */
    Result<String> updateContractStatus(UpdateContractByPCodeReqDto reqDto);

    /**
     * 按项目编号更新项目信息
     * @param reqDto 请求参数
     * @return 操作结果
     */
    Result<String> updateProjectByPCode(UpdateProjectByPCodeReqDto reqDto);

    /**
     * 按流水号更新中标人服务费
     * @param reqDto 请求参数
     * @return 操作结果
     */
    Result<String> updateWinBidServiceFee(UpdateWinBidServiceFeeReqDto reqDto);
}
