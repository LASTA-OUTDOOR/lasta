package com.lastaoutdoor.lasta.data.time

import java.time.LocalDate

class RealTimeProvider : TimeProvider {
  override fun currentDate(): LocalDate = LocalDate.now()
}
