package com.example.md_parser.ui.start.fragment

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.example.md_parser.databinding.FragmentStartEditorBinding
import com.example.md_parser.ui.start.view_model.StartViewModel

class StartEditorFragment : Fragment() {

    companion object {
        fun newInstance() = StartEditorFragment()
    }

    private lateinit var binding: FragmentStartEditorBinding
    private val viewModel: StartViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartEditorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.markupLiveData.observe(viewLifecycleOwner) { markup ->
            updateMarkupEditor(markup)
            updateButtonsState(markup)
        }

        binding.saveBtn.setOnClickListener {
            viewModel.setMarkup(binding.markdownEditor.text.toString())
        }
        binding.cancelBtn.setOnClickListener { viewModel.setMarkup("") }
        binding.markdownEditor.addTextChangedListener { _ ->
            run {
                binding.saveBtn.isEnabled = true
            }
        }

    }

    private fun updateMarkupEditor(markup: String) {
        binding.markdownEditor.setText(markup)
    }

    private fun updateButtonsState(markup: String) {
        binding.cancelBtn.isEnabled = markup.isNotEmpty()
        binding.saveBtn.isEnabled = false
    }
}