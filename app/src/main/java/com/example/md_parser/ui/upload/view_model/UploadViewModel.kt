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
import com.example.md_parser.domain.IGetFileInteractor
import kotlinx.coroutines.launch


class UploadViewModel(
    application: Application,
    private val iGetFileInteractor: IGetFileInteractor
) : AndroidViewModel(application) {

    private val _fileContent = MutableLiveData<Pair<String?, String>>()
    fun loadContentTrigger(): LiveData<Pair<String?, String>> = _fileContent

    fun loadFile(context: Context, uri: Uri) {
        viewModelScope.launch {
            val text = iGetFileInteractor.getFileContent(context, uri)
            _fileContent.value = Pair(text, uri.path.toString())
        }
    }

    fun loadFileFromUrl(url: String) {
        viewModelScope.launch {
            iGetFileInteractor.getWebContent(url).collect { result ->
                _fileContent.value = Pair(result, url)
            }
        }
    }

    companion object {
        fun getUploadViewModelFactory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val application =
                        this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application
                    UploadViewModel(
                        application,
                        Creator.provideGetFileInteractor()
                    )
                }
            }
    }
}