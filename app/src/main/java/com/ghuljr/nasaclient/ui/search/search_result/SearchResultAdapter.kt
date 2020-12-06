package com.ghuljr.nasaclient.ui.search.search_result

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ghuljr.nasaclient.R
import com.ghuljr.nasaclient.data.model.NasaMediaModel
import com.ghuljr.nasaclient.utils.loadImage
import kotlinx.android.synthetic.main.item_search_result.view.*

class SearchResultAdapter(
    private val onItemClick: (nasaMedia: NasaMediaModel) -> Unit
) : RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    var searchResults: List<NasaMediaModel> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_search_result, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(searchResults[position])

    override fun getItemCount(): Int = searchResults.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(nasaModel: NasaMediaModel): Unit = with(itemView) {
            item_search_result_image.loadImage(nasaModel.thumbnailUrl)
            item_search_result_date.text = nasaModel.date
            item_search_result_title.text = nasaModel.title
            item_search_result_description.text = nasaModel.description
            itemView.setOnClickListener { onItemClick(nasaModel) }
        }
    }
}