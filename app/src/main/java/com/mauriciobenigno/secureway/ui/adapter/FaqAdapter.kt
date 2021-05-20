package com.mauriciobenigno.secureway.ui.adapter
import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.mauriciobenigno.secureway.R
import org.jetbrains.anko.layoutInflater
import java.util.*


class FaqAdapter  internal constructor(
    private val context: Context,
    private val _listDataHeader: List<String>,
    private val _listDataChild: HashMap<String, List<String>>
) : BaseExpandableListAdapter(){

    override fun getChild(groupPosition: Int, childPosititon: Int): String? {
        return _listDataChild[_listDataHeader[groupPosition]]?.get(childPosititon)
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getChildView(
        groupPosition: Int, childPosition: Int,
        isLastChild: Boolean, convertView: View?, parent: ViewGroup
    ): View {
        var convertViewChild = convertView
        val childText = getChild(groupPosition, childPosition) as String
        if (convertViewChild == null) {
            convertViewChild = this.context.layoutInflater.inflate(R.layout.faq_item, null)
        }
        val txtListChild = convertViewChild?.findViewById<View>(R.id.tvDescricaoFaq) as TextView
        txtListChild.text = childText
        return convertViewChild!!
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return _listDataChild[_listDataHeader[groupPosition]]!!.size
    }

    override fun getGroup(groupPosition: Int): Any {
        return _listDataHeader[groupPosition]
    }

    override fun getGroupCount(): Int {
        return _listDataHeader.size
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, p3: ViewGroup?): View {
        val headerTitle = getGroup(groupPosition) as String
        var convertViewGroup = convertView
        if (convertViewGroup == null) {
            convertViewGroup = this.context.layoutInflater.inflate(R.layout.faq_group, null)
        }

        val lblListHeader = convertViewGroup?.findViewById<View>(R.id.lblListHeader) as TextView
        lblListHeader.setTypeface(null, Typeface.BOLD)
        lblListHeader.text = headerTitle
        return convertViewGroup!!
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}