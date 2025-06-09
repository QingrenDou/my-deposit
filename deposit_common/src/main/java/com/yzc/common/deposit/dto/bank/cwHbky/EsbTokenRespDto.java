package com.yzc.common.deposit.dto.bank.cwHbky;

import lombok.Data;

/**
 * ESB对接，获取token结果
 */
@Data
public class EsbTokenRespDto {
    private String token;
    private Integer tokenExpiry; //失效计时(单位：秒)默认五分钟
}
