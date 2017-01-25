package com.toretate.aigisandroidtools.capture

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ToggleButton
import com.toretate.aigisandroidtools.R

/**
 * Created by toretate on 2017/01/24.
 */
class ChaStaInfoView : LinearLayout {
    constructor(context: Context?) : super(context) { init(context) }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) { init(context) }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { init(context) }

    private fun init( context : Context? ) {
        var root = View.inflate( context, R.layout.cha_sta_info_view, this )

        var areaSelectButton = root.findViewById( R.id.areaselect_button ) is ToggleButton


    }

}