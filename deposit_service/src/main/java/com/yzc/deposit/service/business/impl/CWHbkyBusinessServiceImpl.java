package com.yzc.deposit.service.business.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yzc.common.api.Result;
import com.yzc.common.api.rpc.FeignTenderPlatformService;
import com.yzc.common.dto.deposit.DefaultDepositAccountDto;
import com.yzc.common.deposit.dto.bank.cwHbky.EsbBaseRespDto;
import com.yzc.common.deposit.dto.bank.cwHbky.EsbInAccReqDto;
import com.yzc.common.deposit.dto.bank.cwHbky.EsbInAccRespDto;
import com.yzc.common.deposit.dto.deposit.InAccRecordSaveReqDto;
import com.yzc.deposit.dao.deposit.IInAccRecordDao;
import com.yzc.deposit.service.bank.impl.BankCWHbkyServiceImpl;
import com.yzc.deposit.service.business.ICWHbkyBusinessService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 聚合业务-淮矿财务系统对接接口 实现类
 */
@Service
@Slf4j
public class CWHbkyBusinessServiceImpl implements ICWHbkyBusinessService {

    @Autowired
    @Qualifier("bankCWHbkyServiceImpl")
    private BankCWHbkyServiceImpl bankCWHbkyServiceImpl;

    @Resource
    private IInAccRecordDao inAccRecordDao;

    /**
     * 招采业务接口
     */
    @Resource
    private FeignTenderPlatformService feignTenderPlatformService;

   /**
     * 同步财务系统来款数据
     *
     * @param sDate 开始日期
     * @param eDate 截止日期
     * @return true:成功，false:失败
     */
    @Override
    public boolean syncInAccData(String sDate, String eDate) {
        String logCode = "(hbky)同步所有子账号入账数据syncInAccData===>";

        //日期默认为当日
        if(StringUtils.isBlank(sDate)){
            sDate = DateUtil.format(DateUtil.date(),"yyyy-MM-dd");
        }

        if (StringUtils.isBlank(eDate)) {
            eDate = DateUtil.format(DateUtil.date(),"yyyy-MM-dd");
        }

        //通过招采平台接口，获取所有主账号集合
        Result<List<DefaultDepositAccountDto>> configResult = feignTenderPlatformService.getDepositConfigList();
        if(ObjectUtil.isNull(configResult) || !configResult.isSuccess()
                || CollectionUtil.isEmpty(configResult.getData())) {
            log.error("{}获取子账号配置失败...", logCode);
            return false;
        }

        List<String> subAccList = configResult.getData().stream().map(DefaultDepositAccountDto::getVirtualAccount).collect(Collectors.toList());

        if(CollectionUtil.isEmpty(subAccList)){
            log.error("{}获取子账号集合为空",logCode);
            return false;
        }

        log.error("{}获取子账号集合成功，准备循环更新数据，子账号数量={}",logCode,subAccList.size());

        //循环子账号集合，刷新来款数据
        for(String item : subAccList){
            //获取子账号下所有来款数据
            this.getInAccDataBySubAcc(item,sDate,eDate);
        }

        return true;
    }

    /**
     * 根据子账号获取财务系统来款数据
     *
     * @param subAcc 子账号
     * @param sDate  开始日期
     * @param eDate  截止日期
     * @return true:成功，false:失败
     */
    @Override
    public boolean getInAccDataBySubAcc(String subAcc, String sDate, String eDate) {
        if(StringUtils.isBlank(subAcc) || StringUtils.isBlank(sDate) || StringUtils.isBlank(eDate)){
            return false;
        }

        String logCode = "(hbky)循环：根据子账号获取来款数据===>";
        log.info("{}开始,子账号={},开始日期={},截止日期={}",logCode,subAcc,sDate,eDate);
        EsbInAccReqDto reqDto = new EsbInAccReqDto();
        reqDto.setZhbh(subAcc);
        reqDto.setSDate(sDate);
        reqDto.setEDate(eDate);
        EsbBaseRespDto<List<EsbInAccRespDto>> respDto = bankCWHbkyServiceImpl.getInAccList(reqDto);
        if(ObjectUtil.isNull(respDto) || !ObjectUtil.equals(respDto.getCode(),"ok")){
            log.info("{}获取来款数据失败,查询失败,子账号={}",logCode,subAcc);
            return false;
        }
        if(CollectionUtil.isEmpty(respDto.getData())){
            log.info("{}获取来款数据为空,子账号={},无需处理",logCode,subAcc);
            return true;
        }

        List<InAccRecordSaveReqDto> saveList = bankCWHbkyServiceImpl.changeBankDtoListToSaveDtoList(respDto.getData());
        if(CollectionUtil.isEmpty(saveList)){
            log.info("{}数据转换结果为空,子账号={},肯定是哪里出了问题",logCode,subAcc);
            return false;
        }

        log.info("{}数据转换结束,准备入库,子账号={},来款总数={}",logCode,subAcc,saveList.size());
        //保存入库
        boolean saveRst = inAccRecordDao.saveBatch(saveList);

        log.info("{}入库结束,子账号={},入库结果={}",logCode,subAcc,saveRst);
        return true;
    }
}
