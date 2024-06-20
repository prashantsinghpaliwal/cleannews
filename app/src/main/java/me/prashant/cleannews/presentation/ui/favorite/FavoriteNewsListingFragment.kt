package me.prashant.cleannews.presentation.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import me.prashant.cleannews.databinding.FragmentFavoriteNewsListingBinding

@AndroidEntryPoint
class FavoriteNewsListingFragment : Fragment() {
    private var _binding: FragmentFavoriteNewsListingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentFavoriteNewsListingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: Set up RecyclerView, ViewModel, and Search functionality
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
