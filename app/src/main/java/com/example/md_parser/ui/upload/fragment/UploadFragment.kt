package com.example.md_parser.ui.upload.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.md_parser.R
import com.example.md_parser.ui.upload.view_model.UploadPagerAdapter
import com.example.md_parser.databinding.FragmentUploadBinding
import com.example.md_parser.ui.upload.view_model.UploadViewModel
import com.google.android.material.tabs.TabLayoutMediator

class UploadFragment : Fragment() {

    private lateinit var viewModel: UploadViewModel

    private lateinit var binding: FragmentUploadBinding

    private lateinit var tabMediator: TabLayoutMediator

    private var fileContent: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUploadBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, UploadViewModel.getUploadViewModelFactory())[UploadViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // назад
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        val owner = getViewLifecycleOwner()

        viewModel.loadContentTrigger().observe(owner) { contentText ->
            fileContent = contentText.first
            binding.loadBtn.isEnabled = fileContent != null
            if (fileContent.isNullOrEmpty()){
                Toast.makeText(
                    activity,
                    "Ошибка загрузки",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.pager.adapter = UploadPagerAdapter(
            this
        )

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.file_tab_title)
                1 -> tab.text = getString(R.string.link_tab_title)
            }
        }
        tabMediator.attach()

        binding.loadBtn.setOnClickListener{
            val bundle = Bundle().apply {
                 putString("MARKUP", fileContent)
            }
            parentFragmentManager.setFragmentResult("KEY", bundle)
            findNavController().popBackStack()
        }
    }
}