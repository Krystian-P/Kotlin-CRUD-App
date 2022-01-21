package com.example.smb2b

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smb2b.databinding.ShopElementsBinding
import com.google.type.LatLng

class ShopListAdapter(private val shopList: ArrayList<Shop>, val listener: RowClickListener) : RecyclerView.Adapter<ShopListAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ShopElementsBinding.inflate(inflater)
        return MyViewHolder(binding, listener)
    }


    override fun getItemCount(): Int {
        return  shopList.size
    }



    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val shop: Shop = shopList[position]
        holder.tvName.text = shop.nazwa
        holder.tvDesc.text = shop.desc
        holder.tvCords.text = shop.lat
        holder.deleteShopID.setOnClickListener {
            listener.onDeleteShopClickListener(shop)
        }
    }

    class MyViewHolder(binding: ShopElementsBinding, listener: RowClickListener): RecyclerView.ViewHolder(binding.root){
        val tvName = binding.tvName
        val tvDesc = binding.tvDesc
        val tvCords = binding.tvCords
        val deleteShopID = binding.deleteShopID
    }

    interface RowClickListener{
        fun onDeleteShopClickListener(shop: Shop)
    }

}