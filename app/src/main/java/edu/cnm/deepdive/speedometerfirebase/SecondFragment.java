package edu.cnm.deepdive.speedometerfirebase;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import dagger.hilt.android.AndroidEntryPoint;
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

    secondFragmentViewModel.getAllSubmissions().observe(getViewLifecycleOwner(), (userSubmissions) -> {
          binding.historyRecyclerView.setAdapter(new Adapter() {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
              View historyRow = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.activity_list_item, parent, false);
              return new ViewHolder(historyRow){
              };
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
              ((TextView) holder.itemView.findViewById(android.R.id.text1)).setText(userSubmissions.get(position).getSpeed());
            }

            @Override
            public int getItemCount() {
              return userSubmissions.size();
            }
          });
        });

        binding.submitButton.setOnClickListener((v) -> {
          int speedValue = Integer.parseInt(binding.speedNumber.getText().toString());
          secondFragmentViewModel.addSubmission(speedValue);
        });
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

}