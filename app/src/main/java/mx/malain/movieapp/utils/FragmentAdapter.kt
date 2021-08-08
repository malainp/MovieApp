package mx.malain.movieapp.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import mx.malain.movieapp.ui.HomeActivity
import mx.malain.movieapp.ui.MoviesFragment
import mx.malain.movieapp.ui.SearchFragment

class FragmentAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager) {

    interface SearchLister {
        fun onQueryUpdated(query: String)
    }

    var searchListener: SearchLister? = null

    private val fragments = ArrayList<Fragment>()

    init {
        fragments.add(MoviesFragment.newInstance(MoviesFragment.METHOD_POPULAR))
        fragments.add(MoviesFragment.newInstance(MoviesFragment.METHOD_TOP_RATED))
        searchListener = SearchFragment.newInstance()
        fragments.add(searchListener as SearchFragment)
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }
}