package com.example.md_parser.creator

import android.app.Application
import com.example.md_parser.data.FileRepository
import com.example.md_parser.domain.GetFileUseCase
import com.example.md_parser.domain.IFileRepository
import com.example.md_parser.domain.IGetFileUseCase

object Creator: Application() {
    private fun getFileRepository(): IFileRepository {
        return FileRepository()
    }

    fun provideGetFileUseCase(): IGetFileUseCase {
        return GetFileUseCase(getFileRepository())
    }
}