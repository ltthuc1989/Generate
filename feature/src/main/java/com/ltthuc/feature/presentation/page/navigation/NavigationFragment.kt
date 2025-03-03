package com.ltthuc.feature.presentation.page.navigation

import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.ltthuc.feature.R
import com.ltthuc.feature.databinding.BottomNavigationBinding
import com.ltthuc.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NavigationFragment: BaseFragment<BottomNavigationBinding, NavigationViewModel>(BottomNavigationBinding::inflate) {
    private val viewModel: NavigationViewModel by viewModels()

    override fun getVM(): NavigationViewModel  = viewModel

    override fun bindVM(binding: BottomNavigationBinding, vm: NavigationViewModel) {
        binding.viewModel = viewModel
        viewModel.navController = requireActivity().findNavController(com.ltthuc.ui.R.id.fragmentContainerView)
    }

}