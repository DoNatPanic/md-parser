package com.example.md_parser.ui.start.view_model

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.md_parser.ui.start.fragment.StartEditorFragment
import com.example.md_parser.ui.start.fragment.StartViewerFragment

class StartPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) StartViewerFragment.newInstance() else StartEditorFragment.newInstance()
    }
}