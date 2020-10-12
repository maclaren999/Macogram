package ua.maclaren99.macogram.ui.fragments

import androidx.fragment.app.Fragment
import ua.maclaren99.macogram.util.APP_ACTIVITY

open class BaseFragment(layout: Int) : Fragment(layout) {
    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.mAppDrawer.disableDrawer()
    }
}