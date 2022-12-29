package com.taipei.attractions.views.detail

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.taipei.attractions.R
import com.taipei.attractions.databinding.FragmentAttractionlDetailBinding
import com.taipei.attractions.model.AttractionsAllModel


class AttractionsDetailFragment  : Fragment() {
    private var _binding: FragmentAttractionlDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAttractionlDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val attractionData by navArgs<AttractionsDetailFragmentArgs>()
        val detailData =  attractionData.detailDataA as AttractionsAllModel.Data

        sharedElementEnterTransition =
            TransitionInflater
                .from(requireContext())
                .inflateTransition(android.R.transition.move)

        if (detailData.images.isNotEmpty())
                    Glide.with(binding.imgPhoto.context)
                        .load(detailData.images[0].src + detailData.images[0].ext)
                        .error(R.mipmap.no_photo)
                        .into(binding.imgPhoto)
        else{
            Glide.with(binding.imgPhoto.context)
                .load(R.mipmap.no_photo)
                .into(binding.imgPhoto)
        }


        binding.tvName.text = String.format(detailData.name)

        binding.tvIntroduction.text = String.format(detailData.introduction)

        binding.tvOpenTime.text = String.format(getString(R.string.detail_open_time) + "\n" + detailData.openTime)
        binding.tvAddress.text = String.format(getString(R.string.detail_address) + "\n" + detailData.address)
        binding.tvLatestUpdate.text = String.format(getString(R.string.detail_latest_Update) + "\n" + detailData.modified)
        binding.tvUrl.text = detailData.url
        binding.tvUrl.paintFlags = binding.tvName.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        binding.tvUrl.setOnClickListener(View.OnClickListener {
            val action = AttractionsDetailFragmentDirections.actionAttractionsDetailFragmentToAttractionsWebViewFragment(detailData.url, detailData.name)
            findNavController().navigate(action)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}