package com.lastaoutdoor.lasta.data.time

import java.time.LocalDate

interface TimeProvider {
  fun currentDate(): LocalDate
}
