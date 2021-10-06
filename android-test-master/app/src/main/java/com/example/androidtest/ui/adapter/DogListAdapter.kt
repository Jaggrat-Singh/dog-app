package com.example.androidtest.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidtest.R
import com.example.androidtest.models.DogUIModel
import com.example.androidtest.ui.DogItemTappedListener
import com.example.androidtest.utils.loadImage

class DogListAdapter(private val listener: DogItemTappedListener) : RecyclerView.Adapter<DogListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textName: TextView = itemView.findViewById(R.id.tv_name)
        val textLifeSpan: TextView = itemView.findViewById(R.id.tv_life_span)
        val textTemperament: TextView = itemView.findViewById(R.id.tv_temperament)
        val dogImage: ImageView = itemView.findViewById(R.id.iv_dog_image)
    }

    private val dogUIModelList = mutableListOf<DogUIModel>()

    override fun getItemCount(): Int = dogUIModelList.size

    fun setDogUIData(list: List<DogUIModel>?) {
        list?.let {
            dogUIModelList.clear()
            dogUIModelList.addAll(list)
            notifyDataSetChanged()
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dogUIModel: DogUIModel = dogUIModelList[position]
        holder.textName.text = dogUIModel.name
        holder.textLifeSpan.text = dogUIModel.lifeSpan
        holder.textTemperament.text = dogUIModel.temperament
        holder.dogImage.loadImage(holder.itemView.context, dogUIModel.imageUrl)
        holder.itemView.setOnClickListener { view -> listener.onItemTapped(dogUIModelList[position]) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(v)
    }
}