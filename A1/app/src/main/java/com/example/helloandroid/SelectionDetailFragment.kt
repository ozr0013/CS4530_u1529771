package com.example.helloandroid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.helloandroid.databinding.FragmentSelectionDetailBinding

/**
 * Second screen. Displays the label of whichever button the user tapped on
 * the menu screen and offers an explicit way back.
 */
class SelectionDetailFragment : Fragment() {

    private var _binding: FragmentSelectionDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectionDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // arguments holds the Bundle handed over by newInstance().
        val selectedLabel = arguments?.getString(ARG_SELECTED_LABEL).orEmpty()
        binding.textSelectedLabel.text =
            getString(R.string.selection_message, selectedLabel)

        binding.buttonBackToMenu.setOnClickListener {
            // Pops this fragment off the back stack, revealing the menu again.
            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_SELECTED_LABEL = "selected_label"

        /**
         * Preferred way to construct this fragment. Arguments are supplied
         * through a Bundle rather than a constructor, because the system
         * recreates fragments using the no-argument constructor and any
         * constructor parameters would be lost.
         */
        fun newInstance(selectedLabel: String): SelectionDetailFragment {
            return SelectionDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_SELECTED_LABEL, selectedLabel)
                }
            }
        }
    }
}