package com.example.surveyapp.Model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.surveyapp.R

class Results(private val appContext: Context, private val resultsList: ArrayList<Result>) :
    BaseAdapter() {
    private val inflater: LayoutInflater =
        appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getItem(p0: Int): Any {
        return p0
    }

    override fun getCount(): Int {
        return resultsList.size
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }


    override fun getView(p0: Int, view: View?, parent: ViewGroup?): View {
        var view: View? = view

        view = inflater.inflate(R.layout.activity_result_list_view, parent, false)

        val questionNo = view.findViewById<TextView>(R.id.questionNo)
        val result1 = view.findViewById<TextView>(R.id.text_result1)
        val result2 = view.findViewById<TextView>(R.id.text_result2)
        val result3 = view.findViewById<TextView>(R.id.text_result3)
        val result4 = view.findViewById<TextView>(R.id.text_result4)
        val result5 = view.findViewById<TextView>(R.id.text_result5)


        questionNo.text = "Question: " + resultsList[p0].x.toString()
        result1.text = resultsList[p0].result1.toString() + "%"
        result2.text = resultsList[p0].result2.toString() + "%"
        result3.text = resultsList[p0].result3.toString() + "%"
        result4.text = resultsList[p0].result4.toString() + "%"
        result5.text = resultsList[p0].result5.toString() + "%"

        return view
    }
}