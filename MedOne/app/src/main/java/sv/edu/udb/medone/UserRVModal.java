package sv.edu.udb.medone;

import android.os.Parcel;
import android.os.Parcelable;

public class UserRVModal implements Parcelable{
    private String userEmail;
    private String userPassword;
    private String userIsUser;
    private String userID;

    protected UserRVModal(Parcel in){
        userEmail=in.readString();
        userPassword=in.readString();
        userIsUser=in.readString();
        userID=in.readString();
    }
    public UserRVModal (){

    }
    public UserRVModal(String userID, String userEmail, String  userPassword,String userIsUser){
        this.userEmail=userEmail;
        this.userPassword=userPassword;
        this.userIsUser=userIsUser;
        this.userID=userID;

    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserIsUser() {
        return userIsUser;
    }

    public void setUserIsUser(String userIsUser) {
        this.userIsUser = userIsUser;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userEmail);
        parcel.writeString(userPassword);
        parcel.writeString(userIsUser);
        parcel.writeString(userID);
    }
    public static final Creator<UserRVModal> CREATOR=new Creator<UserRVModal>() {
        @Override
        public UserRVModal createFromParcel(Parcel parcel) {
            return new UserRVModal(parcel);
        }

        @Override
        public UserRVModal[] newArray(int i) {
            return new UserRVModal[i];
        }
    };
}

