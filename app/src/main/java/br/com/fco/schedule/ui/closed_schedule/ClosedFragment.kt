package br.com.fco.schedule.ui.closed_schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import br.com.fco.schedule.R
import br.com.fco.schedule.data.db.entities.Schedule
import br.com.fco.schedule.ui.create_schedule.CreateScheduleActivity
import br.com.fco.schedule.ui.open_schedule.CustomExpandableListAdapter
import br.com.fco.schedule.utils.initActivity
import br.com.fco.schedule.utils.startButtonProgress
import br.com.fco.schedule.utils.stopButtonProgress
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.adapter_list_item.view.*
import kotlinx.android.synthetic.main.fragment_schedule.*

class ClosedFragment : Fragment() {

    private var adapter: CustomExpandableListAdapter? = null
    private var lastPosition = -1
    private var lastProgressBar: ProgressBar? = null
    private var lastButton: MaterialButton? = null
    private var lastButtonText: CharSequence? = null
    private var presenter: ClosedSchedulePresenter? = null

    private val stateButton: Map<Boolean, Int> = hashMapOf(
        true to R.string.activity_create_schedule_btn_reopen,
        false to R.string.activity_create_schedule_btn_conclude
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = CustomExpandableListAdapter(ArrayList())
        expandableListView.setAdapter(adapter)
    }

    override fun onResume() {
        super.onResume()
        initListeners()
        updateScheduleList()
    }

    override fun onPause() {
        super.onPause()
        stopListeners()
    }

    private fun stopListeners() {
        presenter = null
    }

    private fun initListeners() {

        presenter = ClosedSchedulePresenter

        expandableListView.setOnGroupExpandListener { groupPosition ->
            if (lastPosition != -1
                && groupPosition != lastPosition
            ) {
                expandableListView.collapseGroup(lastPosition)
            }
            lastPosition = groupPosition
        }

        expandableListView.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            v.btnChangeState.setOnClickListener {
                lastProgressBar = v.loading
                lastButton = v.btnChangeState
                lastButtonText = v.btnChangeState.text
                presenter?.handlerChangeStateSchedule(
                    adapter?.getChild(
                        groupPosition,
                        groupPosition
                    ) as Schedule
                )
            }
            false
        }

        adapter?.changeState = { schedule, progress, btn ->
            lastProgressBar = progress
            lastButton = btn
            lastButtonText = btn.text

            presenter?.handlerChangeStateSchedule(schedule)
        }

        presenter?.startProgress = {
            startButtonProgress(lastProgressBar, lastButton)
        }

        presenter?.stopProgress = { success ->
            if (lastProgressBar != null && lastButton != null) {
                stopButtonProgress(
                    lastProgressBar,
                    lastButton,
                    getString(stateButton[success] ?: 0)
                )
            }
        }

        presenter?.updateUi = {
            updateScheduleList()
        }

        presenter?.setStateEmpty = { isEmpty ->
            if (isEmpty) {
                placeholder.isVisible = true
                placeholder.text = context?.getString(R.string.cloded_fragment_placeholder)
                expandableListView.isVisible = false
            } else {
                placeholder.isVisible = false
                expandableListView.isVisible = true
            }
        }

        btnCreateSchedule.setOnClickListener {
            activity?.initActivity(CreateScheduleActivity())
        }
    }

    private fun updateScheduleList() {
        adapter?.replaceAll(presenter?.getAllClosedSchedule() ?: ArrayList())
    }
}
