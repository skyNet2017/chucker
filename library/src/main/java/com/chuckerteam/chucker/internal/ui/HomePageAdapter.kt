package com.chuckerteam.chucker.internal.ui

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.chuckerteam.chucker.R
import com.chuckerteam.chucker.internal.data.entity.ThrowableType
import com.chuckerteam.chucker.internal.ui.throwable.ThrowableListFragment

import java.lang.ref.WeakReference

internal class HomePageAdapter(context: Context, fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val context: WeakReference<Context> = WeakReference(context)

    override fun getItem(position: Int): Fragment {
       return ThrowableListFragment.newInstance(ThrowableType.types.get(position))
    }

    override fun getCount(): Int = ThrowableType.types.size

    override fun getPageTitle(position: Int): CharSequence? = ThrowableType.types.get(position)

    companion object {
        const val CRASH = 0
        const val BLOCK = 1
        const val LEAK = 2
        const val SCREEN_THROWABLE_INDEX = 3
    }
}
