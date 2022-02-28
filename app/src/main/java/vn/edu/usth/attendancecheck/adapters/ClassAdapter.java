package vn.edu.usth.attendancecheck.adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import vn.edu.usth.attendancecheck.R;
import vn.edu.usth.attendancecheck.network.responses.ClassLessonsResponse;
import vn.edu.usth.attendancecheck.ui.ClassFragment;
import vn.edu.usth.attendancecheck.utils.RecyclerViewItemOnClickListener;

public class ClassAdapter
        extends RecyclerView.Adapter<ClassAdapter.ViewModel>
        implements RecyclerViewItemOnClickListener {

    private final ClassLessonsResponse response;
    private final ClassFragment fragment;

    public ClassAdapter(@NonNull ClassFragment fragment,
                        @NonNull ClassLessonsResponse response) {
        this.fragment = fragment;
        this.response = response;
    }

    @NonNull
    @NotNull
    @Override
    public ViewModel onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()
        ).inflate(
                R.layout.rv_class_lessons,
                parent,
                false
        );
        return new ViewModel(
                view,
                this
        );
    }

    @Override
    public void onBindViewHolder(@NotNull ViewModel holder, int position) {
        holder.lessonName.setText(
                response.getLessons().get(position).getName()
        );
        if (response.getLessons().get(position).getCreateAt() != null)
            holder.time.setText(
                    response.getLessons().get(position).getCreateAt().toString()
            );
        if (response.getStatuses().get(position).equals(1))
            holder.status.setImageResource(
                    R.drawable.ic_baseline_done_24
            );
        else {
            holder.status.setImageResource(
                    R.drawable.ic_baseline_close_24
            );
        }
    }

    @Override
    public int getItemCount() {
        return response.getLessons().size();
    }

    @Override
    public void onItemClick(int adapterPosition) {

    }


    /**
     *
     */
    static class ViewModel
            extends RecyclerView.ViewHolder {

        private final TextView lessonName, time;
        private final ImageView status;
        private final ClassAdapter adapter;

        public ViewModel(@NonNull View itemView,
                         @NonNull ClassAdapter classAdapter) {
            super(itemView);
            this.adapter = classAdapter;
            this.lessonName = itemView.findViewById(R.id.lesson_name);
            this.time = itemView.findViewById(R.id.create_at);
            this.status = itemView.findViewById(R.id.status);
        }
    }
}
