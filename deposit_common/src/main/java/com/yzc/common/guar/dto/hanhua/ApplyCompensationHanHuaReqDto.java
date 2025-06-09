package com.yzc.common.guar.dto.hanhua;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description 索赔
 * @author douqr 2021/7/6 11:08
 */
@Data
public class ApplyCompensationHanHuaReqDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String businessType; //业务类型（01 投标保函，02 政采保函，03 履约保函）
    private String guaranteeNumber; //保函编号
    private String contactName; //联系人姓名
    private String contactPhone; //联系人电话
    private String reason; //理赔原因【非必填】
    private String compensateAccNo; //理赔收款账号【非必填】

    private List<ApplyCompensationReqFileDto> fileList;
}
