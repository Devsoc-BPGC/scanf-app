package club.devsoc.scanf.view.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import club.devsoc.scanf.R
import club.devsoc.scanf.databinding.RecentDocumentFragmentBinding
import club.devsoc.scanf.viewmodel.RecentDocumentViewModel

class RecentDocumentFragment : Fragment() {
lateinit var binding:  RecentDocumentFragmentBinding
    companion object {
        fun newInstance() = RecentDocumentFragment()
    }

    private lateinit var viewModel: RecentDocumentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = RecentDocumentFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val animation1 = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = animation1
       sharedElementReturnTransition = animation1
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RecentDocumentViewModel::class.java)
        // TODO: Use the ViewModel
        binding.backIvRecentDocFragment.setOnClickListener {
            requireActivity().onBackPressed()
//            findNavController().navigate(R.id.action_recentDocumentFragment_to_homeFragment)
        }
    }


}