package com.example.unieats.ui.registration

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.unieats.MainActivity
import com.example.unieats.R
import com.example.unieats.databinding.FragmentNameBinding
import com.example.unieats.databinding.FragmentUserBinding


class UserFragment : Fragment() {

    private val hint = "dwyncock6969"

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentUserBinding>(inflater,
            R.layout.fragment_user, container, false)

        val imgr =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        if (MainActivity.username == ""){
            binding.nameInput.hint = hint
        }
        else {
            binding.nameInput.setText(MainActivity.username)
        }

        binding.imageButton3.setOnClickListener{
            view?.findNavController()?.navigate(R.id.action_userFragment_to_lastNameFragment)
        }

        binding.imageButton4.setOnClickListener{
            view?.findNavController()?.navigate(R.id.action_userFragment_to_passFragment)
            MainActivity.username = binding.nameInput.text.toString()
            Log.e("a", MainActivity.username)
        }

        /*binding.nameInput.requestFocus()*/

        return binding.root
    }

    companion object {

    }
}