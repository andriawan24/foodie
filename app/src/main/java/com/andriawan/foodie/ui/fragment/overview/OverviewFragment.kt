package com.andriawan.foodie.ui.fragment.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import coil.load
import com.andriawan.foodie.R
import com.andriawan.foodie.bindingadapters.RecipesRowBinding
import com.andriawan.foodie.databinding.FragmentOverviewBinding
import com.andriawan.foodie.models.Result
import com.andriawan.foodie.util.Constants.Companion.RECIPE_RESULT_KEY

class OverviewFragment : Fragment() {

    private var _binding : FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)

        val args = arguments
        val myBundle: Result = args!!.getParcelable<Result>(RECIPE_RESULT_KEY) as Result

        binding.imageView.load(myBundle.image)
        binding.titleTextView.text = myBundle.title
        binding.timeTextView.text = myBundle.readyInMinutes.toString()
        binding.likeTextView.text = myBundle.aggregateLikes.toString()

        RecipesRowBinding.parseHtml(binding.summaryTextView, myBundle.summary)

        updateColor(myBundle.vegetarian, binding.vegetarianTextView, binding.vegetarianImageView)
        updateColor(myBundle.vegan, binding.veganTextView, binding.veganImageView)
        updateColor(myBundle.cheap, binding.cheapTextView, binding.cheapImageView)
        updateColor(myBundle.dairyFree, binding.dairyFreeTextView, binding.dairyFreeImageView)
        updateColor(myBundle.glutenFree, binding.glutenFreeTextView, binding.glutenFreeImageView)
        updateColor(myBundle.veryHealthy, binding.healthyTextView, binding.healthyImageView)

        return binding.root
    }

    private fun updateColor(stateIsOn: Boolean, textView: TextView, imageView: ImageView) {
        if (stateIsOn) {
            imageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        }
    }
}