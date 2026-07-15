package edu.cnm.deepdive.speedometerfirebase.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseUser;
import dagger.hilt.android.lifecycle.HiltViewModel;
import edu.cnm.deepdive.speedometerfirebase.model.UserSubmission;
import edu.cnm.deepdive.speedometerfirebase.repository.UserSubmissionRepository;
import edu.cnm.deepdive.speedometerfirebase.service.SignInService;
import jakarta.inject.Inject;
import java.util.List;

@HiltViewModel
public class SecondFragmentViewModel extends ViewModel {

  private final SignInService signInService;
  private final UserSubmissionRepository userSubmissionRepository;

  @Inject
  SecondFragmentViewModel(SignInService signInService,
      UserSubmissionRepository userSubmissionRepository) {
    this.signInService = signInService;
    this.userSubmissionRepository = userSubmissionRepository;
  }

  public LiveData<FirebaseUser> getUser() {
    return signInService.getUser();
  }

  public void signOut() {
    signInService.signOut();
  }

  public void addSubmission(int speed) {
    userSubmissionRepository.addSubmission(speed, signInService.getUser().getValue().getUid());
  }

  public LiveData<List<UserSubmission>> getAllSubmissions() {
    return userSubmissionRepository.getAllSubmissions();
  }

}
