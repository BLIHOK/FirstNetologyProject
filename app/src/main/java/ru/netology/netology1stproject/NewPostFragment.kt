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
        arguments?.textArg.let(binding.edit::setText)

        val sharedPreferences = activity?.getSharedPreferences("text", Context.MODE_PRIVATE)

        val viewModel: PostViewModel by activityViewModels()

        binding.edit.requestFocus()

        binding.save.setOnClickListener {
            if (binding.edit.text.isNotBlank()) {
                viewModel.changeContentAndSave(binding.edit.text.toString())
                sharedPreferences?.edit()?.remove("textValue")?.apply()////
                findNavController().navigateUp()
            }
        }

        if (binding.edit.text.toString() == arguments?.textArg){
            sharedPreferences?.edit()?.remove("textValue")?.apply()////
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val textToSave = binding.edit.text.toString()
            sharedPreferences?.edit()?.putString("textValue", textToSave)?.apply()
            findNavController().navigateUp()
        }

        val textValue = sharedPreferences?.getString("textValue", "")

        binding.edit.setText(textValue)

        return binding.root
    }


    companion object {
        var Bundle.textArg: String? by StringArg
    }
}




