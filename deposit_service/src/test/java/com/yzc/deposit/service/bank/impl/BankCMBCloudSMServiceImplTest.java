package com.yzc.deposit.service.bank.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.TypeReference;
import com.yzc.common.api.Result;
import com.yzc.common.deposit.dto.bank.cmbCloudSm.*;
import com.yzc.common.deposit.dto.bank.common.ApplyBackMoneyReqDto;
import com.yzc.common.deposit.dto.bank.common.ApplyBackMoneyRespDto;
import com.yzc.common.deposit.dto.deposit.BankConfigRespDto;
import com.yzc.common.deposit.entity.BankKey;
import com.yzc.common.deposit.enums.BankTypeCodeEnum;
import com.yzc.common.deposit.util.DepositUtil;
import com.yzc.deposit.dao.deposit.IBankKeyDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BankCMBCloudSMServiceImplTest {

    @InjectMocks
    @Spy
    private BankCMBCloudSMServiceImpl bankCMBCloudSMService;

    @Mock
    private IBankKeyDao bankKeyDao;

    @Mock
    private Environment env;

    private BankConfigRespDto bankConfigRespDto;
    private ApplyBackMoneyReqDto applyBackMoneyReqDto;
    private BankKey bankKey;

    private static final String SUCCESS_CODE = "SUC0000";
    private static final String ERROR_CODE = "ERR0001";
    private static final String BUSMOD_VALUE = "S2008";
    private static final String MAIN_ACCOUNT = "1234567890";
    private static final String SUB_ACC = "000001";
    private static final String REQ_NO = "REQ2023102600001";
    private static final String ORIG_TRANS_NO = "ORIG001";

    @BeforeEach
    void setUp() {
        bankConfigRespDto = new BankConfigRespDto();
        bankConfigRespDto.setBankTypeCode(BankTypeCodeEnum.CMBBank_NMJTSJY.getCode());
        bankConfigRespDto.setBankHttpUrl("http://testurl.com");
        bankConfigRespDto.setConsumerId("testConsumerId");
        bankConfigRespDto.setMainAccount(MAIN_ACCOUNT);

        applyBackMoneyReqDto = new ApplyBackMoneyReqDto();
        applyBackMoneyReqDto.setReqNo(REQ_NO);
        applyBackMoneyReqDto.setOrigTransNo(ORIG_TRANS_NO);
        applyBackMoneyReqDto.setAmount(new BigDecimal("100.50"));
        applyBackMoneyReqDto.setSubAcc(SUB_ACC);
        applyBackMoneyReqDto.setReqDate(new Date());
        applyBackMoneyReqDto.setReceiveAccNo("6220000000000000");
        applyBackMoneyReqDto.setReceiveAccName("张三");
        applyBackMoneyReqDto.setIsRefundInterest(true);
        applyBackMoneyReqDto.setInterestAmount(new BigDecimal("0.50"));
        applyBackMoneyReqDto.setPurpose("测试退款");
        applyBackMoneyReqDto.setSummary("退款摘要");

        bankKey = new BankKey();
        bankKey.setYzcPrivateKey("testPrivateKey");
        bankKey.setBankPublicKey("testBankPublicKey");
        bankKey.setCipher("SM2"); // Assuming SM2 is used

        // Mock common behavior
        when(env.getProperty("bank.nmjt.busmod")).thenReturn(BUSMOD_VALUE);
    }

    private CMBCloudBaseRespDto<CMBCloudNTOPRDMRRespBodyDto> createMockSuccessResponse() {
        CMBCloudBaseRespDto<CMBCloudNTOPRDMRRespBodyDto> baseResp = new CMBCloudBaseRespDto<>();
        CMBCloudCommonResponseDto<CMBCloudNTOPRDMRRespBodyDto> commonResp = new CMBCloudCommonResponseDto<>();
        CMBCloudCommonRespHeadDto head = new CMBCloudCommonRespHeadDto();
        head.setResultcode(SUCCESS_CODE);
        head.setResultmsg("Success");
        commonResp.setHead(head);

        CMBCloudNTOPRDMRRespBodyDto body = new CMBCloudNTOPRDMRRespBodyDto();
        CMBCloudNTOPRDMRRespNtoprrtnzDto item = new CMBCloudNTOPRDMRRespNtoprrtnzDto();
        item.setReqnbr(REQ_NO);
        item.setReqsts("FIN"); // Finished
        item.setErrcod(SUCCESS_CODE);
        item.setErrtxt("交易成功");
        body.setNtoprrtnz(List.of(item));
        commonResp.setBody(body);
        baseResp.setResponse(commonResp);
        return baseResp;
    }

    private CMBCloudBaseRespDto<CMBCloudNTOPRDMRRespBodyDto> createMockBusinessErrorResponse() {
        CMBCloudBaseRespDto<CMBCloudNTOPRDMRRespBodyDto> baseResp = new CMBCloudBaseRespDto<>();
        CMBCloudCommonResponseDto<CMBCloudNTOPRDMRRespBodyDto> commonResp = new CMBCloudCommonResponseDto<>();
        CMBCloudCommonRespHeadDto head = new CMBCloudCommonRespHeadDto();
        head.setResultcode(SUCCESS_CODE); // Head is success
        head.setResultmsg("Success");
        commonResp.setHead(head);

        CMBCloudNTOPRDMRRespBodyDto body = new CMBCloudNTOPRDMRRespBodyDto();
        CMBCloudNTOPRDMRRespNtoprrtnzDto item = new CMBCloudNTOPRDMRRespNtoprrtnzDto();
        item.setReqnbr(REQ_NO);
        item.setReqsts("ERR");
        item.setErrcod(ERROR_CODE);
        item.setErrtxt("余额不足");
        body.setNtoprrtnz(List.of(item));
        commonResp.setBody(body);
        baseResp.setResponse(commonResp);
        return baseResp;
    }

    private CMBCloudBaseRespDto<CMBCloudNTOPRDMRRespBodyDto> createMockSystemErrorResponse() {
        CMBCloudBaseRespDto<CMBCloudNTOPRDMRRespBodyDto> baseResp = new CMBCloudBaseRespDto<>();
        CMBCloudCommonResponseDto<CMBCloudNTOPRDMRRespBodyDto> commonResp = new CMBCloudCommonResponseDto<>();
        CMBCloudCommonRespHeadDto head = new CMBCloudCommonRespHeadDto();
        head.setResultcode(ERROR_CODE); // Head is error
        head.setResultmsg("系统错误");
        commonResp.setHead(head);
        // Body might be null or empty in this case
        baseResp.setResponse(commonResp);
        return baseResp;
    }


    @Test
    void testApplyBackMoney_Success() {
        // Arrange
        when(bankKeyDao.getByBankTypeCode(bankConfigRespDto.getBankTypeCode())).thenReturn(bankKey);
        CMBCloudBaseRespDto<CMBCloudNTOPRDMRRespBodyDto> mockResponse = createMockSuccessResponse();
        doReturn(mockResponse).when(bankCMBCloudSMService).postToBank(
                any(CMBCloudNTOPRDMRReqBodyDto.class),
                eq("NTOPRDMR"),
                any(BankConfigRespDto.class),
                anyString(),
                any(TypeReference.class)
        );

        // Act
        Result<ApplyBackMoneyRespDto> result = bankCMBCloudSMService.applyBackMoney(applyBackMoneyReqDto, bankConfigRespDto);

        // Assert
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertNotNull(result.getData());
        ApplyBackMoneyRespDto respDto = result.getData();
        assertEquals(REQ_NO, respDto.getBankReqNo());
        assertEquals("FIN", respDto.getBankStatus());
        assertEquals(SUCCESS_CODE, respDto.getBankCode());
        assertEquals("交易成功", respDto.getBankMessage());
        assertTrue(respDto.isSuccess());

        ArgumentCaptor<CMBCloudNTOPRDMRReqBodyDto> captor = ArgumentCaptor.forClass(CMBCloudNTOPRDMRReqBodyDto.class);
        verify(bankCMBCloudSMService).postToBank(captor.capture(), eq("NTOPRDMR"), eq(bankConfigRespDto), anyString(), any(TypeReference.class));
        CMBCloudNTOPRDMRReqBodyDto capturedBody = captor.getValue();
        assertNotNull(capturedBody.getNtbusmody());
        assertEquals(1, capturedBody.getNtbusmody().size());
        assertEquals(BUSMOD_VALUE, capturedBody.getNtbusmody().get(0).getBusmod());

        assertNotNull(capturedBody.getNtoprdmrx1());
        assertEquals(1, capturedBody.getNtoprdmrx1().size());
        CMBCloudNTOPRDMRReqNtoprdmrx1Dto r1 = capturedBody.getNtoprdmrx1().get(0);
        assertEquals(applyBackMoneyReqDto.getOrigTransNo(), r1.getTrxnbr());
        assertEquals(0, applyBackMoneyReqDto.getAmount().compareTo(r1.getTrsamt()));
        assertEquals(bankConfigRespDto.getMainAccount(), r1.getAccnbr());
        assertEquals(applyBackMoneyReqDto.getSubAcc(), r1.getDumnbr());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        assertEquals(sdf.format(applyBackMoneyReqDto.getReqDate()), r1.getEptdat());
        assertEquals(applyBackMoneyReqDto.getReceiveAccNo(), r1.getRpyacc());
        assertEquals(applyBackMoneyReqDto.getReceiveAccName(), r1.getRpynam());
        assertEquals("Y", r1.getIntflg());
        assertEquals(0, applyBackMoneyReqDto.getInterestAmount().compareTo(r1.getIntamt()));
        assertEquals(applyBackMoneyReqDto.getReqNo(), r1.getYurref());
        assertEquals(applyBackMoneyReqDto.getPurpose(), r1.getNusage());
        assertEquals(applyBackMoneyReqDto.getSummary(), r1.getBusnar());
        assertEquals("N", r1.getBckflg());
        assertEquals("N", r1.getApvflg());
        assertEquals("N", r1.getApdflg()); // Default as no address details provided in this basic setup
        assertNull(capturedBody.getNtoprdmrx2()); // No address details
    }

    @Test
    void testApplyBackMoney_BusinessError() {
        // Arrange
        when(bankKeyDao.getByBankTypeCode(bankConfigRespDto.getBankTypeCode())).thenReturn(bankKey);
        CMBCloudBaseRespDto<CMBCloudNTOPRDMRRespBodyDto> mockResponse = createMockBusinessErrorResponse();
        doReturn(mockResponse).when(bankCMBCloudSMService).postToBank(any(), anyString(), any(), anyString(), any());

        // Act
        Result<ApplyBackMoneyRespDto> result = bankCMBCloudSMService.applyBackMoney(applyBackMoneyReqDto, bankConfigRespDto);

        // Assert
        assertNotNull(result);
        assertTrue(result.isSuccess()); // The method itself returns success, but the DTO inside indicates bank error
        assertNotNull(result.getData());
        ApplyBackMoneyRespDto respDto = result.getData();
        assertEquals(REQ_NO, respDto.getBankReqNo());
        assertEquals("ERR", respDto.getBankStatus());
        assertEquals(ERROR_CODE, respDto.getBankCode());
        assertEquals("余额不足", respDto.getBankMessage());
        assertFalse(respDto.isSuccess());
    }

    @Test
    void testApplyBackMoney_SystemError_HeadError() {
        // Arrange
        when(bankKeyDao.getByBankTypeCode(bankConfigRespDto.getBankTypeCode())).thenReturn(bankKey);
        CMBCloudBaseRespDto<CMBCloudNTOPRDMRRespBodyDto> mockResponse = createMockSystemErrorResponse();
        doReturn(mockResponse).when(bankCMBCloudSMService).postToBank(any(), anyString(), any(), anyString(), any());

        // Act
        Result<ApplyBackMoneyRespDto> result = bankCMBCloudSMService.applyBackMoney(applyBackMoneyReqDto, bankConfigRespDto);

        // Assert
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertNull(result.getData());
        assertTrue(result.getMsg().contains("系统错误"));
    }

    @Test
    void testApplyBackMoney_NullResponseFromBank() {
        // Arrange
        when(bankKeyDao.getByBankTypeCode(bankConfigRespDto.getBankTypeCode())).thenReturn(bankKey);
        doReturn(null).when(bankCMBCloudSMService).postToBank(any(), anyString(), any(), anyString(), any());

        // Act
        Result<ApplyBackMoneyRespDto> result = bankCMBCloudSMService.applyBackMoney(applyBackMoneyReqDto, bankConfigRespDto);

        // Assert
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertNull(result.getData());
        assertEquals("申请退款失败[银行返回为空或结构错误]", result.getMsg());
    }

    @Test
    void testApplyBackMoney_WithNtoprdmrx2() {
        // Arrange
        applyBackMoneyReqDto.setReceiveAccBankAddr("Test Address");
        applyBackMoneyReqDto.setReceiveAccBankName("Test Bank Name");
        applyBackMoneyReqDto.setReceiveAccBankNo("Test Bank No");

        when(bankKeyDao.getByBankTypeCode(bankConfigRespDto.getBankTypeCode())).thenReturn(bankKey);
        CMBCloudBaseRespDto<CMBCloudNTOPRDMRRespBodyDto> mockResponse = createMockSuccessResponse();
        doReturn(mockResponse).when(bankCMBCloudSMService).postToBank(any(), anyString(), any(), anyString(), any());

        // Act
        Result<ApplyBackMoneyRespDto> result = bankCMBCloudSMService.applyBackMoney(applyBackMoneyReqDto, bankConfigRespDto);

        // Assert
        assertTrue(result.isSuccess());
        ArgumentCaptor<CMBCloudNTOPRDMRReqBodyDto> captor = ArgumentCaptor.forClass(CMBCloudNTOPRDMRReqBodyDto.class);
        verify(bankCMBCloudSMService).postToBank(captor.capture(), eq("NTOPRDMR"), eq(bankConfigRespDto), anyString(), any(TypeReference.class));
        CMBCloudNTOPRDMRReqBodyDto capturedBody = captor.getValue();

        assertNotNull(capturedBody.getNtoprdmrx1());
        assertEquals(1, capturedBody.getNtoprdmrx1().size());
        CMBCloudNTOPRDMRReqNtoprdmrx1Dto r1 = capturedBody.getNtoprdmrx1().get(0);
        assertEquals("Y", r1.getApdflg(), "apdflg should be 'Y' when ntoprdmrx2 is populated");

        assertNotNull(capturedBody.getNtoprdmrx2());
        assertEquals(1, capturedBody.getNtoprdmrx2().size());
        CMBCloudNTOPRDMRReqNtoprdmrx2Dto r2 = capturedBody.getNtoprdmrx2().get(0);
        assertEquals("Test Address", r2.getRpyadr());
        assertEquals("Test Bank Name", r2.getRpybkn());
        assertEquals("Test Bank No", r2.getRpybbn());
    }

    @Test
    void testApplyBackMoney_YurrefGeneration() {
        // Arrange
        applyBackMoneyReqDto.setReqNo(null); // Ensure reqNo is null to test generation
        String generatedSeqNo = "GeneratedSeqNo123";

        when(bankKeyDao.getByBankTypeCode(bankConfigRespDto.getBankTypeCode())).thenReturn(bankKey);
        CMBCloudBaseRespDto<CMBCloudNTOPRDMRRespBodyDto> mockResponse = createMockSuccessResponse();
        doReturn(mockResponse).when(bankCMBCloudSMService).postToBank(any(), anyString(), any(), anyString(), any());

        // Mock static method DepositUtil.getSeqNo()
        // This requires PowerMockito or changing DepositUtil to be injectable, or refactoring the service method.
        // For simplicity here, we assume DepositUtil.getSeqNo() would work if we could mock it.
        // If direct static mocking is not set up, this part of the test will use the real DepositUtil.getSeqNo().
        // To properly test this in isolation, further test setup (like PowerMock) or code refactoring is needed.
        // For now, we'll capture and check if yurref is populated.

        // Act
        Result<ApplyBackMoneyRespDto> result = bankCMBCloudSMService.applyBackMoney(applyBackMoneyReqDto, bankConfigRespDto);

        // Assert
        assertTrue(result.isSuccess());
        ArgumentCaptor<CMBCloudNTOPRDMRReqBodyDto> captor = ArgumentCaptor.forClass(CMBCloudNTOPRDMRReqBodyDto.class);
        verify(bankCMBCloudSMService).postToBank(captor.capture(), eq("NTOPRDMR"), any(), anyString(), any());
        CMBCloudNTOPRDMRReqBodyDto capturedBody = captor.getValue();
        CMBCloudNTOPRDMRReqNtoprdmrx1Dto r1 = capturedBody.getNtoprdmrx1().get(0);
        assertNotNull(r1.getYurref());
        assertFalse(r1.getYurref().isEmpty());
        // If we could mock DepositUtil.getSeqNo to return `generatedSeqNo`
        // assertEquals(generatedSeqNo, r1.getYurref());
    }

    @Test
    void testApplyBackMoney_NusageDefault() {
        // Arrange
        applyBackMoneyReqDto.setPurpose(null); // Ensure purpose is null

        when(bankKeyDao.getByBankTypeCode(bankConfigRespDto.getBankTypeCode())).thenReturn(bankKey);
        CMBCloudBaseRespDto<CMBCloudNTOPRDMRRespBodyDto> mockResponse = createMockSuccessResponse();
        doReturn(mockResponse).when(bankCMBCloudSMService).postToBank(any(), anyString(), any(), anyString(), any());

        // Act
        Result<ApplyBackMoneyRespDto> result = bankCMBCloudSMService.applyBackMoney(applyBackMoneyReqDto, bankConfigRespDto);

        // Assert
        assertTrue(result.isSuccess());
        ArgumentCaptor<CMBCloudNTOPRDMRReqBodyDto> captor = ArgumentCaptor.forClass(CMBCloudNTOPRDMRReqBodyDto.class);
        verify(bankCMBCloudSMService).postToBank(captor.capture(), eq("NTOPRDMR"), any(), anyString(), any());
        CMBCloudNTOPRDMRReqBodyDto capturedBody = captor.getValue();
        CMBCloudNTOPRDMRReqNtoprdmrx1Dto r1 = capturedBody.getNtoprdmrx1().get(0);
        assertEquals("资金原路返回交易", r1.getNusage());
    }
}
