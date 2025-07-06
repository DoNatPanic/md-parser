package com.example.md_parser.ui.start.fragment

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.md_parser.R
import com.example.md_parser.ui.start.view_model.StartViewModel

class StartEditorFragment : Fragment() {

    companion object {
        fun newInstance() = StartEditorFragment()
    }

    private val viewModel: StartViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_start_editor, container, false)
    }
}