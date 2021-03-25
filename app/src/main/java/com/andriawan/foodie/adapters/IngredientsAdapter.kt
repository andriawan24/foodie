package com.andriawan.foodie.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.andriawan.foodie.R
import com.andriawan.foodie.models.ExtendedIngredient
import com.andriawan.foodie.util.Constants.Companion.BASE_IMAGE_URL
import com.andriawan.foodie.util.RecipesDiffUtil
import kotlinx.android.synthetic.main.ingredient_row_layout.view.*
import java.util.*

class IngredientsAdapter: RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    private var ingredientList = emptyList<ExtendedIngredient>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.ingredient_row_layout,
            parent,
            false
        ))
    }

    override fun getItemCount(): Int {
        return ingredientList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.ingredient_imageView.load(BASE_IMAGE_URL + ingredientList[position].image) {
            crossfade(600)
            error(R.drawable.error_image_placeholder)
        }
        holder.itemView.ingredient_name.text = ingredientList[position].name.capitalize(Locale.ROOT)
        holder.itemView.ingredient_amount.text = ingredientList[position].amount.toString()
        holder.itemView.ingredient_unit.text = ingredientList[position].unit
        holder.itemView.ingredient_consistency.text = ingredientList[position].consistency
        holder.itemView.ingredient_original.text = ingredientList[position].original
    }

    fun setData(newIngredientList: List<ExtendedIngredient>) {
        val recipesDiffUtl =
            RecipesDiffUtil(ingredientList, newIngredientList)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtl)
        ingredientList = newIngredientList
        diffUtilResult.dispatchUpdatesTo(this)
    }
}