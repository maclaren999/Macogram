package ua.maclaren99.macogram.ui.fragments.single_chat

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_message.view.*
import ua.maclaren99.macogram.R
import ua.maclaren99.macogram.models.CommonModel
import ua.maclaren99.macogram.database.UID
import ua.maclaren99.macogram.util.TYPE_MEDIA
import ua.maclaren99.macogram.util.TYPE_TEXT
import ua.maclaren99.macogram.util.downloadAndSetImage
import ua.maclaren99.macogram.util.asTime

class SingleChatAdapter : RecyclerView.Adapter<SingleChatAdapter.SingleChatViewHolder>() {

    val TAG: String = "SingleCF"

    private lateinit var mDiffResult: DiffUtil.DiffResult
    var mListMessagesCache = mutableListOf<CommonModel>()

    class SingleChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //Text
        val userMessageLayout = view.user_message_layout
        val userMessageText = view.user_message_text
        val userMessageTime = view.user_message_time
        val userTextBackgroundM = view.bg_user_message
        val receivedMessageLayout = view.received_message_layout
        val receivedMessageText = view.received_message_text
        val receivedMessageTime = view.received_message_time
        val receivedTextBackgroundM = view.bg_received_message

        //media
        val userImageLayout = view.user_image_layout
        val userChatImage = view.user_image
        val userImageTime = view.user_image_time
        val receivedImageLayout = view.received_image_layout
        val receivedChatImage = view.received_image
        val receivedImageTime = view.received_image_time
    }
//TODO: Different ViewHolders for every type of message
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return SingleChatViewHolder(view)
    }

    override fun getItemCount(): Int = mListMessagesCache.size

    override fun onBindViewHolder(holder: SingleChatViewHolder, position: Int) {
        when(mListMessagesCache[position].type){
            TYPE_TEXT -> bindTextView(holder, position)
            TYPE_MEDIA -> bindMediaView(holder, position)
        }

    }

    private fun bindTextView(holder: SingleChatViewHolder, position: Int) {
        if (mListMessagesCache[position].from == UID) {
            Log.d(TAG, "${mListMessagesCache[position].id} == $UID")
            holder.receivedMessageLayout.visibility = View.GONE
            holder.userMessageLayout.visibility = View.VISIBLE
            holder.userMessageText.setText(mListMessagesCache[position].text)
            holder.userMessageTime.setText(
                mListMessagesCache[position].timeStamp.toString().asTime()
            )
        } else {
            Log.d(TAG, "listPosition ${mListMessagesCache[position].from} != current UID $UID")
            holder.userMessageLayout.visibility = View.GONE
            holder.receivedMessageLayout.visibility = View.VISIBLE
            holder.receivedMessageText.setText(mListMessagesCache[position].text)
            holder.receivedMessageTime.setText(
                mListMessagesCache[position].timeStamp.toString().asTime()
            )
        }
    }

    private fun bindMediaView(holder: SingleChatViewHolder, position: Int) {
        if (mListMessagesCache[position].from == UID) {
            holder.userMessageLayout.visibility = View.VISIBLE
            holder.receivedMessageLayout.visibility = View.GONE     // Disable opposite layout
            holder.userTextBackgroundM.visibility = View.GONE       // Disable layout for text message
            holder.userImageLayout.visibility = View.VISIBLE        // Image layout

            holder.userChatImage.downloadAndSetImage(mListMessagesCache[position].imageUrl)
            holder.userImageTime.text = mListMessagesCache[position].timeStamp.toString().asTime()

        } else {
            holder.receivedMessageLayout.visibility = View.VISIBLE
            holder.userMessageLayout.visibility = View.GONE         // Disable opposite layout
            holder.receivedTextBackgroundM.visibility = View.GONE   // Disable layout for text message
            holder.receivedImageLayout.visibility = View.VISIBLE    // Image layout

            holder.receivedChatImage.downloadAndSetImage(mListMessagesCache[position].imageUrl)
            holder.receivedImageTime.text = mListMessagesCache[position].timeStamp.toString().asTime()
        }

    }


    fun addItemToBootom(item: CommonModel, onSucces: () -> Unit) {
        if (!mListMessagesCache.contains(item)) {
            mListMessagesCache.add(item)
            notifyItemInserted(mListMessagesCache.size)
        }
        onSucces()
    }

    fun addItemToTop(item: CommonModel, onSucces: () -> Unit) {
        if (!mListMessagesCache.contains(item)) {
            mListMessagesCache.add(item)
            mListMessagesCache.sortBy { it.timeStamp.toString().toLong() }
            notifyItemInserted(0)
        }
        onSucces()
    }


}

