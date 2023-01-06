package com.example.communicator.ui.newConversation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.communicator.R
import com.example.communicator.model.User

class SearchPeopleAdapter(private var listOfSearchPeople: ArrayList<User>):
    RecyclerView.Adapter<SearchPeopleAdapter.SearchPeopleViewHolder>() {

    inner class SearchPeopleViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val searchLogin = view.findViewById<TextView>(R.id.text_search_people)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchPeopleViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val searchPeopleRow =
            layoutInflater.inflate(R.layout.search_people_row, parent, false)
        return SearchPeopleViewHolder(searchPeopleRow)
    }

    override fun onBindViewHolder(holder: SearchPeopleViewHolder, position: Int) {
        val searchLogin = holder.searchLogin
        searchLogin.text = listOfSearchPeople[position].login
    }

    override fun getItemCount(): Int {
        return listOfSearchPeople.size
    }

}