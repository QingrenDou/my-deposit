package com.yzc.deposit.dao.deposit.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yzc.common.deposit.dto.deposit.WFConfigRespDto;
import com.yzc.common.deposit.dto.deposit.WFConfigSaveOrUpdateReqDto;
import com.yzc.common.deposit.entity.WFConfig;
import com.yzc.deposit.dao.deposit.IWFConfigDao;
import com.yzc.deposit.repository.mapper.deposit.WFConfigMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author DouQingRen
 * @since 2025-03-13
 */
@Service
public class WFConfigDaoImpl extends ServiceImpl<WFConfigMapper, WFConfig> implements IWFConfigDao {

    /**
     * 根据企业或子公司id获取工作流配置信息
     *
     * @param companyId 公司id
     * @return 工作流配置集合
     */
    @Override
    public List<WFConfigRespDto> getListByCompanyId(String companyId) {
        if(StringUtils.isBlank(companyId)){
            return null;
        }
        LambdaQueryWrapper<WFConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WFConfig::getCompanyId, companyId);
        List<WFConfig> list = this.list(queryWrapper);
        return CollectionUtil.isEmpty(list) ? null : BeanUtil.copyToList(list, WFConfigRespDto.class);
    }

    /**
     * 根据企业或子公司id和工作流类型获取工作流配置信息
     *
     * @param companyId 公司id
     * @param wfType    工作流类型
     * @return 工作流配置
     */
    @Override
    public WFConfigRespDto getByCompanyIdAndWfType(String companyId, Integer wfType) {
        if(StringUtils.isBlank(companyId) || wfType == null){
            return null;
        }
        LambdaQueryWrapper<WFConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WFConfig::getCompanyId, companyId)
                .eq(WFConfig::getWfType, wfType);
        List<WFConfig> list = this.list(queryWrapper);
        if(CollectionUtil.isEmpty(list)){
            return null;
        }
        return BeanUtil.copyProperties(list.get(0), WFConfigRespDto.class);
    }

    /**
     * 保存工作流配置
     *
     * @param saveOrUpdateReqDto 保存工作流配置请求参数
     * @return 是否保存成功
     */
    @Override
    public boolean saveOrUpdate(WFConfigSaveOrUpdateReqDto saveOrUpdateReqDto) {
        if(saveOrUpdateReqDto != null){
            WFConfig wfConfig = BeanUtil.copyProperties(saveOrUpdateReqDto, WFConfig.class);
            return this.saveOrUpdate(wfConfig);
        }
        return false;
    }

    /**
     * 根据企业或子公司id集合和工作流类型获取工作流配置信息
     *
     * @param childCompanyIdList 企业或子公司id集合
     * @param wfType             工作流类型
     * @param startStatus        启用状态 (可选)
     * @return 工作流配置集合
     */
    @Override
    public List<WFConfigRespDto> getListByCompanyIdList(List<String> childCompanyIdList, Integer wfType, Integer startStatus) {
        if(CollectionUtil.isEmpty(childCompanyIdList) || ObjectUtil.isNull(wfType)){
            return null;
        }
        LambdaQueryWrapper<WFConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(WFConfig::getCompanyId, childCompanyIdList)
                .eq(WFConfig::getWfType, wfType)
                .eq(ObjectUtil.isNotNull(startStatus),WFConfig::getStartStatus, startStatus);
        return BeanUtil.copyToList(this.list(queryWrapper), WFConfigRespDto.class);
    }
}
