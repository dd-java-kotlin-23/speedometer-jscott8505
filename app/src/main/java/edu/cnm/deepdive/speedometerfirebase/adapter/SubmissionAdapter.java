package edu.cnm.deepdive.speedometerfirebase.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.speedometerfirebase.R;
import edu.cnm.deepdive.speedometerfirebase.databinding.ItemSubmissionBinding;
import edu.cnm.deepdive.speedometerfirebase.model.UserSubmission;
import java.text.DateFormat;
import java.util.Date;

/**
 * RecyclerView adapter for displaying user speed submissions.
 * Extends {@link ListAdapter} to handle asynchronous list diffing using {@link DiffUtil}.
 */
public class SubmissionAdapter extends ListAdapter<UserSubmission, SubmissionAdapter.Holder> {

  private final LayoutInflater inflater;
  private final OnSubmissionClickListener clickListener;

  /**
   * Constructs the adapter with a context and an optional click listener.
   *
   * @param context the parent Context
   * @param clickListener optional listener for item click events
   */
  public SubmissionAdapter(@NonNull Context context, @Nullable OnSubmissionClickListener clickListener) {
    super(new DiffCallback());
    this.inflater = LayoutInflater.from(context);
    this.clickListener = clickListener;
  }

  /**
   * Constructs the adapter with only a context.
   *
   * @param context the parent Context
   */
  public SubmissionAdapter(@NonNull Context context) {
    this(context, null);
  }

  @NonNull
  @Override
  public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    ItemSubmissionBinding binding = ItemSubmissionBinding.inflate(inflater, parent, false);
    return new Holder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull Holder holder, int position) {
    holder.bind(getItem(position));
  }

  /**
   * Listener interface for handling click events on individual submissions.
   */
  @FunctionalInterface
  public interface OnSubmissionClickListener {
    void onSubmissionClick(UserSubmission submission);
  }

  /**
   * ViewHolder class that encapsulates the view binding and binding logic for each item.
   */
  class Holder extends RecyclerView.ViewHolder {

    private final ItemSubmissionBinding binding;
    private final DateFormat dateFormat;

    Holder(@NonNull ItemSubmissionBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
      this.dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);
    }

    void bind(UserSubmission submission) {
      Context context = itemView.getContext();

      // Display the formatted speed
      binding.speedValue.setText(context.getString(R.string.speed_format, submission.getSpeed()));

      // Format and display the timestamp (handling null gracefully)
      Date timestamp = submission.getTimestamp();
      if (timestamp != null) {
        binding.submissionTimestamp.setText(dateFormat.format(timestamp));
      } else {
        binding.submissionTimestamp.setText("");
      }

      // Hook up the click listener if provided
      if (clickListener != null) {
        itemView.setOnClickListener(v -> clickListener.onSubmissionClick(submission));
      }
    }
  }

  /**
   * Callback implementation for calculating the diff between two non-null items in a list.
   */
  private static class DiffCallback extends DiffUtil.ItemCallback<UserSubmission> {

    @Override
    public boolean areItemsTheSame(@NonNull UserSubmission oldItem, @NonNull UserSubmission newItem) {
      // Check if both objects refer to the same logical item (using document ID or timestamp)
      if (oldItem.getId() != null && newItem.getId() != null) {
        return oldItem.getId().equals(newItem.getId());
      }
      return false;
    }

    @Override
    public boolean areContentsTheSame(@NonNull UserSubmission oldItem, @NonNull UserSubmission newItem) {
      // Check if the item's contents have changed
      return oldItem.getSpeed() == newItem.getSpeed()
          && (oldItem.getTimestamp() != null ? oldItem.getTimestamp().equals(newItem.getTimestamp()) : newItem.getTimestamp() == null);
    }
  }
}
