package com.example.taskmanager
class NoteItem {

    var id: Int? = null
    var title: String? = null
    var type: String? = null
    var desc: String? = null
    var due_date: String? = null
    var status: String? = null

    constructor(id: Int, title: String, type: String,desc: String, due_date: String, status: String) {
        this.id = id
        this.title = title
        this.type = type
        this.desc = desc
        this.due_date = due_date
        this.status = status
    }

}
