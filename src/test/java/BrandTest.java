import java.util.Arrays;
import org.junit.*;
import java.util.List;
import static org.junit.Assert.*;

public class BrandTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void all_emptyAtFirst() {
    assertEquals(Brand.all().size(), 0);
  }

  @Test
  public void equals_returnsTrueIfNamesAretheSame() {
    Brand firstBrand = new Brand("Nike");
    Brand secondBrand = new Brand("Nike");
    assertTrue(firstBrand.equals(secondBrand));
  }

  @Test
  public void save_savesIntoDatabase_true() {
    Brand myBrand = new Brand("Nike");
    myBrand.save();
    assertTrue(Brand.all().get(0).equals(myBrand));
  }

  @Test
  public void find_findBrandInDatabase_true() {
    Brand myBrand = new Brand("Nike");
    myBrand.save();
    Brand savedBrand = Brand.find(myBrand.getId());
    assertTrue(myBrand.equals(savedBrand));
  }

  @Test
  public void addStore_addsStoreToBrand() {
    Brand myBrand = new Brand("Nike");
    myBrand.save();

    Store myStore = new Store("Target");
    myStore.save();

    myBrand.addStore(myStore);
    Store savedStore = myBrand.getStores().get(0);
    assertTrue(myStore.equals(savedStore));
  }

  @Test
  public void getStores_returnsAllStores_ArrayList() {
    Brand myBrand = new Brand("Nike");
    myBrand.save();

    Store myStore = new Store("Target");
    myStore.save();

    myBrand.addStore(myStore);
    List savedStores = myBrand.getStores();
    assertEquals(savedStores.size(), 1);
  }

  @Test
  public void delete_deletesAllStoresAndListsAssoicationes() {
    Brand myBrand = new Brand("Nike");
    myBrand.save();

    Store myStore = new Store("Target");
    myStore.save();

    myBrand.addStore(myStore);
    myBrand.delete();
    assertEquals(myBrand.getStores().size(), 0);
  }

}
