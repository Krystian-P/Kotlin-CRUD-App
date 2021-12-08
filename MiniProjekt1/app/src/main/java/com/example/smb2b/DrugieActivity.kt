package com.example.smb2b

import android.content.ComponentName
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smb2b.DB.ShopE
import com.example.smb2b.databinding.ActivityDrugieBinding
import java.util.*
import kotlin.collections.ArrayList

class DrugieActivity : AppCompatActivity(), RecyclerViewAdapter.RowClickListener{


    lateinit var viewModel: ActivityDrugieBinding
    lateinit var shopViewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ActivityDrugieBinding.inflate(layoutInflater)
        setContentView(viewModel.root)

        viewModel.rv1.layoutManager = LinearLayoutManager(this@DrugieActivity)
        val adapter = RecyclerViewAdapter(this@DrugieActivity, MainActivityViewModel(application))
        viewModel.rv1.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )

        shopViewModel = MainActivityViewModel(application)
        viewModel.rv1.adapter= adapter



        viewModel.rv1.adapter = adapter
        shopViewModel.getAllUsersObservers().observe(this, Observer{
            it.let{
                adapter.setShopList(ArrayList(it))
            }
        })

        viewModel.btAdd.setOnClickListener {

            val nazwa = viewModel.etProduct.text.toString()
            val cena  = viewModel.etName.text.toString()
            val ilosc = viewModel.etSurname.text.toString()
            if(viewModel.btAdd.text.equals("Save")) {
                val user = ShopE(0, nazwa, cena, ilosc)
                shopViewModel.insertUserInfo(user)
                val broadcastIntent = Intent()
                broadcastIntent.component= ComponentName(
                    "com.example.receiver3",
                    "com.example.receiver3.MyReceiver"
                )
                broadcastIntent.putExtra(
                    "str1",
                    viewModel.etProduct.text.toString()
                )
                sendBroadcast(broadcastIntent)
            }else  {
                val user = ShopE(viewModel.etProduct.getTag(viewModel.etProduct.id).toString().toInt(), nazwa, cena, ilosc)
                shopViewModel.updateUserInfo(user)
                viewModel.btAdd.setText("Save")
            }
            viewModel.etProduct.setText("")
            viewModel.etName.setText("")
            viewModel.etSurname.setText("")
        }
    }

    override fun onDeleteUserClickListener(item: ShopE) {

        shopViewModel.deleteUserInfo(item)
    }
    override fun onItemClickListener(item: ShopE) {
        viewModel.etProduct.setText(item.nazwa)
        viewModel.etName.setText(item.cena)
        viewModel.etSurname.setText(item.ilosc)
        viewModel.etProduct.setTag(viewModel.etProduct.id, item.id)
        viewModel.btAdd.setText("Update")

    }
}
