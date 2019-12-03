package es.usj.mastertsa.fm.memoriesapp

import java.util.*

enum class Categories { Good, Bad, Celebration, BlackList, Love, Friends, Travel }

class Memory
{
    var id : Int = 0
    var title : String
    var category : Categories
    var date : Date
    var description : String

    lateinit var photoPath : String
    lateinit var videoPath : String
    lateinit var audioPath : String

    lateinit var location : LocationData

    constructor (title : String, category : String, date : Date = Date(), description : String)
    {
        this.title = title
        this.category = Categories.valueOf(category)
        this.date = date
        this.description = description
    }
}