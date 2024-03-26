package com.lastaoutdoor.lasta.model.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.lastaoutdoor.lasta.model.data.OutdoorActivity
//import com.lastaoutdoor.lasta.model.persistence.OutdoorActivityDatabase
class OutdoorActivityRepository(context: Context) {
    /**private val database = OutdoorActivityDatabase.getInstance(context)
    private val outdoorActivityDao = database.outdoorActivityDao()

    fun getOutdoorActivities(): LiveData<List<OutdoorActivity>> {
        return outdoorActivityDao.getOutdoorActivities()
    }

    fun getOutdoorActivityById(id: Int): LiveData<OutdoorActivity> {
        return outdoorActivityDao.getOutdoorActivityById(id)
    }

    suspend fun insert(outdoorActivity: OutdoorActivity) {
        outdoorActivityDao.insert(outdoorActivity)
    }

    suspend fun update(outdoorActivity: OutdoorActivity) {
        outdoorActivityDao.update(outdoorActivity)
    }

    suspend fun delete(outdoorActivity: OutdoorActivity) {
        outdoorActivityDao.delete(outdoorActivity)
    } */
}