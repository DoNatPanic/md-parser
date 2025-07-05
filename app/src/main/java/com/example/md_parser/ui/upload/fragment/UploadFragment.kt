package com.example.md_parser.ui.upload.fragment

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.md_parser.R
import com.example.md_parser.ui.upload.view_model.UploadFileViewModel
import com.example.md_parser.ui.upload.view_model.UploadPagerAdapter
import com.example.md_parser.ui.upload.view_model.UploadUrlViewModel
import com.example.md_parser.databinding.FragmentUploadBinding
import com.google.android.material.tabs.TabLayoutMediator

class UploadFragment : Fragment() {

    private val fileViewModel: UploadFileViewModel by viewModels()
    private val urlViewModel: UploadUrlViewModel by viewModels()

    private lateinit var binding: FragmentUploadBinding

    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUploadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
//        fileViewModel.onReload()
//        urlViewModel.onReload()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // назад
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.pager.adapter = UploadPagerAdapter(
            requireActivity().supportFragmentManager,
            lifecycle
        )

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.file_tab_title)
                1 -> tab.text = getString(R.string.link_tab_title)
            }
        }
        tabMediator.attach()
    }
}