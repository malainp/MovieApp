package mx.malain.movieapp.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import mx.malain.movieapp.R
import mx.malain.movieapp.models.videos.Video

class MovieVideoAdapter(context: Context) : GenericRecyclerViewAdapter<Video>(context, null) {
    override fun createView(context: Context, viewGroup: ViewGroup, viewType: Int): View {
        return LayoutInflater.from(context).inflate(viewType, viewGroup, false)
    }

    override fun bindView(item: Video?, position: Int, viewHolder: ViewHolder) {
        item?.let { video ->
            val videoName = viewHolder.getView<TextView>(R.id.video_name)
            val name = context.getString(R.string.video_name, video.name)
            videoName.text = name
        }
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.movie_video_layout
    }
}