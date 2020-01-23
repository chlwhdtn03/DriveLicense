package com.example.drivelicense

import android.graphics.Color
import android.view.View

data class Card (
    var title: String,
    var color: String,
    var onClick: View.OnClickListener
)