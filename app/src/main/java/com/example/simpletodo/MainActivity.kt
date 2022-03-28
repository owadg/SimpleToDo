package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter : TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                // step 1: remove item from list
                listOfTasks.removeAt(position)
                // step 2: update adapter of change
                adapter.notifyDataSetChanged()
                saveItems()
            }
        }

        loadItems()

        //lookup recycler view in layoud
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        //create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val inputTextField = findViewById<EditText>(R.id.addTaskField)
        //set up the button and input field so that the user can enter a task
        findViewById<Button>(R.id.button).setOnClickListener{
            //step 1; grab the text the user has inputting into @id/addTaskField
            val userInputtedTask = inputTextField.text.toString()

            //step 2: add string to list of tasks
            listOfTasks.add(userInputtedTask)
            //notifying data adapter that data set has been updated
            adapter.notifyItemInserted(listOfTasks.size - 1)

            //extra 3: reset text field
            inputTextField.setText("")
            saveItems()
        }
    }

    //save the data the user has inputted by reading and writing a file

    //get the file we need
    fun getDataFile() : File {
        //every line is going to represent a specific task in our list of tasks
        return File(filesDir, "data.txt")
    }

    //load the items by reading every line in the data file
    fun loadItems() {
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    // save items by writing them into our data file
    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

}