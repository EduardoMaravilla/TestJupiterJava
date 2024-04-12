package org.eduardomaravill.classes;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class SumTest {

    private final Sum operation =  new Sum();

    @Test
    void operationSumInt() {
        int a = 10;
        int b = 10;
        int c = operation.operationSum(a,b);
        assertEquals(20, c);
        assertInstanceOf(Integer.class,c);
    }

    @Test
    void testOperationSumDouble() {
        double a = 10.5;
        double b = 10.5;
        double c = operation.operationSum(a, b);
        assertEquals(21.0, c);
        assertInstanceOf(Double.class,c);
    }

    @Test
    void testOperationSumFloat() {
        float a = 10.5f;
        float b = 10.5f;
        float c = operation.operationSum(a, b);
        assertEquals(21.0f, c);
        assertInstanceOf(Float.class,c);
    }

    @Test
    void testOperationSumLong() {
        long a = 10L;
        long b = 10L;
        long c = operation.operationSum(a, b);
        assertEquals(20L, c);
        assertInstanceOf(Long.class,c);
    }

}