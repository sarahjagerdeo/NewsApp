package com.example.redoproject1

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
//import kotlinx.android.synthetic.main.activity_source_selection.*
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SourceSelectionActivity : AppCompatActivity() {

    private lateinit var searchTermTextView: TextView
    private lateinit var categorySpinner: Spinner
    private lateinit var skipButton: Button
    private lateinit var sourcesRecyclerView: RecyclerView
    private lateinit var sourcesAdapter: SourcesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_source_selection)

        searchTermTextView = findViewById(R.id.searchTermTextView)
        categorySpinner = findViewById(R.id.categorySpinner)
        skipButton = findViewById(R.id.skipButton)
        sourcesRecyclerView = findViewById(R.id.sourcesRecyclerView)

        val searchTerm = intent.getStringExtra("searchTerm")
        searchTermTextView.text = "Search for: '$searchTerm'"

        setupSpinner()
        setupRecyclerView()

        // Skip button click listener
        skipButton.setOnClickListener {
            // Proceed to next screen without filtering by source
            Toast.makeText(this, "Skipping source selection", Toast.LENGTH_SHORT).show()
            // Navigate to next screen
        }
    }

    private fun setupSpinner() {
        val categories = arrayOf("business", "entertainment", "general", "health", "science", "sports", "technology")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
        categorySpinner.adapter = adapter

        // Spinner item selection listener
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val category = categories[position]
                loadSources(category)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }

    private fun setupRecyclerView() {
        sourcesAdapter = SourcesAdapter()
        sourcesRecyclerView.apply {
            adapter = sourcesAdapter
            layoutManager = LinearLayoutManager(this@SourceSelectionActivity)
        }
    }

    private fun loadSources(category: String) {
        Thread {
            try {
                val apiKey = "ef2730bd1f0748189ebe227074e238b3"
                val url = URL("https://newsapi.org/v2/top-headlines/sources?apiKey=ef2730bd1f0748189ebe227074e238b3")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 8000
                connection.readTimeout = 8000

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val response = StringBuilder()
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        response.append(line)
                    }
                    reader.close()

                    val jsonResponse = JSONArray(response.toString())
                    val sources = mutableListOf<NewsSource>()
                    for (i in 0 until jsonResponse.length()) {
                        val sourceObj = jsonResponse.getJSONObject(i)
                        val id = sourceObj.getString("id")
                        val name = sourceObj.getString("name")
                        val description = sourceObj.getString("description")
                        sources.add(NewsSource(id, name, description))
                    }

                    runOnUiThread {
                        sourcesAdapter.submitList(sources)
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@SourceSelectionActivity, "Failed to load sources", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@SourceSelectionActivity, "Failed to load sources: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }
}
