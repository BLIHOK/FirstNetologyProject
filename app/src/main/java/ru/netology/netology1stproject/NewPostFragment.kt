package ru.netology.netology1stproject


import android.R
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.netology1stproject.databinding.FragmentNewPostBinding
import ru.netology.netology1stproject.utils.StringArg
import ru.netology.netology1stproject.viewmodel.PostViewModel




class NewPostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(inflater, container, false)
        arguments?.textArg.let(binding.edit::setText)

        val viewModel: PostViewModel by activityViewModels()

        binding.edit.requestFocus()

        binding.save.setOnClickListener {
            if (binding.edit.text.isNotBlank()) {
                viewModel.changeContentAndSave(binding.edit.text.toString())
                findNavController().navigateUp()
            }
        }


        val sharedPreferences = activity?.getSharedPreferences("text", Context.MODE_PRIVATE)

        val editor = sharedPreferences?.edit()
        editor?.putString("textValue",Bundle().apply {
            textArg = post.id.toString()
        }.textArg
        )
        editor?.apply()

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().popBackStack()
        }


        return binding.root
    }


    companion object {
        var Bundle.textArg: String? by StringArg
    }


}




