package com.lastaoutdoor.lasta.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.data.time.TimeProvider
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.unmockkObject
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
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

  @Test
  fun `Meters to kilometers`() {
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
  fun testGetScaledBitmapDescriptorInvalidId() {
    val context: Context = mockk(relaxed = true)
    val vectorResId = 0 // invalid resource ID

    val width = 100
    val height = 100

    val result = getScaledBitmapDescriptor(context, vectorResId, width, height)

    assertNull(result)
  }

  @Test
  fun testGetScaledBitmapDescriptorValidId() {
    // Mock dependencies
    val context: Context = mockk(relaxed = true)
    val vectorDrawable: Drawable = mockk(relaxed = true)
    val bitmap: Bitmap = mockk(relaxed = true)
    val canvas: Canvas = mockk(relaxed = true)
    val bitmapDescriptor: BitmapDescriptor = mockk()

    // Mock static methods
    mockkStatic(ContextCompat::class)
    mockkStatic(Bitmap::class)
    mockkStatic(BitmapDescriptorFactory::class)
    mockkConstructor(VectorDrawable::class)

    // Mock method calls
    every { ContextCompat.getDrawable(context, R.drawable.hiking_icon) } returns vectorDrawable
    every { Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888) } returns bitmap
    every { anyConstructed<VectorDrawable>().draw(any()) } just Runs
    every { BitmapDescriptorFactory.fromBitmap(bitmap) } returns bitmapDescriptor

    // Parameters
    val vectorResId = R.drawable.hiking_icon
    val width = 100
    val height = 100

    // Call the function
    val result = getScaledBitmapDescriptor(context, vectorResId, width, height)

    // Verify
    assertNotNull(result)
  }

  @Test
  fun testCalculateZoomLevel() {
    val radius = 5000.0
    val screenWidthPx = 1000f
    val latitude = 0.0

    val result = calculateZoomLevel(radius, screenWidthPx, latitude)

    assertNotNull(result)
  }
}
/*
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
*/
