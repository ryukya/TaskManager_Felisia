package com.example.taskmanager

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var listNotes = ArrayList<NoteItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val floatingActionButton = findViewById<FloatingActionButton>(R.id.Add)
        floatingActionButton.setOnClickListener {
            var intent = Intent(this, NoteActivity::class.java)
            startActivity(intent)
        }

        loadselectAll()



    }
    override fun onResume() {
        super.onResume()
        loadselectAll()
    }

    fun loadselectAll() {

        var dbManager = DbManager(this)
        val cursor = dbManager.selectAll()

        listNotes.clear()
        if (cursor.moveToFirst()) {

            do {
                val id = cursor.getInt(cursor.getColumnIndex("Id"))
                val title = cursor.getString(cursor.getColumnIndex("Title"))
                val type = cursor.getString(cursor.getColumnIndex("Type"))
                val due = cursor.getString(cursor.getColumnIndex("Due_date"))
                val desc = cursor.getString(cursor.getColumnIndex("Desc"))
                val stat = cursor.getString(cursor.getColumnIndex("Status"))

                listNotes.add(NoteItem(id, title, type,desc, due,stat))

            } while (cursor.moveToNext())
        }

        var notesAdapter = NotesAdapter(this, listNotes)
        listnotes.adapter = notesAdapter
    }

    inner class NotesAdapter : BaseAdapter {

        private var notesList = ArrayList<NoteItem>()
        private var context: Context? = null

        constructor(context: Context, notesList: ArrayList<NoteItem>) : super() {
            this.notesList = notesList
            this.context = context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

            val view: View?
            val vh: ViewHolder

            if (convertView == null) {
                view = layoutInflater.inflate(R.layout.activity_note_item, parent, false)
                vh = ViewHolder(view)
                view.tag = vh
                Log.i("JSA", "set Tag for ViewHolder, position: " + position)
            } else {
                view = convertView
                vh = view.tag as ViewHolder
            }

            var mNote = notesList[position]

            vh.tvTitle.text = mNote.title
            vh.tDate.text = mNote.due_date
            vh.tStatus.text = mNote.status
            if(mNote.type=="Game"){
                vh.ivDelete.setImageResource(R.drawable.ic_sports_esports_24px)
            }else if(mNote.type=="Cooking"){
                vh.ivDelete.setImageResource(R.drawable.ic_local_dining_24px)
            }else if(mNote.type=="Groceries"){
                vh.ivDelete.setImageResource(R.drawable.ic_local_grocery_store_24px)
            }else if(mNote.type=="Mail"){
                vh.ivDelete.setImageResource(R.drawable.ic_email_24px)
            }else if(mNote.type=="Pet Walk"){
                vh.ivDelete.setImageResource(R.drawable.ic_pets_24px)
            }

            vh.ivEdit.setOnClickListener {
                updateNote(mNote)
            }

            vh.ivDelete.setOnClickListener {
                var dbManager = DbManager(this.context!!)
                val selectionArgs = arrayOf(mNote.id.toString())
                dbManager.delete("Id=?", selectionArgs)
                loadselectAll()
            }

            return view
        }

        override fun getItem(position: Int): Any {
            return notesList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return notesList.size
        }
    }

    private fun updateNote(note: NoteItem) {
        var intent = Intent(this, NoteActivity::class.java)
        intent.putExtra("MainActId", note.id)
        intent.putExtra("MainActTitle", note.title)
        intent.putExtra("MainActType", note.type)
        intent.putExtra("MainActDesc", note.desc)
        intent.putExtra("MainActDueDate", note.due_date)
        intent.putExtra("MainActStat", note.status)
        startActivity(intent)
    }

    private class ViewHolder(view: View?) {
        val tvTitle: TextView
        val tDate: TextView
        val tStatus: TextView
        val ivEdit: ImageView
        val ivDelete: ImageView

        init {
            this.tvTitle = view?.findViewById(R.id.tTitle) as TextView
            this.tDate = view?.findViewById(R.id.tdate) as TextView
            this.tStatus = view?.findViewById(R.id.tstatus) as TextView
            this.ivEdit = view?.findViewById(R.id.iEdit) as ImageView
            this.ivDelete = view?.findViewById(R.id.iDelete) as ImageView
        }
    }
}
