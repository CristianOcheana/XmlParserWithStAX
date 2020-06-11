import java.util.*;

public class Supplier {
    private String supplierName;
    private List<Product> productList;

    public Supplier(String supplierName){
        this.supplierName = supplierName;
        this.productList = new ArrayList();
    }

    public void addProduct(Product product) {
        this.productList.add(product);
    }

    public String getSupplierName() {
        return supplierName;
    }

    public List<Product> getProductList() {
        return productList;
    }
}
