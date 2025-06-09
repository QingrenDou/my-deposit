package com.yzc.common.guar.dto.hanhua;

import lombok.Data;

import java.io.Serializable;

/**
 * @description 解密保函-结果
 * @author douqr 2021/6/24 17:21
 */
@Data
public class DecHanHuaRespDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String lgNo; //投标申请编号
    private String status; //开函状态（0-未提交申请，1-审核中，2-通过，3-未通过，4-取消，6-已解保，7-已拒绝）
    private String auditOpinion; //（status=3时给出未通过原因，其他时为空）
    private String guaranteeFileUrl; //保函文件url（status=2时才返回）
    private String guaranteeNumber; //保函编号（在瀚华官网查询保函真实性）（status=2时返回）
    private String guaranteeCode; //保函代码（在瀚华官网查询保函真实性）（status=2时返回）

}
