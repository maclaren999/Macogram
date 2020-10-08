package ua.maclaren99.macogram.ui.fragments

import android.view.View
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.toolbar_chat.view.*
import ua.maclaren99.macogram.R
import ua.maclaren99.macogram.models.CommonModel
import ua.maclaren99.macogram.models.UserModel
import ua.maclaren99.macogram.util.*

class SingleChatFragment (private val contact: CommonModel) : BaseFragment(R.layout.fragment_single_chat) {

    private lateinit var mChatToolbar: View
    private lateinit var mReceivingUser: UserModel
    private lateinit var mListenerChatToolbar: AppValueEventListener
    private lateinit var mRefReceivingUser: DatabaseReference

    override fun onResume() {
        super.onResume()
        //Init toolbar
        mChatToolbar = APP_ACTIVITY.mToolbar.toolbar_chat
        mChatToolbar.visibility = View.VISIBLE

        //Сreate toolbar update listener
        mListenerChatToolbar = AppValueEventListener {
            mReceivingUser = it.getuserModel()
            updateToolbar()
        }
        //Define Contact User Database Reference, set listener
        mRefReceivingUser = REF_DATABASE_ROOT.child(NODE_USERS).child(contact.id)
        mRefReceivingUser.addValueEventListener(mListenerChatToolbar)
    }

    private fun updateToolbar() {
        mChatToolbar.chat_toolbar_name.setText(mReceivingUser.fullname)
        mChatToolbar.chat_toolbar_status.setText(mReceivingUser.status)
        mChatToolbar.chat_toolbar_photo.downloadAndSetImage(mReceivingUser.photoUrl)
    }

    override fun onPause() {
        super.onPause()
        mChatToolbar.visibility = View.GONE
        mRefReceivingUser.removeEventListener(mListenerChatToolbar)
    }

}