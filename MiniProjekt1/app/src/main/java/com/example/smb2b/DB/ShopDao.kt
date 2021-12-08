package com.example.smb2b.DB

import androidx.room.*

@Dao
interface ShopDao {

    @Query("SELECT * FROM shopinfo ORDER BY id DESC")
    fun getAllUserInfo(): List<ShopE>?


    @Insert
    fun insertUser(user: ShopE?)

    @Delete
    fun deleteUser(user: ShopE?)

    @Update
    fun updateUser(user: ShopE?)

}