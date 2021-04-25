package com.andriawan.foodie.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.andriawan.foodie.R
import com.andriawan.foodie.databinding.IngredientRowLayoutBinding
import com.andriawan.foodie.models.ExtendedIngredient
import com.andriawan.foodie.util.Constants.Companion.BASE_IMAGE_URL
import com.andriawan.foodie.util.RecipesDiffUtil
import java.util.*

class IngredientsAdapter: RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    private var ingredientList = emptyList<ExtendedIngredient>()

    inner class ViewHolder(val binding: IngredientRowLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(IngredientRowLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        )
    }

    override fun getItemCount(): Int {
        return ingredientList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.ingredientImageView.load(BASE_IMAGE_URL + ingredientList[position].image) {
            crossfade(600)
            error(R.drawable.error_image_placeholder)
        }
        holder.binding.ingredientName.text = ingredientList[position].name.capitalize(Locale.ROOT)
        holder.binding.ingredientAmount.text = ingredientList[position].amount.toString()
        holder.binding.ingredientUnit.text = ingredientList[position].unit
        holder.binding.ingredientConsistency.text = ingredientList[position].consistency
        holder.binding.ingredientOriginal.text = ingredientList[position].original
    }

    fun setData(newIngredientList: List<ExtendedIngredient>) {
        val recipesDiffUtl =
            RecipesDiffUtil(ingredientList, newIngredientList)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtl)
        ingredientList = newIngredientList
        diffUtilResult.dispatchUpdatesTo(this)
    }
}