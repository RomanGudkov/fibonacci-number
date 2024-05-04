package org.example;

import org.springframework.stereotype.Service;

@Service
public class FibonacciCalculator {

    public Integer getFibonacciNumber(int index) {
        if (index < 1) {
            throw new IllegalArgumentException("Index should be greater or equal to 1");
        }
        if (index == 1 || index == 2) {
            return 1;
        }
        return getFibonacciNumber(index - 1) + getFibonacciNumber(index - 2);
    }
}
