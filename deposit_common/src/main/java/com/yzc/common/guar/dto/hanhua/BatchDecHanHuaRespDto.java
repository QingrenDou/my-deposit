package com.yzc.common.guar.dto.hanhua;

import lombok.Data;

import java.util.List;

/**
 * @description 批量申请返回报文
 * @author douqr 2021/6/26 8:59
 */
@Data
public class BatchDecHanHuaRespDto {

    private String invokeId; //调用链id
    private String resultCode; //状态：成功0000,失败-1
    private String resultMessage; //状态：成功SUCCESS,失败原因

    private List<HanHuaReturnDto> results;
}
