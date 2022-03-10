package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import org.koin.android.ext.android.inject

class VoterInfoFragment : Fragment() {

    private val viewModel: VoterInfoViewModel by inject()
    private val args: VoterInfoFragmentArgs by navArgs()

    private lateinit var binding: FragmentVoterInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentVoterInfoBinding.inflate(inflater)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.voterInfoViewModel = viewModel

        viewModel.retrieveVoterInfo(args.argElectionId.toInt(), args.argDivision)

        viewModel.url.observe(viewLifecycleOwner, Observer {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
            startActivity(intent)
        })

        viewModel.isFollowed.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                binding.btnFollowElection.text = requireContext().resources.getString(R.string.unfollow_election)
            } else {
                binding.btnFollowElection.text = requireContext().resources.getString(R.string.follow_election)
            }
        })

        return binding.root

    }
}
