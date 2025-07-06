package com.example.md_parser.ui.start.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.md_parser.R
import com.example.md_parser.databinding.FragmentStartBinding
import com.example.md_parser.ui.start.view_model.StartPagerAdapter
import com.example.md_parser.ui.start.view_model.StartViewModel
import com.google.android.material.tabs.TabLayoutMediator

class StartFragment : Fragment() {

    private val viewModel: StartViewModel by viewModels()
    private lateinit var binding: FragmentStartBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentFragmentManager.setFragmentResultListener("KEY", viewLifecycleOwner) { key, bundle ->
            val markup = bundle.getString("MARKUP")
            viewModel.setMarkup(markup ?: "")
        }

        binding.loadNavBtn.setOnClickListener {
            findNavController().navigate(
                R.id.action_startFragment_to_uploadFragment,
                null
            )
        }

        binding.pager.adapter = StartPagerAdapter(this)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.preview_tab_title)
                1 -> tab.text = getString(R.string.edit_tab_title)
            }
        }
        tabMediator.attach()
    }
}