package com.example.md_parser.ui.upload.view_model

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.md_parser.ui.upload.fragment.UploadUrlFragment
import com.example.md_parser.ui.upload.fragment.UploadFileFragment

class UploadPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) UploadFileFragment.newInstance() else UploadUrlFragment.newInstance()
    }
}