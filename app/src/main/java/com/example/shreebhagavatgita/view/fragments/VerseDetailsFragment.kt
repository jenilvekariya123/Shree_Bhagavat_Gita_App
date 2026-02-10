package com.example.shreebhagavatgita.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.shreebhagavatgita.Manager.NetworkManager
import com.example.shreebhagavatgita.Models.Commentary
import com.example.shreebhagavatgita.Models.Translation
import com.example.shreebhagavatgita.R
import com.example.shreebhagavatgita.dataSource.API.ApiUtilities
import com.example.shreebhagavatgita.dataSource.API.Room.AppDatabase
import com.example.shreebhagavatgita.databinding.FragmentVerseDetailsBinding
import com.example.shreebhagavatgita.repository.AppRepository
import com.example.shreebhagavatgita.viewModel.MainViewModel
import com.example.shreebhagavatgita.viewModel.MainViewModelFactory
import kotlinx.coroutines.launch


class VerseDetailsFragment : Fragment() {

    private lateinit var binding: FragmentVerseDetailsBinding
    private var chapterNumber = 0
    private var verseNumber = 0
    private lateinit var viewModel: MainViewModel
    private lateinit var repository: AppRepository



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVerseDetailsBinding.inflate(layoutInflater)

        repository = AppRepository(ApiUtilities.api, AppDatabase.getDatabaseInstance(requireContext())!!.savedChaptersDao())

        viewModel = MainViewModelFactory(repository, requireActivity().application).create(MainViewModel::class.java)
        changeStatusBarColor()
        getAndSetVerseDetails()
        getVerseDetails()
        checkInternetConnectivity()
        onReadMoreClicked()
        binding.ivBack.setOnClickListener{
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        return binding.root
    }

    private fun getVerseDetails() {
        lifecycleScope.launch {
            viewModel.getParticularVerse(chapterNumber,verseNumber).collect{
                    verse ->
                binding.tvVerses.text = verse.text
                binding.tvTransliteration.text = verse.transliteration
                binding.tvWordMeaning.text = verse.word_meanings

                val englishList = arrayListOf<Translation>()

                for (i in verse.translations){
                    if (i.language == "english") englishList.add(i)
                }

                val englishListSize = englishList.size

                if (englishList.isNotEmpty()){
                    binding.tvAuthorName.text = englishList[0].author_name
                    binding.tvTranslation.text = englishList[0].description

                    if (englishListSize == 1){
                        binding.idFabRight.visibility = View.GONE
                    }

                    var i = 0
                    binding.idFabRight.setOnClickListener{
                        if (i < englishListSize - 1) {
                            i++
                            binding.tvAuthorName.text = englishList[i].author_name
                            binding.tvTranslation.text = englishList[i].description
                            binding.idFabLeft.visibility = View.VISIBLE

                            if (i == englishListSize - 1) binding.idFabRight.visibility = View.GONE
                        }

                    }

                    binding.idFabLeft.setOnClickListener {
                        if (i > 0){
                            i--
                            binding.tvAuthorName.text = englishList[i].author_name
                            binding.tvTranslation.text = englishList[i].description
                            binding.idFabRight.visibility = View.VISIBLE

                            if (i == 0) binding.idFabLeft.visibility = View.GONE
                        }
                    }
                }

                val englishCommentaryList = arrayListOf<Commentary>()

                for (i in verse.commentaries){
                    if (i.language == "english") englishCommentaryList.add(i)
                }

                val englishCommentaryListSize = englishCommentaryList.size

                if (englishCommentaryList.isNotEmpty()){
                    binding.tvAuthorTitle.text = englishCommentaryList[0].author_name
                    binding.tvCommentary.text = englishCommentaryList[0].description

                    if (englishCommentaryListSize == 1){
                        binding.idFabCommentaryRight.visibility = View.GONE
                    }

                    var i = 0
                    binding.idFabCommentaryRight.setOnClickListener{
                        if (i < englishCommentaryListSize - 1) {
                            i++
                            binding.tvAuthorTitle.text = englishCommentaryList[0].author_name
                            binding.tvCommentary.text = englishCommentaryList[0].description
                            binding.idFabCommentaryLeft.visibility = View.VISIBLE

                            if (i == englishCommentaryListSize - 1) binding.idFabCommentaryRight.visibility = View.GONE
                        }

                    }

                    binding.idFabCommentaryLeft.setOnClickListener {
                        if (i > 0){
                            i--
                            binding.tvAuthorTitle.text = englishCommentaryList[0].author_name
                            binding.tvCommentary.text = englishCommentaryList[0].description
                            binding.idFabCommentaryLeft.visibility = View.VISIBLE

                            if (i == 0) binding.idFabCommentaryLeft.visibility = View.GONE
                        }
                    }
                }
                binding.idProgress.visibility = View.GONE
                binding.clGita.visibility = View.VISIBLE
                binding.idLinerTop.visibility = View.VISIBLE
                binding.idLinerBottom.visibility = View.VISIBLE
                binding.view.visibility = View.VISIBLE
                binding.tvVerseNumber.visibility = View.VISIBLE
                binding.tvVerses.visibility = View.VISIBLE
                binding.tvTransliteration.visibility = View.VISIBLE
                binding.tvWordMeaning.visibility = View.VISIBLE
                binding.LinerLayout.visibility = View.VISIBLE
                binding.idClCommentary.visibility = View.VISIBLE
                binding.idFabRight.visibility = View.VISIBLE

            }
        }
    }

    private fun onReadMoreClicked() {
        var isExpanded = false
        binding.tvSeeMore.setOnClickListener{
            if (!isExpanded){
                binding.tvCommentary.maxLines = 100
                binding.tvSeeMore.text = getString(R.string.read_less)
                isExpanded = true
            }
            else{
                binding.tvCommentary.maxLines = 4
                binding.tvSeeMore.text = getString(R.string.read_more)
                isExpanded = false
            }
        }
    }
    private fun checkInternetConnectivity() {
        val networkManager = NetworkManager(requireContext())
        networkManager.observe(viewLifecycleOwner){
            if (it == true) {
                binding.ivNoInternet.visibility = View.GONE
                binding.tvShowingInternet.visibility = View.GONE
                getAndSetVerseDetails()
                getVerseDetails()
            }
            else{
                binding.ivNoInternet.visibility = View.VISIBLE
                binding.tvShowingInternet.visibility = View.VISIBLE
                binding.clGita.visibility = View.VISIBLE
                binding.idLinerTop.visibility = View.GONE
                binding.idLinerBottom.visibility = View.GONE
                binding.view.visibility = View.GONE
                binding.tvVerseNumber.visibility = View.GONE
                binding.tvVerses.visibility = View.GONE
                binding.tvTransliteration.visibility = View.GONE
                binding.tvWordMeaning.visibility = View.GONE
                binding.LinerLayout.visibility = View.GONE
                binding.idClCommentary.visibility = View.GONE
                binding.idFabRight.visibility = View.GONE
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getAndSetVerseDetails() {
        val bundle = arguments
        Log.e("Bundle ::::", "$bundle")
        chapterNumber = bundle?.getInt("chapterNum",0)!!
        verseNumber = bundle.getInt("verseNum",0)
        Log.e("Chapter Number ::::", "$chapterNumber")
        Log.e("Verse Number ::::", "$verseNumber")

        binding.tvVerseNumber.text = "||$chapterNumber.$verseNumber||"
    }

    private fun changeStatusBarColor() {
        val window = activity?.window
        window!!.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
        }

    }



}