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
import ua.maclaren99.macogram.util.DiffUtilCallback
import ua.maclaren99.macogram.util.toTime

class SingleChatAdapter : RecyclerView.Adapter<SingleChatAdapter.SingleChatViewHolder>() {

    val TAG : String = "SingleCF"

    private lateinit var mDiffResult: DiffUtil.DiffResult
    var mListMessagesCache = mutableListOf<CommonModel>()

    class SingleChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userMessageLayout = view.user_message_layout
        val userMessageText = view.user_message_text
        val userMessageTime = view.user_message_time

        val receivedMessageLayout = view.received_message_layout
        val receivedMessageText = view.received_message_text
        val receivedMessageTime = view.received_message_time
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return SingleChatViewHolder(view)
    }

    override fun getItemCount(): Int = mListMessagesCache.size

    override fun onBindViewHolder(holder: SingleChatViewHolder, position: Int) {
        if (mListMessagesCache[position].from == UID) {
            Log.d(TAG, "${mListMessagesCache[position].id} == $UID")
            holder.receivedMessageLayout.visibility = View.GONE
            holder.userMessageLayout.visibility = View.VISIBLE
            holder.userMessageText.setText(mListMessagesCache[position].text)
            holder.userMessageTime.setText(mListMessagesCache[position].timeStamp.toString().toTime())
        } else {
            Log.d(TAG, "listPosition ${mListMessagesCache[position].from} != current UID $UID")
            holder.userMessageLayout.visibility = View.GONE
            holder.receivedMessageLayout.visibility = View.VISIBLE
            holder.receivedMessageText.setText(mListMessagesCache[position].text)
            holder.receivedMessageTime.setText(mListMessagesCache[position].timeStamp.toString().toTime())
        }
    }

//    fun setList(listOfCommonModels: List<CommonModel>) {
//        mDiffResult = DiffUtil.calculateDiff(DiffUtilCallback(mListMessagesCache, listOfCommonModels))
//        mDiffResult.dispatchUpdatesTo(this)
//        mListMessagesCache = listOfCommonModels
//        //notifyDataSetChanged()
//    }


    fun addItem(item: CommonModel){
        val newList = mutableListOf<CommonModel>()
        newList.addAll(mListMessagesCache)
        newList.add(item)
        mDiffResult = DiffUtil.calculateDiff(DiffUtilCallback(mListMessagesCache, newList))
        mDiffResult.dispatchUpdatesTo(this)
        mListMessagesCache = newList
    }
}

