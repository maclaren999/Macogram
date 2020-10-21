package ua.maclaren99.macogram.ui.fragments.single_chat

import android.view.View
import android.widget.AbsListView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_single_chat.*
import kotlinx.android.synthetic.main.toolbar_chat.view.*
import ua.maclaren99.macogram.R
import ua.maclaren99.macogram.database.*
import ua.maclaren99.macogram.models.CommonModel
import ua.maclaren99.macogram.models.UserModel
import ua.maclaren99.macogram.ui.fragments.BaseFragment
import ua.maclaren99.macogram.util.*

class SingleChatFragment(private val contact: CommonModel) :
    BaseFragment(R.layout.fragment_single_chat) {

    private lateinit var mChatToolbar: View
    private lateinit var mReceivingUser: UserModel
    private lateinit var mListenerChatToolbar: AppValueEventListener
    private lateinit var mRefReceivingUser: DatabaseReference
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mRefUserMessages: DatabaseReference
    private lateinit var mMessagesListener: AppChildEventListener
    private lateinit var mAdapter: SingleChatAdapter
    private var mCountMessages = 10
    private var mIsScrolling: Boolean = false
    private var mSmoothScrollToPosition = true
    private var mListListeners = mutableListOf<AppChildEventListener>()
    private var mListMessages = mutableListOf<CommonModel>()


    private var mSendMessageIconVisibility: Boolean = false

    override fun onResume() {
        super.onResume()
        initLiveupdatingToolbar()
        initSendButtonListener()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mRecyclerView = chat_recycler_view
        mAdapter = SingleChatAdapter()
        mRefUserMessages = REF_DATABASE_ROOT
            .child(NODE_MESSAGES).child(UID).child(contact.id)
        mRecyclerView.adapter = mAdapter

        mMessagesListener = AppChildEventListener {
            mAdapter.addItem(it.getCommonModel())
            if (mSmoothScrollToPosition) mRecyclerView.smoothScrollToPosition(mAdapter.itemCount)
        }

        mRefUserMessages.limitToLast(mCountMessages).addChildEventListener(mMessagesListener)
        mListListeners.add(mMessagesListener)

        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (mIsScrolling && dy < 0) {
                    updateData()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    mIsScrolling = true
                }
            }
        })
    }

    private fun updateData() {
        mSmoothScrollToPosition = false
        mIsScrolling = false
        mCountMessages += 10
        mRefUserMessages.limitToLast(mCountMessages).addChildEventListener(mMessagesListener)
        mListListeners.add(mMessagesListener)
    }


    private fun initLiveupdatingToolbar() {
        //Init toolbar
        mChatToolbar = APP_ACTIVITY.mToolbar.toolbar_chat
        mChatToolbar.visibility = View.VISIBLE

        //Сreate toolbar update listener
        mListenerChatToolbar = AppValueEventListener {
            mReceivingUser = it.getuserModel()
            updateToolbar()
        }
        //Define Contact User Database Reference, set listener
        mRefReceivingUser = REF_DATABASE_ROOT.child(
            NODE_USERS
        ).child(contact.id)
        mRefReceivingUser.addValueEventListener(mListenerChatToolbar)
    }

    private fun initSendButtonListener() {
        chat_ic_send.setOnClickListener {
            mSmoothScrollToPosition = true
            val message = chat_edit_message.text.toString()
            sendMessage(
                message,
                contact.id,
                TYPE_TEXT
            ) {
                chat_edit_message.setText("")
            }
        }
        val textNotBlankWatcher = chat_edit_message.doAfterTextChanged { editable ->
            if (!editable.toString().isBlank()) chat_ic_send.visibility = View.VISIBLE
            else chat_ic_send.visibility = View.GONE
        }
    }

    private fun updateToolbar() {
        if (mReceivingUser.fullname.isEmpty()) {
            mChatToolbar.chat_toolbar_name.text = contact.fullname
        } else mChatToolbar.chat_toolbar_name.text = mReceivingUser.fullname

        mChatToolbar.chat_toolbar_status.text = mReceivingUser.status
        mChatToolbar.chat_toolbar_photo.downloadAndSetImage(mReceivingUser.photoUrl)
    }

    override fun onPause() {
        super.onPause()
        mChatToolbar.visibility = View.GONE
        mRefReceivingUser.removeEventListener(mListenerChatToolbar)
        mListListeners.forEach {
        mRefUserMessages.removeEventListener(it)

        }
    }
}