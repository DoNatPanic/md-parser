package com.example.md_parser.data

import android.content.Context
import android.net.Uri
import com.example.md_parser.domain.IFileRepository

class FileRepository : IFileRepository {

    override fun readTextFromUri(context: Context, uri: Uri): String {
        return context.contentResolver
            .openInputStream(uri)
            ?.bufferedReader()
            ?.use { it.readText() }
            ?: "Ошибка чтения"
    }
}