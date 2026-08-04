package com.epiis.apicitasmedicas.helper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class GenericHelperTest {

    @Test
    void testFollowCodeGeneration() {
        String code1 = GenericHelper.followCodeGeneration();
        String code2 = GenericHelper.followCodeGeneration();

        assertNotNull(code1);
        assertEquals(7, code1.length());
        assertTrue(code1.matches("^[0-9A-Z]{7}$"));
        
        assertNotNull(code2);
        assertNotEquals(code1, code2); // Highly unlikely to be equal
    }
}
