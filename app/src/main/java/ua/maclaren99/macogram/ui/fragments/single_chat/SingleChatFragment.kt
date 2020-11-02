package ua.maclaren99.macogram.ui.fragments.single_chat

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.AbsListView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.database.DatabaseReference
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_settings.*
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
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mLayoutManager: LinearLayoutManager
    private var mCountMessages = 15
    private var mIsScrolling: Boolean = false
    private var mSmoothScrollToPosition = true
    private var mListMessages = mutableListOf<CommonModel>()


    private var mSendMessageIconVisibility: Boolean = false

    override fun onResume() {
        super.onResume()
        initLields()
        initUpdatingToolbar()
        initSendButtonListener()
        initAttachFileButtonListener()
        initRecyclerView()
    }

    private fun initLields() {
        mSwipeRefreshLayout = single_chat_swipe_refresh
        mLayoutManager = LinearLayoutManager(this.context)
    }



    /** Initializes Recycler View, Adapter, sets updating events and listeners.*/
    private fun initRecyclerView() {
        mRecyclerView = chat_recycler_view
        mAdapter = SingleChatAdapter()
        mRefUserMessages = REF_DATABASE_ROOT
            .child(NODE_MESSAGES).child(UID).child(contact.id)
        mRecyclerView.adapter = mAdapter
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.isNestedScrollingEnabled = false
        mRecyclerView.layoutManager = mLayoutManager

        //Listener
        mMessagesListener = AppChildEventListener {
            val message = it.getCommonModel()

            if (mSmoothScrollToPosition) {
                mAdapter.addItemToBootom(message) {
                    mRecyclerView.smoothScrollToPosition(mAdapter.itemCount)
                }
            } else {
                mAdapter.addItemToTop(message) {
                    mSwipeRefreshLayout.isRefreshing = false
                }
            }
        }

        mRefUserMessages.limitToLast(mCountMessages).addChildEventListener(mMessagesListener)

        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (mIsScrolling && dy < 0 && mLayoutManager.findFirstVisibleItemPosition() <= 5) {
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

        mSwipeRefreshLayout.setOnRefreshListener { updateData() }
    }

    private fun updateData() {
        mSmoothScrollToPosition = false
        mIsScrolling = false
        mCountMessages += 10
        mRefUserMessages.removeEventListener(mMessagesListener)
        mRefUserMessages.limitToLast(mCountMessages).addChildEventListener(mMessagesListener)
    }


    private fun initUpdatingToolbar() {
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
            if (!editable.toString().isBlank()) {
                chat_ic_send.visibility = View.VISIBLE
                chat_ic_attach_file.visibility = View.GONE
            } else {
                chat_ic_attach_file.visibility = View.VISIBLE
                chat_ic_send.visibility = View.GONE
            }
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
        mRefUserMessages.removeEventListener(mMessagesListener)

    }

    //TODO: Change library for getting media. CropImage don't pass.
    private fun initAttachFileButtonListener() {
        chat_ic_attach_file.setOnClickListener {
            CropImage.activity()
                .setAspectRatio(1, 1)
                .setRequestedSize(600, 600)
                .start(APP_ACTIVITY, this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val uri = CropImage.getActivityResult(data).uri     //getting path of croped image
            val messageKey = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(UID).child(contact.id)
                .push().key.toString()
            val path = REF_STORAGE_ROOT.child(CHAT_MEDIA_FOLDAER).child(messageKey)

            uploadImageToStorage(uri, path) {
                getItemUrlFromStoarage(path) { url ->
                    sendMediaMessage(contact.id, url, messageKey)
                }
            }

        }
    }

}