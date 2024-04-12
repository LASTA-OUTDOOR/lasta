package com.lastaoutdoor.lasta.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class DiscoveryScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<DiscoveryScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("discoveryScreen") }) {

  val discoveryScreen: KNode = onNode { hasTestTag("discoveryScreen") }
  val discoveryContent: KNode = onNode { hasTestTag("discoveryContent") }
  val floatingActionButtons: KNode = onNode { hasTestTag("floatingActionButtons") }
  val outdoorActivityList: KNode = onNode { hasTestTag("outdoorActivityList") }
  val outdoorActivityItem: KNode = onNode { hasTestTag("outdoorActivityItem") }
  val activityDialog: KNode = onNode { hasTestTag("activityDialog") }
}
