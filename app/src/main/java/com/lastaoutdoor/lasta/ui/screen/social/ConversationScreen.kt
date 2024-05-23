package com.lastaoutdoor.lasta.ui.screen.social

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.activity.Difficulty
import com.lastaoutdoor.lasta.models.social.ConversationModel
import com.lastaoutdoor.lasta.models.social.MessageModel
import com.lastaoutdoor.lasta.models.user.UserModel
import com.lastaoutdoor.lasta.ui.components.SeparatorComponent
import com.lastaoutdoor.lasta.ui.screen.moreinfo.TopBarLogo
import com.lastaoutdoor.lasta.ui.screen.social.components.ProfilePicture
import com.lastaoutdoor.lasta.ui.theme.GreenDifficulty
import com.lastaoutdoor.lasta.ui.theme.RedDifficulty
import com.lastaoutdoor.lasta.ui.theme.YellowDifficulty

@Composable
fun ConversationScreen(
    conversationModel: ConversationModel?,
    refresh: () -> Unit,
    changeActivityToDisplay: (String) -> Unit,
    user: UserModel,
    friend: UserModel,
    send: (String) -> Unit,
    navigateBack: () -> Unit,
    navigateToMoreInfo: () -> Unit
) {
  Column {
    if (conversationModel == null || conversationModel.members.isEmpty()) {
      refresh.invoke()
      return
    }

    Header(navigateBack, friend)
    SeparatorComponent()

    // focus manger
    val focusManger = LocalFocusManager.current

    Column(
        modifier = Modifier.fillMaxSize().testTag("ConversationScreen"),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start) {
          LazyColumn(
              modifier =
                  Modifier.fillMaxWidth().fillMaxSize(0.8f).pointerInput(Unit) {
                    detectTapGestures(onPress = { focusManger.clearFocus() })
                  },
              reverseLayout = true) {
                // display 10 Text for testing
                items(conversationModel.messages.size) {
                  // see this function maybe can be wrong
                  ShowMessage(
                      conversationModel.messages[it],
                      user,
                      changeActivityToDisplay,
                      navigateToMoreInfo)
                }
              }

          // horizontal bar
          SeparatorComponent()

          // Icon to display the send message button
          Row(
              modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(10.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically) {

                // textfield value
                var message by remember { mutableStateOf("") }

                // Message textfield
                TextField(
                    value = message,
                    onValueChange = { message = it },
                    modifier = Modifier.fillMaxWidth(0.8f).testTag("MessageTextField"))

                // Send message button
                IconButton(
                    onClick = {
                      focusManger.clearFocus()
                      send.invoke(message)
                      message = ""
                    },
                    colors =
                        IconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContentColor =
                                MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.38f),
                            disabledContainerColor =
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.38f)),
                    modifier =
                        Modifier.padding(8.dp)
                            .align(Alignment.CenterVertically)
                            .testTag("SendMessageButton")) {
                      Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send message")
                    }
              }
        }
  }
}

// Top bar of the conversation screen
@Composable
fun Header(backCallBack: () -> Unit, friend: UserModel) {
  Row(
      modifier = Modifier.fillMaxWidth().padding(16.dp).testTag("ConversationScreenHeader"),
      horizontalArrangement = Arrangement.Start,
      verticalAlignment = Alignment.CenterVertically) {
        // back button
        TopBarLogo(R.drawable.arrow_back) { backCallBack() }
        Spacer(modifier = Modifier.padding(20.dp))
        ProfilePicture(friend.profilePictureUrl ?: "")
        Spacer(modifier = Modifier.padding(8.dp))
        Text(friend.userName)
      }
}

@Composable
fun ShowMessage(
    message: MessageModel,
    user: UserModel,
    changeActivityToDisplay: (String) -> Unit,
    navigateToMoreInfo: () -> Unit
) {

  var arrangement = Arrangement.Start
  var backgroundColor = MaterialTheme.colorScheme.surfaceContainer
  var textColor = MaterialTheme.colorScheme.onSurface

  if (message.from == user) {
    arrangement = Arrangement.End
    backgroundColor = MaterialTheme.colorScheme.primary
    textColor = MaterialTheme.colorScheme.onPrimary
  }

  // Check if the message is a shared activity by checking if it starts with "|$@!|"
  val isActivityShared = message.content.startsWith("|$@!|")
  val messageParts = message.content.split("|")
  val activityName = if (isActivityShared) messageParts[3] else message.content
  val activityId = if (isActivityShared) messageParts[2] else ""
  val activityType =
      if (isActivityShared) ActivityType.valueOf(messageParts[4]) else ActivityType.HIKING
  val activityDifficulty =
      if (isActivityShared) Difficulty.valueOf(messageParts[5]) else Difficulty.EASY

  Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = arrangement) {
    if (isActivityShared) {
      // Display the activity card
      ActivityCard(
          activityId,
          activityName,
          activityType,
          activityDifficulty,
          changeActivityToDisplay,
          navigateToMoreInfo) // Display the ActivityCard
    } else {
      Card(
          modifier =
              Modifier.padding(8.dp)
                  .widthIn(0.dp, LocalConfiguration.current.screenWidthDp.dp * 0.55f),
          colors =
              CardColors(
                  containerColor = backgroundColor,
                  contentColor = textColor,
                  disabledContentColor = backgroundColor,
                  disabledContainerColor = textColor,
              )) {
            Text(text = message.content, modifier = Modifier.padding(8.dp))
          }
    }
  }
}

/** Display the activity card of a shared activity */
@Composable
fun ActivityCard(
    activityId: String,
    activityName: String,
    activityType: ActivityType,
    activityDifficulty: Difficulty,
    changeActivityToDisplay: (String) -> Unit,
    navigateToMoreInfo: () -> Unit
) {
  // Set the color of the card based on the difficulty of the activity
  val difficultyColor =
      when (activityDifficulty) {
        Difficulty.EASY -> GreenDifficulty
        Difficulty.NORMAL -> YellowDifficulty
        Difficulty.HARD -> RedDifficulty
      }
  Card(
      modifier =
          Modifier.padding(8.dp)
              .testTag("ActivityShared$activityId")
              .fillMaxWidth(0.7f)
              .clickable(
                  onClick = {
                    changeActivityToDisplay(activityId)
                    navigateToMoreInfo()
                  }),
      shape = RoundedCornerShape(8.dp),
      elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(8.dp)) {
              Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                Box( // Activity type
                    modifier =
                        Modifier.shadow(4.dp, RoundedCornerShape(30))
                            .background(difficultyColor, RoundedCornerShape(30))
                            .padding(PaddingValues(4.dp))) {
                      Text(
                          text =
                              LocalContext.current.getString(
                                  when (activityType) {
                                    ActivityType.HIKING -> R.string.hiking
                                    ActivityType.CLIMBING -> R.string.climbing
                                    ActivityType.BIKING -> R.string.biking
                                  }),
                          style = MaterialTheme.typography.labelMedium,
                          color = MaterialTheme.colorScheme.onPrimary)
                    }
              }
              Spacer(modifier = Modifier.height(6.dp))
              Row( // Activity name
                  modifier = Modifier.fillMaxWidth().padding(8.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.Center) {
                    Text(
                        text = activityName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Activity more infos",
                        tint = MaterialTheme.colorScheme.primary)
                  }
              Spacer(modifier = Modifier.height(10.dp))
            }
      }
}
