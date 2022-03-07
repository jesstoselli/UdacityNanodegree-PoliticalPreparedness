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
import org.koin.android.ext.android.inject

class ElectionsFragment : Fragment() {

    private val viewModel: ElectionsViewModel by inject()

//    private val civicsApi = CivicsApi.retrofitService
//    private val electionDao = ElectionDatabase.getInstance(requireContext()).electionDao
//    private val electionRepository = ElectionRepository(electionDao)
//
//    private val politicalPreparednessProvider = PoliticalPreparednessProvider(electionRepository, civicsApi)
//
//    private val viewModel by viewModels<ElectionsViewModel> {
//        ElectionsViewModelFactory(politicalPreparednessProvider)
//    }

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

}
