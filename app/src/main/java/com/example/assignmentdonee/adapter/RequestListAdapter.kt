package com.example.assignmentdonee.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

import java.util.ArrayList

import com.example.assignmentdonee.R

import com.example.assignmentdonee.model.Requests

class RequestListAdapter(private var activity: Activity, private var items: ArrayList<Requests>): BaseAdapter()  {

    private class ViewHolder(row: View?) {
        var txtTitle: TextView? = null

        init {
            this.txtTitle = row?.findViewById(R.id.textModelo)
        }
    }

    @SuppressLint("InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View?
        val viewHolder: ViewHolder
        if (convertView == null) {
            val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.activity_requestlist, null)
            viewHolder = ViewHolder(view)
            view?.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val request = items[position]
        viewHolder.txtTitle?.text = request.requestID

        return view as View
    }

    override fun getItem(i: Int): Requests {
        return items[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getCount(): Int {
        return items.size
    }
}