package com.example.surveyapp.Model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.surveyapp.R

class Results(private val context: Context, private val resultsList: ArrayList<Result>) :
    BaseAdapter() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getItem(position: Int): Any = resultsList[position]

    override fun getCount(): Int = resultsList.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: inflater.inflate(R.layout.activity_result_list_view, parent, false)

        val questionNo = view.findViewById<TextView>(R.id.questionNo)
        val result1 = view.findViewById<TextView>(R.id.text_result1)
        val result2 = view.findViewById<TextView>(R.id.text_result2)
        val result3 = view.findViewById<TextView>(R.id.text_result3)
        val result4 = view.findViewById<TextView>(R.id.text_result4)
        val result5 = view.findViewById<TextView>(R.id.text_result5)

        val currentResult = resultsList[position]
        questionNo.text = "Question: ${currentResult.x}"
        result1.text = "${currentResult.result1}%"
        result2.text = "${currentResult.result2}%"
        result3.text = "${currentResult.result3}%"
        result4.text = "${currentResult.result4}%"
        result5.text = "${currentResult.result5}%"

        return view
    }
}



