package com.vikas.guhyagyan.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.vikas.guhyagyan.R
import com.vikas.guhyagyan.databinding.FragmentTasksBinding

class TasksFragment : Fragment() {

    private lateinit var binding: FragmentTasksBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTasksBinding.inflate(inflater, container, false)

        binding.audio.setOnClickListener {
            findNavController().navigate(R.id.action_tasksFragment_to_uploadRecordingFragment)
        }
        return binding.root
    }
}