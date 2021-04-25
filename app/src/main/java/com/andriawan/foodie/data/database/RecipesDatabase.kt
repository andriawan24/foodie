package com.andriawan.foodie.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.andriawan.foodie.data.database.entities.FavoriteEntity
import com.andriawan.foodie.data.database.entities.FoodJokeEntity
import com.andriawan.foodie.data.database.entities.RecipesEntity

@Database(
    entities = [
        RecipesEntity::class,
        FavoriteEntity::class,
        FoodJokeEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(RecipesTypeConverter::class)
abstract class RecipesDatabase: RoomDatabase() {

    abstract fun recipesDao(): RecipesDao
}