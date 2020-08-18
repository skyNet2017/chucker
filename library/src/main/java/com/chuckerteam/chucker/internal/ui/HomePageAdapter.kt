package com.chuckerteam.chucker.internal.ui

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.chuckerteam.chucker.R
import com.chuckerteam.chucker.internal.ui.throwable.ThrowableListFragment

import java.lang.ref.WeakReference

internal class HomePageAdapter(context: Context, fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val context: WeakReference<Context> = WeakReference(context)

    override fun getItem(position: Int): Fragment = if (position == BLOCK) {
        //TransactionListFragment.newInstance()
        ThrowableListFragment.newInstance()
    } else {
        ThrowableListFragment.newInstance()
    }

    override fun getCount(): Int = 4

    override fun getPageTitle(position: Int): CharSequence? =
        context.get()?.getString(
            if (position == BLOCK) {
                R.string.chucker_tab_network
            } else if (position == LEAK) {
                R.string.chucker_tab_leak
            }else if (position == CRASH) {
                R.string.chucker_tab_crash
            } else {
                R.string.chucker_tab_errors
            }
        )

    companion object {
        const val CRASH = 0
        const val BLOCK = 1
        const val LEAK = 2
        const val SCREEN_THROWABLE_INDEX = 3
    }
}
