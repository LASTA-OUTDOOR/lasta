package com.lastaoutdoor.lasta.viewmodel

import androidx.lifecycle.ViewModel
import com.lastaoutdoor.lasta.repository.SocialRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SocialViewModel @Inject constructor(private val repository: SocialRepository) : ViewModel() {

  val friends = repository.getFriends()

  val isConnected = repository.isConnected
}
