package sv.edu.udb.medone;

import android.os.Parcel;
import android.os.Parcelable;

public class HomeRVModal implements Parcelable {
    // creating variables for our different fields.
    private String homeName;
    private String homeDescription;
    private String homePrice;
    private String homeRestrictions;
    private String homeImg;
    private String homeLink;
    private String homeId;


    public String getHomeId() {
        return homeId;
    }

    public void setHomeId(String homeId) {
        this.homeId = homeId;
    }


    // creating an empty constructor.
    public HomeRVModal() {

    }

    protected HomeRVModal(Parcel in) {
        homeName = in.readString();
        homeId = in.readString();
        homeDescription = in.readString();
        homePrice = in.readString();
        homeRestrictions = in.readString();
        homeImg = in.readString();
        homeLink = in.readString();
    }

    public static final Creator<HomeRVModal> CREATOR = new Creator<HomeRVModal>() {
        @Override
        public HomeRVModal createFromParcel(Parcel in) {
            return new HomeRVModal(in);
        }

        @Override
        public HomeRVModal[] newArray(int size) {
            return new HomeRVModal[size];
        }
    };
    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public String getHomeDescription() {
        return homeDescription;
    }

    public void setHomeDescription(String homeDescription) {
        this.homeDescription = homeDescription;
    }

    public String getHomePrice() {
        return homePrice;
    }

    public void setHomePrice(String homePrice) {
        this.homePrice = homePrice;
    }

    public String getHomeRestrictions() {
        return homeRestrictions;
    }

    public void setHomeRestrictions(String homeRestrictions) {
        this.homeRestrictions = homeRestrictions;
    }

    public String getHomeImg() {
        return homeImg;
    }

    public void setHomeImg(String homeImg) {
        this.homeImg = homeImg;
    }

    public String getHomeLink() {
        return homeLink;
    }

    public void setHomeLink(String homeLink) {
        this.homeLink = homeLink;
    }


    public HomeRVModal(String homeId, String homeName, String homeDescription, String homePrice, String homeRestrictions, String homeImg, String homeLink) {
        this.homeName = homeName;
        this.homeId = homeId;
        this.homeDescription = homeDescription;
        this.homePrice = homePrice;
        this.homeRestrictions = homeRestrictions;
        this.homeImg = homeImg;
        this.homeLink = homeLink;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(homeName);
        dest.writeString(homeId);
        dest.writeString(homeDescription);
        dest.writeString(homePrice);
        dest.writeString(homeRestrictions);
        dest.writeString(homeImg);
        dest.writeString(homeLink);
    }
}
