package sv.edu.udb.medone;

import android.os.Parcel;
import android.os.Parcelable;

public class ServiceRVModal implements Parcelable {
    private String serviceName;
    private String servicePrice;
    private String serviceDescription;
    private String serviceRestrictions;
    private String serviceImg;
    private String serviceLink;
    private String serviceId;

    protected ServiceRVModal(Parcel in) {
        serviceName=in.readString();
        serviceDescription=in.readString();
        serviceRestrictions=in.readString();
        serviceImg=in.readString();
        serviceId=in.readString();
        servicePrice=in.readString();
        serviceLink=in.readString();
    }
    // creating an empty constructor.
    public ServiceRVModal() {

    }

    public ServiceRVModal(String serviceName,String serviceDescription,String serviceRestrictions,String serviceImg, String serviceId,String servicePrice,String serviceLink){
        this.serviceName=serviceName;
        this.serviceDescription=serviceDescription;
        this.serviceRestrictions=serviceRestrictions;
        this.serviceImg=serviceImg;
        this.serviceId=serviceId;
        this.servicePrice=servicePrice;
        this.serviceLink=serviceLink;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public String getServiceImg() {
        return serviceImg;
    }

    public void setServiceImg(String serviceImg) {
        this.serviceImg = serviceImg;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceRestrictions() {
        return serviceRestrictions;
    }

    public void setServiceRestrictions(String serviceRestrictions) {
        this.serviceRestrictions = serviceRestrictions;
    }

    public String getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getServiceLink() {
        return serviceLink;
    }

    public void setServiceLink(String serviceLink) {
        this.serviceLink = serviceLink;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(serviceName);
        dest.writeString(serviceDescription);
        dest.writeString(serviceRestrictions);
        dest.writeString(serviceImg);
        dest.writeString(serviceId);
        dest.writeString(servicePrice);
        dest.writeString(serviceLink);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ServiceRVModal> CREATOR = new Creator<ServiceRVModal>() {
        @Override
        public ServiceRVModal createFromParcel(Parcel in) {
            return new ServiceRVModal(in);
        }

        @Override
        public ServiceRVModal[] newArray(int size) {
            return new ServiceRVModal[size];
        }
    };
}
