package ru.netology.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import ru.netology.R
import ru.netology.databinding.FragmentEditPostBinding
import ru.netology.utils.AndroidUtils
import ru.netology.utils.StringArg
import ru.netology.viewmodel.PostViewModel


class EditPostFragment : Fragment() {
    companion object {
        var Bundle.textArg: String? by StringArg
        var Bundle.textArg2: String? by StringArg
    }

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEditPostBinding.inflate(
            inflater,
            container,
            false
        )

        arguments?.textArg?.let(
            binding.postContent::setText
        )

        binding.btnOk.setOnClickListener {
            viewModel.changeContent(binding.postContent.text.toString())
            viewModel.save()
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }
        return binding.root
    }
}
