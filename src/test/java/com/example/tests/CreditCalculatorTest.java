package com.example.tests;

import com.example.tests.enums.ClientType;
import com.example.tests.exception.CannotBePayedException;
import com.example.tests.exception.CentralBankNotRespondingException;
import com.example.tests.service.CentralBankService;
import com.example.tests.service.CreditCalculator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreditCalculatorTest {

    @InjectMocks
    private CreditCalculator creditCalculator;

    @Mock
    private CentralBankService centralBankService;

    @Test
    public void calculateOverpaymentGovermentTest() {
        when(centralBankService.getKeyRate()).thenReturn(10d);
        double amount = 100000d;
        double monthPaymentAmount = 10000d;
        double result = creditCalculator.calculateOverpayment(amount, monthPaymentAmount, ClientType.GOVERMENT);
        Assertions.assertEquals(10000d, result);
    }

    @Test
    public void calculateOverpaymentBusinessTest() {
        when(centralBankService.getKeyRate()).thenReturn(10d);
        double amount = 100000d;
        double monthPaymentAmount = 10000d;
        double result = creditCalculator.calculateOverpayment(amount, monthPaymentAmount, ClientType.BUSINESS);
        Assertions.assertEquals(11000d, result);
    }

    @Test
    public void calculateOverpaymentIndividualTest() {
        when(centralBankService.getKeyRate()).thenReturn(10d);
        double amount = 100000d;
        double monthPaymentAmount = 10000d;
        double result = creditCalculator.calculateOverpayment(amount, monthPaymentAmount, ClientType.INDIVIDUAL);
        Assertions.assertEquals(12000d, result);
    }

    @Test
    public void calculateOverpaymentOnTooBigAmountTest() {
        when(centralBankService.getKeyRate()).thenReturn(10d);
        double amount = 1000000000d;
        double monthPaymentAmount = 10000d;
        assertThrows(CannotBePayedException.class, () -> creditCalculator.calculateOverpayment(amount, monthPaymentAmount, ClientType.GOVERMENT));
    }

    @Test
    public void calculateOverpaymentOnManyYearCreditTest() {
        when(centralBankService.getKeyRate()).thenReturn(10d);
        double amount = 10000d;
        double monthPaymentAmount = 500d;
        double result = creditCalculator.calculateOverpayment(amount, monthPaymentAmount, ClientType.INDIVIDUAL);
        Assertions.assertEquals(1824d, result);
    }

    @Test
    public void calculateOverpaymentWhenNoConnectionTest() {
        when(centralBankService.getKeyRate()).thenThrow(CentralBankNotRespondingException.class);
        when(centralBankService.getDefaultCreditRate()).thenReturn(30d);
        double amount = 100000d;
        double monthPaymentAmount = 10000d;
        double result = creditCalculator.calculateOverpayment(amount, monthPaymentAmount, ClientType.GOVERMENT);
        Assertions.assertEquals(33000d, result);
//        assertThrows(CentralBankNotRespondingException.class, () -> creditCalculator.calculateOverpayment(amount, monthPaymentAmount, ClientType.GOVERMENT));
    }

    @ParameterizedTest
    @EnumSource(ClientType.class)
    public void testWithEnumSource(ClientType clientType) {
        when(centralBankService.getKeyRate()).thenReturn(10d);
        double amount = 100000d;
        double monthPaymentAmount = 10000d;
        double result = creditCalculator.calculateOverpayment(amount, monthPaymentAmount, clientType);

        double overpayment;
        if (clientType == ClientType.GOVERMENT) {
            overpayment = 10000d;
        } else if (clientType == ClientType.BUSINESS) {
            overpayment = 11000d;
        } else {
            overpayment = 12000d;
        }

        Assertions.assertEquals(overpayment, result);
    }
}