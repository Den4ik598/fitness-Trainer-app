package com.malkinfo.editingrecyclerview.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.malkinfo.editingrecyclerview.R
import com.malkinfo.editingrecyclerview.model.ClassLink


class UserAdapter(val c:Context,val userList:ArrayList<ClassLink>, val filename: String):RecyclerView.Adapter<UserAdapter.UserViewHolder>()
{



  inner class UserViewHolder(val v:View):RecyclerView.ViewHolder(v){
      var name:TextView
      var mbNum:TextView
      var deleteButton: ImageView

      init {
          name = v.findViewById<TextView>(R.id.mTitle)
          mbNum = v.findViewById<TextView>(R.id.mSubTitle)
          deleteButton = v.findViewById<ImageView>(R.id.delete)
          if (adapterPosition >= 0 && adapterPosition < userList.size) {
              val position = adapterPosition
              val deletedItem = userList[position]
          }
          val popupMenus = PopupMenu(c, v)
          deleteButton.setOnClickListener {
              val position2 = adapterPosition
              if (position != RecyclerView.NO_POSITION) {

                  AlertDialog.Builder(c)
                      .setTitle("Delete")
                      .setIcon(R.drawable.ic_warning)
                      .setMessage("Are you sure you want to delete this information?")
                      .setPositiveButton("Yes") { dialog, _ ->

                          val deletedItem = userList.removeAt(position)
                          notifyDataSetChanged()


                          val edit = c.getSharedPreferences(filename, Context.MODE_PRIVATE).edit()
                          edit.clear()
                          val linkJson = Gson().toJson(userList)
                          edit.putString("links_key", linkJson)
                          edit.apply()



                          println("Deleted item: $deletedItem")
                          println("Remaining items: $userList")

                          Toast.makeText(c, "Deleted this information", Toast.LENGTH_SHORT).show()
                          dialog.dismiss()
                      }
                      .setNegativeButton("No") { dialog, _ ->
                          dialog.dismiss()
                      }
                      .create()
                      .show()
              }

          }
      }
  }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
       val inflater = LayoutInflater.from(parent.context)
        val v  = inflater.inflate(R.layout.list_item,parent,false)
        return UserViewHolder(v)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
       val newList = userList[position]
        holder.name.text = newList.Protocol
        holder.mbNum.text = newList.Country
    }

    override fun getItemCount(): Int {
      return  userList.size
    }
}