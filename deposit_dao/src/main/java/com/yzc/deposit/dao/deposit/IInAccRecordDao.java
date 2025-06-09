package com.yzc.deposit.dao.deposit;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yzc.common.api.Result;
import com.yzc.common.deposit.dto.api.UpdateInfoBySubAccReqDto;
import com.yzc.common.deposit.dto.deposit.InAccRecordCountRespDto;
import com.yzc.common.deposit.dto.deposit.InAccRecordRespDto;
import com.yzc.common.deposit.dto.deposit.InAccRecordSaveReqDto;
import com.yzc.common.deposit.dto.deposit.InAccRecordUpdateReqDto;
import com.yzc.common.deposit.entity.InAccRecord;

import java.util.List;

public interface IInAccRecordDao  extends IService<InAccRecord> {

    /**
     * 根据子账号，统计数量
     * @param subAcc 子账号
     * @return 统计结果
     */
    InAccRecordCountRespDto countBySubAcc(String subAcc);

    /**
     * 根据子账号，查询入账列表
     * @param subAcc 子账号
     * @return 入账列表
     */
    List<InAccRecordRespDto> getListBySubAcc(String subAcc);

    /**
     * 根据id，查询入账记录
     * @param id 入账记录id
     * @return 入账记录
     */
    InAccRecordRespDto getById(String id);

    /**
     * 根据保证金账号+银行流水号，查询入账记录【防止银行流水号重复问题】
     * @param subAcc 保证金账号
     * @param bankSeqNo 银行流水号
     * @return 入账记录
     */
    InAccRecordRespDto getByBankSeqNo(String subAcc,String bankSeqNo);
    /**
     * 根据id列表，查询入账记录
     * @param inAccRecordIdList 入账记录id列表
     * @return 入账记录列表
     */
    List<InAccRecordRespDto> getListByIds(List<String> inAccRecordIdList);

    /**
     * 更新入账记录
     * @param recordUpdateReqDto 入账记录更新请求
     * @return 操作结果
     */
    boolean update(InAccRecordUpdateReqDto recordUpdateReqDto);

    /**
     * 保存入账记录
     * @param saveReqDto 入账记录保存请求
     * @return 操作结果
     */
    boolean save(InAccRecordSaveReqDto saveReqDto);

    /**
     * 批量保存入账记录
     * @param saveReqDtoList 入账记录保存请求列表
     * @return 操作结果
     */
    boolean saveBatch(List<InAccRecordSaveReqDto> saveReqDtoList);

    /**
     * 清除中标人和候选人状态
     * @param subAcc 子账号
     * @return 操作结果
     */
    boolean clearWinStatusBySubAcc(String subAcc);

    /**
     * 更新入账记录业务状态
     * @param reqDto 入账记录更新请求
     * @return 操作结果
     */
    Result<String> updateRecordStatus(UpdateInfoBySubAccReqDto reqDto);
}
