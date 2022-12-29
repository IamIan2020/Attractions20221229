package com.taipei.attractions.views.all

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.taipei.attractions.ClickListener
import com.taipei.attractions.R
import com.taipei.attractions.databinding.FragmentAttractionsAllBinding
import com.taipei.attractions.model.AttractionsAllModel
import com.taipei.attractions.network.NetworkResult
import com.taipei.attractions.viewmodel.CommentsViewModel
import com.taipei.attractions.views.viewtools.OnLoadMoreListener
import com.taipei.attractions.views.viewtools.RecyclerViewLoadMoreScroll

class AttractionsAllFragment : Fragment() {
    private lateinit var viewModel: CommentsViewModel
    private var _binding: FragmentAttractionsAllBinding? = null

    private val attractionsAllAdapter = AttractionsAllAdapter()
    lateinit var scrollListener: RecyclerViewLoadMoreScroll
    private val binding get() = _binding!!
    private var pageIndex = 1
    private var maxSize = 1
    private var language = "zh-tw"
    var isLoading = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this)[CommentsViewModel::class.java]
        _binding = FragmentAttractionsAllBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuLanguage: MenuHost = requireActivity()
        menuLanguage.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_language, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_language -> {
                        val builder = AlertDialog.Builder(requireActivity())
                        val languages = requireActivity().resources.getStringArray(R.array.language_list)
                        builder.setItems(languages) { _, which ->
                            val languagesCode = requireActivity().resources.getStringArray(R.array.language_code)
                            if (languagesCode[which] == language)
                                return@setItems

                            language = languagesCode[which]
                            attractionsAllAdapter.submitList(null)
                            attractionsAllAdapter.setLoadMore()
                            viewModel.getNewAttractionsAll1(language, pageIndex)

                        }
                        Log.d("log", "onMenuItemSelected")
                        val dialog = builder.create()
                        dialog.show()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        val layoutManager = LinearLayoutManager(requireContext())
        scrollListener = RecyclerViewLoadMoreScroll(layoutManager as LinearLayoutManager)
        scrollListener.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                if (maxSize <= attractionsAllAdapter.itemCount && scrollListener.getLoaded())
                    return

                attractionsAllAdapter.setLoadMore()
                viewModel.getNewAttractionsAll1(language, pageIndex)
            }
        })

        binding.recyclerview.addOnScrollListener(scrollListener)
        binding.recyclerview.layoutManager = layoutManager
        binding.recyclerview.adapter = attractionsAllAdapter
        attractionsAllAdapter.submitList(ArrayList())

        viewModel.attractionAllResponse.observe(viewLifecycleOwner){

            when(it) {

                is NetworkResult.Loading -> {
                    Log.d("log", "NetworkResult.Loading")
                }

                is NetworkResult.Failure -> {
                    Log.d("log", "NetworkResult.Failure")
                    attractionsAllAdapter.setLoadEnd()
                    scrollListener.setLoaded()
                    Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT).show()
                }

                is  NetworkResult.Success -> {
                    Log.d("log", "NetworkResult.Success it.data?.data?.size== " + it.data.data.size)
                    it.data.let { comment ->

                        val currentList = attractionsAllAdapter.currentList.toMutableList()
                        if (currentList.isNotEmpty())
                            currentList.removeLast()
                        currentList.addAll(comment.data)
                        if (pageIndex == 1) {
                            maxSize = comment.total
                        }
                        attractionsAllAdapter.submitList(currentList)
                        pageIndex++
                        Log.d("log", "it.status== NetworkResult.Success" + " currentList.size== " + currentList.size)
                    }

                    isLoading = false
                    scrollListener.setLoaded()
                }
            }
        }

        attractionsAllAdapter.setOnItemClickListener(object : ClickListener<AttractionsAllModel.Data> {
            override fun onClick(view: View, data: AttractionsAllModel.Data, position: Int) {
                val imageView = view.findViewById<ImageView>(R.id.imgPhoto)

                val extras = FragmentNavigatorExtras( imageView to "imgPhoto")


                val action = AttractionsAllFragmentDirections.actionAttractionsFragmentToAttractionDetailFragment(data, data.name)
                findNavController().navigate(action, extras)
            }
        })

        if (attractionsAllAdapter.itemCount <= 0){
            attractionsAllAdapter.setLoadMore()
            viewModel.getNewAttractionsAll1(language, pageIndex)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.recyclerview.visibility = View.VISIBLE
        if (attractionsAllAdapter.itemCount >= 1){
            attractionsAllAdapter.submitList(attractionsAllAdapter.currentList)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}