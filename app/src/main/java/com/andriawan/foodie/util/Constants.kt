package com.andriawan.foodie.util

class Constants {
    companion object {
        const val API_KEY = "aba6dd8243d5474a947ef1fdbaf23043"
        const val BASE_URL = "https://api.spoonacular.com"

        // API Query keys
        const val QUERY_NUMBER = "number"
        const val QUERY_API_KEY = "apiKey"
        const val QUERY_TYPE = "type"
        const val QUERY_DIET = "diet"
        const val QUERY_ADD_RECIPE_INFORMATION = "addRecipeInformation"
        const val QUERY_FILL_INGREDIENTS = "fillIngredients"

        // ROOM Database
        const val DATABASE_NAME = "recipes_database"
        const val RECIPES_TABLE = "recipes_table"
    }
}