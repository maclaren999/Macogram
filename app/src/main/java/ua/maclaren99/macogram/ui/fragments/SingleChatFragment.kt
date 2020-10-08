package ua.maclaren99.macogram.ui.fragments

import android.view.View
import kotlinx.android.synthetic.main.activity_main.view.*
import ua.maclaren99.macogram.R
import ua.maclaren99.macogram.models.CommonModel
import ua.maclaren99.macogram.util.APP_ACTIVITY

class SingleChatFragment(val contact: CommonModel) : BaseFragment(R.layout.fragment_single_chat) {

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.mToolbar.toolbar_chat.visibility = View.VISIBLE
    }

    override fun onPause() {
        super.onPause()
        APP_ACTIVITY.mToolbar.toolbar_chat.visibility = View.GONE
    }

}