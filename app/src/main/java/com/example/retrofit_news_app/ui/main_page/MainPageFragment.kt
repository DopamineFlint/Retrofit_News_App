package com.example.retrofit_news_app.ui.main_page

import android.app.Fragment
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.retrofit_news_app.R
import com.example.retrofit_news_app.data.ArticlesModel
import com.example.retrofit_news_app.databinding.FragmentMainPageBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.news_post_load_state_footer.*

// instead of import android.widget.SearchView. This way you avoid the following error:
// java.lang.ClassCastException: androidx.appcompat.widget.SearchView
// cannot be cast to android.widget.SearchView

@AndroidEntryPoint
class MainPageFragment : Fragment(R.layout.fragment_main_page),
    NewsModelAdapter.OnItemClickListener {

    private val viewModel by viewModels<NewsViewModel>()

    private var _binding: FragmentMainPageBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentMainPageBinding.bind(view)

        val adapter = NewsModelAdapter(this)

        binding.apply {
            newsRecyclerView.setHasFixedSize(true)
            newsRecyclerView.itemAnimator = null
            newsRecyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                header = NewsPostLoadStateAdapter { adapter.retry() },
                footer = NewsPostLoadStateAdapter { adapter.retry() },
            )
            newsRetryButton.setOnClickListener {
                adapter.retry()
            }
        }

        viewModel.posts.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                newsProgressBar.isVisible = loadState.source.refresh is LoadState.Loading
                newsRecyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                newsRetryButton.isVisible = loadState.source.refresh is LoadState.Error
                newsNoResultText.isVisible = loadState.source.refresh is LoadState.Error //???

                //empty view
                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1
                ) {
                    newsRecyclerView.isVisible = false
                    newsNoResultQueryText.isVisible = true
                } else {
                    newsNoResultQueryText.isVisible = false
                }
            }
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_news, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                if (query != null) {
                    binding.newsRecyclerView.scrollToPosition(0)
                    viewModel.searchPosts(query)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }

    override fun onItemClick(post: ArticlesModel) {
        val action = MainPageFragmentDirections.actionMainPageFragmentToDetailsFragment(post)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}