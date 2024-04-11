package com.lastaoutdoor.lasta.data.model.profile;

import junit.framework.TestCase.assertEquals
import org.junit.Test;

class TimeFrameTest {

    @Test
    fun `The toInt() from TimeFrame enum returns the right value`(){
        assertEquals(7,TimeFrame.W.toInt())
        assertEquals(5,TimeFrame.M.toInt())
        assertEquals(12,TimeFrame.Y.toInt())
        assertEquals(0,TimeFrame.ALL.toInt())
    }

    @Test
    fun `The toString() from MonthsInYear enum returns the right value`() {
        assertEquals("J",MonthsInYear.Jul.toString())
        assertEquals("M",MonthsInYear.Mar.toString())
    }

    @Test
    fun `The toString() from Year dataclass returns the right value`() {
        assertEquals("2021",Year(2021).toString())
    }
}
