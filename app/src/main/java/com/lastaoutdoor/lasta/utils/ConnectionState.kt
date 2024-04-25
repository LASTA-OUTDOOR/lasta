package com.lastaoutdoor.lasta.utils

/** Represents the current connection state of the device. */
sealed class ConnectionState {
  /** State when the device is connected to the internet. */
  object CONNECTED : ConnectionState()

  /** State when the device is not connected to the internet. */
  object OFFLINE : ConnectionState()
}
