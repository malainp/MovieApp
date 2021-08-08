package mx.malain.movieapp.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import mx.malain.movieapp.R
import mx.malain.movieapp.extensions.isConnectionAvailable
import mx.malain.movieapp.models.movie_detail.ProductionCompany

class ProductionCompaniesAdapter(context: Context): GenericRecyclerViewAdapter<ProductionCompany>(context, null) {
    override fun createView(context: Context, viewGroup: ViewGroup, viewType: Int): View {
        return LayoutInflater.from(context).inflate(viewType, viewGroup, false)
    }

    override fun bindView(item: ProductionCompany?, position: Int, viewHolder: ViewHolder) {
        item?.let { company ->
            val companyLogo = viewHolder.getView<ImageView>(R.id.company_logo)
            val companyName = viewHolder.getView<TextView>(R.id.company_name)

            with(company){
                if (context.isConnectionAvailable()) {
                    logoPath?.let {
                        val url = context.getString(R.string.url_movie_image, it)
                        Glide.with(context)
                            .load(url/*"https://image.tmdb.org/t/p/original$it"*/)
                            .into(companyLogo)
                    }
                }
                if (logoPath.isNullOrEmpty() || !context.isConnectionAvailable()){
                    companyName.visibility = View.VISIBLE
                    companyLogo.setImageDrawable(null)
                } else {
                    companyName.visibility = View.GONE
                }
                companyName.text = name
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.production_company_layout
    }
}