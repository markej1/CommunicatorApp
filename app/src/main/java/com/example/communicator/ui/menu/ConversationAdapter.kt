package com.example.communicator.ui.menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.communicator.R
import com.example.communicator.model.Conversation

class ConversationAdapter(private var listOfConversations: ArrayList<Conversation>,
                          private val onConversationClickListener: OnConversationClickListener):
    RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder>() {

    inner class ConversationViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val friendUser: TextView = view.findViewById(R.id.text_friend_user)
        init {
            view.setOnClickListener {
                onConversationClickListener
                    .onConversationClick(listOfConversations[adapterPosition])
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val conversationRow = layoutInflater
            .inflate(R.layout.conversation_row, parent, false)
        return ConversationViewHolder(conversationRow)
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        val friendUser = holder.friendUser

        friendUser.text = listOfConversations[position].name

    }

    override fun getItemCount(): Int {
        return listOfConversations.size
    }

}