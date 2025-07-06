package com.example.md_parser.ui.upload.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.md_parser.databinding.FragmentUploadUrlBinding
import com.example.md_parser.ui.start.fragment.StartEditorFragment
import com.example.md_parser.ui.upload.view_model.UploadViewModel

class UploadUrlFragment : Fragment() {

    private lateinit var binding: FragmentUploadUrlBinding

    private val viewModel: UploadViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUploadUrlBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.goLinkIcon.setOnClickListener {
            val url = binding.urlInput.text.toString()
            viewModel.loadFileFromUrl(url)
        }
    }

    companion object {
        fun newInstance() = UploadUrlFragment().apply {
            arguments = Bundle().apply { }
        }
    }
}