//Add new request
package com.example.assignmentdonee

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.assignmentdonee.adapter.RequestListAdapter
import com.example.assignmentdonee.model.Requests
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_addrequest.*
import kotlinx.android.synthetic.main.activity_requestmanagement.*
import java.util.HashMap

class RequestManagement : AppCompatActivity() {

    internal lateinit var progressDialog: ProgressDialog
    internal var requestsList: ArrayList<Requests> = ArrayList()
    internal lateinit var queue: RequestQueue

    internal lateinit var adapter: RequestListAdapter

    private var jsonParser: JsonParser? = null
    private var gson: Gson? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_requestmanagement)

        jsonParser = JsonParser()
        gson = Gson()

        swipeRefresh.setOnRefreshListener { carregarLista() }
        progressDialog = ProgressDialog(this@RequestManagement)
        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        queue = Volley.newRequestQueue(this)
        carregarLista()

    }


    fun carregarLista() {
        val URL: String = "http://192.168.0.117/request/readRequest.php"
        requestsList.clear()
        val stringRequests = object : StringRequest(Request.Method.POST,
            URL, Response.Listener { response ->
                try {

                    val mJson = jsonParser!!.parse(response) as JsonArray
                    requestsList = java.util.ArrayList()

                    (0 until mJson.size())
                        .map {
                            gson!!.fromJson(
                                mJson.get(it),
                                Requests::class.java
                            )
                        }
                        .forEach { requestsList.add(it) }

                    adapter = RequestListAdapter(this, requestsList)

                    veiculosListView?.adapter = adapter
                    adapter.notifyDataSetChanged()




                    veiculosListView.setOnItemClickListener { _, view, position, _ ->
                        val i = Intent(view.context, RequestDetails::class.java)
                        i.putExtra("ID", requestsList[position].requestID)
                        view.context.startActivity(i)
                    }

                    progressDialog.cancel()
                    swipeRefresh.isRefreshing = false

                } catch (e: Exception) {
                    Toast.makeText(
                        this@RequestManagement,
                        "Problem Connecting To Server.",
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
                    this@RequestManagement,
                    "Problem Connecting123 To Server!",
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




