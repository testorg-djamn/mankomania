package com.example.mankomania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.mankomania.logik.Pseudo;
import com.example.mankomania.screens.MainActivityLogin;

import org.junit.jupiter.api.Test;


public class PseudoTest {
    @Test
    public void testCalc() {
        assertEquals(Pseudo.calc(), 3);
    }
}
