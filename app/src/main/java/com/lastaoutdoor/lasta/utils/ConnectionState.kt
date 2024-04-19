package com.lastaoutdoor.lasta.utils

sealed class ConnectionState() {
  object CONNECTED : ConnectionState()

  object OFFLINE : ConnectionState()
}
