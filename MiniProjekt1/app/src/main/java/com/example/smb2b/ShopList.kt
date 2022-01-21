package com.example.smb2b

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smb2b.databinding.ActivityShopListBinding
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

class ShopList : AppCompatActivity(), ShopListAdapter.RowClickListener {

    lateinit var binding: ActivityShopListBinding
    lateinit var db: FirebaseFirestore
    lateinit var ShopListAdapter: ShopListAdapter
    lateinit var shopList: ArrayList<Shop>
    lateinit var recyclerView: RecyclerView

    private val shopCollectionReference = Firebase.firestore.collection("Shop_location")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_list)

        binding = ActivityShopListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = findViewById(R.id.rv2)
        recyclerView.layoutManager =LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        shopList = arrayListOf()

        ShopListAdapter = ShopListAdapter(shopList, this)

        recyclerView.adapter= ShopListAdapter
        EventChangeListener()
    }

    override fun onDeleteShopClickListener(shop: Shop){
        deleteShop(shop)
    }

    private fun deleteShop(shop: Shop) = CoroutineScope(Dispatchers.IO).launch {
        val shopQueue = shopCollectionReference
            .whereEqualTo("nazwa", shop.nazwa)
            .get()
            .await()
        if(shopQueue.documents.isNotEmpty()){
            for (document in shopQueue){
                try {
                    shopCollectionReference.document(document.id).delete().await()
                }catch (e: Exception){
                    withContext(Dispatchers.Main){}
                    Toast.makeText(this@ShopList, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            withContext(Dispatchers.Main){
                Toast.makeText(this@ShopList, "No shop in store", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun EventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("Shop_location")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }
                for (dc in snapshot!!.documentChanges) {
                    when (dc.type ) {
                        DocumentChange.Type.ADDED-> shopList.add(dc.document.toObject(Shop::class.java))
                        DocumentChange.Type.REMOVED-> shopList.remove(dc.document.toObject(Shop::class.java))
                   }
                }
                ShopListAdapter.notifyDataSetChanged()
            }
    }

}