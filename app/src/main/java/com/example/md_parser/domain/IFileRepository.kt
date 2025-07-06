package com.example.md_parser.domain

import android.content.Context
import android.net.Uri

interface IFileRepository {
    fun readTextFromUri(context: Context, uri: Uri): String
}