package com.example.assignmentdonee

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.assignmentdonee.adapter.RequestListAdapter
import com.example.assignmentdonee.model.Requests
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import java.util.HashMap

class MainActivity : AppCompatActivity() {

    internal lateinit var progressDialog: ProgressDialog
    internal var requestsList: ArrayList<com.example.assignmentdonee.model.Requests> = ArrayList()
    internal lateinit var queue: RequestQueue

    internal lateinit var adapter: RequestListAdapter

    private var jsonParser: JsonParser? = null
    private var gson: Gson? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        jsonParser = JsonParser()
        gson = Gson()

        swipeRefresh.setOnRefreshListener { carregarLista() }
        progressDialog = ProgressDialog(this@MainActivity)
        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        queue = Volley.newRequestQueue(this)
        carregarLista()

    }

    fun carregarLista() {
        val URL: String = "http://192.168.0.103/request/readRequest.php"
        requestsList.clear()
        val stringRequests = object : StringRequest(Requests.Method.POST,
            URL, Response.Listener { response ->
                try {

                    val mJson = jsonParser!!.parse(response) as JsonArray
                    requestsList = java.util.ArrayList()

                    (0 until mJson.size())
                        .map {
                            gson!!.fromJson(
                                mJson.get(it),
                                com.example.assignmentdonee.model.Requests::class.java
                            )
                        }
                        .forEach { requestsList.add(it) }

                    adapter = RequestListAdapter(this, requestsList)

                    veiculosListView?.adapter = adapter
                    adapter.notifyDataSetChanged()




                    veiculosListView.setOnItemClickListener { _, view, position, _ ->
                        val i = Intent(view.context, RequestDetails::class.java)
                        i.putExtra("ID", requestsList[position].ID)
                        view.context.startActivity(i)
                    }

                    progressDialog.cancel()
                    swipeRefresh.isRefreshing = false

                } catch (e: Exception) {
                    Toast.makeText(
                        this@MainActivity,
                        "Problemas na comuncação com o servidor.",
                        Toast.LENGTH_SHORT
                    ).show()
                    e.printStackTrace()
                    progressDialog.cancel()
                    swipeRefresh.isRefreshing = false
                }
            }, Response.ErrorListener {
                progressDialog.cancel()
                swipeRefresh.isRefreshing = false
                Toast.makeText(
                    this@MainActivity,
                    "Problema na comunicação com o servidor!",
                    Toast.LENGTH_LONG
                ).show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                params["PATH"] = "getRequest"

                return params
            }
        }
        queue.add(stringRequests)
    }

        fun requestBtn(view: View?) {
            val intent = Intent(this, AddRequest::class.java)
            startActivity(intent)
            finish()

        }
    }

}

