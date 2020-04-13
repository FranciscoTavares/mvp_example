package br.com.fco.schedule.ui.open_schedule

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ProgressBar
import br.com.fco.schedule.R
import br.com.fco.schedule.data.db.entities.Schedule
import br.com.fco.schedule.data.model.StatusSchedule
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.adapter_list_group.view.*
import kotlinx.android.synthetic.main.adapter_list_item.view.*

class CustomExpandableListAdapter internal constructor(
    private val list: ArrayList<Schedule>
) : BaseExpandableListAdapter() {

    var changeState: (schedule: Schedule, progress: ProgressBar, btn: MaterialButton) -> Unit =
        { _, _, _ -> }

    override fun getGroup(groupPosition: Int): Any {
        return this.list[groupPosition]
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val view = if (convertView == null) {
            val layoutInflater =
                parent?.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            layoutInflater.inflate(R.layout.adapter_list_group, null)
        } else {
            convertView
        }

        val title = view.title
        title.setTypeface(null, Typeface.BOLD)
        title.text = list[groupPosition].title

        val description = view.description
        description.text = list[groupPosition].description
        return view
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return 1
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return this.list[groupPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val view = if (convertView == null) {
            val layoutInflater =
                parent?.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            layoutInflater.inflate(R.layout.adapter_list_item, null)
        } else {
            convertView
        }

        view.edtTitle.setText(list[groupPosition].title)
        view.edtDescription.setText(list[groupPosition].description)
        view.edtDetails.setText(list[groupPosition].details)
        view.edtAuthor.setText(list[groupPosition].author)

        view.btnChangeState.text = if (list[groupPosition].status == StatusSchedule.OPEN.name) {
            view.context.getString(R.string.activity_create_schedule_btn_conclude)
        } else {
            view.context.getString(R.string.activity_create_schedule_btn_reopen)
        }
        view.btnChangeState.setOnClickListener {
            changeState(list[groupPosition], view.loading, view.btnChangeState)
        }
        return view
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return list.size
    }

    fun replaceAll(newList: ArrayList<Schedule>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}