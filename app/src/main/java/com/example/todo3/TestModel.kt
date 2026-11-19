package com.example.todo3

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class TestModel : RealmObject() {
    @PrimaryKey
    var id: String? = ""
    var date: String? = ""
    var description: String? = ""
}
