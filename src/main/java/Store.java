import java.util.List;
import org.sql2o.*;
import java.util.ArrayList;

public class Store {
  private int id;
  private String name;
  public Store(String name) {
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object otherStore){
    if (!(otherStore instanceof Store)) {
      return false;
    } else {
      Store newStore = (Store) otherStore;
      return this.getName().equals(newStore.getName()) &&
             this.getId() == newStore.getId();
    }
  }


  public static List<Store> all() {
    String sql = "SELECT * FROM stores ORDER BY due_date ASC";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Store.class);
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO stores(name) VALUES (:name)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", name)
        .executeUpdate()
        .getKey();
    }
  }

  public static Store find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM stores where id=:id";
      Store store = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Store.class);
      return store;
    }
  }

  public void update(String name) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE stores SET name = :name WHERE id = :id";
      con.createQuery(sql)
        .addParameter("name", name)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String deleteQuery = "DELETE FROM stores WHERE id = :id;";
        con.createQuery(deleteQuery)
          .addParameter("id", id)
          .executeUpdate();

      String joinDeleteQuery = "DELETE FROM stores_brands WHERE store_id = :store_id";
        con.createQuery(joinDeleteQuery)
          .addParameter("store_id", this.getId())
          .executeUpdate();
    }
  }

  public void addBrand(Brand brand) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO stores_brands (brand_id, store_id) VALUES (:brand_id, :store_id)";
      con.createQuery(sql)
        .addParameter("brand_id", brand.getId())
        .addParameter("store_id", this.getId())
        .executeUpdate();
    }
  }

    public List<Brand> getBrands() {
      try(Connection con = DB.sql2o.open()) {
        String sql = "SELECT brands.* FROM stores JOIN stores_brands ON (stores_brands.store_id = stores.id) JOIN brands ON (stores_brands.brand_id = brands.id) WHERE store_id=:id ORDER BY due_date ASC";
        return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetch(Brand.class);
    }
  }


}
