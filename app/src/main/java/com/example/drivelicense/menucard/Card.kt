package com.example.drivelicense.menucard

import android.view.View

data class Card (
    var title: String,
    var color: String,
    var onClick: View.OnClickListener
)