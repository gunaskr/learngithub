package com.example.webapp.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MathUtilsTest {
    @Test
    void testAdd() {
        assertEquals(5, MathUtils.add(2, 3));
        assertEquals(0, MathUtils.add(-2, 2));
        assertEquals(-5, MathUtils.add(-2, -3));
    }
}
