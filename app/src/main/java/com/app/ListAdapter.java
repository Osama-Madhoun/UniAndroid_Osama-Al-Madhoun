package com.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolderList> {
    private Context context;
    private List<ListItem> listItems;
    private IListElement iListElement;

    interface IListElement {
        void openTasks(String listID, String title);
    }

    public ListAdapter(Context context, List<ListItem> listItems, IListElement iListElement) {
        this.context = context;
        this.listItems = listItems;
        this.iListElement = iListElement;
    }

    @NonNull
    @Override
    public ViewHolderList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderList(LayoutInflater.from(context).inflate(R.layout.element_list_rv, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderList holder, int position) {
        holder.title.setText(listItems.get(position).getTitleList());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iListElement.openTasks(listItems.get(position).getListID(), listItems.get(position).getTitleList());
            }
        });
    }

    public void addAll(List<ListItem> listItems) {
        this.listItems.addAll(listItems);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public void clear() {
        listItems.clear();
    }

    class ViewHolderList extends RecyclerView.ViewHolder {
        private TextView title;
        private View itemView;

        public ViewHolderList(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_list);
            this.itemView = itemView;
        }
    }
}
