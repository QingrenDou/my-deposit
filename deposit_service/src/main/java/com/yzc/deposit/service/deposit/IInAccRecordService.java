package com.yzc.deposit.service.deposit;

import com.yzc.common.api.Result;
import com.yzc.common.deposit.dto.api.BindProjectReqDto;
import com.yzc.common.deposit.dto.api.InAccRecordQuery4BindReqDto;
import com.yzc.common.deposit.dto.api.InAccRecordQuery4BindRespDto;
import com.yzc.common.deposit.vo.InAccRecordRespVo;
import com.yzc.common.deposit.vo.TestInAccReqVo;

import java.util.List;

public interface IInAccRecordService {

    /**
     * 根据子账号查询入账记录,用于按项目退款时的入账列表
     * @param subAcc 子账号
     * @return 入账记录集合
     */
    List<InAccRecordRespVo> getApplyListBySubAcc(String subAcc);

    /**
     * 根据入账id查询明细
     * @param id 入账id
     * @return 入账明细
     */
    InAccRecordRespVo getById(String id);

    /**
     * 根据入账id集合查询明细
     * @param inAccRecordIdList 入账id集合
     * @return 入账明细集合
     */
    List<InAccRecordRespVo> getListByIds(List<String> inAccRecordIdList);

    /**
     * 模拟入账
     * @param reqVo 请求入参
     * @return 操作结果
     */
    Result<String> testInAcc(TestInAccReqVo reqVo);

    /**
     * 根据入账记录查询可绑定记录
     * @param reqDto 请求入参
     * @return 绑定记录集合
     */
    List<InAccRecordQuery4BindRespDto> queryList4Bind(InAccRecordQuery4BindReqDto reqDto);

    /**
     * 绑定项目
     * @param reqDto 请求入参
     * @return 操作结果
     */
    Result<String> bindProject(BindProjectReqDto reqDto);
}
