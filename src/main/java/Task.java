import java.util.List;
import org.sql2o.*;
import java.util.ArrayList;

public class Task {
  private int id;
  private String description;
  private String due_date;
  private boolean is_completed = false;

  public Task(String description, String due_date, boolean is_completed) {
    this.description = description;
    this.due_date = due_date;
    this.is_completed = is_completed;
  }

  public int getId() {
    return id;
  }

  public String getDescription() {
    return description;
  }

  public String getDueDate() {
    return due_date;
  }

  public boolean getIsCompleted() {
    return is_completed;
  }


  public static Task getFirstDBEntry() {
    return all().get(0);
  }

  @Override
  public boolean equals(Object otherTask){
    if (!(otherTask instanceof Task)) {
      return false;
    } else {
      Task newTask = (Task) otherTask;
      return this.getDescription().equals(newTask.getDescription()) &&
             this.getId() == newTask.getId();
    }
  }


  public static List<Task> all() {
    String sql = "SELECT * FROM tasks ORDER BY due_date ASC";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Task.class);
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO tasks(description, due_date, is_completed) VALUES (:description, :due_date, :is_completed)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("description", description)
        .addParameter("due_date", due_date)
        .addParameter("is_completed", is_completed)
        .executeUpdate()
        .getKey();
    }
  }

  public static Task find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM tasks where id=:id";
      Task task = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Task.class);
      return task;
    }
  }

  public void update(String description) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE tasks SET description = :description WHERE id = :id";
      con.createQuery(sql)
        .addParameter("description", description)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public void updateIsCompleted(boolean is_completed) {
   try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE tasks SET is_completed = :is_completed WHERE id = :id";
      con.createQuery(sql)
        .addParameter("is_completed", is_completed)
        .addParameter("id", id)
        .executeUpdate();
    }
  }


  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String deleteQuery = "DELETE FROM tasks WHERE id = :id;";
        con.createQuery(deleteQuery)
          .addParameter("id", id)
          .executeUpdate();

      String joinDeleteQuery = "DELETE FROM categories_tasks WHERE task_id = :task_id";
        con.createQuery(joinDeleteQuery)
          .addParameter("task_id", this.getId())
          .executeUpdate();
    }
  }

  public void addCategory(Category category) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO categories_tasks (category_id, task_id) VALUES (:category_id, :task_id)";
      con.createQuery(sql)
        .addParameter("category_id", category.getId())
        .addParameter("task_id", this.getId())
        .executeUpdate();
    }
  }

  // public ArrayList<Category> getCategories() {
  //   try(Connection con = DB.sql2o.open()){
  //     String sql = "SELECT category_id FROM categories_tasks WHERE task_id = :task_id";
  //     List<Integer> categoryIds = con.createQuery(sql)
  //       .addParameter("task_id", this.getId())
  //       .executeAndFetch(Integer.class);
  //
  //     ArrayList<Category> categories = new ArrayList<Category>();
  //
  //     for (Integer categoryId : categoryIds) {
  //         String taskQuery = "Select * From categories WHERE id = :category_id";
  //         Category category = con.createQuery(taskQuery)
  //           .addParameter("category_id", categoryId)
  //           .executeAndFetchFirst(Category.class);
  //         categories.add(category);
  //     }
  //     return categories;
  //   }
  //}


  public List<Category> getCategories() {
  try(Connection con = DB.sql2o.open()) {
    String sql = "SELECT categories.* FROM tasks JOIN categories_tasks ON (categories_tasks.task_id = tasks.id) JOIN categories ON (categories_tasks.category_id = categories.id) WHERE task_id=:id ORDER BY due_date ASC";
    return con.createQuery(sql)
      .addParameter("id", id)
      .executeAndFetch(Category.class);
    }
  }


}
