package com.example.demoproject

import android.database.Cursor
import android.database.sqlite.SQLiteCursor
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.github.pavlospt.CircleView
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.annotation.Nullable


class SuggestionSearchActivitiy : AppCompatActivity(), SearchView.OnQueryTextListener, SearchView.OnSuggestionListener {
    private var mSuggestionAdapter: SearchSuggestionAdapter? = null
    lateinit var database: SuggestionsDatabase
    private lateinit var searchView: SearchView


    private var urlInputField: EditText? = null
    private var circularProgress: CircleView? = null
    private val REQUEST_WRITE_PERMISSION = 200
    private var storageDirectory: File? = null
    private var bitmap: Bitmap? = null
    private var timeBeforeDownload: Long = 0
    private var timeAfterDownload: Long = 0
    private var fileSizeInBytes: Long = 0
    private var fileSizeInBytes1: Long = 0
    lateinit var btn: TextView
    lateinit var btn1: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suggestion_search_activitiy)

//        title = "Download image"

        btn = findViewById<TextView>(R.id.txtView)
        btn1 = findViewById<TextView>(R.id.txtView22)

        urlInputField = findViewById(R.id.input_url) as EditText
        btn!!.text = "0 Kb/s"
        btn1!!.text = "Download speed"
        val downloadImageButton = (findViewById(R.id.download_button) as Button)

        circularProgress = findViewById(R.id.circular_display) as CircleView

        circularProgress!!.titleText = "0 Kb/s"
        circularProgress!!.subtitleText = "Download speed"

        downloadImageButton.setOnClickListener(View.OnClickListener {
            val newUrl = Helper.getUrl(urlInputField)
            val isValid = Helper.isTextNullOrEmpty(newUrl)
            if (isValid) {
                Helper.displayNoticeMessage(this@SuggestionSearchActivitiy, resources.getString(R.string.download_input_error))
                return@OnClickListener
            }
            if (urlInputField!!.text.toString().length < 15) {
                Helper.displayNoticeMessage(this@SuggestionSearchActivitiy, resources.getString(R.string.url_error))
                return@OnClickListener
            }
            timeBeforeDownload = System.currentTimeMillis()
            returnDownloadedImageAsBitmap(newUrl)
        })

        database = SuggestionsDatabase(this)
        searchView = findViewById<SearchView>(R.id.searchView1);
        this.searchView.setOnQueryTextListener(this);
        this.searchView.setOnSuggestionListener(this);






        btn.setOnClickListener {
            SearchRecentSuggestions(this, SimpleSearchSuggestionsProvider.AUTHORITY, SimpleSearchSuggestionsProvider.MODE)
                .clearHistory()
            Log.e("TAG", "onCreate: -clear")
        }

//        handleIntent(getIntent())


    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        val result: Long = database.insertSuggestion(query)
        return result != -1L
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        val cursor: Cursor = database.getSuggestions(newText!!)
        return if (cursor.count != 0) {
            val columns = arrayOf<String?>(SuggestionsDatabase.FIELD_SUGGESTION)
            val columnTextId = intArrayOf(android.R.id.text1)
            val simple = SuggestionSimpleCursorAdapter(
                baseContext,
                android.R.layout.simple_list_item_1, cursor,
                columns, columnTextId, 0
            )
            searchView.setSuggestionsAdapter(simple)
            true
        } else {
            false
        }
    }

    override fun onSuggestionSelect(p0: Int): Boolean {
        return false
    }

    override fun onSuggestionClick(p0: Int): Boolean {
        val cursor = searchView.suggestionsAdapter.getItem(p0) as SQLiteCursor
        val indexColumnSuggestion = cursor.getColumnIndex(SuggestionsDatabase.FIELD_SUGGESTION)
        searchView.setQuery(cursor.getString(indexColumnSuggestion), false)

        return true
    }

    /*override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu.findItem(R.id.action_search)
        setupSearchView(searchItem)
        return true
    }*/

    /*private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            Log.v("Log", "handleIntent(): query = $query")
            val suggestions = SearchRecentSuggestions(
                this,
                SimpleSearchSuggestionsProvider.AUTHORITY, SimpleSearchSuggestionsProvider.MODE
            )
            suggestions.saveRecentQuery(query, null)
        }

    }*/

    /*private fun setupSearchView(searchItem: MenuItem) {
        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        val searchView: SearchView = MenuItemCompat.getActionView(searchItem) as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        mSuggestionAdapter = SearchSuggestionAdapter(this, null, 0)
        searchView.setSuggestionsAdapter(mSuggestionAdapter)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val cursor = getRecentSuggestions(newText!!)
                mSuggestionAdapter!!.swapCursor(cursor)
                return false
            }
        })
        searchView.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return false
            }

            override fun onSuggestionClick(position: Int): Boolean {
                searchView.setQuery(mSuggestionAdapter!!.getSuggestionText(position), true)
                return true
            }
        })
    }*/

    /*fun getRecentSuggestions(query: String): Cursor? {
        val uriBuilder = Uri.Builder()
            .scheme(ContentResolver.SCHEME_CONTENT)
            .authority(SimpleSearchSuggestionsProvider.AUTHORITY)

        uriBuilder.appendPath(SearchManager.SUGGEST_URI_PATH_QUERY)

        val selection = " ?"
        val selArgs = arrayOf(query)

        val uri = uriBuilder.build()
        return this.contentResolver?.query(uri, null, selection, selArgs, null)
    }*/

    // this function will save video in Device Explorer path
    private fun createAndStoreFileInExternalStorage() {
        val dateFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss", Locale.ENGLISH)
        val timeStamp = dateFormat.format(Date())
        storageDirectory = File(this.filesDir.toString(), "ReviewLifeRepeat")
        if (!storageDirectory?.exists()!!) {
            storageDirectory?.mkdir();
        }
        val filename = "image-$timeStamp.mp4"
        val file = File(storageDirectory, filename)
        val file1 = File(storageDirectory, filename).path
        try {
            Log.d("TAG", "The file path = " + file.absolutePath)
            file.createNewFile()
            fileSizeInBytes = file.length().toLong()
            fileSizeInBytes1 = file1.length.toLong()
            Log.e("TAG", "createAndStoreFileInExternalStorage: --------------------------$fileSizeInBytes")

            val outStream = FileOutputStream(file)
            bitmap!!.compress(Bitmap.CompressFormat.JPEG, 80, outStream)
            outStream.flush()
            outStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun saveBitmapToExternalStorage() {
        if (!Helper.isExternalStorageWritable() && !Helper.isExternalStorageReadable()) {
            Toast.makeText(this@SuggestionSearchActivitiy, "There is no external or writable storage in your device", Toast.LENGTH_LONG).show()
            return
        }
        createAndStoreFileInExternalStorage()
    }

    private fun returnDownloadedImageAsBitmap(pathToImageDownload: String) {

        Glide.with(this)
            .asBitmap()
            .load(pathToImageDownload)
            .into(object : CustomTarget<Bitmap?>() {
                override fun onResourceReady(resource: Bitmap, @Nullable transition: Transition<in Bitmap?>?) {
                    // you can do something with loaded bitmap here
                    bitmap = resource
                    saveBitmapToExternalStorage()
                    timeAfterDownload = System.currentTimeMillis()
                    val timeDiff = ((timeAfterDownload - timeBeforeDownload) / 1000).toDouble()
                    val fileSizeInKiloByte = (fileSizeInBytes1 / 1024).toDouble()

                    val fileUploadRate = timeDiff / fileSizeInKiloByte
                    val result = getSize(fileSizeInBytes1)
                    Log.e("TAG", "onResourceReady: ------------$result")
                    circularProgress!!.titleText = result + "Kb/s"
                    circularProgress!!.subtitleText = "Download speed"
                    Toast.makeText(this@SuggestionSearchActivitiy, "${result.toString()}", Toast.LENGTH_SHORT).show()
                }

                override fun onLoadCleared(@Nullable placeholder: Drawable?) {}
            })


    }

    fun getSize(size: Long): String {
        var s = ""
        val kb = (size / 1024).toDouble()
        val mb = kb / 1024
        val gb = mb / 1024
        val tb = gb / 1024
        if (size < 1024L) {
            s = "$size Bytes"
        } else if (size >= 1024 && size < 1024L * 1024) {
            s = String.format("%.2f", kb) + " KB"
        } else if (size >= 1024L * 1024 && size < 1024L * 1024 * 1024) {
            s = String.format("%.2f", mb) + " MB"
        } else if (size >= 1024L * 1024 * 1024 && size < 1024L * 1024 * 1024 * 1024) {
            s = String.format("%.2f", gb) + " GB"
        } else if (size >= 1024L * 1024 * 1024 * 1024) {
            s = String.format("%.2f", tb) + " TB"
        }
        return s
    }


    override fun onResume() {
        super.onResume()
        storageDirectory = File(Helper.PATH_TO_EXTERNAL_STORAGE)
        if (!storageDirectory!!.exists()) {
            storageDirectory!!.mkdir()
        }
        if (null == storageDirectory!!.list() && null != bitmap) {
            createAndStoreFileInExternalStorage()
        }
    }
}




