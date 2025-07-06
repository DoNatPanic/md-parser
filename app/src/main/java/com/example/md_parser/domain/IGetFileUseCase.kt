package com.example.md_parser.domain

import android.content.Context
import android.net.Uri

interface IGetFileUseCase {
    fun getFileContent(context: Context, uri: Uri): String
}