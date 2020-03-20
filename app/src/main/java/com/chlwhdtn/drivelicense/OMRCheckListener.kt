package com.chlwhdtn.drivelicense

import android.view.View
import android.widget.CheckBox

class OMRCheckListener(omr: HashMap<Int, ArrayList<Int>>, id: Int, count: Int) : View.OnClickListener {

    var omr = omr
    var id = id
    var count = count

    override fun onClick(v: View?) {
        var cb: CheckBox? = null
        if(v is CheckBox) {
            if (v.isChecked) {
                var checks: ArrayList<Int>? = omr[id]
                if (checks == null)
                    checks = ArrayList()
                checks.add(count + 1)
                omr.put(id, checks)
            } else {
                var checks: ArrayList<Int> = omr[id]!!
                checks.remove(count + 1)
                if (checks.isEmpty()) {
                    omr.remove(id)
                } else {
                    omr.put(id, checks)
                }
            }
        }
    }
}