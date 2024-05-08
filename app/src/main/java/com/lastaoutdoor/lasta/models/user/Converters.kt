package com.lastaoutdoor.lasta.models.user


import androidx.core.text.isDigitsOnly
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lastaoutdoor.lasta.models.activity.ActivityType
import com.lastaoutdoor.lasta.models.activity.ClimbingStyle
import com.lastaoutdoor.lasta.models.activity.Difficulty
import com.lastaoutdoor.lasta.models.activity.Rating
import com.lastaoutdoor.lasta.models.api.Position

class Converters {
    @TypeConverter
    fun fromClimbingStyle(c : ClimbingStyle) : String{
        return c.toString()
    }
    @TypeConverter
    fun toClimbingSyle(s: String) : ClimbingStyle{
        return if(s == "Outdoor"){
            ClimbingStyle.OUTDOOR
        }else{
            ClimbingStyle.INDOOR
        }
    }
    @TypeConverter
    fun fromDifficulty(d : Difficulty) : String{
        return d.toString()
    }
    @TypeConverter
    fun toDifficulty(s : String) : Difficulty{
        return if(s == "Easy"){
            Difficulty.EASY
        }else if (s == "Normal"){
            Difficulty.NORMAL
        }else{
            Difficulty.HARD
        }
    }
    @TypeConverter
    fun fromActivityType( a : ActivityType): String{
        return a.toString()
    }
    @TypeConverter
    fun toActivity(s : String) : ActivityType{
        return if(s == "Hiking"){
            ActivityType.HIKING
        }else if(s=="Climbing"){
            ActivityType.CLIMBING
        }else{
            ActivityType.BIKING
        }
    }
    @TypeConverter
    fun toListR(s : String) : List<Rating>{
        val gson = Gson()

        return gson.fromJson(s, TypeToken.get(listOf<Rating>().javaClass))
    }
    @TypeConverter
    fun fromListR(l : List<Rating>) : String{
        val gson = Gson()
        return gson.toJson(l)
    }
    @TypeConverter
    fun toListS(s : String) : List<String>{
        val gson = Gson()

        return gson.fromJson(s, TypeToken.get(listOf<String>().javaClass))
    }
    @TypeConverter
    fun fromListS(l : List<String>) : String{
        val gson = Gson()
        return gson.toJson(l)
    }
    @TypeConverter
    fun toUserModel(s : String) : UserModel{
        val gson = Gson()

        return gson.fromJson(s, TypeToken.get(UserModel::class.java))
    }
    @TypeConverter
    fun fromModel( u : UserModel) : String{
        val gson = Gson()
        return gson.toJson(u)
    }


}