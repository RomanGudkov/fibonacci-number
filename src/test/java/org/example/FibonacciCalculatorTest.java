package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FibonacciCalculatorTest {
    private FibonacciCalculator fibonacciCalculator;

    @BeforeEach
    public void setUp() {
        fibonacciCalculator = new FibonacciCalculator();
    }

    @DisplayName("Get numbers by many index")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    public void givenManyIndex_whenGetFibonacciNumber_thenManyNumbers(int index) {
        int[] expectedNumbers = {1, 1, 2, 3, 5, 8, 13, 21, 34, 55};
        assertEquals(expectedNumbers[index - 1], fibonacciCalculator.getFibonacciNumber(index));
    }

    @Test
    @DisplayName("Get exception by zero index")
    public void givenZeroIndex_whenGetFibonacciNumber_thenException() {
        int index = 0;
        assertThrows(IllegalArgumentException.class, () ->
            fibonacciCalculator.getFibonacciNumber(index)
        );
    }

    @DisplayName("Get number by 1 and 2 index")
    @ParameterizedTest
    @ValueSource(ints = {1, 2})
    public void givenTwoIndex_whenGetFibonacciNumber_thenGetOne(int index) {
        assertEquals(1, fibonacciCalculator.getFibonacciNumber(index));
    }
}
