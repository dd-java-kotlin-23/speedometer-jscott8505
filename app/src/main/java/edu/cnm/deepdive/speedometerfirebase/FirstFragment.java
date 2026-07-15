package edu.cnm.deepdive.speedometerfirebase;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.speedometerfirebase.databinding.FragmentFirstBinding;
import edu.cnm.deepdive.speedometerfirebase.viewmodel.SignInViewModel;

@AndroidEntryPoint
public class FirstFragment extends Fragment {

  private FragmentFirstBinding binding;

  private SignInViewModel signInViewModel;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,
     @Nullable Bundle savedInstanceState) {
    binding = FragmentFirstBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    signInViewModel = new ViewModelProvider(this).get(SignInViewModel.class);

    signInViewModel.getUser().observe(getViewLifecycleOwner(), (user) -> {
      if (user != null) {
        Navigation.findNavController(requireView()).navigate(R.id.action_FirstFragment_to_SecondFragment);
      }
    });

    binding.loginButton.setOnClickListener((v) -> {
      String email = binding.emailInput.getText().toString().trim();
      String password = binding.passwordInput.getText().toString().trim();
      signInViewModel.signInWithEmail(email, password);
    });
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

}