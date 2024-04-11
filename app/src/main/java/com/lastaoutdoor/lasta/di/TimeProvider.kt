package com.lastaoutdoor.lasta.di

import java.time.LocalDate

interface TimeProvider {
  fun currentDate(): LocalDate
}
