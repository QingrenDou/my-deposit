package com.yzc.common.deposit.dto.bank.cmbCloudSm;

import lombok.Data;

@Data
public class CMBCloudApplyBackMoney2Dto {
    private String rpyadr;//收付方开户行地址
    private String rpybkn;//收付方开户行行名称
    private String rpybbn;//收付方开户行行号
}
