package com.yzc.deposit.dao.guar;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yzc.common.api.PageResult;
import com.yzc.common.guar.dto.GuarInfoPageReqDto;
import com.yzc.common.guar.dto.GuarInfoRespDto;
import com.yzc.common.guar.entity.GuarInfo;

/**
 * <p>
 * 保函申请信息 服务类
 * </p>
 *
 * @author DouQingRen
 * @since 2025-04-07
 */
public interface IGuarInfoDao extends IService<GuarInfo> {

    /**
     * 保函申请信息分页列表
     * @param reqDto 请求参数
     * @return 分页列表
     */
    PageResult<GuarInfoRespDto> pageList(GuarInfoPageReqDto reqDto);

    /**
     * 根据lgNo查询保函信息
     * @param lgNo 保函申请编号
     * @return 保函信息
     */
    GuarInfoRespDto getByLgNo(String lgNo);

    /**
     * 重置为暂存状态
     * @param guarInfoId 保函申请信息ID
     * @return 是否成功
     */
    boolean resetToTemp(Long guarInfoId);
}
