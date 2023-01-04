package com.example.communicator.ui.menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.communicator.R

class ConversationAdapter: RecyclerView.Adapter<ConversationViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val conversationRow = layoutInflater
            .inflate(R.layout.conversation_row, parent, false)
        return ConversationViewHolder(conversationRow)
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        val friendUser = holder.friendUser
        val timeLastMessage = holder.timeLastMessage

        friendUser.text = ExampleConversationsDB.friends[position]
        timeLastMessage.text = ExampleConversationsDB.times[position]
    }

    override fun getItemCount(): Int {
        return ExampleConversationsDB.friends.size
    }

}