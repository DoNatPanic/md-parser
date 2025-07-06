package com.example.md_parser.domain

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface IGetFileInteractor {
    fun getFileContent(context: Context, uri: Uri): String?
    fun getWebContent(url: String): Flow<String?>
}