package com.example.drivelicense.placelist

import android.view.View

data class Place(
    var name: String,
    var locate: String,
    var TEL: String?,
    var isPersonalPlace: Boolean,
    var isExpanded: Boolean = false
)
