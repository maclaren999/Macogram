package ua.maclaren99.macogram.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import ua.maclaren99.macogram.R
import ua.maclaren99.macogram.activities.MainActivity
import ua.maclaren99.macogram.util.APP_ACTIVITY
import ua.maclaren99.macogram.util.hideKeyboard


open class BaseChangeFragment(layout: Int) : Fragment(layout) {

    override fun onStart() {
        super.onStart()
        setHasOptionsMenu(true)
        APP_ACTIVITY.mAppDrawer.disableDrawer()

    }

    override fun onStop() {
        super.onStop()
        hideKeyboard()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        APP_ACTIVITY.menuInflater.inflate(R.menu.settings_confirm_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_confirm_change -> change()
        }
        return true
    }

    open fun change(){

    }
}