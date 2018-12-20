package com.karl.phimmoi

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.base_item.view.*
import kotlinx.android.synthetic.main.item_rcv.view.*
import kotlin.contracts.contract

class CustomGridViewSearch(private val listPhim: ArrayList<Phim>, val context: Context) : BaseAdapter() {


    private var onItemClickListenner: OnItemClickListenner? = null

    private val layoutInflater = LayoutInflater.from(context)
    @SuppressLint("ViewHolder")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View? {
        var viewHolder: ViewHolder? = null
        var view: View? = null
        if (view == null) {
            view = layoutInflater.inflate(R.layout.base_item, p2, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }
        val phim = listPhim[p0]
        viewHolder.txtName?.text = phim.vnName
        Glide.with(context)
            .load(phim.imageLink)
            .into(viewHolder.imgView!!)

        view?.setOnClickListener {
            onItemClickListenner?.onItemClicked(phim)

        }

        return view

    }

    fun setOnItemClickListenner(onItemClickListenner: OnItemClickListenner) {
        this.onItemClickListenner = onItemClickListenner
    }

    interface OnItemClickListenner {
        fun onItemClicked(phim: Phim)
    }


    override fun getItem(p0: Int): Any {
        return 0
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return listPhim.size
    }

    inner class ViewHolder(view: View) {
        var txtName: TextView? = null
        var imgView: ImageView? = null

        init {
            txtName = view.txtName
            imgView = view.imageView
        }
    }
}