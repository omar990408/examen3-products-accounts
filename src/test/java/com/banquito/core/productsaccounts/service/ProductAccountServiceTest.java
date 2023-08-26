package com.banquito.core.productsaccounts.service;

import com.banquito.core.productsaccounts.exception.CRUDException;
import com.banquito.core.productsaccounts.model.ProductAccount;
import com.banquito.core.productsaccounts.repository.ProductAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductAccountServiceTest {
    @InjectMocks
    private ProductAccountService underTest;

    @Mock
    private ProductAccountRepository productAccountRepository;

    @Test
    void listAllActivesTest() {
        //given
        ProductAccount productAccount = new ProductAccount();
        productAccount.setId("1");
        productAccount.setName("Product Account 1");
        productAccount.setDescription("Product Account 1 Description");
        productAccount.setMinimunBalance(BigDecimal.valueOf(100));
        productAccount.setPayInterest("Y");
        productAccount.setAcceptsChecks("Y");
        productAccount.setState("ACT");
        productAccount.setCreationDate(new Date());

        ProductAccount productAccount2 = new ProductAccount();
        productAccount2.setId("2");
        productAccount2.setName("Product Account 1");
        productAccount2.setDescription("Product Account 1 Description");
        productAccount2.setMinimunBalance(BigDecimal.valueOf(100));
        productAccount2.setPayInterest("Y");
        productAccount2.setAcceptsChecks("Y");
        productAccount2.setState("ACT");
        productAccount2.setCreationDate(new Date());
        //when
        given(productAccountRepository.findByState("ACT")).willReturn(List.of(productAccount, productAccount2));
        List<ProductAccount> productAccounts =  underTest.listAllActives();
        //then
        assertEquals(2, productAccounts.size());
    }

    @Test
    void obtainByIdTest() {
        //given
        String id = "Id1";
        ProductAccount productAccount = new ProductAccount();
        productAccount.setId(id);
        productAccount.setName("Product Account 1");
        productAccount.setDescription("Product Account 1 Description");
        productAccount.setMinimunBalance(BigDecimal.valueOf(100));
        productAccount.setPayInterest("Y");
        productAccount.setAcceptsChecks("Y");
        productAccount.setState("ACT");
        productAccount.setCreationDate(new Date());
        given(productAccountRepository.findById(id)).willReturn(Optional.of(productAccount));
        //when
        ProductAccount result = underTest.obtainById(id);
        //then
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(productAccountRepository).findById(stringArgumentCaptor.capture());
        String idCaptor = stringArgumentCaptor.getValue();
        assertEquals(id, result.getId());
    }

    @Test
    void obtainByIdTestThrowException() {
        //given
        String id = "Id1";
        given(productAccountRepository.findById(id)).willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> underTest.obtainById(id))
                .isInstanceOf(CRUDException.class)
                .hasMessageContaining("Product Account with id: {" + id + "} does not exist");
    }

    @Test
    void createTest() {
        //given
        ProductAccount productAccount = new ProductAccount();
        productAccount.setId("1");
        productAccount.setName("Product Account 1");
        productAccount.setDescription("Product Account 1 Description");
        productAccount.setMinimunBalance(BigDecimal.valueOf(100));
        productAccount.setPayInterest("Y");
        productAccount.setAcceptsChecks("Y");
        productAccount.setState("ACT");
        productAccount.setCreationDate(new Date());
        //when
        given(productAccountRepository.save(productAccount)).willReturn(productAccount);
        underTest.create(productAccount);
        //then
        ArgumentCaptor<ProductAccount> productAccountCaptor = ArgumentCaptor.forClass(ProductAccount.class);
        verify(productAccountRepository).save(productAccountCaptor.capture());
        ProductAccount productAccountCaptured = productAccountCaptor.getValue();
        assertEquals(productAccount, productAccountCaptured);
    }
}