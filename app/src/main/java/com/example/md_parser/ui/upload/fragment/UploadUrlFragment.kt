package com.example.md_parser.ui.upload.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import com.example.md_parser.creator.Creator.getSystemService
import com.example.md_parser.databinding.FragmentUploadUrlBinding
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
            load()
        }

        binding.urlInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                load()
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.urlInput.windowToken, 0)
                true
            } else {
                false
            }
        }
    }

    private fun load() {
        val url = binding.urlInput.text.toString()
        viewModel.loadFileFromUrl(url)
    }

    companion object {
        fun newInstance() = UploadUrlFragment().apply {
            arguments = Bundle().apply { }
        }
    }
}