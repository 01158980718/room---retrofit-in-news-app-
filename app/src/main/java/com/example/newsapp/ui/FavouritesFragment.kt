package com.example.newsapp.ui

import NewsViewModel
import android.os.Bundle
import androidx.fragment.app.Fragment

import android.view.View

import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsprojectpractice.R
import com.example.newsprojectpractice.databinding.FragmentFavouritesBinding
import com.google.android.material.snackbar.Snackbar

class FavouritesFragment : Fragment(R.layout.fragment_favourites) {
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var binding: FragmentFavouritesBinding



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=FragmentFavouritesBinding.bind(view)

        // Initialize ViewModel
        newsViewModel = (activity as MainActivity).newsViewModel
        setUpFavouritesRecycler()

        // Set click listener for navigating to article details
        newsAdapter.setOnClickListner { article ->
            val bundle = Bundle().apply {
                putSerializable("article", article)
            }
            findNavController().navigate(R.id.action_favouritesFragment_to_articleFragment, bundle)
        }

        // Set up ItemTouchHelper for swipe-to-remove functionality
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0, // No drag & drop
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false // Not used
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Handle item removal from favourites
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]

                // Remove the article and show a Snackbar for undo
                newsViewModel.deleteArticle(article)

                Snackbar.make(view, "Deleted from favourites", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        newsViewModel.addToFavourites(article)
                    }
                    show()
                }
            }
        }

        // Attach the ItemTouchHelper to the RecyclerView
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.recyclerFavourites)

        // Observe the favourites data
        newsViewModel.getFavourites().observe(viewLifecycleOwner, Observer { articles ->
            newsAdapter.differ.submitList(articles)
        })
    }

    private fun setUpFavouritesRecycler() {
        newsAdapter = NewsAdapter()
        binding.recyclerFavourites.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}
