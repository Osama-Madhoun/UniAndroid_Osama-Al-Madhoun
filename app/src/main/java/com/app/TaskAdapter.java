package com.app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolderTask> {
    private Context context;
    private List<TaskItem> taskItems;
    private ITaskElement iListElement;

    interface ITaskElement {
        void openTaskDetails(String taskID, String taskTitle, String taskDesc);
    }

    public TaskAdapter(Context context, List<TaskItem> taskItems, ITaskElement iListElement) {
        this.context = context;
        this.taskItems = taskItems;
        this.iListElement = iListElement;
    }

    @NonNull
    @Override
    public ViewHolderTask onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderTask(LayoutInflater.from(context).inflate(R.layout.element_task_rv, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTask holder, int position) {
        holder.titleTask.setText(taskItems.get(position).getTitleTask());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iListElement.openTaskDetails(taskItems.get(position).getTaskID(),
                        taskItems.get(position).getTitleTask(),
                        taskItems.get(position).getTaskDesc());
            }
        });
    }

    public void addAll(List<TaskItem> listItems) {
        this.taskItems.addAll(listItems);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return taskItems.size();
    }

    public void clear() {
        taskItems.clear();
    }

    class ViewHolderTask extends RecyclerView.ViewHolder {
        private RadioButton titleTask;
        private View itemView;

        public ViewHolderTask(@NonNull View itemView) {
            super(itemView);
            titleTask = itemView.findViewById(R.id.title_task);
            this.itemView = itemView;
        }
    }
}
