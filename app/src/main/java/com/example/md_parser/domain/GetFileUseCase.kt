package com.example.md_parser.domain

import android.content.Context
import android.net.Uri

class GetFileUseCase(private val iFileRepository: IFileRepository) : IGetFileUseCase {

    override fun getFileContent(context: Context, uri: Uri): String {
        return iFileRepository.readTextFromUri(context, uri)
    }
}