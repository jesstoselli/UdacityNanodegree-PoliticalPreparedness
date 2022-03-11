package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import com.example.android.politicalpreparedness.utils.ApiStatus
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject

class ElectionsFragment : Fragment() {

    private val viewModel: ElectionsViewModel by inject()

    private lateinit var binding: FragmentElectionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentElectionBinding.inflate(inflater)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner

            electionsViewModel = viewModel

            rvUpcomingElections.adapter = ElectionListAdapter(ElectionListener {
                viewModel.navigateToVoterInfoFragment(it)
            })

            rvSavedElections.adapter = ElectionListAdapter(ElectionListener {
                viewModel.navigateToVoterInfoFragment(it)
            })
        }

        viewModel.navigateToVoterInfoFragment.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                navigateToVoterInfoFragment(it)
                viewModel.returnFromVoterInfoFragment()
            }
        })

        viewModel.apiStatus.observe(viewLifecycleOwner, Observer { apiStatus ->
            with(binding) {
                when (apiStatus) {
                    ApiStatus.DONE -> {
                        tvNoNetwork.visibility = View.GONE
                        rlLoadingPanel.visibility = View.GONE
                        rvUpcomingElections.visibility = View.VISIBLE
                    }
                    ApiStatus.ERROR -> {
                        rlLoadingPanel.visibility = View.GONE
                        rvUpcomingElections.visibility = View.GONE
                        tvNoNetwork.visibility = View.VISIBLE
                    }
                    ApiStatus.LOADING -> {
                        rvUpcomingElections.visibility = View.GONE
                        tvNoNetwork.visibility = View.GONE
                        rlLoadingPanel.visibility = View.VISIBLE
                    }
                }
            }
        })

        return binding.root
    }

    private fun navigateToVoterInfoFragment(election: Election) {
        this.findNavController().navigate(
            ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                election.id,
                election.division
            )
        )
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshElectionData()
    }

    companion object {
        const val TAG = "ElectionsFragment"
    }
}
