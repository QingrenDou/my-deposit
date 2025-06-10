package com.yzc.deposit.service.bank.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.TypeReference;
import com.yzc.common.api.Result;
import com.yzc.common.deposit.dto.bank.cmbCloudSm.*;
import com.yzc.common.deposit.dto.bank.common.RefreshRecordListHisReqDto;
import com.yzc.common.deposit.dto.bank.common.RefreshRecordListTodayReqDto;
import com.yzc.common.deposit.dto.deposit.BankConfigRespDto;
import com.yzc.common.deposit.dto.deposit.InAccRecordSaveReqDto;
import com.yzc.common.deposit.entity.BankKey;
import com.yzc.common.deposit.enums.BankTypeCodeEnum;
import com.yzc.deposit.dao.deposit.IBankKeyDao;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BankCMBCloudSMServiceImplTest {

    @InjectMocks
    @Spy // Using Spy to allow mocking of postToBank.
    private BankCMBCloudSMServiceImpl bankCMBCloudSMService;

    @Mock
    private IBankKeyDao bankKeyDao;

    @Mock
    private Environment env;

    private BankConfigRespDto bankConfigRespDto;
    private SimpleDateFormat bankDateFormat = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyyMMddHHmmss");


    @BeforeEach
    void setUp() {
        bankConfigRespDto = new BankConfigRespDto();
        bankConfigRespDto.setMainAccount("1234567890");
        bankConfigRespDto.setBankTypeCode(BankTypeCodeEnum.CMBBank_NMJTSJY.getCode());
        bankConfigRespDto.setConsumerId("testUser");
        bankConfigRespDto.setBankHttpUrl("http://testbankapi.com");

        BankKey bankKey = new BankKey();
        bankKey.setYzcPrivateKey("testPrivateKey");
        bankKey.setBankPublicKey("testBankPublicKey");
        bankKey.setCipher("SM2");
        when(bankKeyDao.getByBankTypeCode(any())).thenReturn(bankKey);
        when(env.getProperty("bank.nmjt.busmod")).thenReturn("S2008");
    }

    // --- Helper Methods to create Mock Bank Responses ---

    private CMBCloudBaseRespDto<CMBCloudQueryRecordTodayRespBodyDto> createMockTodayResponse(
            List<CMBCloudQueryRecordTodayItemDto> items, String ctnKeyResponse, String errorCode, String errorMsg, boolean headError) {

        CMBCloudBaseRespDto<CMBCloudQueryRecordTodayRespBodyDto> baseResp = new CMBCloudBaseRespDto<>();
        CMBCloudCommonRespDto<CMBCloudQueryRecordTodayRespBodyDto> response = new CMBCloudCommonRespDto<>();
        CMBCloudCommonRespHeadDto head = new CMBCloudCommonRespHeadDto();
        CMBCloudQueryRecordTodayRespBodyDto body = new CMBCloudQueryRecordTodayRespBodyDto();

        if (headError) {
            head.setResultcode("FAIL001");
            head.setResultmsg("Head Error Message");
        } else {
            head.setResultcode("SUC0000");
            head.setResultmsg("Success");
        }
        response.setHead(head);

        if (!headError) {
            CMBCloudQueryRecordTodayRespNtdmtlstyDto ntdmtlstyResp = new CMBCloudQueryRecordTodayRespNtdmtlstyDto();
            ntdmtlstyResp.setCtnkey(ctnKeyResponse);
            body.setNtdmtlsty(List.of(ntdmtlstyResp));

            if (StringUtils.isNotBlank(errorCode)) {
                CMBCloudQueryRecordTodayRespNtdmtlstz1Dto errorNode = new CMBCloudQueryRecordTodayRespNtdmtlstz1Dto();
                errorNode.setErrcod(errorCode);
                errorNode.setErrtxt(errorMsg);
                body.setNtdmtlstz1(List.of(errorNode));
                body.setNtdmtlstz(new ArrayList<>());
            } else {
                body.setNtdmtlstz(items != null ? items : new ArrayList<>());
                body.setNtdmtlstz2(new ArrayList<>());
            }
        }
        response.setBody(body);
        baseResp.setResponse(response);
        return baseResp;
    }

    private CMBCloudBaseRespDto<CMBCloudQueryRecordHisRespBodyDto> createMockHisResponse(
            List<CMBCloudQueryRecordHisItemDto> items, String ctnKeyResponse, boolean headError, String headErrorCode, String headErrorMsg) {
        CMBCloudBaseRespDto<CMBCloudQueryRecordHisRespBodyDto> baseResp = new CMBCloudBaseRespDto<>();
        CMBCloudCommonRespDto<CMBCloudQueryRecordHisRespBodyDto> response = new CMBCloudCommonRespDto<>();
        CMBCloudCommonRespHeadDto head = new CMBCloudCommonRespHeadDto();
        CMBCloudQueryRecordHisRespBodyDto body = new CMBCloudQueryRecordHisRespBodyDto();

        if (headError) {
            head.setResultcode(headErrorCode != null ? headErrorCode : "FAIL001");
            head.setResultmsg(headErrorMsg != null ? headErrorMsg : "Head Error Message");
        } else {
            head.setResultcode("SUC0000");
            head.setResultmsg("Success");
        }
        response.setHead(head);

        if (!headError) {
            CMBCloudQueryRecordHisRespNtdmthlsyDto ntdmthlsyResp = new CMBCloudQueryRecordHisRespNtdmthlsyDto();
            ntdmthlsyResp.setCtnkey(ctnKeyResponse);
            body.setNtdmthlsy(List.of(ntdmthlsyResp));
            body.setNtdmthlsz(items != null ? items : new ArrayList<>());
            body.setNtdmthlsz2(new ArrayList<>());
        }
        response.setBody(body);
        baseResp.setResponse(response);
        return baseResp;
    }


    // --- Tests for refreshRecordListToday ---

    @Test
    void testRefreshRecordListToday_Success_SinglePage() throws Exception {
        RefreshRecordListTodayReqDto reqDto = new RefreshRecordListTodayReqDto();
        reqDto.setTenantId(1L);

        List<CMBCloudQueryRecordTodayItemDto> items = new ArrayList<>();
        CMBCloudQueryRecordTodayItemDto item1 = new CMBCloudQueryRecordTodayItemDto();
        item1.setTrxnbr("TRX001");
        item1.setTrxdat("20230101");
        item1.setTrxtim("100000");
        item1.setTrsam(new BigDecimal("100.50"));
        item1.setDcflag("C");
        item1.setCltacc("payerAcc1");
        item1.setCltnam("Payer Name 1");
        item1.setEtyacc(bankConfigRespDto.getMainAccount());
        item1.setEtynam("Payee Name 1");
        item1.setNaryur("Summary 1");
        item1.setDmanbr("subAcc1");
        item1.setRtnsts("S");
        item1.setCcynbr("CNY");
        items.add(item1);

        CMBCloudBaseRespDto<CMBCloudQueryRecordTodayRespBodyDto> mockResponse =
            createMockTodayResponse(items, "", null, null, false);

        // The service instance is a @Spy, so we can mock specific methods.
        // postToBank is private, for Mockito @Spy to work on private methods, it usually requires PowerMock or making the method accessible (e.g. package-private/protected).
        // Assuming the method was made testable (e.g. changed to protected for test purposes)
        doReturn(mockResponse).when(bankCMBCloudSMService).postToBank(any(CMBCloudQueryRecordTodayReqBodyDto.class), eq("NTDMTLST"), any(BankConfigRespDto.class), any(String.class), any(TypeReference.class));

        Result<List<InAccRecordSaveReqDto>> result = bankCMBCloudSMService.refreshRecordListToday(reqDto, bankConfigRespDto);

        assertTrue(result.isSuccess());
        assertNotNull(result.getData());
        assertEquals(1, result.getData().size());
        InAccRecordSaveReqDto savedRecord = result.getData().get(0);
        assertEquals("TRX001", savedRecord.getBankTransNo());
        assertEquals(0, new BigDecimal("100.50").compareTo(savedRecord.getTransAmount()));
        assertEquals(1, savedRecord.getTransType());
        assertEquals("subAcc1", savedRecord.getSubAcc());
        assertEquals(dateTimeFormat.parse("20230101100000"), savedRecord.getTransDate());
    }

    @Test
    void testRefreshRecordListToday_Success_MultiPage() throws Exception {
        RefreshRecordListTodayReqDto reqDto = new RefreshRecordListTodayReqDto();
        reqDto.setTenantId(1L);

        List<CMBCloudQueryRecordTodayItemDto> itemsPage1 = new ArrayList<>();
        CMBCloudQueryRecordTodayItemDto item1 = new CMBCloudQueryRecordTodayItemDto();
        item1.setTrxnbr("TRX001"); item1.setTrxdat("20230101"); item1.setTrxtim("100000"); item1.setTrsam(new BigDecimal("100.00")); item1.setDcflag("C");
        itemsPage1.add(item1);
        CMBCloudBaseRespDto<CMBCloudQueryRecordTodayRespBodyDto> responsePage1 = createMockTodayResponse(itemsPage1, "NEXTKEY123", null, null, false);

        List<CMBCloudQueryRecordTodayItemDto> itemsPage2 = new ArrayList<>();
        CMBCloudQueryRecordTodayItemDto item2 = new CMBCloudQueryRecordTodayItemDto();
        item2.setTrxnbr("TRX002"); item2.setTrxdat("20230101"); item2.setTrxtim("110000"); item2.setTrsam(new BigDecimal("200.00")); item2.setDcflag("D");
        itemsPage2.add(item2);
        CMBCloudBaseRespDto<CMBCloudQueryRecordTodayRespBodyDto> responsePage2 = createMockTodayResponse(itemsPage2, "", null, null, false);

        doReturn(responsePage1)
            .doReturn(responsePage2)
            .when(bankCMBCloudSMService).postToBank(any(CMBCloudQueryRecordTodayReqBodyDto.class), eq("NTDMTLST"), any(BankConfigRespDto.class), any(String.class), any(TypeReference.class));

        Result<List<InAccRecordSaveReqDto>> result = bankCMBCloudSMService.refreshRecordListToday(reqDto, bankConfigRespDto);

        assertTrue(result.isSuccess());
        assertNotNull(result.getData());
        assertEquals(2, result.getData().size());
        assertEquals("TRX001", result.getData().get(0).getBankTransNo());
        assertEquals("TRX002", result.getData().get(1).getBankTransNo());
    }

    @Test
    void testRefreshRecordListToday_Error_BankHeadError() {
        RefreshRecordListTodayReqDto reqDto = new RefreshRecordListTodayReqDto();
        CMBCloudBaseRespDto<CMBCloudQueryRecordTodayRespBodyDto> mockResponse =
            createMockTodayResponse(null, null, null, null, true);

        doReturn(mockResponse).when(bankCMBCloudSMService).postToBank(any(CMBCloudQueryRecordTodayReqBodyDto.class), eq("NTDMTLST"), any(BankConfigRespDto.class), any(String.class), any(TypeReference.class));

        Result<List<InAccRecordSaveReqDto>> result = bankCMBCloudSMService.refreshRecordListToday(reqDto, bankConfigRespDto);

        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("Head Error Message"));
    }

    @Test
    void testRefreshRecordListToday_Error_BankBodyError() {
        RefreshRecordListTodayReqDto reqDto = new RefreshRecordListTodayReqDto();
        CMBCloudBaseRespDto<CMBCloudQueryRecordTodayRespBodyDto> mockResponse =
            createMockTodayResponse(null, null, "ERRBODY01", "Body Error Detail", false);

        doReturn(mockResponse).when(bankCMBCloudSMService).postToBank(any(CMBCloudQueryRecordTodayReqBodyDto.class), eq("NTDMTLST"), any(BankConfigRespDto.class), any(String.class), any(TypeReference.class));

        Result<List<InAccRecordSaveReqDto>> result = bankCMBCloudSMService.refreshRecordListToday(reqDto, bankConfigRespDto);

        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("Body Error Detail"));
    }

    @Test
    void testRefreshRecordListToday_EmptyResponse() {
        RefreshRecordListTodayReqDto reqDto = new RefreshRecordListTodayReqDto();
        CMBCloudBaseRespDto<CMBCloudQueryRecordTodayRespBodyDto> mockResponse =
            createMockTodayResponse(new ArrayList<>(), "", null, null, false);

        doReturn(mockResponse).when(bankCMBCloudSMService).postToBank(any(CMBCloudQueryRecordTodayReqBodyDto.class), eq("NTDMTLST"), any(BankConfigRespDto.class), any(String.class), any(TypeReference.class));

        Result<List<InAccRecordSaveReqDto>> result = bankCMBCloudSMService.refreshRecordListToday(reqDto, bankConfigRespDto);

        assertTrue(result.isSuccess());
        assertNotNull(result.getData());
        assertTrue(result.getData().isEmpty());
    }

    // --- Tests for refreshRecordListHis ---

    @Test
    void testRefreshRecordListHis_Success_SinglePage() throws Exception {
        RefreshRecordListHisReqDto reqDto = new RefreshRecordListHisReqDto();
        reqDto.setTenantId(1L);
        reqDto.setStartDate(bankDateFormat.parse("20230101"));
        reqDto.setEndDate(bankDateFormat.parse("20230101"));

        List<CMBCloudQueryRecordHisItemDto> items = new ArrayList<>();
        CMBCloudQueryRecordHisItemDto item1 = new CMBCloudQueryRecordHisItemDto();
        item1.setTrxnbr("TRXH001");
        item1.setTrxdat("20230101");
        item1.setTrxtim("093000");
        item1.setTrsam(new BigDecimal("500.75"));
        item1.setDcflag("D");
        item1.setNaryur("Historic Summary 1");
        items.add(item1);

        CMBCloudBaseRespDto<CMBCloudQueryRecordHisRespBodyDto> mockResponse =
            createMockHisResponse(items, "", false, null, null);

        doReturn(mockResponse).when(bankCMBCloudSMService).postToBank(any(CMBCloudQueryRecordHisReqBodyDto.class), eq("NTDMTHLS"), any(BankConfigRespDto.class), any(String.class), any(TypeReference.class));

        Result<List<InAccRecordSaveReqDto>> result = bankCMBCloudSMService.refreshRecordListHis(reqDto, bankConfigRespDto);

        assertTrue(result.isSuccess());
        assertNotNull(result.getData());
        assertEquals(1, result.getData().size());
        InAccRecordSaveReqDto savedRecord = result.getData().get(0);
        assertEquals("TRXH001", savedRecord.getBankTransNo());
        assertEquals(0, new BigDecimal("500.75").compareTo(savedRecord.getTransAmount()));
        assertEquals(2, savedRecord.getTransType());
        assertEquals(dateTimeFormat.parse("20230101093000"), savedRecord.getTransDate());
    }

    @Test
    void testRefreshRecordListHis_Success_MultiPage() throws Exception {
        RefreshRecordListHisReqDto reqDto = new RefreshRecordListHisReqDto();
        reqDto.setTenantId(1L);
        reqDto.setStartDate(bankDateFormat.parse("20230101"));
        reqDto.setEndDate(bankDateFormat.parse("20230102"));

        List<CMBCloudQueryRecordHisItemDto> itemsPage1 = new ArrayList<>();
        CMBCloudQueryRecordHisItemDto item1 = new CMBCloudQueryRecordHisItemDto();
        item1.setTrxnbr("TRXH001"); item1.setTrxdat("20230101"); item1.setTrsam(new BigDecimal("10.00"));
        itemsPage1.add(item1);
        CMBCloudBaseRespDto<CMBCloudQueryRecordHisRespBodyDto> responsePage1 = createMockHisResponse(itemsPage1, "NEXTHISKEY", false, null, null);

        List<CMBCloudQueryRecordHisItemDto> itemsPage2 = new ArrayList<>();
        CMBCloudQueryRecordHisItemDto item2 = new CMBCloudQueryRecordHisItemDto();
        item2.setTrxnbr("TRXH002"); item2.setTrxdat("20230102"); item2.setTrsam(new BigDecimal("20.00"));
        itemsPage2.add(item2);
        CMBCloudBaseRespDto<CMBCloudQueryRecordHisRespBodyDto> responsePage2 = createMockHisResponse(itemsPage2, "", false, null, null);

        doReturn(responsePage1)
            .doReturn(responsePage2)
            .when(bankCMBCloudSMService).postToBank(any(CMBCloudQueryRecordHisReqBodyDto.class), eq("NTDMTHLS"), any(BankConfigRespDto.class), any(String.class), any(TypeReference.class));

        Result<List<InAccRecordSaveReqDto>> result = bankCMBCloudSMService.refreshRecordListHis(reqDto, bankConfigRespDto);

        assertTrue(result.isSuccess());
        assertNotNull(result.getData());
        assertEquals(2, result.getData().size());
        assertEquals("TRXH001", result.getData().get(0).getBankTransNo());
        assertEquals("TRXH002", result.getData().get(1).getBankTransNo());
    }

    @Test
    void testRefreshRecordListHis_Error_BankHeadError() throws ParseException {
        RefreshRecordListHisReqDto reqDto = new RefreshRecordListHisReqDto();
        reqDto.setStartDate(bankDateFormat.parse("20230101"));
        reqDto.setEndDate(bankDateFormat.parse("20230101"));

        CMBCloudBaseRespDto<CMBCloudQueryRecordHisRespBodyDto> mockResponse =
            createMockHisResponse(null, "", true, "HISFAIL01", "Historic Head Error");

        doReturn(mockResponse).when(bankCMBCloudSMService).postToBank(any(CMBCloudQueryRecordHisReqBodyDto.class), eq("NTDMTHLS"), any(BankConfigRespDto.class), any(String.class), any(TypeReference.class));

        Result<List<InAccRecordSaveReqDto>> result = bankCMBCloudSMService.refreshRecordListHis(reqDto, bankConfigRespDto);

        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("Historic Head Error"));
    }

    @Test
    void testRefreshRecordListHis_EmptyResponse() throws ParseException {
        RefreshRecordListHisReqDto reqDto = new RefreshRecordListHisReqDto();
        reqDto.setStartDate(bankDateFormat.parse("20230101"));
        reqDto.setEndDate(bankDateFormat.parse("20230101"));

        CMBCloudBaseRespDto<CMBCloudQueryRecordHisRespBodyDto> mockResponse =
            createMockHisResponse(new ArrayList<>(), "", false, null, null);

        doReturn(mockResponse).when(bankCMBCloudSMService).postToBank(any(CMBCloudQueryRecordHisReqBodyDto.class), eq("NTDMTHLS"), any(BankConfigRespDto.class), any(String.class), any(TypeReference.class));

        Result<List<InAccRecordSaveReqDto>> result = bankCMBCloudSMService.refreshRecordListHis(reqDto, bankConfigRespDto);

        assertTrue(result.isSuccess());
        assertNotNull(result.getData());
        assertTrue(result.getData().isEmpty());
    }
}
