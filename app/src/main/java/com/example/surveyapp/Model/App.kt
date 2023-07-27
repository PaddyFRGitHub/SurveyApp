package com.example.surveyapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.surveyapp.Model.Survey

class App(private val appContext: Context, private val surveyList: ArrayList<Survey>) :
    BaseAdapter() {

    private val inflater: LayoutInflater =
        appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getItem(p0: Int): Any {
        return p0
    }

    override fun getCount(): Int {
        return surveyList.size
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }


    override fun getView(p0: Int, view: View?, parent: ViewGroup?): View {
        var view: View? = view

        view = inflater.inflate(R.layout.activity_list_view, parent, false)

        val surveyTitle = view.findViewById<TextView>(R.id.textView3)
        val surveyStartDate = view.findViewById<TextView>(R.id.textView4)
        val surveyEndDate = view.findViewById<TextView>(R.id.textView5)


        surveyTitle.text = surveyList[p0].surveyTitle
        surveyStartDate.text = surveyList[p0].surveyStartDate.toString()
        surveyEndDate.text = surveyList[p0].surveyEndDate.toString()

        return view
    }


}
