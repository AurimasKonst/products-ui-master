package lt.bit.products.ui.model;

public class Supplier {

  private int id;
  private String name;
  private String companyCode;
  private String vatCode;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCompanyCode() {
    return companyCode;
  }

  public void setCompanyCode(String companyCode) {
    this.companyCode = companyCode;
  }

  public String getVatCode() {
    return vatCode;
  }

  public void setVatCode(String vatCode) {
    this.vatCode = vatCode;
  }
}
