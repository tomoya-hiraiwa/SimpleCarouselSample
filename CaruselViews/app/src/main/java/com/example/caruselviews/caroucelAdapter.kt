package com.example.caruselviews

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.caruselviews.databinding.CaroucelItemBinding

class caroucelAdapter(private val imageList: List<Int>):RecyclerView.Adapter<caroucelAdapter.caroucelViewHolder>() {
    //実際のアイテム数+2した数を準備しておく
    private val addCount = imageList.size+2
    inner class caroucelViewHolder(private val binding: CaroucelItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bindData(data: Int){
            binding.caroucelImage.setImageResource(data)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): caroucelViewHolder {
        val binding = CaroucelItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return caroucelViewHolder(binding)
    }

    override fun getItemCount(): Int {
        //アイテム数は+2している方の値を渡す
        return addCount
    }
    //実際のアイテム数も取得できるようにしておく
    fun getRealCount():Int{
       return  imageList.size
    }

    override fun onBindViewHolder(holder: caroucelViewHolder, position: Int) {
        //実際に取得するべきアイテムのポジションを取得する
        val dataPosition = when(position){
            //0の時→一番最後のアイテムを指定
            0 -> getRealCount() - 1
            //実際のアイテムの個数を超えた時→最初のアイテムを指定
            getRealCount() + 1 -> 0
            //それ以外は実際のポジションから-1したアイテムを指定（アダプターのポジションが1を基準点にしているため）
            else  -> position -1
        }
        val data = imageList[dataPosition]
        holder.bindData(data)
    }
}