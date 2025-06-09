package com.yzc.deposit.dao.deposit;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yzc.common.deposit.dto.deposit.WFConfigRespDto;
import com.yzc.common.deposit.dto.deposit.WFConfigSaveOrUpdateReqDto;
import com.yzc.common.deposit.entity.WFConfig;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author DouQingRen
 * @since 2025-03-13
 */
public interface IWFConfigDao extends IService<WFConfig> {

    /**
     * 根据企业或子公司id获取工作流配置信息
     * @param companyId 公司id
     * @return 工作流配置集合
     */
    List<WFConfigRespDto> getListByCompanyId(String companyId);

    /**
     * 根据企业或子公司id和工作流类型获取工作流配置信息
     * @param companyId 公司id
     * @param wfType 工作流类型
     * @return 工作流配置
     */
    WFConfigRespDto getByCompanyIdAndWfType(String companyId, Integer wfType);

    /**
     * 保存工作流配置
     * @param saveOrUpdateReqDto 保存工作流配置请求参数
     * @return 是否保存成功
     */
    boolean saveOrUpdate(WFConfigSaveOrUpdateReqDto saveOrUpdateReqDto);

    /**
     * 根据企业或子公司id集合和工作流类型获取工作流配置信息
     * @param childCompanyIdList 企业或子公司id集合
     * @param wfType 工作流类型
     * @param startStatus 启用状态
     * @return 工作流配置集合
     */
    List<WFConfigRespDto> getListByCompanyIdList(List<String> childCompanyIdList, Integer wfType, Integer startStatus);
}
