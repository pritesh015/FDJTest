package com.example.fdjtest

import android.app.SearchManager
import android.database.Cursor
import android.database.MatrixCursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.cursoradapter.widget.CursorAdapter
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.fdjtest.Utils.Status
import com.example.fdjtest.Utils.hideKeyboard
import com.example.fdjtest.api.models.ResponseTeamsInLeagueDetails
import com.example.fdjtest.databinding.ActivityMainBinding
import com.example.fdjtest.ui.TeamsInLeagueFragment
import com.example.fdjtest.viewmodel.MainActivityViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel

    private lateinit var searchView: SearchView
    private var suggestions: MutableList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        viewModel.getLeagueName()
        setUpBinding()
    }

    private fun setUpBinding() {
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, TeamsInLeagueFragment())
            .commit()

        lifecycleScope.launch {
            viewModel.leagueNameState.collect {

                when (it.status) {
                    Status.LOADING -> {
                        Log.i(TAG, getString(R.string.loading))
                    }

                    Status.SUCCESS -> {
                        setSearchView(it.data)
                    }

                    Status.ERROR -> {
                        Toast.makeText(this@MainActivity, "${it.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setSearchView(data: MutableList<String>?) {
        val from = arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1)
        val to = intArrayOf(R.id.item_label)
        val cursorAdapter = SimpleCursorAdapter(this@MainActivity, R.layout.searchview_item_layout, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)
        suggestions = data

        searchView.suggestionsAdapter = cursorAdapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                hideKeyboard(searchView)
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                val cursor = MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1))
                query?.let {
                    suggestions?.forEachIndexed { index, suggestion ->
                        if (suggestion.contains(query, true))
                            cursor.addRow(arrayOf(index, suggestion))
                    }
                }

                cursorAdapter.changeCursor(cursor)
                return true
            }
        })

        searchView.setOnSuggestionListener(object: SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return false
            }

            override fun onSuggestionClick(position: Int): Boolean {
                hideKeyboard(searchView)
                val cursor = searchView.suggestionsAdapter.getItem(position) as Cursor
                val selection = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1))
                searchView.setQuery(selection, false)

                viewModel.getTeamsInLeague(selection)
                return true
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.searchview_menu, menu)
        val searchItem: MenuItem? = menu?.findItem(R.id.action_search)
        if (searchItem != null) {
            searchView = searchItem.actionView as SearchView
            searchView.queryHint = getString(R.string.leagueSearchQuery)
        }

        setSearchView(viewModel.leagueNameState.value.data)
        return super.onCreateOptionsMenu(menu)
    }
}