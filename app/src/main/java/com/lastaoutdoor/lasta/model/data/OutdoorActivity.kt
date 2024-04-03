package com.lastaoutdoor.lasta.model.data

open class OutdoorActivity(private var activityType: ActivityType) {

    public fun setActivityType(a:ActivityType){
        activityType = a
    }
    public fun getActivityType() : ActivityType{
        return activityType
    }
}
