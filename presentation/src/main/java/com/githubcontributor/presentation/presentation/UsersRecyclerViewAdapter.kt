package com.githubcontributor.presentation.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.githubcontributor.presentation.R
import com.githubcontributor.domain.User

class UsersRecyclerViewAdapter(): RecyclerView.Adapter<UsersRecyclerViewAdapter.UserHolder>() {

    var users: List<User> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val inflate = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contributor, parent, false)
        return UserHolder(inflate)
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.bind(users.get(position))
    }

    override fun getItemCount(): Int = users.size

    class UserHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(user: User) {
            itemView.findViewById<TextView>(R.id.loginTextView)
                .setText(user.login)
            itemView.findViewById<TextView>(R.id.contributorsTextView)
                .setText("${user.contributions}")
        }

    }
}