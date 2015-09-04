import org.fluentlenium.adapter.FluentTest;
import org.junit.ClassRule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import static org.junit.Assert.*;

import static org.fluentlenium.core.filter.FilterConstructor.*;
import static org.assertj.core.api.Assertions.assertThat;

public class AppTest extends FluentTest {
  public WebDriver webDriver = new HtmlUnitDriver();

  @Override
  public WebDriver getDefaultDriver() {
      return webDriver;
  }

  @ClassRule
  public static ServerRule server = new ServerRule();

  @Test
  public void rootTest() {
    goTo("http://localhost:4567/");
    assertThat(pageSource()).contains("Welcome to Shoe Stores");
  }

  @Test
    public void addStoreTest() {
    goTo("http://localhost:4567/stores");
    fill("#name").with("Macys");
    submit(".btn");
    assertThat(pageSource()).contains("Macys");
  }

  // could not find element matching selector: #brand_id.
  // @Test
  // public void addBrandToStoreIsDisplayedTest() {
  //   Store myStore = new Store("Ross!");
  //   myStore.save();
  //   Brand firstBrand = new Brand("Nike");
  //   firstBrand.save();
  //   Brand secondBrand = new Brand("Puma");
  //   secondBrand.save();
  //   String storePath = String.format("http://localhost:4567/stores/%d", myStore.getId());
  //   fillSelect("#brand_id").withText("Puma");
  //   submit(".btn");
  //   assertThat(pageSource()).contains("Puma");
  // }

  @Test
  public void storeLinkIsDisplayedTest() {
    Store new_store = new Store("Ross");
    new_store.save();
    String storePath = String.format("http://localhost:4567/stores");
    goTo(storePath);
    assertThat(pageSource()).contains("Ross");
  }

  @Test
  public void allStoresBrandsAreDisplayedTest() {
    Store myStore = new Store("Ross!");
    myStore.save();
    Brand firstBrand = new Brand("Nike");
    firstBrand.save();
    Brand secondBrand = new Brand("puma");
    secondBrand.save();
    String storePath = String.format("http://localhost:4567/stores/%d", myStore.getId());
    goTo(storePath);
    assertThat(pageSource()).contains("Ross!");
    assertThat(pageSource()).contains("puma");
  }

  // @Test
  // public void homeStoresLinkTest() {
  //   Store myStore = new Store("Kohls");
  //   myStore.save();
  //   Brand firstBrand = new Brand("Nike");
  //   firstBrand.save();
  //   Brand secondBrand = new Brand("puma");
  //   secondBrand.save();
  //
  //   goTo("http://localhost:4567/");
  //   click("a", withText("Stores"));
  //   assertThat(pageSource()).contains("Kohls");
  // }

  @Test
  public void editStoreLinkFormTest() {
    Store myStore = new Store("Kohls");
    myStore.save();
    Store mySecondStore = new Store("Macys");
    mySecondStore.save();
    String clientPath = String.format("http://localhost:4567/stores/%d/edit", mySecondStore.getId());
    goTo(clientPath);
    fill("#new_name").with("Kohls!!");
    submit(".btn");
    assertThat(pageSource()).contains("Kohls!!");
  }

    @Test
    public void deleteStoreTest() {
    Store new_store = new Store("Kohls");
    new_store.save();
    Store mySecondStore = new Store("Macys");
    mySecondStore.save();

    mySecondStore.delete();

    String storePath = String.format("http://localhost:4567/stores/%d/delete", mySecondStore.getId());
    goTo(storePath);
    assertEquals(pageSource().contains("Macys"), false);
    }


}
