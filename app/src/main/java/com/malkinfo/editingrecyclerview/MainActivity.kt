package com.malkinfo.editingrecyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.malkinfo.editingrecyclerview.model.ClassLink
import com.malkinfo.editingrecyclerview.parsing.parsingurl
import com.malkinfo.editingrecyclerview.view.UserAdapter


private const val LINKS_PREFS = "links_prefs"
private const val LINKS_KEY = "links_key"

class MainActivity : AppCompatActivity() {
    private lateinit var addsBtn:FloatingActionButton
    private lateinit var rev:RecyclerView
    private lateinit var userList:ArrayList<ClassLink>
    private lateinit var userAdapter:UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**set List*/
        userList = ArrayList()
        /**set find Id*/
        addsBtn = findViewById(R.id.addingBtn)
        rev = findViewById(R.id.mRecycler)
        /**set Adapter*/
        userAdapter = UserAdapter(this,userList,LINKS_PREFS)
        /**setRecycler view Adapter*/
        rev.layoutManager = LinearLayoutManager(this)
        rev.adapter = userAdapter
        /**set Dialog*/
        val linksJson = getSharedPreferences(LINKS_PREFS, MODE_PRIVATE).getString(LINKS_KEY, "")
        if (linksJson != null) {
            if (linksJson.isNotEmpty()) {
                val links = Gson().fromJson(linksJson, Array<ClassLink>::class.java)
                println("jsfail1: $links")
                userList.addAll(links)
                println("restart app: $userList")
                userAdapter.notifyDataSetChanged()
            }
        }

        addsBtn.setOnClickListener { addInfo() }

    }



    private fun addInfo() {
        val parser = parsingurl()
        val inflater = LayoutInflater.from(this)
        val v = inflater.inflate(R.layout.add_item,null)
        /**set view*/
        val Links = v.findViewById<EditText>(R.id.Link)

        val addDialog = AlertDialog.Builder(this)

        addDialog.setView(v)
        addDialog.setPositiveButton("Ok"){
            dialog,_->
            val Link = Links.text.toString()

            if (Link.isNotEmpty()){
                try {
                    val id = userList.size + 1
                    val linkParsing = parser.fromUrl(Link, id)
                    userList.add(linkParsing)
                    userAdapter.notifyDataSetChanged()

                    val linksJson = Gson().toJson(userList)
                    val editor = getSharedPreferences(LINKS_PREFS, MODE_PRIVATE).edit()
                    editor.putString(LINKS_KEY, linksJson)
                    editor.apply()

                    Toast.makeText(this,"Adding User Information Success",Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                } catch (e: Exception){
                    Toast.makeText(this,"Error occurred: ${e.message}",Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this,"Cancel",Toast.LENGTH_SHORT).show()
            }
        }
        addDialog.setNegativeButton("Cancel"){
            dialog,_->
            dialog.dismiss()
            Toast.makeText(this,"Cancel",Toast.LENGTH_SHORT).show()

        }
        addDialog.create()
        addDialog.show()
    }
    /**ok now run this */

}

//@SuppressLint("NotifyDataSetChanged")
//private fun addInfo() {
//    val parser = parsingurl()
//    val inflater = LayoutInflater.from(this)
//    val v = inflater.inflate(R.layout.add_item,null)
//    /**set view*/
//    val addDialog = AlertDialog.Builder(this)
//    addDialog.setView(v)
//    addDialog.setPositiveButton("Ok"){ dialog,_ ->
//        val enteredLink =Link.text.toString().trim()
//        if (enteredLink.isNotEmpty()) {
//            val id = userList.size + 1
//            val link = parser.fromUrl(enteredLink, id)
//            userList.add(link)
//            userAdapter.notifyDataSetChanged()
//            Toast.makeText(this,"Adding User Information Success",Toast.LENGTH_SHORT).show()
//            dialog.dismiss()
//        } else {
//            Toast.makeText(this,"Please enter a valid link",Toast.LENGTH_SHORT).show()
//        }
//    }
//    addDialog.setNegativeButton("Cancel"){ dialog, _ ->
//        dialog.dismiss()
//        Toast.makeText(this,"Cancel",Toast.LENGTH_SHORT).show()
//    }
//    addDialog.create()
//    addDialog.show()
//}