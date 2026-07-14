package edu.cnm.deepdive.speedometerfirebase.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.auth.FirebaseUser;
import dagger.hilt.android.lifecycle.HiltViewModel;
import edu.cnm.deepdive.speedometerfirebase.service.SignInService;
import jakarta.inject.Inject;

@HiltViewModel
public class SignInViewModel extends ViewModel {

  private final SignInService signInService;

  @Inject
  SignInViewModel(SignInService signInService) {
    this.signInService = signInService;
  }

  public LiveData<FirebaseUser> getUser() {
    return signInService.getUser();
  }

  public void signInWithEmail(String email, String password){
    signInService.signInWithEmail(email, password);
  }

  public void signOut(){
    signInService.signOut();
  }

}
