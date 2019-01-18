package com.example.kaitlynwright.to_dolist;

public class Task {
    private String name = null;
    private String dueDate = null;
    private String note = null;
    private boolean complete = false;
    private boolean longTerm = false;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public Boolean getComplete() { return complete; }
    public void setComplete(Boolean complete) { this.complete = complete; }

    public Boolean getLongTerm() { return longTerm; }
    public void setLongTerm(Boolean longTerm) { this.longTerm = longTerm; }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Task)) {
            return false;
        }
        Task other = (Task) o;
        return name.equals(other.name) && dueDate.equals(other.dueDate) && note.equals(other.note)
                && complete == other.complete && longTerm == other.longTerm;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

}
