package com.andriawan.foodie.bindingadapters

import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.andriawan.foodie.data.database.entities.FoodJokeEntity
import com.andriawan.foodie.models.FoodJoke
import com.andriawan.foodie.util.NetworkResult
import com.google.android.material.card.MaterialCardView

class FoodJokeBinding {

    companion object {

        @BindingAdapter("readApiResponse3", "readDatabase3", requireAll = false)
        @JvmStatic
        fun setCardAndProgressVisibility(
            view: View,
            apiResponse: NetworkResult<FoodJoke>?,
            database: List<FoodJokeEntity>?
        ) {
            when (apiResponse) {
                is NetworkResult.Loading -> {
                    when (view) {
                        is ProgressBar -> {
                            view.visibility = VISIBLE
                        }

                        is MaterialCardView -> {
                            view.visibility = INVISIBLE
                        }
                    }
                }

                is NetworkResult.Error -> {
                    when (view) {
                        is ProgressBar -> {
                            view.visibility = INVISIBLE
                        }
                        is MaterialCardView -> {
                            view.visibility = VISIBLE
                            if (database != null) {
                                if (database.isEmpty()) {
                                    view.visibility = INVISIBLE
                                }
                            }
                        }
                    }
                }
                is NetworkResult.Success -> {
                    when (view) {
                        is ProgressBar -> {
                            view.visibility = INVISIBLE
                        }
                        is MaterialCardView -> {
                            view.visibility = VISIBLE
                        }
                    }
                }
            }
        }

        @BindingAdapter("readApiResponse4", "readDatabase4", requireAll = true)
        @JvmStatic
        fun setErrorViewsVisibility(
            view: View,
            apiResponse: NetworkResult<FoodJoke>?,
            database: List<FoodJokeEntity>?
        ) {
            if (database != null) {
                if (database.isEmpty()) {
                    view.visibility = VISIBLE
                    if (view is TextView) {
                        if (apiResponse != null) {
                            view.text = apiResponse.message.toString()
                        }
                    }
                }
            }

            if (apiResponse is NetworkResult.Success) {
                view.visibility = INVISIBLE
            }
        }
    }
}