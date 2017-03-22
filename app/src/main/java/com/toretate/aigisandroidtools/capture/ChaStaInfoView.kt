package com.toretate.aigisandroidtools.capture

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.ToggleButton
import com.toretate.aigisandroidtools.R

/**
 * Created by toretate on 2017/01/24.
 */
class ChaStaInfoView : LinearLayout {

    private val m_root : View
    private var m_areaSelectButton : ToggleButton? = null

    constructor(context: Context?) : super(context) { m_root = init(context) }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) { m_root = init(context) }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { m_root = init(context) }

    private fun init( context : Context? ) : View {
        var root = View.inflate( context, R.layout.cha_sta_info_view, this )

        m_areaSelectButton = root.findViewById( R.id.areaselect_button ) as ToggleButton
        return root;
    }

    public fun setAreaSelectButtonChangeListener( l : CompoundButton.OnCheckedChangeListener ) {
        m_areaSelectButton?.setOnCheckedChangeListener( l )
    }

}