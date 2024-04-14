package com.example.mankomania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.mankomania.logik.Pseudo;

import org.junit.jupiter.api.Test;

//import at.aau.serg.websocketdemoapp.networking.WebSocketClient;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
class ExampleUnitTest {
    @Test
    void testConcatenateStringsMethod() {
        String first = "Hello";
        String second = "World";

        String result = Pseudo.concatenateStrings(first, second);

        assertEquals("Hello World", result);
    }
}