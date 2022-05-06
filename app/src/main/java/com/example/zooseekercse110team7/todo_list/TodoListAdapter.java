package com.example.zooseekercse110team7.todo_list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zooseekercse110team7.R;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class TodoListAdapter extends RecyclerView.Adapter <TodoListAdapter.ViewHolder>{
    private List<TodoListItem> todoItems = Collections.emptyList();
    private Consumer<TodoListItem> onCheckBoxClicked;
    private Consumer<TodoListItem> onDeleteButtonClicked;
    private BiConsumer<TodoListItem, String> onTextEditedHandler;

    public void setTodoListItems (List newTodoItems) {
        this.todoItems.clear();
        this.todoItems = newTodoItems;
        notifyDataSetChanged();
    }

    public void setOnCheckBoxClickedHandler(Consumer<TodoListItem> onCheckBoxClicked){
        this.onCheckBoxClicked = onCheckBoxClicked;
    }

    public void setOnTextEditedHandler(BiConsumer<TodoListItem, String> onTextEdited){
        this.onTextEditedHandler = onTextEdited;
    }

    public void setOnDeleteButtonClicked(Consumer<TodoListItem> onDeleteButtonClicked){
        this.onDeleteButtonClicked = onDeleteButtonClicked;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext()) .inflate(R.layout.todo_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setTodoItem(todoItems.get(position));
    }

    @Override
    public int getItemCount() {
        return todoItems.size();
    }

    @Override
    public long getItemId(int position) { return todoItems.get(position).id; }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView textView;
        private TodoListItem todoItem;
        private final CheckBox todoCheck;//
        private final TextView deleteBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.todo_list_item);
            this.todoCheck = itemView.findViewById(R.id.completed);//
            this.deleteBtn = itemView.findViewById(R.id.delete_btn);

            this.textView.setOnFocusChangeListener(((view, hasFocus) -> {
                if(!hasFocus){
                    onTextEditedHandler.accept(todoItem, textView.getText().toString());
                }
            }));

            this.todoCheck.setOnClickListener(view -> {
                if(onCheckBoxClicked == null){
                    return;
                }
                onCheckBoxClicked.accept(todoItem);
            });

            this.deleteBtn.setOnClickListener(view -> {
                if(onDeleteButtonClicked == null){ return; }
                onDeleteButtonClicked.accept(todoItem);
            });
        }

        public TodoListItem getTodoItem() { return todoItem; }

        public void setTodoItem (TodoListItem todoItem) {
            this.todoItem = todoItem;
            this.textView.setText(todoItem.text);

            this.todoCheck.setChecked(todoItem.completed);//
        }
    }
}