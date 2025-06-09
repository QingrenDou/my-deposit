package com.yzc.deposit.dao.deposit;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yzc.common.api.PageResult;
import com.yzc.common.api.Result;
import com.yzc.common.deposit.dto.api.UpdateInfoBySubAccReqDto;
import com.yzc.common.deposit.dto.deposit.ProjectSubAccPageReqDto;
import com.yzc.common.deposit.dto.deposit.ProjectSubAccRespDto;
import com.yzc.common.deposit.entity.ProjectSubAcc;

public interface IProjectSubAccDao extends IService<ProjectSubAcc> {

    /**
     * 分页查询
     * @param reqDto 请求参数
     * @return 查询结果
     */
    PageResult<ProjectSubAccRespDto> pageList(ProjectSubAccPageReqDto reqDto);


    /**
     * 根据子账号查询
     * @param inSubAcc 子账号
     * @return 查询结果
     */
    ProjectSubAccRespDto getBySubAcc(String inSubAcc);

    /**
     * 根据项目唯一码查询
     * @param uniqueCode 项目唯一识别码
     * @return 查询结果
     */
    ProjectSubAccRespDto getByUniqueCode(String uniqueCode);

    /**
     * 修改项目状态数据
     * @param reqDto 入参
     * @return 操作结果
     */
    Result<String> updateProjectStatus(UpdateInfoBySubAccReqDto reqDto);

    /**
     * 根据项目唯一编号修改合同状态
     * @param projectUniqueCode 项目唯一编号
     * @param contractStatus 合同状态
     * @return 操作结果
     */
    boolean updateContractStatusByUniqueCode(String projectUniqueCode, Integer contractStatus);
}
