package br.com.fco.schedule.data.db.entities

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class User : RealmObject() {
    @PrimaryKey
    var id: Int = 0
    var name: String = ""
    var email: String = ""
    var password: String = ""
    var logged: Boolean = false
    var scheduleList: RealmList<Schedule>? = RealmList()
}