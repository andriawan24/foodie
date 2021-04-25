package com.andriawan.foodie.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.andriawan.foodie.R
import com.andriawan.foodie.adapters.PagerAdapter
import com.andriawan.foodie.data.database.entities.FavoriteEntity
import com.andriawan.foodie.databinding.ActivityDetailBinding
import com.andriawan.foodie.ui.fragment.ingredient.IngredientFragment
import com.andriawan.foodie.ui.fragment.instruction.InstructionFragment
import com.andriawan.foodie.ui.fragment.overview.OverviewFragment
import com.andriawan.foodie.util.Constants.Companion.RECIPE_RESULT_KEY
import com.andriawan.foodie.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private val args by navArgs<DetailActivityArgs>()
    private val mainViewModel: MainViewModel by viewModels()

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!

    private var savedRecipe = false
    private var savedRecipeId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(IngredientFragment())
        fragments.add(InstructionFragment())

        val titles = ArrayList<String>()
        titles.add("Overview")
        titles.add("Ingredients")
        titles.add("Instructions")

        val resultBundle = Bundle()
        resultBundle.putParcelable(RECIPE_RESULT_KEY, args.result)

        val pagerAdapter = PagerAdapter(
            resultBundle,
            fragments,
            this
        )

        binding.viewPager2.apply {
            adapter = pagerAdapter
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        val menuItem = menu?.findItem(R.id.save_to_favorites_menu)
        checkSavedRecipes(menuItem!!)
        return true
    }

    private fun checkSavedRecipes(menuItem: MenuItem) {
        mainViewModel.readFavoriteRecipes.observe(this, { favoritesEntity ->
            try {
                for (savedRecipes in favoritesEntity) {
                    if (savedRecipes.result.id == args.result.id) {
                        changeMenuIconColor(menuItem, R.color.yellow)
                        savedRecipeId = savedRecipes.id
                        savedRecipe = true
                    }

                }
            } catch (e: Exception) {
                Log.d("DetailsActivity", e.message.toString())
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.save_to_favorites_menu && !savedRecipe) {
            saveToFavorite(item)
        } else if (item.itemId == R.id.save_to_favorites_menu && savedRecipe) {
            removeFromFavorite(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveToFavorite(item: MenuItem) {
        val favoriteEntity =
            FavoriteEntity(
                0,
                args.result
            )

        mainViewModel.insertFavoriteRecipe(favoriteEntity)
        changeMenuIconColor(item, R.color.yellow)
        showSnackBar("Recipe saved.")
        savedRecipe = true
    }

    private fun removeFromFavorite(item: MenuItem) {
        val favoriteEntity =
            FavoriteEntity(
                savedRecipeId,
                args.result
            )

        mainViewModel.deleteFavoriteRecipe(favoriteEntity)
        changeMenuIconColor(item, R.color.white)
        showSnackBar("Removed from favorites")
        savedRecipe = false
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            binding.detailsLayout,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay") {}
            .show()
    }

    private fun changeMenuIconColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(this, color))
    }
}