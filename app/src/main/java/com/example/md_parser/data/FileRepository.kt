package com.example.md_parser.data

import android.content.Context
import android.net.Uri
import com.example.md_parser.domain.IFileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
class FileRepository : IFileRepository {

    override fun readTextFromUri(context: Context, uri: Uri): String? {
        return context.contentResolver
            .openInputStream(uri)
            ?.bufferedReader()
            ?.use { it.readText() }
            ?: null
    }

    override fun readTextFromUrl(url: String): Flow<String?> = flow {
        val response = WebClient.doRequest(url)
        emit(response)
    }
}