package com.example.md_parser.ui.upload.view_model

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.md_parser.creator.Creator
import com.example.md_parser.domain.IGetFileUseCase
import com.example.md_parser.ui.SingleEventLiveData
import kotlinx.coroutines.launch

class UploadFileViewModel(
    application: Application,
    private val iGetFileUseCase: IGetFileUseCase
) : AndroidViewModel(application) {

    private val _fileContent = MutableLiveData<String>()
    fun loadContentTrigger(): LiveData<String> = _fileContent

    fun loadFile(context: Context, uri: Uri) {
        viewModelScope.launch {
            val text = iGetFileUseCase.getFileContent(context, uri)
            _fileContent.value = text
        }
    }

    companion object {
        fun getSearchViewModelFactory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val application =
                        this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application
                    UploadFileViewModel(
                        application,
                        Creator.provideGetFileUseCase()
                    )
                }
            }
    }
}