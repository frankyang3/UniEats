package com.example.unieats.ui.search

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.unieats.MainActivity
import com.example.unieats.R
import com.example.unieats.models.History
import com.example.unieats.models.User
import com.example.unieats.ui.search.SearchFragment.Companion.clickedFood
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.food_fragment.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class FoodFragment : Fragment() {

    companion object {
        fun newInstance() = FoodFragment()
    }

    private lateinit var viewModel: FoodViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.food_fragment, container, false)
        var ref = FirebaseDatabase.getInstance().getReference("Users/"+"${MainActivity.selectedUser.id}"+"/history")
        //var readRef = FirebaseDatabase.getInstance().getReference("Users/"+"${MainActivity.selectedUser.id}"+"/history")
        var rootRef = FirebaseDatabase.getInstance().getReference()


        var title = root.findViewById<TextView>(R.id.foodTitle)
        var cals = root.findViewById<TextView>(R.id.foodCals)
        var img = root.findViewById<ImageView>(R.id.foodImage)

        var total = root.findViewById<TextView>(R.id.todayTotal)
        var totalCnt = 0

        title.text = clickedFood.name
        cals.text = clickedFood.calories.toString()
        img.setImageBitmap(toImage(clickedFood.image))



        root.foodButton.setOnClickListener { view: View ->
            val current = LocalDateTime.now()

            val formatter = DateTimeFormatter.BASIC_ISO_DATE
            val formatted = current.format(formatter)
            Log.e(formatted, formatted)
            ref.push().setValue(History(formatted.toInt(),clickedFood.id)).addOnCompleteListener{
                Toast.makeText(requireContext(), "Food logged successfully", Toast.LENGTH_SHORT).show()
            }
        }

        root.removeButton.setOnClickListener { view: View ->
            val current = LocalDateTime.now()

            val formatter = DateTimeFormatter.BASIC_ISO_DATE
            val formatted = current.format(formatter)
            Log.e(formatted, formatted)
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (childSnapshot in dataSnapshot.children) {
                        if(childSnapshot.child("foodId").getValue(String::class.java) == clickedFood.id){
                            childSnapshot.ref.removeValue()
                            Toast.makeText(requireContext(), "Food removed successfully", Toast.LENGTH_SHORT).show()
                            ref.removeEventListener(this);
                            break;
                        }
                        Toast.makeText(requireContext(), "No food history found", Toast.LENGTH_SHORT).show()
                    }
                    ref.removeEventListener(this);
                }
                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    //Toast.makeText(this@SearchFragment, "error error", Toast.LENGTH_LONG).show()
                }
            })




        }

        //reads onchange -> for text
        rootRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.e("ASDF", "AFAFAFF")
                totalCnt = 0
                MainActivity.selectedUser = dataSnapshot.child("Users/" + "${MainActivity.selectedUser.id}" ).getValue(User::class.java)!!
                for (childSnapshot in dataSnapshot.child("Users/" + "${MainActivity.selectedUser.id}" + "/history").children) {
                    try {
                        Log.e("ASDF", "poops")
                        val id = childSnapshot.child("foodId").getValue(String::class.java)!!
                        Log.e("poopsajf", id)
                        totalCnt += dataSnapshot.child("Food").child(id).child("calories").getValue(Int::class.java)!!
                    }catch(e: Exception){
                        Log.e("ASDF", "error")
                        totalCnt = 0
                    }
                    //val addition = childSnapshot.child("cals").getValue(Long::class.java)!!
                   // totalCnt += addition.toInt()
                    Log.e("ASDF", "totcnt here")
                    total.text = "Today's Total: " + totalCnt
                }
            }


            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                //Toast.makeText(this@SearchFragment, "error error", Toast.LENGTH_LONG).show()
            }
        })


        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FoodViewModel::class.java)
        // TODO: Use the ViewModel
    }

    fun toImage(inString: String?) : Bitmap?{
        val encodedImage: String;
        return if(inString != null) {
            encodedImage = inString!!.replace("data:image/jpeg;base64,","")
            val decodedString: ByteArray = Base64.decode(encodedImage, Base64.DEFAULT)
            val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

            decodedByte
        }else{
            null
        }
    }

}