package ru.netology.netology1stproject


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
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
        val sharedPreferences = activity?.getSharedPreferences("text", Context.MODE_PRIVATE)

        arguments?.textArg.let(binding.edit::setText)

        if (binding.edit.text.isBlank()) {
            val textValue = sharedPreferences?.getString("textValue", "")
            binding.edit.setText(textValue)
        }

        val viewModel: PostViewModel by activityViewModels()

        if (binding.edit.id == 0){
            binding.edit.id = -1
        }

        binding.edit.requestFocus()

        binding.save.setOnClickListener {
            if (binding.edit.text.isNotBlank()) {
                viewModel.changeContentAndSave(binding.edit.text.toString())
                sharedPreferences?.edit()?.remove("textValue")?.apply()////
                findNavController().navigateUp()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val textToSave = binding.edit.text.toString()
            viewModel.cancelEdit() //<----
            sharedPreferences?.edit()?.putString("textValue", textToSave)?.apply()
            findNavController().navigateUp()
        }
            return binding.root
        }


    companion object {
        var Bundle.textArg: String? by StringArg
    }
}




