package com.example.headspacechallenge.ui.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.headspacechallenge.data.model.FeatureModel
import kotlinx.android.synthetic.main.item_feature.view.*

class ItemViewHolder(item: View) : RecyclerView.ViewHolder(item) {
    fun bindItem(feature: FeatureModel) {
        itemView.authorTv.text = feature.author
        itemView.heightTv.text = feature.height.toString()
        itemView.widthTv.text = feature.width.toString()
        Glide.with(itemView).load(feature.download_url).into(itemView.itemImage)
    }
}