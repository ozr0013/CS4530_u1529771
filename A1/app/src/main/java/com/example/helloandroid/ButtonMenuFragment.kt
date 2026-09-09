package com.example.helloandroid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.helloandroid.databinding.FragmentButtonMenuBinding

/**
 * First screen. Shows a set of option buttons and forwards whichever label
 * the user tapped to the detail screen.
 */
class ButtonMenuFragment : Fragment() {

    // A fragment's view can be destroyed while the fragment instance itself
    // stays alive, so the binding is cleared in onDestroyView to avoid
    // holding on to views that no longer exist.
    private var _binding: FragmentButtonMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentButtonMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Every option button behaves identically, so they are wired up in a
        // single loop rather than with five near-identical click listeners.
        val optionButtons: List<Button> = listOf(
            binding.buttonPizza,
            binding.buttonTacos,
            binding.buttonSushi,
            binding.buttonBurgers,
            binding.buttonRamen
        )

        optionButtons.forEach { optionButton ->
            optionButton.setOnClickListener {
                showDetailFor(optionButton.text.toString())
            }
        }
    }

    /** Swaps in the detail fragment, carrying the chosen label with it. */
    private fun showDetailFor(selectedLabel: String) {
        parentFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container,
                SelectionDetailFragment.newInstance(selectedLabel)
            )
            // Adding to the back stack lets the system back gesture return
            // the user to this menu without any extra code.
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}