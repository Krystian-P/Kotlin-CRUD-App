package com.example.smb2b

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.smb2b.DB.ShopE
import com.example.smb2b.DB.ShopDB


class MainActivityViewModel(app: Application): AndroidViewModel(app) {
    lateinit var allUsers: MutableLiveData<List<ShopE>>

    init {
        allUsers = MutableLiveData()
        getAllUsers()
    }

    fun getAllUsersObservers(): MutableLiveData<List<ShopE>> {
        return allUsers
    }

    fun getAllUsers() {
        val shopDao = ShopDB.getAppDatabase((getApplication()))?.shopDao()
        val list = shopDao?.getAllUserInfo()

        allUsers.postValue(list)
    }

    fun insertUserInfo(entity: ShopE) {
        val userDao = ShopDB.getAppDatabase(getApplication())?.shopDao()
        userDao?.insertUser(entity)
        getAllUsers()
    }

    fun updateUserInfo(entity: ShopE){
        val userDao = ShopDB.getAppDatabase(getApplication())?.shopDao()
        userDao?.updateUser(entity)
        getAllUsers()
    }

    fun deleteUserInfo(entity: ShopE){
        val userDao = ShopDB.getAppDatabase(getApplication())?.shopDao()
        userDao?.deleteUser(entity)
        getAllUsers()
    }
}