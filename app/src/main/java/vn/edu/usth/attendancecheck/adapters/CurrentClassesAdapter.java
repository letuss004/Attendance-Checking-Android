package vn.edu.usth.attendancecheck.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import vn.edu.usth.attendancecheck.R;
import vn.edu.usth.attendancecheck.models.CurrentClasses;
import vn.edu.usth.attendancecheck.ui.CurrentClassesFragment;

import java.util.List;

public final class CurrentClassesAdapter
        extends RecyclerView.Adapter<CurrentClassesAdapter.ViewHolder> {
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
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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


    /**
     *
     */
    static class ViewHolder
            extends RecyclerView.ViewHolder {
        private TextView courseName, teacherName;

        public ViewHolder(@NotNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.course_name);
            teacherName = itemView.findViewById(R.id.teacher);
        }

    }
}
