package com.example.md_parser.domain

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.flow.Flow

class GetFileInteractor(private val iFileRepository: IFileRepository) : IGetFileInteractor {

    override fun getFileContent(context: Context, uri: Uri): String? {
        return iFileRepository.readTextFromUri(context, uri)
    }

    override fun getWebContent(url: String): Flow<String?> {
        return iFileRepository.readTextFromUrl(url)
    }
}