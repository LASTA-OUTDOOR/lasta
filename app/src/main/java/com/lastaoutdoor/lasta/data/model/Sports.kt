package com.lastaoutdoor.lasta.data.model

enum class Sports {
  HIKING,
  CLIMBING;

  override fun toString(): String {
    return name.lowercase().replaceFirstChar { it.uppercase() }
  }
}
