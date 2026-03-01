package com.vikas.guhyagyan.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vikas.guhyagyan.LoginSignUpActivity
import com.vikas.guhyagyan.databinding.FragmentProfileBinding
import com.vikas.guhyagyan.utils.LoginManager

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val loginManager by lazy { LoginManager(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {

        binding.logoutBtn.setOnClickListener {
            loginManager.removeSharedPreference()
            startActivity(Intent(requireActivity(), LoginSignUpActivity::class.java))
            requireActivity().finishAffinity()
        }

    }

}