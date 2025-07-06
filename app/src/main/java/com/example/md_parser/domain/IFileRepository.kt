package com.example.md_parser.domain

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface IFileRepository {
    fun readTextFromUri(context: Context, uri: Uri): String?
    fun readTextFromUrl(url: String): Flow<String?>
}