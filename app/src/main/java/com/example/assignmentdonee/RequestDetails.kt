//will show request list then able to delete and edit it
package com.example.assignmentdonee

import android.app.ProgressDialog

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

import android.widget.Toast

import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.assignmentdonee.model.Requests
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_requestdescription.*


import java.util.HashMap



class RequestDetails:AppCompatActivity() {
    internal lateinit var progressDialog:ProgressDialog
    private var jsonParser:JsonParser = JsonParser()
    private var gson:Gson = Gson()
    internal lateinit var queue:RequestQueue
    internal var bundle:Bundle? = null
    internal lateinit var requests: Requests

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_requestdescription)

        bundle=intent.extras
        queue = Volley.newRequestQueue(this)

        progressDialog = ProgressDialog(this@RequestDetails)
        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        btnvoltar.setOnClickListener { onBackPressed() }

        btneditar.setOnClickListener { v ->
            val i = Intent(v.context, AddRequest::class.java)
            i.putExtra("editar", "editar")
            i.putExtra("requestID", requests.requestID)
            i.putExtra("requestFirstName", requests.requestFirstName)
            i.putExtra("requestLastName", requests.requestLastName)
            i.putExtra("requestAddress", requests.requestAddress)
            i.putExtra("requestZip", requests.requestZip)
            i.putExtra("requestCity", requests.requestCity)
            i.putExtra("requestState", requests.requestState)
            i.putExtra("requestPhoneNumber", requests.requestPhoneNumber)
            i.putExtra("requestReason", requests.requestReason)
            i.putExtra("ID", requests.ID)

            v.context.startActivity(i)
        }
        val URL2: String="http://192.168.0.117/request/readRequestDetails.php"

        val URL1: String="http://192.168.0.117/request/deleteRequest.php"
        btnremover.setOnClickListener {
            val lista = arrayOfNulls<String>(2)
            lista[0] = "Yes"
            lista[1] = "No"
            val builder = AlertDialog.Builder(this@RequestDetails)
            builder.setTitle("Are You Sure?")
            builder.setItems(lista) { _, which ->
                if (which == 0) {

                    val stringRequest = object:StringRequest(Request.Method.POST,
                        URL1, Response.Listener<String> { response ->
                            try {
                                progressDialog.cancel()
                                Toast.makeText(this@RequestDetails, response, Toast.LENGTH_LONG).show()
                                val i = Intent(this@RequestDetails, MainActivity::class.java)
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                startActivity(i)
                            } catch (e:Exception) {
                                Toast.makeText(this@RequestDetails, "Problems communicating with the server.", Toast.LENGTH_SHORT).show()
                                e.printStackTrace()
                                progressDialog.cancel()
                            }
                        }, Response.ErrorListener {
                            progressDialog.cancel()
                            Toast.makeText(this@RequestDetails ,
                                "Problems communicating with the server.",
                                Toast.LENGTH_LONG).show()
                        }) {
                        @Throws(AuthFailureError::class)
                        override fun getHeaders():Map<String, String> {
                            val params = HashMap<String, String>()
                            params["PATH"] = "deleteRequest"
                            params["ID"] = bundle!!.getString("ID")!!
                            return params
                        }
                    }
                    queue.add<String>(stringRequest)
                }
            }
            val alertDialog = builder.create()
            alertDialog.show()
        }

        val stringRequest = object:StringRequest(Request.Method.POST,
            URL2, Response.Listener<String> { response ->
                try {
                    val mJson = jsonParser.parse(response) as JsonArray
                    requests = gson.fromJson<Requests>(mJson.get(0), Requests::class.java)

                    txtmodelo.text = requests.requestID
                    txtcor.text = requests.requestFirstName
                    txtano.text = requests.requestLastName




                    progressDialog.cancel()
                } catch (e:Exception) {
                    Toast.makeText(this@RequestDetails, "Error.", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                    progressDialog.cancel()
                }
            }, Response.ErrorListener {




                progressDialog.cancel()
                Toast.makeText(this@RequestDetails,
                    "Problem Communicating to server333",
                    Toast.LENGTH_LONG).show()
            }) {

            @Throws(AuthFailureError::class)
            override fun getHeaders():Map<String, String> {

                val params = HashMap<String, String>()
                params["PATH"] = "getRequestDetails"
                params["ID"] = bundle!!.getString("ID")!!
                return params
            }

        }
        queue.add<String>(stringRequest)

    }
}
