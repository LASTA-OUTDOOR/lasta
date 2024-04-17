package com.lastaoutdoor.lasta.ui.screen.social.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.lastaoutdoor.lasta.viewmodel.SocialViewModel

@Composable
fun MessageList(viewModel: SocialViewModel = hiltViewModel()) {
    if (viewModel.messages.isNullOrEmpty()) {
        MessageMissing()
        return
    }
    LazyColumn { items(10) { Text("$it - to be implemented") } }
}