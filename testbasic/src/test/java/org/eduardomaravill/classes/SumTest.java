package org.eduardomaravill.classes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SumTest {

    private final Sum operation =  new Sum();

    @Test
    void operationSumInt() {
        int a = 10;
        int b = 10;
        assertEquals(20, operation.operationSum(a,b));
        assertInstanceOf(Integer.class,operation.operationSum(a,b));
    }

    @Test
    void testOperationSumDouble() {
        double a = 10.5;
        double b = 10.5;
        assertEquals(21.0, operation.operationSum(a,b));
        assertInstanceOf(Double.class,operation.operationSum(a,b));
    }

    @Test
    void testOperationSumFloat() {
        float a = 10.5f;
        float b = 10.5f;
        assertEquals(21.0f, operation.operationSum(a,b));
        assertInstanceOf(Float.class,operation.operationSum(a,b));
    }

    @Test
    void testOperationSumLong() {
        long a = 100000000000000L;
        long b = 100000000000000L;
        assertEquals(200000000000000L, operation.operationSum(a,b));
        assertInstanceOf(Long.class,operation.operationSum(a,b));
    }
}