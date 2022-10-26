package cl.flepe.payment.ui.customSpinner.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import cl.flepe.payment.R
import com.bumptech.glide.Glide

data class AttrsCustomSpinner(
    val id: String,
    val name: String,
    val icon: String? = null
)

class CustomSpinnerAdapter(
    private val context: Context,
    private val attrsCustomSpinner: List<AttrsCustomSpinner>
) : BaseAdapter() {

    override fun getCount(): Int = attrsCustomSpinner.count()

    override fun getItem(i: Int): Any = i

    override fun getItemId(i: Int): Long = i.toLong()

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup?): View {
        val rootView =
            LayoutInflater.from(context).inflate(R.layout.item_custom_spinner, viewGroup, false)

        val textViewName = rootView.findViewById<TextView>(R.id.tv_left_label)
        val imgIcon = rootView.findViewById<ImageView>(R.id.imgView_icon)

        textViewName.text = attrsCustomSpinner[i].name

        if (attrsCustomSpinner[i].icon?.isNotEmpty() == true) {
            Glide.with(context).load(attrsCustomSpinner[i].icon).into(imgIcon)
            imgIcon.isVisible = true
        } else {
            imgIcon.isGone = true
        }
        return rootView
    }
}
