package vn.edu.usth.attendancecheck.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import vn.edu.usth.attendancecheck.R;
import vn.edu.usth.attendancecheck.models.HistoryClasses;
import vn.edu.usth.attendancecheck.ui.HistoryFragment;
import vn.edu.usth.attendancecheck.utils.RecyclerViewItemOnClickListener;

import java.util.List;


public final class HistoryAdapter
        extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>
        implements RecyclerViewItemOnClickListener {
    private static final String TAG = "HistoryAdapter";
    private final List<HistoryClasses> data;
    private final HistoryFragment fragment;

    public HistoryAdapter(@NonNull HistoryFragment historyFragment,
                          @NonNull List<HistoryClasses> liveData) {
        this.data = liveData;
        this.fragment = historyFragment;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()
        ).inflate(
                R.layout.rv_current_courses,
                parent,
                false
        );
        return new ViewHolder(
                view,
                this
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setViewHolderOnClickListener();
        holder.courseName.setText(
                data.get(position).getCourse()
        );
        holder.teacherName.setText(
                data.get(position).getTeacher()
        );
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onItemClick(int adapterPosition) {
        fragment.switchFragment(adapterPosition);
    }


    /**
     *
     */
    static class ViewHolder
            extends RecyclerView.ViewHolder {
        //
        private static final String TAG = "ViewHolder";
        private final TextView courseName;
        private final TextView teacherName;
        private final RecyclerViewItemOnClickListener itemOnClickListener;

        public ViewHolder(@NotNull View itemView,
                          @NonNull RecyclerViewItemOnClickListener itemOnClickListener) {
            super(itemView);
            this.itemOnClickListener = itemOnClickListener;

            courseName = itemView.findViewById(R.id.course_name);
            teacherName = itemView.findViewById(R.id.teacher);
        }

        /**
         *
         */
        public void setViewHolderOnClickListener() {
            itemView.setOnClickListener(
                    v -> {
                        Log.e(TAG, "setRecyclerViewItemOnClickListener: position " + getAdapterPosition() + " is listening!");
                        itemOnClickListener.onItemClick(getAdapterPosition());
                    }
            );
        }

    }
}
