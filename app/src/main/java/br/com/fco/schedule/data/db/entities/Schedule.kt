package br.com.fco.schedule.data.db.entities

import br.com.fco.schedule.data.model.StatusSchedule
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Schedule : RealmObject() {
    @PrimaryKey
    var id: Int = 0
    var title: String = ""
    var description: String = ""
    var details: String = ""
    var author: String = ""
    var status: String = StatusSchedule.OPEN.name
}