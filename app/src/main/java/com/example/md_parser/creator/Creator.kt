package com.example.md_parser.creator

import android.app.Application
import com.example.md_parser.data.FileRepository
import com.example.md_parser.domain.GetFileInteractor
import com.example.md_parser.domain.IFileRepository
import com.example.md_parser.domain.IGetFileInteractor

object Creator: Application() {
    private fun getFileRepository(): IFileRepository {
        return FileRepository()
    }

    fun provideGetFileInteractor(): IGetFileInteractor {
        return GetFileInteractor(getFileRepository())
    }
}