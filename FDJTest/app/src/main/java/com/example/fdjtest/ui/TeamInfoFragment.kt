package com.example.fdjtest.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.fdjtest.R
import com.example.fdjtest.Utils.Status
import com.example.fdjtest.databinding.FragmentTeamInfoBinding
import com.example.fdjtest.viewmodel.MainActivityViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TeamInfoFragment : Fragment() {
    private val TAG = "TeamInfoFragment"

    private var _binding: FragmentTeamInfoBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainActivityViewModel
    private val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeamInfoBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        setUpBinding()
    }

    private fun setUpBinding() {

        lifecycleScope.launch {
            viewModel.teamInfoState.collect {
                when (it.status) {
                    Status.LOADING -> {
                        Log.i(TAG, getString(R.string.loading))
                    }

                    Status.SUCCESS -> {
                        binding.tvLeague.text = it.data?.get(0)?.teamLeague
                        binding.tvCountry.text = it.data?.get(0)?.country
                        binding.tvDescription.text = it.data?.get(0)?.teamDescription

                        Glide.with(requireActivity())
                            .load(it.data?.get(0)?.teamBanner)
                            .apply(requestOptions)
                            .into(binding.ivTeamBanner)

                        (activity as AppCompatActivity).supportActionBar?.title = it.data?.get(0)?.teamName
                        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
                    }

                    Status.ERROR -> {
                        Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
//        val searchItem: MenuItem? = menu?.findItem(R.id.action_search)
//        if (searchItem != null) {
//            val searchView = searchItem.actionView as SearchView
//            searchView.visibility = View.GONE
//            searchItem.isVisible = false
//        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = TeamInfoFragment()
    }
}