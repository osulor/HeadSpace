package com.example.headspacechallenge.ui.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.headspacechallenge.R
import com.example.headspacechallenge.data.model.FeatureModel
import kotlinx.android.synthetic.main.item_feature.view.*

class ItemViewHolder(item: View) : RecyclerView.ViewHolder(item) {
    fun bindItem(feature: FeatureModel) {
        itemView.authorTv.text = feature.author
        itemView.heightTv.text = feature.height.toString()
        itemView.widthTv.text = feature.width.toString()
        Glide.with(itemView)
            .load(feature.download_url)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_background)
            .fallback(R.drawable.ic_launcher_background)
            .into(itemView.itemImage)
    }
}