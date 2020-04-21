package com.example.taskmanager

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_note.*

class NoteActivity : AppCompatActivity() {
    var id = 0
    var types= "Meeting"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        try {
            var bundle: Bundle = intent.extras!!
            id = bundle.getInt("MainActId", 0)
            if (id != 0) {
                editTitle.setText(bundle.getString("MainActTitle"))
                editDesc.setText(bundle.getString("MainActDesc"))
                editDate.setText(bundle.getString("MainActDueDate"))
                sType.prompt=(bundle.getString("MainActType"))
                vStat.setText(bundle.getString("MainActStat"))

            }
        } catch (ex: Exception) {
        }

        btSave.setOnClickListener {
            var dbManager = DbManager(this)

            var values = ContentValues()
            values.put("Title", editTitle.text.toString())
            values.put("Desc", editDesc.text.toString())
            values.put("Type", types)
            values.put("Due_date", editDate.text.toString())
            values.put("Status", vStat.text.toString())

            if (id == 0) {
                val modelID = dbManager.insert(values)

                if (modelID > 0) {
                    Toast.makeText(this, "Task saved!", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(this, "Fail to save!", Toast.LENGTH_LONG).show()
                }
            } else {
                var selectionArs = arrayOf(id.toString())
                val modelID = dbManager.update(values, "Id=?", selectionArs)

                if (modelID > 0) {
                    Toast.makeText(this, "Task saved!", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(this, "Fail to save!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
