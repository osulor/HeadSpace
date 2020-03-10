package com.example.headspacechallenge.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.headspacechallenge.R
import com.example.headspacechallenge.data.model.FeatureModel
import com.example.headspacechallenge.ui.adapter.viewholder.ItemViewHolder

class ItemAdapter constructor(val featureList: MutableList<FeatureModel>) :
    RecyclerView.Adapter<ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_feature,
                parent,
                false
            )
        )

    override fun getItemCount() = featureList.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bindItem(featureList[position])
    }
}