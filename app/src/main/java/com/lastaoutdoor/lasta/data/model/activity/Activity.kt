package com.lastaoutdoor.lasta.data.model.activity

import com.lastaoutdoor.lasta.data.model.api.Position

open class Activity(
    open val activityId: String,
    open val osmId: Long,
    val activityType: ActivityType = ActivityType.CLIMBING,
    open val name: String = "",
    open val startPosition: Position = Position(0.0, 0.0),
    open val rating: Float = 0.0f,
    open val numRatings: Int = 0,
    open val ratings: List<Rating> = emptyList(),
    open val difficulty: Difficulty = Difficulty.EASY,
    open val activityImageUrl: String = "",
)

class ClimbingActivity(
    override val activityId: String,
    override val osmId: Long,
    override val name: String = "",
    override val startPosition: Position = Position(0.0, 0.0),
    override val rating: Float = 0.0f,
    override val numRatings: Int = 0,
    override val ratings: List<Rating> = emptyList(),
    override val difficulty: Difficulty = Difficulty.EASY,
    override val activityImageUrl: String = "",
    val climbingStyle: ClimbingStyle = ClimbingStyle.OUTDOOR,
    val elevationTotal: Float = 0.0f
) :
    Activity(
        activityId,
        osmId,
        ActivityType.CLIMBING,
        name,
        startPosition,
        rating,
        numRatings,
        ratings,
        difficulty,
        activityImageUrl)

class HikingActivity(
    override val activityId: String,
    override val osmId: Long,
    override val name: String = "",
    override val startPosition: Position = Position(0.0, 0.0),
    override val rating: Float = 0.0f,
    override val numRatings: Int = 0,
    override val ratings: List<Rating> = emptyList(),
    override val difficulty: Difficulty = Difficulty.EASY,
    override val activityImageUrl: String = "",
    val from: String = "",
    val to: String = "",
    val distance: Float = 0.0f
) :
    Activity(
        activityId,
        osmId,
        ActivityType.HIKING,
        name,
        startPosition,
        rating,
        numRatings,
        ratings,
        difficulty,
        activityImageUrl)

class BikingActivity(
    override val activityId: String,
    override val osmId: Long,
    override val name: String = "",
    override val startPosition: Position = Position(0.0, 0.0),
    override val rating: Float = 0.0f,
    override val numRatings: Int = 0,
    override val ratings: List<Rating> = emptyList(),
    override val difficulty: Difficulty = Difficulty.EASY,
    override val activityImageUrl: String = "",
    val from: String = "",
    val to: String = "",
    val distance: Float = 0.0f
) :
    Activity(
        activityId,
        osmId,
        ActivityType.BIKING,
        name,
        startPosition,
        rating,
        numRatings,
        ratings,
        difficulty,
        activityImageUrl)
