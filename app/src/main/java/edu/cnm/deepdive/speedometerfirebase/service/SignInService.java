package edu.cnm.deepdive.speedometerfirebase.service;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class SignInService {

  private final FirebaseAuth auth;

  private final MutableLiveData<FirebaseUser> user;

  @Inject
  SignInService() {
    auth = FirebaseAuth.getInstance();
    user = new MutableLiveData<>(auth.getCurrentUser());
    auth.addAuthStateListener(firebaseAuth -> user.postValue(firebaseAuth.getCurrentUser()));
  }

  public LiveData<FirebaseUser> getUser() {
    return user;
  }

  public Task<AuthResult> signInWithEmail(String email, String password) {
    return auth.signInWithEmailAndPassword(email, password);
  }

  public void signOut() {
    auth.signOut();
  }

}
