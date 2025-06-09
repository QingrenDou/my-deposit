package com.yzc.common.guar.dto.hanhua;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @description 解密保函-请求
 * @author douqr 2021/6/24 17:20
 */
@Data
@Builder
public class DecHanHuaReqDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String bidNo; //标段编号
    private String bidName; //标段名称
    private String lgNo; //投标申请编号
    private String creditCode; //统一社会信用代码
    private String enterpriseName; //企业名称
    private String tenderee; //招标人/受益人

}
