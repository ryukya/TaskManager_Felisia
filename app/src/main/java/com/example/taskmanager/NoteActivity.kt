package com.example.taskmanager

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import kotlinx.android.synthetic.main.activity_note.*

class NoteActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    var id = 0
    var types= "Meeting"
    lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        try {
            var bundle: Bundle = intent.extras!!
            id = bundle.getInt("MainActId", 0)
            if (id != 0) {
                editTitle.setText(bundle.getString("MainActTitle"))
                editDesc.setText(bundle.getString("MainActDesc"))
                var date=bundle.getString("MainActDueDate") //ex 01/mm/2020
                var monthOfYear= date?.substring(3,4)?.toInt()
                var year= date?.substring(6,9)?.toInt()
                var dayOfMonth= date?.substring(0,1)?.toInt()
                if (monthOfYear != null) {
                    if (year != null) {
                        if (dayOfMonth != null) {
                            editDate.init( year,  monthOfYear,  dayOfMonth)
                        }
                    }
                }
                sType.prompt=(bundle.getString("MainActType"))
                vStat.setText(bundle.getString("MainActStat"))

            }
        } catch (ex: Exception) {
        }
        spinner = findViewById(R.id.sType)
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.type_arrays,
            android.R.layout.simple_spinner_item
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this


        btSave.setOnClickListener {
            if (editTitle.text.toString().isEmpty() || editTitle.text.toString().isEmpty() )
            {
            Toast.makeText(this, getString(R.string.emptynote), Toast.LENGTH_LONG).show()
        } else{
                var dbManager = DbManager(this)

                var values = ContentValues()
                values.put("Title", editTitle.text.toString())
                values.put("Desc", editDesc.text.toString())

                values.put("Type", types)
                var day=editDate.dayOfMonth.toString()
                var month =editDate.month.toString()
                var year =editDate.year.toString()
                var sep="/"
                var t_date= day+sep+month+sep+year //ex 01/23/2020
                values.put("Due_date", t_date.toString())
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

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    override  fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val text: String = parent?.getItemAtPosition(position).toString()
       types = text
    }
}

private fun DatePicker.init(year: Int, monthOfYear: Int, dayOfMonth: Int) {

}
