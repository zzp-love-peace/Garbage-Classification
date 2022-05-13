package com.zzp.rubbish.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.ldf.calendar.Utils
import com.ldf.calendar.component.State
import com.ldf.calendar.interf.IDayRenderer
import com.ldf.calendar.model.CalendarDate
import com.ldf.calendar.view.DayView
import com.zzp.rubbish.R

@SuppressLint("ViewConstructor")
class CustomDayView(context: Context, layoutResource: Int) : DayView(context, layoutResource) {

    private var dateTv: TextView = findViewById<View>(R.id.date) as TextView
    private var marker: ImageView = findViewById<View>(R.id.maker) as ImageView
    private var selectedBackground: View = findViewById(R.id.selected_background)
    private var todayBackground: View = findViewById(R.id.today_background)
    private val today = CalendarDate()

    override fun refreshContent() {
        renderToday(day.date)
        renderSelect(day.state)
        renderMarker(day.date, day.state)
        super.refreshContent()
    }

    private fun renderMarker(date: CalendarDate, state: State) {
        if (Utils.loadMarkData().containsKey(date.toString())) {
            if (state == State.SELECT || date.toString() == today.toString()) {
                marker.visibility = GONE
            } else {
                marker.visibility = VISIBLE
                marker.isEnabled = Utils.loadMarkData()[date.toString()] == "0"
            }
        } else {
            marker.visibility = GONE
        }
    }

    private fun renderSelect(state: State) {
        if (state == State.SELECT) {
            selectedBackground.visibility = VISIBLE
            dateTv.setTextColor(Color.WHITE)
        } else if (state == State.NEXT_MONTH || state == State.PAST_MONTH) {
            selectedBackground.visibility = GONE
            dateTv.setTextColor(Color.parseColor("#d5d5d5"))
        } else {
            selectedBackground.visibility = GONE
            dateTv.setTextColor(Color.parseColor("#111111"))
        }
    }

    private fun renderToday(date: CalendarDate?) {
        if (date != null) {
            if (date.equals(today)) {
                dateTv.text = "今"
                todayBackground.visibility = VISIBLE
            } else {
                dateTv.text = date.day.toString()
                todayBackground.visibility = GONE
            }
        }
    }

    override fun copy(): IDayRenderer {
        return CustomDayView(context, layoutResource)
    }
}