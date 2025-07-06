package com.example.md_parser.ui.upload.fragment

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.example.md_parser.databinding.FragmentUploadFileBinding
import com.example.md_parser.ui.upload.view_model.UploadViewModel

class UploadFileFragment : Fragment() {

    private lateinit var binding: FragmentUploadFileBinding

    private val viewModel: UploadViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUploadFileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgUploadIcon.setOnClickListener {
            openDocumentLauncher.launch(arrayOf("text/plain"))
        }

        val owner = getViewLifecycleOwner()
        viewModel.loadContentTrigger().observe(owner) { data ->
            binding.fileName.text = data.second
        }
    }

    private val openDocumentLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.loadFile(requireContext(), it)
        }
    }

    companion object {
        fun newInstance() = UploadFileFragment().apply {
            arguments = Bundle().apply { }
        }
    }
}