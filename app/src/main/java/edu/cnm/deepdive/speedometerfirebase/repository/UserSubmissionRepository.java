package edu.cnm.deepdive.speedometerfirebase.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import edu.cnm.deepdive.speedometerfirebase.model.UserSubmission;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.Date;
import java.util.List;

@Singleton
public class UserSubmissionRepository {

  private final FirebaseFirestore firestore;

  private final CollectionReference submissionCollection;

  @Inject
  public UserSubmissionRepository() {
    firestore = FirebaseFirestore.getInstance();
    submissionCollection = firestore.collection("submission");
  }

  public Task<DocumentReference> addSubmission(int speed, String userId) {
    UserSubmission submission = new UserSubmission();
    submission.setUserId(userId);
    submission.setSpeed(speed);
    submission.setTimestamp(new Date());
    return submissionCollection.add(submission);
  }

  public LiveData<List<UserSubmission>> getAllSubmissions() {
    MutableLiveData<List<UserSubmission>> liveData = new MutableLiveData<>();
    submissionCollection
        .orderBy("timestamp", Query.Direction.DESCENDING)
        .addSnapshotListener((snapshot, error) -> {
          if (error != null) {
            return;
          }
          if (snapshot != null) {
            liveData.postValue(snapshot.toObjects(UserSubmission.class));
          }
        });
    return liveData;
  }

}
