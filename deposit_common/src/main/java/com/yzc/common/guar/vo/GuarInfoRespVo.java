package com.yzc.common.guar.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 电子保函-查询结果
 */
@Data
public class GuarInfoRespVo extends GuarBaseInfoVo{

    //详细信息 见父类

    /*******************自定义内容*************************************************/
    /**
     * 保函状态汇总，用于前端显示(仅用于投标人保函页)
     */
    private String guarStatusStr;

    /**
     * 营业执照附件-预览url
     */
    private String busiLicensePreviewUrl;

    /**
     * 身份证正面附件-预览url
     */
    private String idcardFrontPreviewUrl;

    /**
     * 身份证反面附件-预览url
     */
    private String idcardBackPreviewUrl;

    /**
     * 是否可以发起退保【仅投标人端使用】
     */
    private Integer canApplyBack;

    /**
     * 判断退保原因描述
     */
    private String canApplyBackDesc;

}
