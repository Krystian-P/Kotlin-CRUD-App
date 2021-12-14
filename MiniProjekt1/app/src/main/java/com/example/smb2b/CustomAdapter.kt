package com.example.smb2b


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.smb2b.DB.ShopE
import com.example.smb2b.databinding.ElementBinding
import java.security.AccessController.getContext
import kotlin.collections.ArrayList


class RecyclerViewAdapter(val listener: RowClickListener) : RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {

    var items = ArrayList<ShopE>()

    fun setListData(data: ArrayList<ShopE>) {
        this.items = data
    }



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):  RecyclerViewAdapter.MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ElementBinding.inflate(inflater)
        return MyViewHolder(binding, listener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerViewAdapter.MyViewHolder, position: Int) {

        holder.itemView.setOnClickListener{
            listener.onItemClickListener(items[position])
        }

        holder.bind(items[position])

    }


    class MyViewHolder(val binding: ElementBinding, val listener: RowClickListener): RecyclerView.ViewHolder(binding.root) {

        val tvName = binding.tvItem
        val tvEmail = binding.tvPrice
        val tvPhone = binding.tvAmount
        val deleteUserID = binding.deleteUserID

        fun bind(data: ShopE) {
            tvName.text = data.nazwa
            tvEmail.text = data.cena
            tvPhone.text = data.ilosc
            deleteUserID.setOnClickListener {
                listener.onDeleteUserClickListener(data)
            }
        }
    }

    interface RowClickListener{
        fun onDeleteUserClickListener(item: ShopE)
        fun onItemClickListener(item: ShopE)
    }

    fun setShopList(students: ArrayList<ShopE>){
        this.items = students
        notifyDataSetChanged()
    }
}