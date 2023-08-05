package com.example.surveyapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.surveyapp.Model.Database.Survey
import java.util.Date

class SurveyLists(private val context: Context, private val surveyList: ArrayList<Survey>) : BaseAdapter() {
    override fun getItem(position: Int): Any = surveyList[position]

    override fun getCount(): Int = surveyList.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.activity_list_view, parent, false)

        val surveyTitle = view.findViewById<TextView>(R.id.textView3)
        val surveyStartDate = view.findViewById<TextView>(R.id.textView4)
        val surveyEndDate = view.findViewById<TextView>(R.id.textView5)
        val currentSurvey = surveyList[position]

        if (currentSurvey.startDate != null && currentSurvey.endDate != null) {
            surveyTitle.text = currentSurvey.title
            surveyStartDate.text = Date(currentSurvey.startDate).toString()
            surveyEndDate.text = Date(currentSurvey.endDate).toString()

        }

        return view
    }
}