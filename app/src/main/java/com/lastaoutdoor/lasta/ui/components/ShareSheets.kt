package com.lastaoutdoor.lasta.ui.components

import android.content.Context
import android.content.Intent
import com.lastaoutdoor.lasta.R
import com.lastaoutdoor.lasta.models.activity.Activity

/**
 * Share a text to other apps (share sheet)
 *
 * @param content the text to share
 * @param context the context of the app
 * @return void
 */
private fun shareText(content: String, context: Context) {
  val sendIntent: Intent =
      Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, content)
        type = "text/plain"
      }
  context.startActivity(Intent.createChooser(sendIntent, context.getString(R.string.share_title)))
}

/**
 * Starts a share sheet to share an activity
 *
 * @param activity the activity to share
 * @param context the context of the app
 * @return void
 */
fun shareActivity(activity: Activity, context: Context) {

  val textFromActivity =
      context.getString(R.string.share_body) +
          "\n${activity.name} \n" +
          context.getString(R.string.share_base_url) +
          activity.activityId

  shareText(textFromActivity, context)
}
