package com.chlwhdtn.drivelicense.placelist

data class Place(
    var name: String,
    var locate: String,
    var TEL: String?,
    var isPersonalPlace: Boolean,
    var isExpanded: Boolean = false
)
