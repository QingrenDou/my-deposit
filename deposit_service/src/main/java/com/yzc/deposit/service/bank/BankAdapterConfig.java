package com.yzc.deposit.service.bank;

import com.yzc.common.api.Result;
import com.yzc.common.deposit.dto.bank.common.ApplySubAccReqDto;
import com.yzc.common.deposit.dto.bank.common.ApplySubAccRespDto;
import com.yzc.common.deposit.dto.deposit.BankConfigRespDto;
import com.yzc.deposit.dao.deposit.IBankConfigDao;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class BankAdapterConfig {

    @Resource
    private IBankConfigDao bankConfigDao;

    @Resource
    private List<IBankAdapterService> bankAdapterServiceList;

    /**
     * 申请子账号
     * @param reqDto 请求参数
     * @return 申请结果
     */
    public Result<ApplySubAccRespDto> applySubAcc(ApplySubAccReqDto reqDto){
        //根据银行类型，获取银行配置
        BankConfigRespDto bankConfigRespDto = bankConfigDao.getByBankTypeCode(reqDto.getBankTypeCode());

        //TODO 判断银行状态，预处理

        //如果是新加入的则调用新的方式  不改变原来的任何代码
        return bankAdapterServiceList.stream()
                .filter(item -> item.isCurrentBank(bankConfigRespDto.getBankMold()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("未适配到银行"))
                .applySubAcc(reqDto,bankConfigRespDto);
    }
}
