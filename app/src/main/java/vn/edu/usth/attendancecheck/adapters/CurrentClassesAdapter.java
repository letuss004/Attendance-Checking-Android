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
import vn.edu.usth.attendancecheck.models.CurrentClasses;
import vn.edu.usth.attendancecheck.ui.CurrentClassesFragment;
import vn.edu.usth.attendancecheck.utils.RecyclerViewItemOnClickListener;

import java.util.List;


public final class CurrentClassesAdapter
        extends RecyclerView.Adapter<CurrentClassesAdapter.ViewHolder>
        implements RecyclerViewItemOnClickListener {
    private static final String TAG = "CurrentClassesAdapter";
    private final List<CurrentClasses> data;
    private final CurrentClassesFragment fragment;

    public CurrentClassesAdapter(
            @NonNull CurrentClassesFragment currentClassesFragment,
            @NonNull List<CurrentClasses> liveData) {
        this.data = liveData;
        this.fragment = currentClassesFragment;
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
        Log.e(TAG, "onItemClick: switch to class fragment");
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
