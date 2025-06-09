package com.yzc.common.deposit.vo;

import lombok.Data;

@Data
public class ProjectSubAccPageReqVo {
    public String projectNameLike; //项目编号、名称【模糊查询】
    public String openBidTimeStart; //开标时间【开始时间】
    public String openBidTimeEnd; //开标时间【结束时间】
}
