package es.usj.mastertsa.fm.memoriesapp.data_layer.core

interface IDao<T>
{
    fun insert(element : T) : T
    fun update(element : T) : Long?
    fun delete(id : Long) : T?
    fun list() : List<T>
    fun findById(id : Long) : T?
}
