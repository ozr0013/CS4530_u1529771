package com.example.emailsplitter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.emailsplitter.databinding.FragmentResultBinding

/**
 * Fragment 2: displays the split result it received through arguments.
 */
class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = arguments?.getString(KEY_USER)
        val domain = arguments?.getString(KEY_DOMAIN)

        binding.userView.text = user
        binding.domainView.text = domain

        // Confirm the data actually made it across the Fragment boundary
        Toast.makeText(
            requireContext(),
            "Received: $user and $domain",
            Toast.LENGTH_SHORT
        ).show()

        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "result_fragment"
        const val KEY_USER = "key_user"
        const val KEY_DOMAIN = "key_domain"
    }
}
