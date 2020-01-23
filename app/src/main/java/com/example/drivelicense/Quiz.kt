package com.example.drivelicense

data class Quiz (
    var id: Int,
    var title: String,
    var answer: ArrayList<Int>,
    var question: ArrayList<String>,
    var hint: String
)