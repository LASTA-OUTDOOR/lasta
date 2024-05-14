package com.lastaoutdoor.lasta.ui.components

import android.content.Context
import android.content.Intent
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.activity.Activity

// Opens the android share sheet, will contain a description and a link to the activity -> will be
// used to share activities
fun shareActivity(activity: Activity, context: Context) {

  val sendIntent: Intent =
      Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT,
            context.getString(R.string.share_body) +
                "\n${activity.name} \nhttps://lasta.jerem.ch/activities/${activity.activityId}")
        type = "text/plain"
      }

  context.startActivity(Intent.createChooser(sendIntent, context.getString(R.string.share_title)))
}
