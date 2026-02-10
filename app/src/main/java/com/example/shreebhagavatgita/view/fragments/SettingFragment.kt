package com.example.shreebhagavatgita.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.shreebhagavatgita.R
import com.example.shreebhagavatgita.databinding.FragmentSettingBinding


class SettingFragment : Fragment() {

    private lateinit var binding : FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(layoutInflater)

        binding.apply {
            idSavedChapters.setOnClickListener { findNavController().navigate(R.id.action_settingFragment_to_saveChapterFragment) }

            idSavedVerses.setOnClickListener { findNavController().navigate(R.id.action_settingFragment_to_saveVersaFragment) }
        }
        return binding.root
    }


}