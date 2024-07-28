package ru.netology.netology1stproject

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.netology1stproject.databinding.FragmentNewPostBinding
import ru.netology.netology1stproject.dto.PostViewModel
import ru.netology.netology1stproject.utils.StringArg


class NewPostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(inflater, container, false)
        arguments?.textArg.let(binding.edit::setText)

//        val viewModel: PostViewModel by viewModels (
//            ownerProducer = ::requireParentFragment
//        )
        val viewModel: PostViewModel by activityViewModels()

        binding.edit.requestFocus()

        binding.save.setOnClickListener {
            if (binding.edit.text.isNotBlank()) {
                viewModel.changeContentAndSave(binding.edit.text.toString())
                findNavController().navigateUp()
            }
        }
        return binding.root
    }


    companion object {
        var Bundle.textArg: String? by StringArg
    }
}
