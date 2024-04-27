package com.lastaoutdoor.lasta.models.profile

private const val DAYS_IN_A_WEEK = 7
private const val WEEKS_IN_A_MONTH = 5
private const val MONTHS_IN_A_YEAR = 12

enum class TimeFrame {
  W,
  M,
  Y,
  ALL;

  fun toInt(): Int {
    return when (this) {
      W -> DAYS_IN_A_WEEK
      M -> WEEKS_IN_A_MONTH
      Y -> MONTHS_IN_A_YEAR
      ALL -> 0
    }
  }
}

interface SubTimeFrames {
  override fun toString(): String
}

enum class DaysInWeek : SubTimeFrames {
  Mon,
  Tue,
  Wed,
  Thu,
  Fri,
  Sat,
  Sun;

  override fun toString(): String {
    return name
  }
}

enum class WeeksInMonth : SubTimeFrames {
  Week1,
  Week2,
  Week3,
  Week4,
  Week5;

  override fun toString(): String {
    return name
  }
}

enum class MonthsInYear : SubTimeFrames {
  Jan,
  Feb,
  Mar,
  Apr,
  May,
  Jun,
  Jul,
  Aug,
  Sep,
  Oct,
  Nov,
  Dec;

  override fun toString(): String {
    return name[0].toString()
  }
}

data class Year(val year: Int) : SubTimeFrames {
  override fun toString(): String {
    return year.toString()
  }
}
