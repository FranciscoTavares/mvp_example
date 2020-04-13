package br.com.fco.schedule.ui.open_schedule

import br.com.fco.schedule.data.db.entities.Schedule
import br.com.fco.schedule.data.db.repositories.ScheduleRepository
import br.com.fco.schedule.data.model.StatusSchedule

object OpenSchedulePresenter {
    var startProgress = {}
    var stopProgress: (success: Boolean) -> Unit = {}
    var updateUi = {}
    var setStateEmpty: (stateEmpty: Boolean) -> Unit = {}

    fun getAllOpenScedule(): ArrayList<Schedule> {
        val list = ScheduleRepository.findAllOpen()
        setStateEmpty(list.isEmpty())
        return ArrayList(list)
    }

    fun handlerChangeStateSchedule(schedule: Schedule) {
        val statusActual: String = if (schedule.status == StatusSchedule.OPEN.name) {
            StatusSchedule.CLOSED.name
        } else {
            StatusSchedule.OPEN.name
        }
        ScheduleRepository.initChangeListener {
            updateUi()
        }
        ScheduleRepository.updateStatus(schedule, statusActual)
    }
}
