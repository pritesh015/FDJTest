package com.example.fdjtest.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.fdjtest.R
import com.example.fdjtest.Utils.Status
import com.example.fdjtest.databinding.FragmentTeamsInLeagueBinding
import com.example.fdjtest.ui.adapter.TeamsListAdapter
import com.example.fdjtest.viewmodel.MainActivityViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TeamsInLeagueFragment : Fragment() {
    private val TAG = "TeamsInLeagueFragment"

    private var _binding: FragmentTeamsInLeagueBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainActivityViewModel

    private lateinit var teamsListAdapter: TeamsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeamsInLeagueBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        teamsListAdapter = TeamsListAdapter(listOf(), requireContext())

        setUpBinding()
    }

    override fun onResume() {
        super.onResume()

        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setUpBinding() {
        binding.rvListTeams.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvListTeams.adapter = teamsListAdapter

        lifecycleScope.launch {
            viewModel.teamsInLeagueState.collect {
                when (it.status) {
                    Status.LOADING -> {
                        Log.i(TAG, getString(R.string.loading))
                    }

                    Status.SUCCESS -> {
                        it.data?.let { it1 -> teamsListAdapter.setList(it1) }
                    }

                    Status.ERROR -> {
                        Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        teamsListAdapter.listener = object : TeamsListAdapter.OnClickItemListener {
            override fun onItemClicked(teamName: String) {
                viewModel.getTeamInfo(teamName)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, TeamInfoFragment.newInstance())
                    .addToBackStack("TeamInfoFragment")
                    .commit()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}