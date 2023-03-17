package com.example.communicator.ui.conversation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.communicator.R
import com.example.communicator.model.MessageGet
import java.text.SimpleDateFormat
import java.util.Date

class MessagesAdapter(private var listOfMessages: ArrayList<MessageGet>,
                      private val myUserId: String):
    RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder>() {

    inner class MessagesViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val messageSomebody: TextView = view.findViewById(R.id.text_message_somebody)
        val messageMe: TextView = view.findViewById(R.id.text_message_me)
        val loginSomebody: TextView = view.findViewById(R.id.text_message_somebody_login)
        val loginMe: TextView = view.findViewById(R.id.text_message_me_login)
        val timeSomebody: TextView = view.findViewById(R.id. text_message_somebody_time)
        val timeMe: TextView = view.findViewById(R.id.text_message_me_time)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val messageRow = layoutInflater.inflate(R.layout.message_row,  parent,false)
        return MessagesViewHolder(messageRow)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: MessagesViewHolder, position: Int) {
        val messageSomebody = holder.messageSomebody
        val messageMe = holder.messageMe
        val loginSomebody = holder.loginSomebody
        val loginMe = holder.loginMe
        val timeSomebody = holder.timeSomebody
        val timeMe = holder.timeMe

        val date = Date(listOfMessages[position].sendingTime)

        val month = date.month+1
        var monthString = ""

        when (month) {
            1 -> monthString = "sty"
            2 -> monthString = "lut"
            3 -> monthString = "mar"
            4 -> monthString = "kwi"
            5 -> monthString = "maj"
            6 -> monthString = "cze"
            7 -> monthString = "lip"
            8 -> monthString = "sie"
            9 -> monthString = "wrz"
            10 -> monthString = "paź"
            11 -> monthString = "lis"
            12 -> monthString = "gru"
        }

        val yearString = SimpleDateFormat("YYYY").format(date)

        val timeString = date.date.toString() + " " +
                monthString + " " +
                yearString + ", godz. " +
                date.hours.toString() + ":" +
                date.minutes.toString()

        if (listOfMessages[position].Participation.User.id == myUserId) {
            messageSomebody.visibility = View.INVISIBLE
            loginSomebody.visibility = View.INVISIBLE
            timeSomebody.visibility = View.INVISIBLE

            messageMe.visibility = View.VISIBLE
            loginMe.visibility = View.VISIBLE
            timeMe.visibility = View.VISIBLE

            messageMe.text = listOfMessages[position].content
            timeMe.text = timeString
        } else {
            messageMe.visibility = View.INVISIBLE
            loginMe.visibility = View.INVISIBLE
            timeMe.visibility = View.INVISIBLE

            messageSomebody.visibility = View.VISIBLE
            loginSomebody.visibility = View.VISIBLE
            timeSomebody.visibility = View.VISIBLE

            messageSomebody.text = listOfMessages[position].content
            timeSomebody.text = timeString
            loginSomebody.text = listOfMessages[position].Participation.User.firstName + " " +
                    listOfMessages[position].Participation.User.lastName

        }
    }

    override fun getItemCount(): Int {
        return listOfMessages.size
    }

}