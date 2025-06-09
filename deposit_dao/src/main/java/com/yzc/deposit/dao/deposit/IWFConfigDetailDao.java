package com.yzc.deposit.dao.deposit;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yzc.common.deposit.dto.deposit.WFConfigDetailRespDto;
import com.yzc.common.deposit.dto.deposit.WFConfigDetailSaveReqDto;
import com.yzc.common.deposit.entity.WFConfigDetail;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author DouQingRen
 * @since 2025-03-13
 */
public interface IWFConfigDetailDao extends IService<WFConfigDetail> {

    /**
     * 根据公司id和工作流类型获取工作流详情
     * @param companyId 公司id
     * @param wfType 工作流类型
     * @return 工作流详情集合
     */
    List<WFConfigDetailRespDto> getListByCompanyIdAndType(String companyId,Integer wfType);

    /**
     * 根据公司id和工作流类型删除工作流详情
     * @param companyId 公司id
     * @param wfType 工作流类型
     * @return 是否删除成功
     */
    boolean deleteByCompanyIdAndType(String companyId, Integer wfType);

    /**
     * 批量保存工作流详情
     * @param list 工作流详情集合
     * @return 是否保存成功
     */
    boolean saveList(List<WFConfigDetailSaveReqDto> list);
}
