package com.example.shreebhagavatgita.Models

import com.google.gson.annotations.SerializedName


data class ChaptersItems(

    @SerializedName("id")
    var id : Int?    = null,

    @SerializedName("name")
    var name : String? = null,

    @SerializedName("slug")
    var slug : String? = null,

    @SerializedName("name_transliterated")
    var nameTransliterated : String? = null,

    @SerializedName("name_translated" )
    var nameTranslated : String? = null,

    @SerializedName("verses_count")
    var versesCount : Int?    = null,

    @SerializedName("chapter_number")
    var chapterNumber : Int?    = null,

    @SerializedName("name_meaning")
    var nameMeaning  : String? = null,

    @SerializedName("chapter_summary")
    var chapterSummary : String? = null,

    @SerializedName("chapter_summary_hindi" )
    var chapterSummaryHindi : String? = null


)