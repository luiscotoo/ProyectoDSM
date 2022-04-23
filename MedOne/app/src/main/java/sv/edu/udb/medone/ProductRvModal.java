package sv.edu.udb.medone;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductRvModal implements Parcelable{
    private String productName;
    private String productPrice;
    private String productDescription;
    private String productRestrictions;
    private String productImg;
    private String productLink;
    private String productId;

    protected ProductRvModal(Parcel in){
        productName=in.readString();
        productPrice=in.readString();
        productDescription=in.readString();
        productRestrictions=in.readString();
        productImg=in.readString();
        productLink=in.readString();
        productId=in.readString();
    }
    public ProductRvModal (){

    }
    public ProductRvModal(String productName, String productPrice, String productDescription, String productRestrictions, String productImg, String productLink, String productId){
        this.productName=productName;
        this.productPrice=productPrice;
        this.productDescription=productDescription;
        this.productRestrictions=productRestrictions;
        this.productImg=productImg;
        this.productLink=productLink;
        this.productId=productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductLink() {
        return productLink;
    }

    public void setProductLink(String productLink) {
        this.productLink = productLink;
    }

    public String getProductRestrictions() {
        return productRestrictions;
    }

    public void setProductRestrictions(String productRestrictions) {
        this.productRestrictions = productRestrictions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(productName);
        parcel.writeString(productPrice);
        parcel.writeString(productDescription);
        parcel.writeString(productRestrictions);
        parcel.writeString(productId);
        parcel.writeString(productImg);
        parcel.writeString(productLink);

    }
    public static final Creator<ProductRvModal> CREATOR=new Creator<ProductRvModal>() {
        @Override
        public ProductRvModal createFromParcel(Parcel parcel) {
            return new ProductRvModal(parcel);
        }

        @Override
        public ProductRvModal[] newArray(int i) {
            return new ProductRvModal[i];
        }
    };
}
