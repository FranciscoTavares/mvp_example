package br.com.fco.schedule.data.db.repositories

import br.com.fco.schedule.data.db.entities.Schedule
import br.com.fco.schedule.data.db.entities.User
import br.com.fco.schedule.data.model.StatusSchedule
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmList
import io.realm.RealmResults
import io.realm.kotlin.where

object ScheduleRepository {

    fun findAllOpen(): RealmList<Schedule> {
        val result = Realm.getDefaultInstance().where<User>()
            .beginGroup()
            .equalTo("logged", true)
            .equalTo("scheduleList.status", StatusSchedule.OPEN.name)
            .endGroup()
            .findFirst()
        return result?.scheduleList ?: RealmList()
    }

    fun findAllClosed(): RealmList<Schedule> {
        val result = Realm.getDefaultInstance().where<User>()
            .beginGroup()
            .equalTo("logged", true)
            .equalTo("scheduleList.status", StatusSchedule.CLOSED.name)
            .endGroup()
            .findFirst()
        return result?.scheduleList ?: RealmList()
    }

    fun insertOne(t: Schedule) {
        val realm = Realm.getDefaultInstance()
        val user = realm.where<User>().equalTo("logged", true).findFirst()
        val scheduleCount =
            realm.executeTransaction {
                val currentIdNum = realm.where<Schedule>().max("id")
                val nextId = if (currentIdNum == null || currentIdNum == 0) {
                    1
                } else {
                    currentIdNum.toInt() + 1
                }
                t.id = nextId

                user?.scheduleList?.add(t)
            }
    }

    fun initChangeListener(callBack: () -> Unit) {
        val realm = Realm.getDefaultInstance()
        val customers = realm.where<User>().findAllAsync()
        val changeListener = RealmChangeListener<RealmResults<User>> {
            callBack()
        }
        customers.addChangeListener(changeListener)
    }

    fun updateStatus(t: Schedule, newStatus: String) {
        Realm.getDefaultInstance().executeTransaction { realm ->

            val user = realm.where<User>()
                .beginGroup()
                .equalTo("logged", true)
                .and()
                .equalTo("scheduleList.id", t.id)
                .endGroup()
                .findFirst()

            user?.let {
                it.scheduleList?.filter { schedule ->
                    schedule.id == t.id
                }?.first()?.status = newStatus

                realm.insertOrUpdate(it)
            }
        }
    }
}