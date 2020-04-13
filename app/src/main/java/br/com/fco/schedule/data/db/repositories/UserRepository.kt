package br.com.fco.schedule.data.db.repositories

import br.com.fco.schedule.data.db.entities.User
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where

object UserRepository {

    fun findOne(id: Int): User? {
        return Realm.getDefaultInstance().where<User>().equalTo("id", id).findFirst()
    }

    fun findAll(): RealmResults<User> {
        return Realm.getDefaultInstance().where<User>().findAll()
    }

    fun authentication(email: String, password: String): Boolean {
        val realm = Realm.getDefaultInstance()
        val result =
            realm.where<User>()
                .beginGroup()
                .equalTo("email", email)
                .and()
                .equalTo("password", password)
                .endGroup()
                .findFirst()
        return result?.let { user ->
            realm.executeTransaction {
                user.logged = true
                it.insertOrUpdate(user)
            }
            true
        } ?: run {
            false
        }
    }

    fun getLoggedUser(): User? {
        return Realm.getDefaultInstance().where<User>().equalTo("logged", true).findFirst()
    }

    fun getUserByEmail(email: String): User? {
        return Realm.getDefaultInstance().where<User>().equalTo("email", email).findFirst()
    }

    fun setLogout(callBack: (success: Boolean) -> Unit) {
        Realm.getDefaultInstance().executeTransactionAsync({ bgRealm ->
            val user = bgRealm.where<User>().equalTo("logged", true).findFirst()
            user?.let {
                it.logged = false
                bgRealm.insertOrUpdate(it)
            }
        }, {
            callBack(true)
        }, {
            callBack(false)
        })
    }

    fun insertOne(t: User, callBack: (success: Boolean) -> Unit) {
        Realm.getDefaultInstance().executeTransactionAsync({ bgRealm ->
            val currentIdNum = bgRealm.where<User>().max("id")
            val nextId = if (currentIdNum == null) {
                1
            } else {
                currentIdNum.toInt() + 1
            }
            t.id = nextId
            bgRealm.insertOrUpdate(t)
        }, {
            callBack(true)
        }, {
            callBack(false)
        })
    }

    fun saveOrUpdate(t: User) {
        Realm.getDefaultInstance().executeTransaction {
            it.insertOrUpdate(t)
        }
    }
}