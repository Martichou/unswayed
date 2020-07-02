package me.martichou.unswayedphotos.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import me.martichou.unswayedphotos.data.Result
import me.martichou.unswayedphotos.databinding.HomeFragmentBinding
import me.martichou.unswayedphotos.di.Injectable
import me.martichou.unswayedphotos.di.injectViewModel
import me.martichou.unswayedphotos.ui.home.adapter.HomeAdapter
import me.martichou.unswayedphotos.util.HomeSpacingDecorator
import me.martichou.unswayedphotos.util.toDp
import timber.log.Timber
import javax.inject.Inject

class HomeFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = injectViewModel(viewModelFactory)

        val binding = HomeFragmentBinding.inflate(inflater, container, false)
        context ?: return binding.root

        val adapter = HomeAdapter()
        val gridLayoutManager = GridLayoutManager(context, 4)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (adapter.getItemViewType(position) == 1) 1 else 4
            }
        }
        binding.mainRecyclerview.addItemDecoration(HomeSpacingDecorator(2f.toDp(resources).toInt()))
        binding.mainRecyclerview.layoutManager = gridLayoutManager
        binding.mainRecyclerview.adapter = adapter

        subscribeUi(binding, adapter)

        return binding.root
    }

    private fun subscribeUi(binding: HomeFragmentBinding, adapter: HomeAdapter) {
        viewModel.imagesList.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    Timber.d("Success")
                }
                Result.Status.LOADING -> {
                    Timber.d("Loading")
                }
                Result.Status.ERROR -> {
                    Timber.d("Error ${result.message}")
                }
            }
        })
    }

}