package com.dragondevl.clog;

import com.dragondevl.utils.NumberUtil;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testIsNumeric() {
        assertTrue(NumberUtil.isNumeric("-1"));
        assertTrue(NumberUtil.isNumeric("-10"));
        assertTrue(NumberUtil.isNumeric("-10.2"));
        assertTrue(NumberUtil.isNumeric("-10."));
        assertTrue(NumberUtil.isNumeric("1."));
        assertTrue(NumberUtil.isNumeric("1.3"));
        assertTrue(NumberUtil.isNumeric("12.3"));
        assertTrue(NumberUtil.isNumeric("3"));
        assertTrue(NumberUtil.isNumeric("+6"));
        assertTrue(NumberUtil.isNumeric("+-6"));
        assertFalse(NumberUtil.isNumeric("i6"));
        assertFalse(NumberUtil.isNumeric("6q"));
        assertFalse(NumberUtil.isNumeric("-6q"));
        assertFalse(NumberUtil.isNumeric("+6q"));
        assertFalse(NumberUtil.isNumeric("+6q.5"));

        //assertTrue(NumberUtil.isNumeric("+6gg"));
    }

}