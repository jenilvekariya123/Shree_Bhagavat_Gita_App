package com.example.shreebhagavatgita.view.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.shreebhagavatgita.Manager.NetworkManager
import com.example.shreebhagavatgita.Models.ChaptersItems
import com.example.shreebhagavatgita.R
import com.example.shreebhagavatgita.dataSource.API.ApiUtilities
import com.example.shreebhagavatgita.dataSource.API.Room.AppDatabase
import com.example.shreebhagavatgita.dataSource.API.Room.SavedChapters
import com.example.shreebhagavatgita.databinding.FragmentHomeBinding
import com.example.shreebhagavatgita.repository.AppRepository
import com.example.shreebhagavatgita.view.adapter.AdapterChapters
import com.example.shreebhagavatgita.viewModel.MainViewModel
import com.example.shreebhagavatgita.viewModel.MainViewModelFactory
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var repository: AppRepository
    private var adapterChapters = AdapterChapters(::onChapterClick, ::onFavouriteClick, true)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        repository = AppRepository(ApiUtilities.api, AppDatabase.getDatabaseInstance(requireContext())!!.savedChaptersDao())

        viewModel = MainViewModelFactory(repository, requireActivity().application).create(MainViewModel::class.java)

        binding.idRecyclerChapter.adapter = adapterChapters
        changeStatusBarColor()
        getAllChapters()
        checkInternetConnectivity()

        binding.ivSetting.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_settingFragment)
        }

        return binding.root
    }


    private fun checkInternetConnectivity() {
        val networkManager = NetworkManager(requireContext())
        networkManager.observe(viewLifecycleOwner){
            if (it == true) {
                binding.shimmerView.visibility = View.VISIBLE
                binding.idRecyclerChapter.visibility = View.VISIBLE
                binding.ivNoInternet.visibility = View.GONE
                binding.tvNoInternet.visibility = View.GONE
                getAllChapters()
            }
            else{
                binding.shimmerView.visibility = View.GONE
                binding.idRecyclerChapter.visibility = View.GONE
                binding.ivNoInternet.visibility = View.VISIBLE
                binding.tvNoInternet.visibility = View.VISIBLE
            }
        }
    }

    private fun onFavouriteClick(chaptersItems: ChaptersItems){

        lifecycleScope.launch {
            viewModel.getVerses(chaptersItems.chapterNumber!!).collect {
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
                }

                val savedChapters = SavedChapters(
                    chapterNumber = chaptersItems.chapterNumber,
                    nameTranslated = chaptersItems.nameTranslated,
                    chapterSummary = chaptersItems.chapterSummary,
                    versesCount = chaptersItems.versesCount,
                    id = chaptersItems.id,
                    name = chaptersItems.name,
                    slug = chaptersItems.slug,
                    nameTransliterated = chaptersItems.nameTransliterated,
                    nameMeaning = chaptersItems.nameMeaning,
                    chapterSummaryHindi = chaptersItems.chapterSummaryHindi,
                    verses = versesList
                )

                viewModel.insertChapters(savedChapters)
            }
        }
    }

    private fun getAllChapters() {
        lifecycleScope.launch {
            viewModel.getAllChapters().collect{
                chapters ->
                Log.e('a'.toString(), "$chapters")
                adapterChapters.differ.submitList(chapters)
                binding.shimmerView.visibility = View.GONE
            }
        }

    }

    private fun onChapterClick(chaptersItems: ChaptersItems){
        val bundle = Bundle()
        bundle.putInt("chapterNumber", chaptersItems.chapterNumber!!)
        bundle.putString("nameTranslated", chaptersItems.nameTranslated!!)
        bundle.putString("chapterSummary", chaptersItems.chapterSummary!!)
        bundle.putInt("versesCount", chaptersItems.versesCount!!)

        findNavController().navigate(R.id.action_homeFragment_to_verseFragment, bundle)
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