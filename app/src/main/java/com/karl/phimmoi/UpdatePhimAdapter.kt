package com.karl.phimmoi

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_rcv.view.*

class UpdatePhimAdapter(private val listPhims: ArrayList<Phim>, private val context: Context) :
    RecyclerView.Adapter<UpdatePhimAdapter.ViewHolder>() {
    private var onItemClickListenner: OnItemClickListenner? = null
    private val inflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.item_rcv, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listPhims.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val phim = listPhims[position]
        holder.txtVNName.text = phim.vnName
        Glide.with(context).load(phim.imageLink).into(holder.imvPhim)
        holder.imvPhim.setOnClickListener {
            onItemClickListenner!!.onItemClicked(phim)
        }
    }

    fun setOnItemClickListenner(onItemClickListenner: OnItemClickListenner) {
        this.onItemClickListenner = onItemClickListenner
    }

    interface OnItemClickListenner {
        fun onItemClicked(phim: Phim)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtVNName = itemView.txtVNName
        val imvPhim = itemView.imvPhim

    }
}