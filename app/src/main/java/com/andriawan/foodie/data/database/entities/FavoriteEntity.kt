package com.andriawan.foodie.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andriawan.foodie.models.Result
import com.andriawan.foodie.util.Constants.Companion.FAVORITE_RECIPES_TABLE

@Entity(tableName = FAVORITE_RECIPES_TABLE)
class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val result: Result
) {

}