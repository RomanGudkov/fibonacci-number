package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class FibonacciServiceTest {
    private final FibonacciRepository repository = Mockito.mock(FibonacciRepository.class);
    private final FibonacciCalculator calculator = Mockito.mock(FibonacciCalculator.class);
    private final FibonacciService service =
            new FibonacciService(repository, calculator);

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})    // values 1, 1, 2, 3, 5, 8, 13, 21, 34, 55
    @DisplayName("Get number by index")
    public void givenIndex_whenSavedInDatabase_thenGetNumber(int index) {
        FibonacciNumber checkNumber = new FibonacciNumber();
        checkNumber.setIndex(index);
        when(repository.findByIndex(index)).thenReturn(Optional.of(checkNumber));
        assertEquals(index, service.fibonacciNumber(index).getIndex());
        verify(repository, times(1)).findByIndex(index);
        verify(calculator, times(0)).getFibonacciNumber(index);
        verify(repository, times(0)).save(checkNumber);

    }

    @Test
    @DisplayName("Set number by index")
    public void givenIndex_whenNotSavedInDatabase_thenSetNumberFromDatabase() {
        int index = 4, value = 3;
        when(calculator.getFibonacciNumber(index)).thenReturn(value);
        assertEquals(value, service.fibonacciNumber(index).getValue());
        verify(repository, times(1)).findByIndex(index);
        verify(calculator, times(1)).getFibonacciNumber(index);
        verify(repository, times(1));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    @DisplayName("Get by zero index")
    public void givenZeroIndex_whenMakeRequest_thenGetException(int index) {
        assertThrows(IllegalArgumentException.class, () ->
                service.fibonacciNumber(index)
        );
    }
}
