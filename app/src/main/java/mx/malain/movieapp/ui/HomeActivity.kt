package mx.malain.movieapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.core.view.get
import androidx.viewpager.widget.ViewPager
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_home.*
import mx.malain.movieapp.R
import mx.malain.movieapp.utils.FragmentAdapter

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    var lastFragment = 0
    lateinit var fragmentAdapter: FragmentAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        fragmentAdapter = FragmentAdapter(supportFragmentManager)
        viewpager.adapter = fragmentAdapter
        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                bottom_navigation.menu.getItem(position).isCheckable = true
                bottom_navigation.menu.getItem(position).isChecked = true
            }
        })

        bottom_navigation.setOnItemSelectedListener {
            viewpager.currentItem = it.order
            true
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        val searchViewItem = menu?.findItem(R.id.app_bar_search)
        val searchView: SearchView = MenuItemCompat.getActionView(searchViewItem) as SearchView
        searchView.setOnSearchClickListener {
            lastFragment = viewpager.currentItem
            viewpager.currentItem = 2
        }
        searchView.setOnCloseListener {
            viewpager.currentItem = lastFragment
            false
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    fragmentAdapter.searchListener?.onQueryUpdated(it)
                }
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }
}