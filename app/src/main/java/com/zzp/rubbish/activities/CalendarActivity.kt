package com.zzp.rubbish.activities

import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ldf.calendar.Utils
import com.ldf.calendar.component.CalendarAttr
import com.ldf.calendar.component.CalendarViewAdapter
import com.ldf.calendar.interf.OnSelectDateListener
import com.ldf.calendar.model.CalendarDate
import com.ldf.calendar.view.Calendar
import com.ldf.calendar.view.MonthPager
import com.zzp.rubbish.R
import com.zzp.rubbish.adapters.TaskAdapter
import com.zzp.rubbish.checkWindow
import com.zzp.rubbish.data.Task
import com.zzp.rubbish.util.TaskDatabase
import com.zzp.rubbish.views.CustomDayView
import kotlinx.coroutines.DelicateCoroutinesApi
import java.util.ArrayList
import java.util.HashMap
import kotlin.concurrent.thread
import kotlin.math.abs
import kotlin.math.sqrt

class CalendarActivity : AppCompatActivity() {
    private lateinit var tvYear: TextView
    private lateinit var tvMonth: TextView
    private lateinit var backImage: ImageView
    private lateinit var content: CoordinatorLayout
    private lateinit var monthPager: MonthPager
    private lateinit var rvToDoList: RecyclerView
    private var currentCalendars = ArrayList<Calendar?>()
    private lateinit var calendarAdapter: CalendarViewAdapter
    private var onSelectDateListener: OnSelectDateListener? = null
    private var mCurrentPage = MonthPager.CURRENT_DAY_INDEX
    private lateinit var context: Context
    private lateinit var currentDate: CalendarDate
    private var initiated = false
    private var taskAdapter = TaskAdapter()


    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        checkWindow(window)
        context = this
        content = findViewById(R.id.content)
        monthPager = findViewById(R.id.calendar_view)
        backImage = findViewById(R.id.back_image)
        //????????????setViewHeight??????????????????????????????????????????
        monthPager.viewHeight = Utils.dpi2px(context, 270f)
        tvYear = findViewById(R.id.show_year_view)
        tvMonth = findViewById(R.id.show_month_view)
        rvToDoList = findViewById(R.id.task_recycler)
        rvToDoList.setHasFixedSize(true)

        rvToDoList.layoutManager = LinearLayoutManager(this)
        rvToDoList.adapter = taskAdapter

        initCurrentDate()
        initCalendarView()
        backImage.setOnClickListener { finish() }
    }

    /**
     * onWindowFocusChanged??????????????????????????????????????????????????????
     *
     * @return void
     */
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus && !initiated) {
            refreshMonthPager()
            initiated = true
        }
    }

    /**
     * ?????????currentDate
     *
     * @return void
     */
    private fun initCurrentDate() {
        currentDate = CalendarDate()
        tvYear.text =  "${currentDate.year}???"
        tvMonth.text = currentDate.month.toString() + ""
        val taskDao =  TaskDatabase.getDatabase().taskDao()
        thread {
//            taskDao.insertTask(Task("??????????????????????????????", "????????????",
//                "${currentDate.year}.${currentDate.month}.${currentDate.day -1}"))
//            taskDao.insertTask(Task("?????????????????????????????????", "????????????",
//                "${currentDate.year}.${currentDate.month}.${currentDate.day -1}"))
//            taskDao.insertTask(Task("?????????????????????????????????", "????????????",
//                "${currentDate.year}.${currentDate.month}.${currentDate.day}"))
            taskAdapter.setTaskList(
                TaskDatabase.getDatabase().taskDao().selectTaskByTime(
                    "${currentDate.year}.${currentDate.month}.${currentDate.day}"))
            runOnUiThread {
                taskAdapter.notifyDataSetChanged()
            }
        }
    }

    /**
     * ?????????CustomDayView????????????CalendarViewAdapter???????????????
     */
    @DelicateCoroutinesApi
    private fun initCalendarView() {
        initListener()
        val customDayView = CustomDayView(context, R.layout.custom_day)
        calendarAdapter = CalendarViewAdapter(
            context,
            onSelectDateListener,
            CalendarAttr.CalendarType.MONTH,
            CalendarAttr.WeekArrayType.Monday,
            customDayView
        )
        calendarAdapter.setOnCalendarTypeChangedListener { rvToDoList.scrollToPosition(0) }
        initMarkData()
        initMonthPager()
    }

    /**
     * ????????????????????????HashMap????????????????????????
     * ????????????????????????????????????setMarkData???????????? calendarAdapter.notifyDataChanged();
     */
    private fun initMarkData() {
        val markData = HashMap<String, String>()
        markData["2017-8-9"] = "1"
        markData["2017-7-9"] = "0"
        markData["2017-6-9"] = "1"
        markData["2017-6-10"] = "0"
        calendarAdapter.setMarkData(markData)
    }

    @DelicateCoroutinesApi
    private fun initListener() {
        onSelectDateListener = object : OnSelectDateListener {
            override fun onSelectDate(date: CalendarDate) {
                refreshClickDate(date)
                thread {
                    taskAdapter.setTaskList(
                        TaskDatabase.getDatabase().taskDao().selectTaskByTime(
                            "${date.year}.${date.month}.${date.day}"))
                    runOnUiThread {
                        taskAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onSelectOtherMonth(offset: Int) {
                //????????? -1????????????????????????????????? ??? 1?????????????????????????????????
                monthPager.selectOtherMonth(offset)
            }
        }
    }


    private fun refreshClickDate(date: CalendarDate) {
        currentDate = date
        tvYear.text = date.getYear().toString() + "???"
        tvMonth.text = date.getMonth().toString() + ""
    }

    /**
     * ?????????monthPager???MonthPager?????????ViewPager
     *
     * @return void
     */
    private fun initMonthPager() {
        monthPager.adapter = calendarAdapter
        monthPager.currentItem = MonthPager.CURRENT_DAY_INDEX
        monthPager.setPageTransformer(false
        ) { page, position ->
            val position1 = sqrt((1 - abs(position)).toDouble()).toFloat()
            page.alpha = position1
        }
        monthPager.addOnPageChangeListener(object : MonthPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                mCurrentPage = position
                currentCalendars = calendarAdapter.pagers
                if (currentCalendars[position % currentCalendars.size] != null) {
                    val date = currentCalendars[position % currentCalendars.size]!!
                        .seedDate
                    currentDate = date
                    tvYear.text = date.year.toString() + "???"
                    tvMonth.text = date.month.toString() + ""
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun refreshMonthPager() {
        val today = CalendarDate()
        calendarAdapter.notifyDataChanged(today)
        tvYear.text = today.year.toString() + "???"
        tvMonth.text = today.month.toString() + ""
    }
}