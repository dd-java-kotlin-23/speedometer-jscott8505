package edu.cnm.deepdive.speedometerfirebase;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.speedometerfirebase.adapter.SubmissionAdapter;
import edu.cnm.deepdive.speedometerfirebase.databinding.FragmentSecondBinding;
import edu.cnm.deepdive.speedometerfirebase.viewmodel.SecondFragmentViewModel;

@AndroidEntryPoint
public class SecondFragment extends Fragment {

  private FragmentSecondBinding binding;
  private SecondFragmentViewModel secondFragmentViewModel;

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentSecondBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    secondFragmentViewModel = new ViewModelProvider(this).get(SecondFragmentViewModel.class);

    secondFragmentViewModel.getUser().observe(getViewLifecycleOwner(), (user) -> {
      if (user == null) {
        NavController navController = Navigation.findNavController(requireView());
        if (navController.getCurrentDestination() != null
            && navController.getCurrentDestination().getId() == R.id.SecondFragment) {
          navController.navigate(R.id.action_SecondFragment_to_FirstFragment);
        }
      } else {
        binding.loginInfo.setText(getString(R.string.logged_in_as, user.getEmail()));
      }
    });

    binding.buttonLogout.setOnClickListener((v) -> {
      secondFragmentViewModel.signOut();
    });

    // Set up initial speed label
    updateSpeedLabel((int) binding.speedSlider.getValue());

    // Update speed label dynamically when the slider value changes
    binding.speedSlider.addOnChangeListener((slider, value, fromUser) -> {
      updateSpeedLabel((int) value);
    });

    SubmissionAdapter adapter = new SubmissionAdapter(requireContext());
    binding.historyRecyclerView.setAdapter(adapter);

    secondFragmentViewModel.getAllSubmissions().observe(getViewLifecycleOwner(), (userSubmissions) -> {
      adapter.submitList(userSubmissions, () -> {
        if (userSubmissions != null && !userSubmissions.isEmpty()) {
          binding.historyRecyclerView.scrollToPosition(0);
        }
      });
    });

    binding.submitButton.setOnClickListener((v) -> {
      int speedValue = (int) binding.speedSlider.getValue();
      secondFragmentViewModel.addSubmission(speedValue);
    });
  }

  private void updateSpeedLabel(int value) {
    String description;
    if (value == 1) {
      description = getString(R.string.speed_description_too_slow);
    } else if (value < 5) {
      description = getString(R.string.speed_description_slow, value);
    } else if (value == 5) {
      description = getString(R.string.speed_description_just_right);
    } else if (value < 10) {
      description = getString(R.string.speed_description_fast, value);
    } else {
      description = getString(R.string.speed_description_too_fast);
    }
    binding.speedLabel.setText(getString(R.string.speed_label_format, description));
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

}
