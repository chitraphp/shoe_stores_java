import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;

public class StoreTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void all_emptyAtFirst() {
    assertEquals(Store.all().size(), 0);
  }

  @Test
  public void equals_returnsTrueIfDescriptionsAretheSame() {
    Store firstStore = new Store("Target");
    Store secondStore = new Store("Target");
    assertTrue(firstStore.equals(secondStore));
  }

  @Test
  public void find_findsStoreInDatabase_true() {
    Store myStore = new Store("Target");
    myStore.save();
    Store savedStore = Store.find(myStore.getId());
    assertTrue(myStore.equals(savedStore));
  }

  @Test
  public void addBrand_addsBrandToStore() {
    Brand myBrand = new Brand("Nike");
    myBrand.save();

    Store myStore = new Store("Target");
    myStore.save();

    myStore.addBrand(myBrand);
    Brand savedBrand = myStore.getBrands().get(0);
    assertTrue(myBrand.equals(savedBrand));
  }

  @Test
  public void getBrands_returnsAllBrands_ArrayList() {
    Brand myBrand = new Brand("Nike");
    myBrand.save();

    Store myStore = new Store("Target");
    myStore.save();

    myStore.addBrand(myBrand);
    List savedBrands = myStore.getBrands();
    assertEquals(savedBrands.size(), 1);
  }

}
