package com.example.shreebhagavatgita.view.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.shreebhagavatgita.Manager.NetworkManager
import com.example.shreebhagavatgita.R
import com.example.shreebhagavatgita.dataSource.API.ApiUtilities
import com.example.shreebhagavatgita.dataSource.API.Room.AppDatabase
import com.example.shreebhagavatgita.databinding.FragmentVerseBinding
import com.example.shreebhagavatgita.repository.AppRepository
import com.example.shreebhagavatgita.view.adapter.AdapterVerses
import com.example.shreebhagavatgita.viewModel.MainViewModel
import com.example.shreebhagavatgita.viewModel.MainViewModelFactory
import kotlinx.coroutines.launch


class VerseFragment : Fragment() {

    private lateinit var binding: FragmentVerseBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var repository: AppRepository
    private val adapterVerses = AdapterVerses(::onVerseItemClick)
    private var chapterNumber = 0

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVerseBinding.inflate(inflater, container, false)
        repository = AppRepository(ApiUtilities.api, AppDatabase.getDatabaseInstance(requireContext())!!.savedChaptersDao())

        viewModel = MainViewModelFactory(repository, requireActivity().application).create(MainViewModel::class.java)

        binding.idRecyclerVerse.adapter = adapterVerses
        getAndSetChapterDetails()
        getAllVerses()
        changeStatusBarColor()
        onReadMoreClicked()
        getData()

        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        return binding.root
    }

    private fun getData() {
        val bundle = arguments
        val showRoomData = bundle!!.getBoolean("showRoomData",false)

        if (showRoomData == true){
            getDataSetFromRoom()
        }else{
            checkInternetConnectivity()
        }
    }

    private fun getDataSetFromRoom() {

        viewModel.getParticularChapters(chapterNumber).observe(viewLifecycleOwner) {
            binding.tvChapterNo.text = getString(R.string.chapter, it[0].chapterNumber.toString())
            binding.tvChapterTitle.text = it[0].nameTranslated
            binding.tvChapterDescription.text = it[0].chapterSummary
            binding.tvVerseCount.text = it[0].versesCount.toString()

            showListInAdapter(it[0].verses, false)
        }

    }

    private fun onReadMoreClicked() {
        var isExpanded = false
        binding.tvReadMore.setOnClickListener{
            if (!isExpanded){
                binding.tvChapterDescription.maxLines = 50
                binding.tvReadMore.text = getString(R.string.read_less)
                isExpanded = true
            }
            else{
                binding.tvChapterDescription.maxLines = 4
                binding.tvReadMore.text = getString(R.string.read_more)
                isExpanded = false
            }
        }
    }

    private fun getAndSetChapterDetails() {
        val bundle = arguments
        chapterNumber = bundle?.getInt("chapterNumber")!!
        binding.tvChapterNo.text = getString(R.string.chapter, chapterNumber.toString())
        binding.tvChapterTitle.text = bundle.getString("nameTranslated").toString()
        binding.tvChapterDescription.text = bundle.getString("chapterSummary").toString()
        binding.tvVerseCount.text = bundle.getInt("versesCount").toString()


    }

    private fun checkInternetConnectivity() {
        val networkManager = NetworkManager(requireContext())
        networkManager.observe(viewLifecycleOwner){
            if (it == true) {
                binding.shimmer.visibility = View.VISIBLE
                binding.idRecyclerVerse.visibility = View.VISIBLE
                binding.tvShowingInternet.visibility = View.GONE
                getAllVerses()
            }
            else{
                binding.shimmer.visibility = View.GONE
                binding.idRecyclerVerse.visibility = View.GONE
                binding.tvShowingInternet.visibility = View.VISIBLE
            }
        }
    }

    private fun onVerseItemClick(verse: String, verseNumber: Int ){
        val bundle = Bundle()
        bundle.putInt("chapterNum", chapterNumber)
        bundle.putInt("verseNum", verseNumber)
        findNavController().navigate(R.id.action_verseFragment_to_verseDetailsFragment, bundle)
    }


    private fun getAllVerses() {
        lifecycleScope.launch {
            viewModel.getVerses(chapterNumber).collect {
                verses ->
                Log.e("Verses ::::", "$verses")
                val versesList = arrayListOf<String>()
                for (currentVerse in verses){
                    versesList.add(currentVerse.text)
//                    for (desc in currentVerse.translations){
//                        if (desc.language == "english")
//                        versesList.add(desc.description)
//                        break
//                    }
                    for(desc in currentVerse.commentaries){
                        if (desc.language == "english"){
                            versesList.add(desc.description)
                            break
                        }
                    }

                }

                showListInAdapter(versesList, true)

            }
        }
    }

    private fun showListInAdapter(versesList: List<String>, onClick : Boolean) {
        adapterVerses.differ.submitList(versesList)
        binding.shimmer.visibility = View.GONE
        binding.idRecyclerVerse.visibility = View.VISIBLE
        binding.tvShowingInternet.visibility = View.GONE
    }
    private fun changeStatusBarColor() {
        val window = activity?.window
        window!!.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.black)
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
        }

    }


}