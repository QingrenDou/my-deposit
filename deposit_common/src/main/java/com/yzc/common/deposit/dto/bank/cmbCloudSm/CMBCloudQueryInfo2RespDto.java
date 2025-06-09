package com.yzc.common.deposit.dto.bank.cmbCloudSm;

import lombok.Data;

/**
 * 查询明细-获取退款状态 响应参数
 * 退款成功：reqsta="FIN" && rtnflg="S"
 * 退款失败：reqsta="FIN" && rtnflg!="S"
 */
@Data
public class CMBCloudQueryInfo2RespDto {
    private String reqnbr;//流程实例号
    private String buscod;//业务类型
    private String busmod;//业务模式
    private String oprtyp;//操作类型
    private String reqsta;//业务请求状态 [BNK：银行处理中 FIN：完成]
    private String oprsqn;//待处理操作序列
    private String oprals;//操作别名
    private String lgnnam;//用户名
    private String usrnam;//用户姓名
    private String rtnflg;//业务请求结果  [S：成功 F：失败 B：退票]
    private String rtnnar;//结果描述
    private String athflg;//是否有附件信息
    private String yurref;//业务参考号
}
