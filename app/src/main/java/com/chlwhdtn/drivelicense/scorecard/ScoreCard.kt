package com.chlwhdtn.drivelicense.scorecard

data class ScoreCard (
    var score: Int,
    var quiz_amount: Int,
    var correct_amount: Int,
    var gradient_id: Int,
    var date: String
)