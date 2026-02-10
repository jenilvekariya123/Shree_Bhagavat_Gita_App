package com.example.shreebhagavatgita.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.shreebhagavatgita.Models.ChaptersItems
import com.example.shreebhagavatgita.R
import com.example.shreebhagavatgita.dataSource.API.ApiUtilities
import com.example.shreebhagavatgita.dataSource.API.Room.AppDatabase
import com.example.shreebhagavatgita.databinding.FragmentSaveChapterBinding
import com.example.shreebhagavatgita.repository.AppRepository
import com.example.shreebhagavatgita.view.adapter.AdapterChapters
import com.example.shreebhagavatgita.viewModel.MainViewModel
import com.example.shreebhagavatgita.viewModel.MainViewModelFactory


class SaveChapterFragment : Fragment() {

    private lateinit var binding : FragmentSaveChapterBinding
    private var adapter = AdapterChapters(::onChapterClick, ::onFavouriteClick, false)
    private lateinit var viewModel: MainViewModel
    private lateinit var repository: AppRepository


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSaveChapterBinding.inflate(layoutInflater)

        repository = AppRepository(ApiUtilities.api, AppDatabase.getDatabaseInstance(requireContext())!!.savedChaptersDao())
        viewModel = MainViewModelFactory(repository,requireActivity().application).create(MainViewModel::class.java)
        binding.idRecyclerSavedChapter.adapter = adapter

        getSavedChapters()
        changeStatusBarColor()
        return binding.root
    }
    private fun onChapterClick(chaptersItems: ChaptersItems){

        val bundle = Bundle()
        bundle.putInt("chapterNumber", chaptersItems.chapterNumber!!)
        bundle.putBoolean("showRoomData", true)
        findNavController().navigate(R.id.action_saveChapterFragment_to_verseFragment, bundle)
    }

    private fun onFavouriteClick(chaptersItems: ChaptersItems){

    }

    private fun getSavedChapters() {
        viewModel.getSavedChapters().observe(viewLifecycleOwner){

            val chapterList = arrayListOf<ChaptersItems>()
            for (chapter in it) {
                val chaptersItems = ChaptersItems(
                    chapterNumber = chapter.chapterNumber,
                    chapterSummaryHindi = chapter.chapterSummaryHindi,
                    slug = chapter.slug,
                    id = chapter.id,
                    nameMeaning = chapter.nameMeaning,
                    nameTransliterated = chapter.nameTransliterated,
                    chapterSummary = chapter.chapterSummary,
                    name = chapter.name,
                    nameTranslated = chapter.nameTranslated,
                    versesCount = chapter.versesCount,
                )
                chapterList.add(chaptersItems)
            }

            if (chapterList.isEmpty()){
                binding.shimmerView.visibility = View.GONE
                binding.idRecyclerSavedChapter.visibility = View.GONE
                binding.idNoSavedChapters.visibility = View.VISIBLE
            }else{
                binding.shimmerView.visibility = View.GONE
                binding.idRecyclerSavedChapter.visibility = View.VISIBLE
                binding.idNoSavedChapters.visibility = View.GONE
                adapter.differ.submitList(chapterList)
            }
        }
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