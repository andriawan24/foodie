package com.andriawan.foodie.adapters

import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.andriawan.foodie.R
import com.andriawan.foodie.data.database.entities.FavoriteEntity
import com.andriawan.foodie.databinding.FavoritesRecipeRowLayoutBinding
import com.andriawan.foodie.ui.fragment.favorites.FavoriteRecipesFragmentDirections
import com.andriawan.foodie.util.RecipesDiffUtil
import com.andriawan.foodie.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.favorites_recipe_row_layout.view.*

class FavoriteRecipeAdapter(
    private val requireActivity: FragmentActivity,
    private val mainViewModel: MainViewModel
): RecyclerView.Adapter<FavoriteRecipeAdapter.ViewHolder>(), ActionMode.Callback {

    private var multiSelection = false

    private lateinit var mActionMode: ActionMode
    private lateinit var rootView: View

    private var selectedRecipes = arrayListOf<FavoriteEntity>()
    private val myViewHolder = arrayListOf<ViewHolder>()
    private var favoriteEntity = emptyList<FavoriteEntity>()

    class ViewHolder(private val binding: FavoritesRecipeRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favoriteEntity: FavoriteEntity) {
            binding.favoritesEntity = favoriteEntity
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavoritesRecipeRowLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    /**
     * Single click listener
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        myViewHolder.add(holder)
        rootView = holder.itemView.rootView

        val currentRecipes = favoriteEntity[position]
        holder.bind(currentRecipes)

        holder.itemView.favoriteRecipesRowLayout.setOnClickListener {
            if (multiSelection) {
                applySelection(holder, currentRecipes)
            } else {
                val action =
                    FavoriteRecipesFragmentDirections.actionFavoriteRecipesFragmentToDetailActivity(
                        currentRecipes.result
                    )

                holder.itemView.findNavController().navigate(action)
            }
        }

        /**
         * Long click listener
         */
        holder.itemView.favoriteRecipesRowLayout.setOnLongClickListener {
            if (!multiSelection) {
                multiSelection = true
                requireActivity.startActionMode(this)
                applySelection(holder, currentRecipes)
                true
            } else {
                multiSelection = false
                false
            }
        }
    }

    override fun getItemCount(): Int {
        return favoriteEntity.size
    }

    fun setData(newFavoriteRecipe: List<FavoriteEntity>) {
        val favoritesRecipeDiffUtil =
            RecipesDiffUtil(favoriteEntity, newFavoriteRecipe)
        val diffUtilResult = DiffUtil.calculateDiff(favoritesRecipeDiffUtil)
        favoriteEntity = newFavoriteRecipe
        diffUtilResult.dispatchUpdatesTo(this)
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.favorites_contextual_menu, menu)
        mActionMode = mode!!
        applyStatusBarColor(R.color.contextualStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        if (item?.itemId == R.id.delete_favorite_recipe_menu) {
            selectedRecipes.forEach {
                mainViewModel.deleteFavoriteRecipe(it)
            }
            showSnackBar("${selectedRecipes.size} Recipe/s removed.")

            multiSelection = false
            selectedRecipes.clear()
            mode?.finish()
        }
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        myViewHolder.forEach { holder ->
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
        }
        multiSelection = false
        selectedRecipes.clear()
        applyStatusBarColor(R.color.statusBarColor)
    }

    private fun applyStatusBarColor(color: Int) {
        requireActivity.window.statusBarColor =
            ContextCompat.getColor(requireActivity, color)
    }

    private fun applySelection(holder: ViewHolder, currentRecipe: FavoriteEntity) {
        if (selectedRecipes.contains(currentRecipe)) {
            selectedRecipes.remove(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
        } else {
            selectedRecipes.add(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackgroundLightColor, R.color.colorPrimary)
        }
        applyActionModeTitle()
    }

    private fun changeRecipeStyle(holder: ViewHolder, backgroundColor: Int, strokeColor: Int) {
        holder.itemView.favoriteRecipesRowLayout.setBackgroundColor(
            ContextCompat.getColor(requireActivity, backgroundColor)
        )

        holder.itemView.favorite_row_cardView.strokeColor =
            ContextCompat.getColor(requireActivity, strokeColor)
    }

    private fun applyActionModeTitle() {
        when (selectedRecipes.size) {
            0 -> {
                mActionMode.finish()
            }
            1 -> {
                mActionMode.title = "${selectedRecipes.size} item selected"
            }
            else -> {
                mActionMode.title = "${selectedRecipes.size} items selected"
            }
        }
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            rootView,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay") {}
            .show()
    }

    fun clearContextualActionMode() {
        if (this::mActionMode.isInitialized) {
            mActionMode.finish()
        }
    }
}