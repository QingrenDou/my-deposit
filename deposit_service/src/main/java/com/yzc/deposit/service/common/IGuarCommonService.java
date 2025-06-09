package com.yzc.deposit.service.common;

import com.yzc.common.api.Result;
import com.yzc.common.dto.order.GetOrderReqDto;
import com.yzc.common.guar.dto.CertFileCopyRespDto;
import com.yzc.common.guar.entity.GuarInfo;
import com.yzc.common.guar.vo.RandomGuarOrgRespVo;

/**
 * 电子保函-通用方法
 */
public interface IGuarCommonService {

    /**
     * 根据保函信息，生成优质采业务订单入参
     * @param guarInfo 保函信息
     * @return 优质采业务订单入参
     */
    GetOrderReqDto getOrderReqDto(GuarInfo guarInfo);

    /**
     * 随机匹配保函机构
     * @return 随机匹配保函机构结果
     */
    Result<RandomGuarOrgRespVo> getRandomGuarOrg();

    /**
     * 复制企业证件 并生成新的文件
     * @param companyId 企业id
     * @return 新的文件id集合
     */
    Result<CertFileCopyRespDto> certFileCopy(String companyId);
}
