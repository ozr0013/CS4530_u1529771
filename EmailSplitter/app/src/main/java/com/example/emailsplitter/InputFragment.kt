package com.example.emailsplitter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.emailsplitter.databinding.FragmentInputBinding

/**
 * Fragment 1: text input plus a button. Splits the email and hands the two
 * pieces to ResultFragment through a Bundle set on its "arguments" property.
 */
class InputFragment : Fragment() {

    private var _binding: FragmentInputBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInputBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button.setOnClickListener {

            val email = binding.emailInput.text.toString()
            val pieces = email.split('@')

            if (pieces.size != 2 || pieces.any(String::isEmpty)) {
                Toast.makeText(requireContext(), "Invalid email!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val resultFragment = ResultFragment()

            // Send data to the Fragment, readable there as the "arguments" property
            val sentData = Bundle()
            sentData.putString(ResultFragment.KEY_USER, pieces[0])
            sentData.putString(ResultFragment.KEY_DOMAIN, pieces[1])
            resultFragment.arguments = sentData

            // Inside a Fragment, use parentFragmentManager
            val fTrans = parentFragmentManager.beginTransaction()
            fTrans.replace(R.id.fl_frag_container, resultFragment, ResultFragment.TAG)
            fTrans.addToBackStack(null)
            fTrans.commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // The Fragment outlives its view, so drop the binding to avoid a leak
        _binding = null
    }

    companion object {
        const val TAG = "input_fragment"
    }
}
