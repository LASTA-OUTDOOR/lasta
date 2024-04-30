package com.lastaoutdoor.lasta.utils

import java.util.*

class HelperFunctionsTest {
  /*@Test
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

  @Test
  fun `Meters to kilomters`() {
    assert(metersToKilometers(1000L) == 1f)
  }

  @Test
  fun `Format date`() {
    val date = Date(1712864046759)

    assert(formatDate(date) == "11/04/2024")
  }

  @Test
  fun `index of day of week`() {
    val date = Date(1712864046759)

    assert(indexDayOfWeekFromDate(date) == 3)
  }

  @Test
  fun `index of Month`() {
    val date = Date(1712864046759)

    assert(indexMonthOfYearFromDate(date) == 3)
  }

  @Test
  fun `index of week of month`() {
    val date = Date(1712864046759)

    assert(indexWeekOfMonthFromDate(date) == 1)
  }

  @Test
  fun `index of year `() {
    val date = Date(1712864046759)
    val d1970 = Date(0)
    assert(indexYearFromDate(date, d1970) == 54)
  }

  @Test
  fun `Time from millis`() {
    val time = 10800000L // eq of 3 hours
    assert(timeFromMillis(time) == "03:00:00")
  }

  @Test
  fun `time from activity millis`() {
    val date1 = Date(1712864046759)
    val date2 = Date(1712864040000)
    val list = listOf(ActivitiesDatabaseType.Trail(timeStarted = date2, timeFinished = date1))
    assert(timeFromActivityInMillis(list) == 6759L)
  }

  @Test
  fun `chart display timeFrame = W`() {
    val date1 = Date(1712864046759) // 11/04
    val date2 = Date(86400000 + 1712864046759) // 12/04
    val timeFrame = TimeFrame.W
    val trail = ActivitiesDatabaseType.Trail(timeStarted = date1, distanceInMeters = 500)
    val climb = ActivitiesDatabaseType.Climb(timeStarted = date2, elevationGainedInMeters = 250)

    val list = listOf(trail, climb)
    assert(
        chartDisplayValues(list, timeFrame)[3] == 0.5f &&
            chartDisplayValues(list, timeFrame)[4] == 0.25f)
  }

  @Test
  fun `chart display timeFrame = M`() {
    val date1 = Date(1712864046759) // 11/04
    val timeFrame = TimeFrame.M
    val trail = ActivitiesDatabaseType.Trail(timeStarted = date1, distanceInMeters = 500)

    val list = listOf(trail)
    assert(chartDisplayValues(list, timeFrame)[1] == 0.5f)
  }

  @Test
  fun `chart display timeFrame = Y`() {
    val date1 = Date(1712864046759) // 11/04
    val date2 = Date(1712864046759 - 2629800000) // 11/03
    val timeFrame = TimeFrame.Y
    val trail = ActivitiesDatabaseType.Trail(timeStarted = date1, distanceInMeters = 500)
    val climb = ActivitiesDatabaseType.Climb(timeStarted = date2, elevationGainedInMeters = 250)

    val list = listOf(trail, climb)
    assert(
        chartDisplayValues(list, timeFrame)[3] == 0.5f &&
            chartDisplayValues(list, timeFrame)[2] == 0.25f)
  }

  @Test
  fun `chart display timeFrame = ALL`() {
    val date1 = Date(1712864046759) // 11/04
    val date2 = Date(1712864046759 - 2629800000) // 11/03
    val timeFrame = TimeFrame.ALL
    val trail = ActivitiesDatabaseType.Trail(timeStarted = date1, distanceInMeters = 500)
    val climb = ActivitiesDatabaseType.Climb(timeStarted = date2, elevationGainedInMeters = 250)

    val list = listOf(trail, climb)
    assert(chartDisplayValues(list, timeFrame)[0] == 0.75f)
  }

  @Test
  fun respLoading() {
    val ld1 = Response.Loading
    val ld2 = Response.Loading
    assert(ld1.javaClass == ld2.javaClass)
  }

  @Test
  fun respSuccessNotNull() {
    val ld1 = Response.Success<String>("wow")

    assert(ld1.data == "wow")
  }

  @Test
  fun respSuccessNull() {
    val ld1 = Response.Success<String>(null)

    assert(ld1.data == null)
  }

  @Test
  fun respFailure() {

    val ld2 = Response.Failure(Exception("ex"))
    assert(ld2.e.message == "ex")
  }*/
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
