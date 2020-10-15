package ua.maclaren99.macogram.ui.fragments

import androidx.fragment.app.Fragment
import ua.maclaren99.macogram.R
import ua.maclaren99.macogram.databinding.FragmentChatsBinding
import ua.maclaren99.macogram.ui.objects.AppDrawer
import ua.maclaren99.macogram.util.APP_ACTIVITY
import ua.maclaren99.macogram.util.hideKeyboard

class MainFragment : Fragment(R.layout.fragment_chats) {

    private lateinit var mBinding: FragmentChatsBinding

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "Macogram"
        APP_ACTIVITY.mAppDrawer.enableDrawer()
        hideKeyboard()
    }
}