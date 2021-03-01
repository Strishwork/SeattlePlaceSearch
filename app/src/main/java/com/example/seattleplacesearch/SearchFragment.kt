package com.example.seattleplacesearch

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.search_fragment.*
import javax.inject.Inject


@AndroidEntryPoint
class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    @Inject
    lateinit var viewModelFactory: SearchViewModelFactory
    private val viewModel: SearchViewModel by viewModels { viewModelFactory }

    private lateinit var venuesAdapter: VenuesListAdapter
    private lateinit var itemClickedListener: ItemClickedListener
    private lateinit var queryUpdatedListener: SearchQueryUpdatedListener
    private val handler = Handler()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ItemClickedListener) {
            itemClickedListener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    private fun initRecyclerView() {
        recycler.apply {
            layoutManager = LinearLayoutManager(activity)
            venuesAdapter = VenuesListAdapter { item -> itemClickedListener.itemClicked(item) }
            adapter = venuesAdapter
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (viewModel is SearchQueryUpdatedListener) {
            queryUpdatedListener = viewModel
        }
        initRecyclerView()
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                changeSearchQuery(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        startSearchButton.setOnClickListener { startSearchButtonClick() }

        viewModel.venuesLiveData.observe(viewLifecycleOwner, {
            when (val venuesPreviewViewState = it) {
                is VenuePreviewViewState.Default -> {
                    venuesAdapter.submitList(venuesPreviewViewState.venues)
                    changeStartSearchButtonVisibility(false)

                }
                is VenuePreviewViewState.Error -> {
                    Toast.makeText(
                        context,
                        venuesPreviewViewState.error.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
                is VenuePreviewViewState.Empty -> {
                    venuesAdapter.submitList(emptyList())
                }
            }
        })
    }

    private fun changeSearchQuery(query: String) {
        handler.removeCallbacksAndMessages(null)
        handler.postDelayed({ queryUpdatedListener.queryUpdated(query) }, 1000)
    }

    private fun startSearchButtonClick() {
        searchEditText.requestFocus()
        val imm: InputMethodManager? =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT)
        changeStartSearchButtonVisibility(false)
    }

    private fun changeStartSearchButtonVisibility(state: Boolean) {
        startSearchButton.isVisible = state
    }

    interface ItemClickedListener {
        fun itemClicked(venue: Venue)
    }

    interface SearchQueryUpdatedListener {
        fun queryUpdated(query: String)
    }
}