package com.example.communicator.ui.menu

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.communicator.R

class ConversationViewHolder(val view: View): RecyclerView.ViewHolder(view) {

    val friendUser: TextView = view.findViewById<TextView>(R.id.text_friend_user)
    val timeLastMessage: TextView = view.findViewById<TextView>(R.id.text_time)

}