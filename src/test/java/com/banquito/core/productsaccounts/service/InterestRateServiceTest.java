package com.banquito.core.productsaccounts.service;

import com.banquito.core.productsaccounts.exception.CRUDException;
import com.banquito.core.productsaccounts.model.InterestRate;
import com.banquito.core.productsaccounts.repository.InterestRateRepository;
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
class InterestRateServiceTest {
    @InjectMocks
    private InterestRateService underTest;
    @Mock
    private InterestRateRepository interestRateRepository;

    @Test
    void listAllActivesTest() {
        //given
        InterestRate interestRate = new InterestRate();
        interestRate.setId(1);
        interestRate.setName("Interest Rate 1");
        interestRate.setInterestRate(BigDecimal.valueOf(0.1));
        interestRate.setState("ACT");
        interestRate.setStart(new Date());
        interestRate.setEnd(new Date());
        InterestRate interestRate2 = new InterestRate();
        interestRate.setId(2);
        interestRate.setName("Interest Rate 1");
        interestRate.setInterestRate(BigDecimal.valueOf(0.1));
        interestRate.setState("ACT");
        interestRate.setStart(new Date());
        interestRate.setEnd(new Date());
        //when
        given(interestRateRepository.findByState("ACT")).willReturn(List.of(interestRate, interestRate2));
        List<InterestRate> interestRates = underTest.listAllActives();
        //then
        assertEquals(2, interestRates.size());
    }

    @Test
    void obtainByIdTest() {
        //given
        InterestRate interestRate = new InterestRate();
        interestRate.setId(1);
        interestRate.setName("Interest Rate 1");
        interestRate.setInterestRate(BigDecimal.valueOf(0.1));
        interestRate.setState("ACT");
        interestRate.setStart(new Date());
        interestRate.setEnd(new Date());
        //when
        given(interestRateRepository.findById(1)).willReturn(java.util.Optional.of(interestRate));
        underTest.obtainById(1);
        //then
        ArgumentCaptor<Integer> interestRateIdCaptor = ArgumentCaptor.forClass(Integer.class);
        InterestRate interestRateResult = underTest.obtainById(1);
        assertEquals(1, interestRateResult.getId());
    }

    @Test
    void obtainByIdTestThrowException() {
        //given
        int id = 1;
        //when
        given(interestRateRepository.findById(1)).willReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> underTest.obtainById(1))
                .isInstanceOf(CRUDException.class)
                .hasMessageContaining("Interest Rate with id: {" + id + "} does not exist");
    }

    @Test
    void create() {
        //given
        InterestRate interestRate = new InterestRate();
        interestRate.setId(1);
        interestRate.setName("Interest Rate 1");
        interestRate.setInterestRate(BigDecimal.valueOf(0.1));
        interestRate.setState("ACT");
        interestRate.setStart(new Date());
        interestRate.setEnd(new Date());
        //when
        given(interestRateRepository.save(interestRate)).willReturn(interestRate);
        underTest.create(interestRate);
        //then
        ArgumentCaptor<InterestRate> interestRateCaptor = ArgumentCaptor.forClass(InterestRate.class);
        verify(interestRateRepository).save(interestRateCaptor.capture());
        InterestRate interestCaptured = interestRateCaptor.getValue();
        assertEquals(interestRate, interestCaptured);
    }

    @Test
    void updateTest() {
        //given
        int id = 1;

        InterestRate interestRateCreated = new InterestRate();
        interestRateCreated.setId(1);
        interestRateCreated.setName("Interest Rate 1");
        interestRateCreated.setInterestRate(BigDecimal.valueOf(0.1));
        interestRateCreated.setState("ACT");
        interestRateCreated.setStart(new Date());
        interestRateCreated.setEnd(new Date());

        InterestRate interestRateUpdated = new InterestRate();
        interestRateUpdated.setName("Interest Rate 1 Updated");
        interestRateUpdated.setInterestRate(BigDecimal.valueOf(0.2));
        given(interestRateRepository.findById(id)).willReturn(Optional.of(interestRateCreated));
        //when
        underTest.update(id, interestRateUpdated);
        //then
        ArgumentCaptor<InterestRate> interestRateCaptor = ArgumentCaptor.forClass(InterestRate.class);
        verify(interestRateRepository).save(interestRateCaptor.capture());
        InterestRate interestCaptured = interestRateCaptor.getValue();
        assertEquals(interestRateUpdated.getName(), interestCaptured.getName());
        assertEquals(interestRateUpdated.getInterestRate(), interestCaptured.getInterestRate());

    }

    @Test
    void updateTestThrowException() {
        //given
        int id = 1;

        InterestRate interestRateCreated = new InterestRate();
        interestRateCreated.setId(1);
        interestRateCreated.setName("Interest Rate 1");
        interestRateCreated.setInterestRate(BigDecimal.valueOf(0.1));
        interestRateCreated.setState("ACT");
        interestRateCreated.setStart(new Date());
        interestRateCreated.setEnd(new Date());

        InterestRate interestRateUpdated = new InterestRate();
        interestRateUpdated.setName("Interest Rate 1 Updated");
        interestRateUpdated.setInterestRate(BigDecimal.valueOf(0.2));
        given(interestRateRepository.findById(id)).willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> underTest.update(id, interestRateUpdated))
                .isInstanceOf(CRUDException.class)
                .hasMessageContaining("Interest Rate with id: {" + id + "} does not exist");

    }

    @Test
    void inactivateThrowException() {
        //given
        int id = 1;
        given(interestRateRepository.findById(id)).willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> underTest.inactivate(id))
                .isInstanceOf(CRUDException.class)
                .hasMessageContaining("Interest Rate with id: {" + id + "} does not exist");
    }

    @Test
    void inactivateTest() {
        //given
        int id = 1;
        InterestRate interestRateCreated = new InterestRate();
        interestRateCreated.setId(1);
        interestRateCreated.setName("Interest Rate 1");
        interestRateCreated.setInterestRate(BigDecimal.valueOf(0.1));
        interestRateCreated.setState("ACT");
        interestRateCreated.setStart(new Date());
        interestRateCreated.setEnd(new Date());
        given(interestRateRepository.findById(id)).willReturn(Optional.of(interestRateCreated));
        //when
        underTest.inactivate(id);
        //then
        ArgumentCaptor<InterestRate> interestRateCaptor = ArgumentCaptor.forClass(InterestRate.class);
        verify(interestRateRepository).save(interestRateCaptor.capture());
        InterestRate interestCaptured = interestRateCaptor.getValue();
        assertEquals("INA", interestCaptured.getState());

    }
}