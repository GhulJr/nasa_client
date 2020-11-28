package com.ghuljr.nasaclient.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ghuljr.nasaclient.R
import com.ghuljr.nasaclient.data.model.ApodModel
import com.ghuljr.nasaclient.utils.loadImage
import kotlinx.android.synthetic.main.item_apod.view.*

class ApodAdapter(
    private val onItemClick: (apod: ApodModel) -> Unit
) : RecyclerView.Adapter<ApodAdapter.ViewHolder>() {

    var apods: List<ApodModel> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_apod, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(apods[position])

    override fun getItemCount(): Int = apods.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(apod: ApodModel): Unit = with(itemView) {
            apod_thumbnail.loadImage(apod.url)
            apod_date.text = apod.date
            apod_title.text = apod.title
            itemView.setOnClickListener { onItemClick(apod) }
        }
    }
}