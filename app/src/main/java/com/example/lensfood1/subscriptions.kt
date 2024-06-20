package com.example.lensfood1

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.db.RepositoryClass
import com.example.myapplication.db.HistoryDB

class subscriptions : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var myAdapter: AdapterHistory
    private lateinit var repository: RepositoryClass
    private lateinit var dataList: MutableList<HistoryDB>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_subscriptions, container, false)

        // Initialize RecyclerView
        recyclerView = rootView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        // Initialize repository and data list
        repository = RepositoryClass(requireContext())
        dataList = mutableListOf()

        // Initialize adapter
        myAdapter = AdapterHistory(requireContext(), repository, dataList)
        recyclerView.adapter = myAdapter

        // Load data from database
        loadDataFromDatabase()

        return rootView
    }

    private fun loadDataFromDatabase() {
        // Retrieve data from the repository and update the adapter
        val historyList = repository.getAllHistory()
        dataList.clear()
        dataList.addAll(historyList)
        myAdapter.notifyDataSetChanged()
    }
}
