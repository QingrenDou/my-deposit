package com.yzc.common.guar.vo;

import lombok.Data;

/**
 * 生成随机保函机构-响应参数
 */
@Data
public class RandomGuarOrgRespVo {

    /**
     * 保函申请id
     */
    private Long guarInfoId;

    /**
     * 保函申请编号
     */
    private String lgNo;

    /**
     * 保函类型编码
     */
    private Integer guarTypeCode;

    /**
     * 金融机构名称（orgName）
     */
    private String financeOrgName;

    /**
     * 收款方
     */
    private String payeeName;

    /**
     * 当前投标企业id
     */
    private String bidderId;

    /**
     * 当前投标企业名称
     */
    private String bidderName;

    /**
     * 当前投标人统一社会信用代码
     */
    private String creditCode;

    /**
     * 当前登录人名称
     */
    private String applyUserName;
}
