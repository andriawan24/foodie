package com.andriawan.foodie.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.andriawan.foodie.adapters.FavoriteRecipeAdapter
import com.andriawan.foodie.data.database.entities.FavoriteEntity

class FavoriteRecipesBinding {

    companion object {

        @BindingAdapter("setVisibility", "setData", requireAll = false)
        @JvmStatic
        fun setVisibility(view: View, favoriteEntity: List<FavoriteEntity>?, mAdapter: FavoriteRecipeAdapter?) {
            when (view) {
                is RecyclerView -> {
                    val dataCheck = favoriteEntity.isNullOrEmpty()
                    view.isInvisible = dataCheck
                    if (!dataCheck) {
                        favoriteEntity?.let { mAdapter?.setData(it) }
                    }
                }

                else -> view.isVisible = favoriteEntity.isNullOrEmpty()
            }
        }
    }
}