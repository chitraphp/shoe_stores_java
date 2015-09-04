import java.util.HashMap;
import java.util.List;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;
import java.util.*;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    /* Index */
    get("/", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    /* Index --> Stores*/
    get("/stores", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      List<Store> stores = Store.all();
      model.put("stores", stores);
      model.put("template", "templates/stores.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    /* Store list/form --> POST a new store */
    post("/stores", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name");
      Store newStore = new Store(name);
      newStore.save();
      response.redirect("/stores");
      return null;
    });

    /* Store list/form --> See a particular store */
    get("/stores/:id", (request,response) ->{
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params(":id"));
      Store store = Store.find(id);
      model.put("store", store);
      model.put("allBrands", Brand.all());
      model.put("template", "templates/store.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    /* Brand list/form --> POST a new brand */
    post("/brands", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name");
      Brand newBrand = new Brand(name);
      newBrand.save();
      response.redirect("/brands");
      return null;
    });

    /* Index --> Brands*/
    get("/brands", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      List<Brand> brands = Brand.all();
      model.put("brands", brands);
      model.put("template", "templates/brands.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    /* Store page --> POST a brand to this store */
    post("/add_brands", (request, response) -> {
      int brandId = Integer.parseInt(request.queryParams("brand_id"));
      int storeId = Integer.parseInt(request.queryParams("store_id"));
      Store store = Store.find(storeId);
      Brand brand = Brand.find(brandId);
      store.addBrand(brand);
      response.redirect("/stores/" + storeId);
      return null;
    });

    get("/brands/:id", (request,response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      int id = Integer.parseInt(request.params(":id"));
      Brand brand = Brand.find(id);
      model.put("brand", brand);
      model.put("allStores", Store.all());
      model.put("template", "templates/brand.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());



    /* Brand page --> POST a store to this brand */
    post("/add_stores", (request, response) -> {
      int brandId = Integer.parseInt(request.queryParams("brand_id"));
      int storeId = Integer.parseInt(request.queryParams("store_id"));
      Store store = Store.find(storeId);
      Brand brand = Brand.find(brandId);
      brand.addStore(store);
      response.redirect("/brands/" + brandId);
      return null;
    });

    get("/stores/:id/edit", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Store store = Store.find(Integer.parseInt(request.params("id")));
      model.put("store", store);
      model.put("template", "templates/store-edit-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/stores/:id/edit", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      Store store = Store.find(Integer.parseInt(request.params(":id")));
      String new_name = request.queryParams("new_name");
      store.update(new_name);

      model.put("stores",Store.all());
      model.put("store", store);
      model.put("template", "templates/store.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/stores/:id/delete", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Store store = Store.find(Integer.parseInt(request.params("id")));
      store.delete();
      response.redirect("/stores");
      return null;

    });

  }
}
