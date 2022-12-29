package com.taipei.attractions.views

import android.graphics.Bitmap
import android.os.Bundle
import android.view.*
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.taipei.attractions.databinding.FragmentWebViewBinding


class AttractionsWebViewFragment  : Fragment() {
    private var _binding: FragmentWebViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentWebViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val urlString by navArgs<AttractionsWebViewFragmentArgs>()

        binding.progressBar.max = 100
        binding.progressBar.progress = 1

        binding.swipe.setOnRefreshListener { binding.webView.reload() }

        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.useWideViewPort = true
        binding.webView.settings.loadWithOverviewMode = true

        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, progress: Int) {
                binding.progressBar.progress = progress
            }
        }

        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.progressBar.visibility = View.VISIBLE
                binding.swipe.isRefreshing = false
            }

            override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
                view.loadUrl("about:blank")
                binding.progressBar.visibility = View.GONE
                binding.swipe.isRefreshing = false
            }

            override fun onLoadResource(view: WebView, url: String) {
//                binding.swipe.isRefreshing = false
            }

            override fun onPageFinished(view: WebView, url: String) {
                binding.progressBar.visibility = View.GONE
                binding.swipe.isRefreshing = false
            }
        }

        binding.webView.loadUrl(urlString.urlString)

        requireActivity().onBackPressedDispatcher.addCallback(requireActivity()) {
            checkBackPress()
        }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    android.R.id.home -> {
                        checkBackPress()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

    private fun checkBackPress(){
        if (binding.webView.canGoBack())
            binding.webView.goBack()
        else
            findNavController().popBackStack()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}