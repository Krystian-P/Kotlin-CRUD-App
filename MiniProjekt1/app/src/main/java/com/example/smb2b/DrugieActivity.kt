package com.example.smb2b

import android.content.ComponentName
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smb2b.databinding.ActivityDrugieBinding
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList

class DrugieActivity : AppCompatActivity(), RecyclerViewAdapter.RowClickListener{


    lateinit var viewModel: ActivityDrugieBinding
    lateinit var db: FirebaseFirestore
    lateinit var customAdapter: RecyclerViewAdapter
    lateinit var shopList: java.util.ArrayList<Item>
    lateinit var recyclerView: RecyclerView

    private val itemCollectionReference = Firebase.firestore.collection("item_list")
    private val itemInstantReference = FirebaseFirestore.getInstance().collection("item_list")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drugie)

        viewModel = ActivityDrugieBinding.inflate(layoutInflater)
        setContentView(viewModel.root)



        recyclerView = findViewById(R.id.rv1)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        shopList = arrayListOf()

        customAdapter = RecyclerViewAdapter(shopList, this)

        recyclerView.adapter =customAdapter

        EventChangeListener()


        viewModel.btAdd.setOnClickListener {

            val nazwa = viewModel.etProduct.text.toString()
            val cena  = viewModel.etPrice.text.toString()
            val ilosc = viewModel.etAmount.text.toString()
            val item = Item(nazwa, cena, ilosc)
            val Nnazwa = viewModel.tvProductO.text.toString()
            val Ncena  = viewModel.tvPriceO.text.toString()
            val Nilosc = viewModel.tvAmountO.text.toString()
            val newitem = Item(Nnazwa, Ncena, Nilosc)
            if(viewModel.btAdd.text.equals("Save")) {
                Toast.makeText(this,"clik", Toast.LENGTH_SHORT).show()
                saveItem(item)
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
                val newItemMap = getNewItemMap()
                updateItem(newitem, newItemMap)
                viewModel.btAdd.setText("Save")
                viewModel.tvProductO.setText("")
                viewModel.tvPriceO.setText("")
                viewModel.tvAmountO.setText("")
            }
            viewModel.etProduct.setText("")
            viewModel.etPrice.setText("")
            viewModel.etAmount.setText("")
        }

    }




    override fun onDeleteUserClickListener(item: Item) {
        deleteItem(item)
    }
    override fun onItemClickListener(nazwa: String, cena: String, ilosc: String) {

        viewModel.etProduct.setText(nazwa)
        viewModel.etPrice.setText(cena)
        viewModel.etAmount.setText(ilosc)

        viewModel.tvProductO.setText(nazwa)
        viewModel.tvPriceO.setText(cena)
        viewModel.tvAmountO.setText(ilosc)

        viewModel.btAdd.setText("Updata")
    }


    private fun saveItem(item: Item) = CoroutineScope(Dispatchers.IO).launch {
        try {
            itemCollectionReference.add(item).await()
            withContext(Dispatchers.Main){
                Toast.makeText(this@DrugieActivity, "Succesfully saved data", Toast.LENGTH_SHORT).show()
            }
        }catch (e: Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(this@DrugieActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getNewItemMap(): Map<String, Any>{
        val nazwa = viewModel.etProduct.text.toString()
        val cena =viewModel.etPrice.text.toString()
        val ilosc =viewModel.etAmount.text.toString()
        val map = mutableMapOf<String, Any>()
        if(nazwa.isNotEmpty()){
            map["nazwa"]= nazwa
        }
        if (cena.isNotEmpty()){
            map["cena"]= cena
        }
        if (ilosc.isNotEmpty()){
            map["ilosc"]= ilosc
        }
        return map
    }


    private fun updateItem(item: Item, newItemMap: Map<String, Any>) = CoroutineScope(Dispatchers.IO).launch {
        val itemQueue = itemCollectionReference
            .whereEqualTo("nazwa", item.nazwa)
            .whereEqualTo("cena", item.cena)
            .whereEqualTo("ilosc", item.ilosc)
            .get()
            .await()
        if(itemQueue.documents.isNotEmpty()){
            for (document in itemQueue){
                try {
                    itemCollectionReference.document(document.id).set(
                        newItemMap,
                        SetOptions.merge()
                    ).await()
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@DrugieActivity, "e.message", Toast.LENGTH_SHORT).show()
                    }
                }catch(e: Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@DrugieActivity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }else{
            withContext(Dispatchers.Main){
                Toast.makeText(this@DrugieActivity, "No item in store", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteItem(item: Item) = CoroutineScope(Dispatchers.IO).launch {
        val itemQueue = itemCollectionReference
            .whereEqualTo("nazwa", item.nazwa)
            .whereEqualTo("cena", item.cena)
            .whereEqualTo("ilosc", item.ilosc)
            .get()
            .await()
        if(itemQueue.documents.isNotEmpty()){
            for (document in itemQueue){
                try {
                    itemCollectionReference.document(document.id).delete().await()
                }catch(e: Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@DrugieActivity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }else{
            withContext(Dispatchers.Main){
                Toast.makeText(this@DrugieActivity, "No item in store", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun EventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("item_list")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }
                for (dc in snapshot!!.documentChanges) {
                    when (dc.type ) {
                       DocumentChange.Type.ADDED-> shopList.add(dc.document.toObject(Item::class.java))
//                        DocumentChange.Type.MODIFIED-> shopList.remove(dc.document.toObject(Item::class.java))
//                        DocumentChange.Type.MODIFIED-> shopList.add(dc.document.toObject(Item::class.java))
                        DocumentChange.Type.REMOVED-> shopList.remove(dc.document.toObject(Item::class.java))
                    }
                }
                customAdapter.notifyDataSetChanged()
            }

    }

    }

    //private fun EventChangeListener(){
    //db = FirebaseFirestore.getInstance()
    //db.collection("item_list")
    //.get()
    //.addOnSuccessListener { value ->
    //for(dc: DocumentChange in value?.documentChanges!!){
    //if( dc.type == DocumentChange.Type.ADDED){
    //shopList.add(dc.document.toObject(Item::class.java))
    //}
    //}
    //customAdapter.notifyDataSetChanged()
    //}


    //}

//    private fun lifeUpdate() {
//        itemInstantReference.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
//            firebaseFirestoreException?.let {
//                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
//                return@addSnapshotListener
//            }
//            querySnapshot?.let {
//
//                for (document in values!!) {
//
//                    val item = document.toObject(Item::class.java)
//                    shopList.add(item!!)
//                }
//                customAdapter.notifyDataSetChanged()
//            }
//        }
//    }


//    private fun retrievPersons() = CoroutineScope(Dispatchers.IO).launch {
//
//        try {
//            val querySnapshot = itemInstantReference.get().await()
//            for (document in querySnapshot.documents) {
//                val item = document.toObject(Item::class.java)
//                shopList.add(item!!)
//            }
//
//        } catch (e: Exception) {
//            withContext(Dispatchers.Main) {
//                Toast.makeText(this@DrugieActivity, e.message, Toast.LENGTH_LONG).show()
//            }
//            customAdapter.notifyDataSetChanged()
//        }
//    }




