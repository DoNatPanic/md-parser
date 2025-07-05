package com.example.md_parser.ui.upload.fragment

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.md_parser.R
import com.example.md_parser.ui.upload.view_model.UploadFileViewModel

class UploadFileFragment : Fragment() {

    companion object {
        fun newInstance() = UploadFileFragment()
    }

    private val viewModel: UploadFileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_upload_file, container, false)
    }
}