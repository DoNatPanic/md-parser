package com.example.md_parser.ui.upload.fragment

import android.net.Uri
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.md_parser.databinding.FragmentUploadFileBinding
import com.example.md_parser.domain.GetFileUseCase
import com.example.md_parser.ui.upload.view_model.UploadFileViewModel

class UploadFileFragment : Fragment() {

    private lateinit var binding: FragmentUploadFileBinding

    private lateinit var viewModel: UploadFileViewModel

    private var fileContent: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUploadFileBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(
            this,
            UploadFileViewModel.getSearchViewModelFactory()
        )[UploadFileViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgUploadIcon.setOnClickListener {
            openDocumentLauncher.launch(arrayOf("text/plain"))
        }

        var owner = getViewLifecycleOwner()
        viewModel.loadContentTrigger().observe(owner) { contentText ->
            Toast.makeText(
                activity,
                "Файл успешно прочитан",
                Toast.LENGTH_SHORT
            ).show()
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