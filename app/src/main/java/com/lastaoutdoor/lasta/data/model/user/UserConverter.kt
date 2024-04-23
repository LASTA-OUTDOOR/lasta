package com.lastaoutdoor.lasta.data.model.user

fun UserPreferences.toUserModel(): UserModel {
  return UserModel(
      userId = uid,
      userName = userName,
      email = email,
      profilePictureUrl = profilePictureUrl,
      hikingLevel = hikingLevel)
}
