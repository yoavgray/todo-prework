package com.prework.mytodoapp.todoornottodo;

public class TodoItem extends ListItem {
    String todo;

    public TodoItem(String header, boolean isChecked, String todo) {
        super(header, isChecked);
        this.todo = todo;
    }

    public String getTodoText() {
        return this.todo;
    }

    public void setTodoText(String todo) {
        this.todo = todo;
    }
}
