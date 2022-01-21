package com.example.smb2b


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.smb2b.databinding.ElementBinding
import java.security.AccessController.getContext
import kotlin.collections.ArrayList


class RecyclerViewAdapter(private val itemList: ArrayList<Item>,val listener: RowClickListener) : RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):  MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ElementBinding.inflate(inflater)
        return MyViewHolder(binding, listener)
    }

    override fun getItemCount(): Int {

        return itemList.size

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {



        val item: Item = itemList[position]
        holder.tvNazwa.text = item.nazwa
        holder.tvCena.text = item.cena
        holder.tvIlosc.text = item.ilosc
        holder.deleteUserID.setOnClickListener {
            listener.onDeleteUserClickListener(item)
        }

        //holder.itemView.setOnClickListener{
            //listener.onItemClickListener(items[position])
        //}

        //holder.bind(items[position])
        holder.itemView.setOnClickListener{
            listener.onItemClickListener( item.nazwa, item.cena, item.ilosc)
        }

    }




    class MyViewHolder(binding: ElementBinding, val listener: RowClickListener): RecyclerView.ViewHolder(binding.root) {

        val tvNazwa = binding.tvItem
        val tvCena = binding.tvPrice
        val tvIlosc = binding.tvAmount
        val deleteUserID = binding.deleteUserID

    }

    interface RowClickListener{
        fun onDeleteUserClickListener(item: Item)
        fun onItemClickListener(nazwa: String, cena: String, ilosc: String)
    }



    //fun setShopList(item: ArrayList<Item>){
        //this.item = item
        //notifyDataSetChanged()
    //}
}
