package com.example.assignmentdonee

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast

import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

import kotlinx.android.synthetic.main.donee_request_form.*

import java.util.HashMap


class AddRequest : AppCompatActivity() {

    internal lateinit var progressDialog: ProgressDialog
    internal lateinit var queue: RequestQueue
    internal var bundle: Bundle? = null

    internal var url: String? = null
    internal lateinit var ID: String
    internal var editar = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.donee_request_form)

        queue = Volley.newRequestQueue(this)



        btnAdd.setOnClickListener {
            if (testForm()) {

                progressDialog = ProgressDialog(this@AddRequest)
                progressDialog.setMessage("Loading...")
                progressDialog.setCancelable(false)
                progressDialog.show()

                url = "http://192.168.0.117/request/addRequest.php"
                if (editar) {
                    url = "http://192.168.0.117/request/updateRequest.php"
                }

                val stringRequest = object : StringRequest(Method.POST,
                    url, Response.Listener { response ->

                        try {
                            progressDialog.cancel()
                            Toast.makeText(this@AddRequest, response, Toast.LENGTH_LONG).show()
                            val i = Intent(this@AddRequest, MainActivity::class.java)
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(i)

                        } catch (e: Exception) {
                            Toast.makeText(
                                this@AddRequest,
                                "Problem connecting to server.",
                                Toast.LENGTH_SHORT
                            ).show()
                            e.printStackTrace()
                            progressDialog.cancel()
                        }
                    }, Response.ErrorListener {
                        progressDialog.cancel()

                        Toast.makeText(
                            this@AddRequest,
                            "Problem connecting to server.",
                            Toast.LENGTH_LONG
                        ).show()
                    }) {
                    @Throws(AuthFailureError::class)
                    override fun getHeaders(): Map<String, String> {
                        val params = HashMap<String, String>()
                        if (editar) {
                            params["PATH"] = "updateRequest"
                        } else {
                            params["PATH"] = "addRequest"
                        }
                        params["requestID"] = editTextID.text.toString().trim { it <= ' ' }
                        params["requestFirstName"] = editTextFirstName.text.toString().trim { it <= ' ' }
                        params["requestLastName"] = editTextLastName.text.toString().trim { it <= ' ' }
                        params["requestAddress"] = editTextAddress.text.toString().trim { it <= ' ' }
                        params["requestZip"] = editTextZip.text.toString().trim { it <= ' ' }
                        params["requestCity"] = editTextCity.text.toString().trim { it <= ' ' }
                        params["requestState"] = editTextState.text.toString().trim { it <= ' ' }
                        params["requestPhoneNumber"] = editTextPhoneNumber.text.toString().trim { it <= ' ' }
                        params["requestReason"] = editTextReason.text.toString().trim { it <= ' ' }

                        if (editar) {
                            params["ID"] = ID

                        }

                        return params
                    }
                }

                queue.add(stringRequest)
            }
        }

        try {
            bundle = intent.extras
            if (bundle!!.getString("editar")!!.equals("editar", ignoreCase = true)) {

                editar = true

                editTextID.setText(
                    bundle!!.getString("requestID"),
                    TextView.BufferType.EDITABLE
                )

                editTextFirstName.setText(
                    bundle!!.getString("requestFirstName"),
                    TextView.BufferType.EDITABLE
                )
                editTextLastName.setText(
                    bundle!!.getString("requestLastName"),
                    TextView.BufferType.EDITABLE
                )
                editTextAddress.setText(
                    bundle!!.getString("requestAddress"),
                    TextView.BufferType.EDITABLE
                )
                editTextZip.setText(
                    bundle!!.getString("requestZip"),
                    TextView.BufferType.EDITABLE
                )
                editTextCity.setText(
                    bundle!!.getString("requestCity"),
                    TextView.BufferType.EDITABLE
                )
                editTextState.setText(
                    bundle!!.getString("requestState"),
                    TextView.BufferType.EDITABLE
                )
                editTextPhoneNumber.setText(
                    bundle!!.getString("requestPhoneNumber"),
                    TextView.BufferType.EDITABLE
                )
                editTextReason.setText(
                    bundle!!.getString("requestReason"),
                    TextView.BufferType.EDITABLE
                )


                ID = bundle!!.getString("ID").toString()


            }

        } catch (e: Exception) {
        }

    }

    private fun testForm(): Boolean {
        if (editTextID.text.toString().trim { it <= ' ' }.equals("", ignoreCase = true)
            || editTextFirstName.text.toString().trim { it <= ' ' }.equals("", ignoreCase = true)
            || editTextLastName.text.toString().trim { it <= ' ' }.equals("", ignoreCase = true)
            || editTextAddress.text.toString().trim { it <= ' ' }.equals("", ignoreCase = true)
            || editTextZip.text.toString().trim { it <= ' ' }.equals("", ignoreCase = true)
            || editTextCity.text.toString().trim { it <= ' ' }.equals("", ignoreCase = true)
            || editTextState.text.toString().trim { it <= ' ' }.equals("", ignoreCase = true)
            || editTextPhoneNumber.text.toString().trim { it <= ' ' }.equals("", ignoreCase = true)
            || editTextReason.text.toString().trim { it <= ' ' }.equals("", ignoreCase = true)
        ) {
            Toast.makeText(this, "Fill the entire form.", Toast.LENGTH_SHORT).show()
            return false
        }
        return true


    }
}