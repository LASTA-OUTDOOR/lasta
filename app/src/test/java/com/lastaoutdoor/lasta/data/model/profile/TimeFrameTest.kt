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
        assertEquals("J",MonthsInYear.Jan.toString())
        assertEquals("F",MonthsInYear.Feb.toString())
        assertEquals("M",MonthsInYear.Mar.toString())
        assertEquals("A",MonthsInYear.Apr.toString())
        assertEquals("M",MonthsInYear.May.toString())
        assertEquals("J",MonthsInYear.Jun.toString())
        assertEquals("J",MonthsInYear.Jul.toString())
        assertEquals("A",MonthsInYear.Aug.toString())
        assertEquals("S",MonthsInYear.Sep.toString())
        assertEquals("O",MonthsInYear.Oct.toString())
        assertEquals("N",MonthsInYear.Nov.toString())
        assertEquals("D",MonthsInYear.Dec.toString())
    }

    @Test
    fun `The toString() from DaysInWeek dataclass returns the right value`() {
        assertEquals("Mon",DaysInWeek.Mon.toString())
        assertEquals("Tue",DaysInWeek.Tue.toString())
        assertEquals("Wed",DaysInWeek.Wed.toString())
        assertEquals("Thu",DaysInWeek.Thu.toString())
        assertEquals("Fri",DaysInWeek.Fri.toString())
        assertEquals("Sat",DaysInWeek.Sat.toString())
        assertEquals("Sun",DaysInWeek.Sun.toString())

    }
    @Test
    fun `The toString() from WeeksInMonth dataclass returns the right value`() {
        assertEquals("Week1",WeeksInMonth.Week1.toString())
        assertEquals("Week2",WeeksInMonth.Week2.toString())
        assertEquals("Week3",WeeksInMonth.Week3.toString())
        assertEquals("Week4",WeeksInMonth.Week4.toString())
        assertEquals("Week5",WeeksInMonth.Week5.toString())

    }

    @Test
    fun `The toString() from Year dataclass returns the right value`() {
        assertEquals("2021",Year(2021).toString())
    }
}
