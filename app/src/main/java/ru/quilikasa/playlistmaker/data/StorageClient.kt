package ru.quilikasa.playlistmaker.data

interface StorageClient<T> {

    fun addItemList(list: List<T>, key: String)
    fun getItemList(key: String): List<T>
    fun addItem(item: T, key: String)
    fun getItem(key: String, default: T): T
    fun clear(key: String)
}