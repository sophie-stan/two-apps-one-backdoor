package com.example.contacts

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TextItemViewHolder(textView: View): RecyclerView.ViewHolder(textView){
    val textView1: TextView = textView.findViewById(R.id.contact_name)
}
