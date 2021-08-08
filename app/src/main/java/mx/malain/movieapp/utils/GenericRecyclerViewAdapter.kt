package mx.malain.movieapp.utils

import android.content.Context
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class GenericRecyclerViewAdapter<T>(val context: Context,
                                             private var clickListener: OnViewHolderClickListener<T>?) : RecyclerView.Adapter<GenericRecyclerViewAdapter<T>.ViewHolder>() {

    interface OnViewHolderClickListener<T> {
        fun onClick(view: View, position: Int, item: T?)
    }

    interface OnViewHolderLongClickListener<T> {
        fun onLongClick(view: View, position: Int, item: T?)
    }

    private var onBind: Boolean = false
    private var items: MutableList<T>

    var longClickListener: OnViewHolderLongClickListener<T>? = null

    fun setClickListener(listener: OnViewHolderClickListener<T>) {
        this.clickListener = listener
    }

    var list: MutableList<T>
        get() = items
        set(value) {
            items = value
            notifyDataSetChanged()
        }

    init {
        items = ArrayList()
    }

    protected abstract fun createView(context: Context, viewGroup: ViewGroup, viewType: Int): View

    protected abstract fun bindView(item: T?, position: Int, viewHolder: ViewHolder)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(createView(context, parent, viewType), clickListener, longClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        bindView(getItem(position), position, holder)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    /**
     * Returns the item on the given position. If the index is negative or greater than the
     * adapter list size null is returned.
     *
     * @param index -> The item index.
     *
     * @return The item if the item index is contained within 0 to list size, else null.
     */
    fun getItem(index: Int): T? {
        return if (index >= 0 && index < items.size) items[index] else null
    }

    /**
     * Adds all the elements in the given list at the end of the adapter list
     * and notifies the data set changed.
     *
     * @param list -> The list of new elements to be added
     */
    fun addAll(list: List<T>) {
        items.addAll(list)
        notifyItemRangeInserted(items.size -list.size, list.size)
    }

    /**
     * Adds the item T at the given position. If the adapter list is empty the item is added
     * at first position (zero). Then it notifies the data set an item was inserted at given
     * position.
     *
     * @param position -> The list position where the item should be inserted at.
     * @param item -> The item to be inserted.
     */
    fun addAt(position: Int, item: T) {
        if (items.isEmpty()) {
            items.add(item)
        } else {
            items.add(position, item)
        }
        notifyItemInserted(position)
    }

    /**
     * Removes the item in the given position and then it notifies with item removed on the
     * data set.
     *
     * @param position -> The position of the element that should be removed.
     */
    fun removeAtPosition(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    /**
     * Clears the adapter list and adds all the content of the given list and notifies the data
     * set the whole list changed.
     *
     * @param list -> The new list to display.
     */
    fun setNewContent(list: List<T>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    /**
     * Clears the adapter list and notifies the data set the content changed.
     */
    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        view: View,
        clickListener: OnViewHolderClickListener<T>?,
        longClickListener: OnViewHolderLongClickListener<T>?
    ) : RecyclerView.ViewHolder(view), View.OnClickListener, View.OnLongClickListener {

        private val views: SparseArray<View> = SparseArray()

        val view: View?
            get() = getView(0)

        init {
            views.append(0, view)
            if (clickListener != null)
                view.setOnClickListener(this)
            if (longClickListener != null)
                view.setOnLongClickListener(this)
        }

        override fun onClick(v: View) {
            clickListener?.onClick(v, adapterPosition, getItem(adapterPosition))
        }

        override fun onLongClick(v: View): Boolean {
            longClickListener?.onLongClick(v, adapterPosition, getItem(adapterPosition))
            return true
        }

        fun initViewList(idList: IntArray) {
            for (id in idList)
                initViewById(id)
        }

        private fun initViewById(id: Int) {
            val view = view?.findViewById<View>(id)
            if (view != null)
                views.append(id, view)
        }

        fun <V : View> getView(id: Int): V {
            if (views.get(id) == null)
                initViewById(id)
            return views.get(id) as V
        }
    }
}