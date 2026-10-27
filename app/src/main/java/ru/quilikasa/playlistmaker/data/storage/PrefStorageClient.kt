package ru.quilikasa.playlistmaker.data.storage

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.google.gson.Gson
import ru.quilikasa.playlistmaker.data.StorageClient
import java.lang.reflect.Type

class PrefStorageClient<T>(context: Context, prefsName: String, private val type: Type) : StorageClient<T> {

    private val prefs: SharedPreferences = context.getSharedPreferences(prefsName, MODE_PRIVATE)
    private val gson: Gson = Gson()

    override fun addItemList(list: List<T>, key: String) {
        val json = gson.toJson(list, type)
        prefs.edit().putString(key, json).apply()
    }

    override fun getItemList(key: String): List<T> {
        val json = prefs.getString(key, "")
        if (json?.length == 0) return listOf()
        val list: MutableList<T> = gson.fromJson(json, type)
        list.reverse()
        return list
    }

    override fun clear(key: String) {
        prefs.edit().putString(key, "").apply()
    }
}