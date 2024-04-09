package com.lastaoutdoor.lasta.di

import java.time.LocalDate

class RealTimeProvider : TimeProvider {
  override fun currentDate(): LocalDate = LocalDate.now()
}
