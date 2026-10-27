package ru.quilikasa.playlistmaker.data

interface StorageClient<T> {

    fun addItemList(list : List<T>, key: String)
    fun getItemList(key: String) : List<T>
    fun clear(key: String)
}