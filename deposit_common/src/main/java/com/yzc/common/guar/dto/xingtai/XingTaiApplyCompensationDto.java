package com.yzc.common.guar.dto.xingtai;

import com.yzc.common.guar.dto.common.GuarFileListDto;
import lombok.Data;

import java.util.List;

@Data
public class XingTaiApplyCompensationDto {

    //保函申请编号
    private String lgNo;


    //联系人姓名
    private String contactName;

    //联系人电话
    private String contactPhone;

    //招标人/受益人
    private String tenderee;

    //理赔原因
    private String reason;

    //理赔收款账号
    private String compensateAccNo;

    //fileList
    private List<GuarFileListDto> fileList;
}
