package com.lastaoutdoor.lasta.utils

import com.lastaoutdoor.lasta.data.model.profile.TimeFrame
import com.lastaoutdoor.lasta.di.TimeProvider
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkObject
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*
import org.junit.Assert.assertEquals
import org.junit.Test

class HelperFunctionsTest {
  @Test
  fun `calculateTimeRange works in general case`() {
    // Start mocking static methods of LocalDate
    val mockTimeProvider = mockk<TimeProvider>()
    every { mockTimeProvider.currentDate() } returns
        LocalDate.of(2022, 4, 15) // For example, April 15, 2022 Friday

    // Now calling LocalDate.now() within calculateTimeRange will return the mocked date

    // Testing for the Week TimeFrame
    var (start, end) = calculateTimeRangeUntilNow(TimeFrame.W, mockTimeProvider)

    // Expected values for the Week TimeFrame
    var expectedStart =
        LocalDate.of(2022, 4, 11) // Assuming TimeFrame.W starts on Monday of the week
    var expectedEnd = LocalDate.of(2022, 4, 15).atTime(23, 59, 59) // The end of the mocked day

    // Assert with expected values
    assertEquals(
        expectedStart.atStartOfDay(ZoneId.systemDefault()).toInstant(),
        start.toDate().toInstant().truncatedTo(ChronoUnit.SECONDS))
    assertEquals(
        expectedEnd.atZone(ZoneId.systemDefault()).toInstant(),
        end.toDate().toInstant().truncatedTo(ChronoUnit.SECONDS))

    // Testing for the Month TimeFrame
    val rangeMonth = calculateTimeRangeUntilNow(TimeFrame.M, mockTimeProvider)
    start = rangeMonth.first
    end = rangeMonth.second

    // Expected values for the Month TimeFrame
    expectedStart = LocalDate.of(2022, 4, 1) // The first day of the month
    expectedEnd = LocalDate.of(2022, 4, 15).atTime(23, 59, 59) // The end of the mocked day

    // Assert with expected values
    assertEquals(
        expectedStart.atStartOfDay(ZoneId.systemDefault()).toInstant(),
        start.toDate().toInstant().truncatedTo(ChronoUnit.SECONDS))
    assertEquals(
        expectedEnd.atZone(ZoneId.systemDefault()).toInstant(),
        end.toDate().toInstant().truncatedTo(ChronoUnit.SECONDS))

    // Testing for the Year TimeFrame
    val rangeYear = calculateTimeRangeUntilNow(TimeFrame.Y, mockTimeProvider)
    start = rangeYear.first
    end = rangeYear.second

    // Expected values for the Month TimeFrame
    expectedStart = LocalDate.of(2022, 1, 1) // The first day of the month
    expectedEnd = LocalDate.of(2022, 4, 15).atTime(23, 59, 59) // The end of the mocked day

    // Assert with expected values
    assertEquals(
        expectedStart.atStartOfDay(ZoneId.systemDefault()).toInstant(),
        start.toDate().toInstant().truncatedTo(ChronoUnit.SECONDS))
    assertEquals(
        expectedEnd.atZone(ZoneId.systemDefault()).toInstant(),
        end.toDate().toInstant().truncatedTo(ChronoUnit.SECONDS))

    // Testing for the All TimeFrame
    val rangeAll = calculateTimeRangeUntilNow(TimeFrame.ALL, mockTimeProvider)
    start = rangeAll.first
    end = rangeAll.second

    // Expected values for the Month TimeFrame
    expectedStart = firstValidDate // The first day of the month
    expectedEnd = LocalDate.of(2022, 4, 15).atTime(23, 59, 59) // The end of the mocked day

    // Assert with expected values
    assertEquals(
        expectedStart.atStartOfDay(ZoneId.systemDefault()).toInstant(),
        start.toDate().toInstant().truncatedTo(ChronoUnit.SECONDS))
    assertEquals(
        expectedEnd.atZone(ZoneId.systemDefault()).toInstant(),
        end.toDate().toInstant().truncatedTo(ChronoUnit.SECONDS))

    // Clear MockK static mocks after the test
    unmockkObject(mockTimeProvider)
  }

  @Test
  fun `createDateTime returns correct Date`() {
    val year = 2022
    val month = 4 // April
    val day = 6
    val hour = 15 // 3 PM
    val minute = 30
    val second = 45

    val result = createDateTime(year, month, day, hour, minute, second)

    val expectedDate =
        Date.from(
            LocalDate.of(year, month, day)
                .atTime(hour, minute, second)
                .atZone(ZoneId.systemDefault())
                .toInstant())

    assertEquals(expectedDate, result)
  }
}
    /*
      @Test
      fun `weekDisplay returns correct distances per day`() {
        // Given a list of trails with known distances and dates
        val trails =
            listOf(
                Trail(
                    timeStarted =
                        Date.from(
                            LocalDate.of(2022, 4, 15)
                                .atTime(8, 0, 0)
                                .atZone(ZoneId.systemDefault())
                                .toInstant()),
                    distanceInMeters = 3000),
                Trail(
                    timeStarted =
                        Date.from(
                            LocalDate.of(2022, 4, 11)
                                .atTime(8, 0, 0)
                                .atZone(ZoneId.systemDefault())
                                .toInstant()),
                    distanceInMeters = 2525),
                Trail(
                    timeStarted =
                        Date.from(
                            LocalDate.of(2022, 4, 12)
                                .atTime(8, 0, 0)
                                .atZone(ZoneId.systemDefault())
                                .toInstant()),
                    distanceInMeters = 5100),
                Trail(
                    timeStarted =
                        Date.from(
                            LocalDate.of(2022, 4, 13)
                                .atTime(8, 0, 0)
                                .atZone(ZoneId.systemDefault())
                                .toInstant()),
                    distanceInMeters = 2100),
                Trail(
                    timeStarted =
                        Date.from(
                            LocalDate.of(2022, 4, 14)
                                .atTime(8, 0, 0)
                                .atZone(ZoneId.systemDefault())
                                .toInstant()),
                    distanceInMeters = 3400),
                Trail(
                    timeStarted =
                        Date.from(
                            LocalDate.of(2022, 4, 11)
                                .atTime(8, 0, 0)
                                .atZone(ZoneId.systemDefault())
                                .toInstant()),
                    distanceInMeters = 3434),
                Trail(
                    timeStarted =
                        Date.from(
                            LocalDate.of(2022, 4, 17)
                                .atTime(8, 0, 0)
                                .atZone(ZoneId.systemDefault())
                                .toInstant()),
                    distanceInMeters = 1),
                Trail(
                    timeStarted =
                        Date.from(
                            LocalDate.of(2022, 4, 17)
                                .atTime(8, 0, 0)
                                .atZone(ZoneId.systemDefault())
                                .toInstant()),
                    distanceInMeters = 0),
                Trail(
                    timeStarted =
                        Date.from(
                            LocalDate.of(2022, 4, 16)
                                .atTime(8, 0, 0)
                                .atZone(ZoneId.systemDefault())
                                .toInstant()),
                    distanceInMeters = 400),
                Trail(
                    timeStarted =
                        Date.from(
                            LocalDate.of(2022, 4, 15)
                                .atTime(8, 0, 0)
                                .atZone(ZoneId.systemDefault())
                                .toInstant()),
                    distanceInMeters = 1549),
            )

        // When weekDisplay is called
        val result = chartDisplayValues(trails, TimeFrame.W)

        // Then the result should match expected distances
        val expected = listOf(5.959f, 5.1f, 2.1f, 3.4f, 4.549f, 0.4f, 0.001f)
        assertEquals(expected, result)
      }

      @Test
      fun `dayOfWeekFromDate returns correct day index`() {
        val date =
            Date.from(
                LocalDate.of(2022, 4, 15).atTime(8, 0, 0).atZone(ZoneId.systemDefault()).toInstant())

        val result = indexDayOfWeekFromDate(date)

        val expected = 4 // Friday

        assertEquals(expected, result)
      }
    }

         */
