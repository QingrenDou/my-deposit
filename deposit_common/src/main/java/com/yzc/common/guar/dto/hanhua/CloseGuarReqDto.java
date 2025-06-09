package com.yzc.common.guar.dto.hanhua;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author douqr 2021/6/26 11:30
 * @description 退保-入参
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CloseGuarReqDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String bidNo;//标段编号，开标前密文，开标后明文
    private String bidName;//标段名称，开标前密文，开标后明文
    private String lgNo;//保函申请编号
    private String guaranteeNumber;//保函编号(只有开函成功)
    private String memo;//退保原因说明
    private String operatorPhone;//经办人手机号

    private List<FileListDto> fileList;
}
