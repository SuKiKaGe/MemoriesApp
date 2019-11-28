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

class Memory {

    var id: Int = 0
    lateinit var title: String
    lateinit var categorie: Categories
    lateinit var date: Date
    lateinit var description: String

    lateinit var photoPath: String
    lateinit var videoPath: String
    lateinit var audioPath: String

    lateinit var ubication: UbicationData
}