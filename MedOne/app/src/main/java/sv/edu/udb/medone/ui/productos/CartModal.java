package sv.edu.udb.medone.ui.productos;

import java.util.ArrayList;

import sv.edu.udb.medone.ProductRvModal;

public class CartModal {
    private String userid ;
    private ArrayList<ProductRvModal> productRvModalArrayList ;
    private Float total;
    private Integer cantidad;

    public CartModal(){

    }
    public CartModal(String userid, ArrayList productRvModalArrayList, Float total, Integer cantidad){
        this.userid=userid;
        this.productRvModalArrayList=productRvModalArrayList;
        this.total=total;
        this.cantidad=cantidad;

    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public ArrayList<ProductRvModal> getProductRvModalArrayList() {
        return productRvModalArrayList;
    }

    public void setProductRvModalArrayList(ArrayList<ProductRvModal> productRvModalArrayList) {
        this.productRvModalArrayList = productRvModalArrayList;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

}
