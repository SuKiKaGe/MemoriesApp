package es.usj.mastertsa.fm.memoriesapp

import java.util.*

enum class Categories {
    Good,
    Bad,
    Birthday,
    DarkList,
    Love,
    Friends,
    Travel
}

class Memorie {

    var id: Int = 0
    var title: String = ""
    lateinit var categorie: Categories
    lateinit var date: Date
}