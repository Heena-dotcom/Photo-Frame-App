package crephotoseditor.valentinephotoeditor.notiOS;

import android.os.Parcel;
import android.os.Parcelable;

public class GCMModel implements Parcelable {

    private String message;
    private String banner;
    private String icon;
    private String playlink;
    private String appname;
    private int priority;
    private String militime;

    public GCMModel() {
    }

    protected GCMModel(Parcel in) {
        message = in.readString();
        banner = in.readString();
        icon = in.readString();
        playlink = in.readString();
        appname = in.readString();
        priority = in.readInt();
        militime = in.readString();
    }

    public static final Creator<GCMModel> CREATOR = new Creator<GCMModel>() {
        @Override
        public GCMModel createFromParcel(Parcel in) {
            return new GCMModel(in);
        }

        @Override
        public GCMModel[] newArray(int size) {
            return new GCMModel[size];
        }
    };

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPlaylink() {
        return playlink;
    }

    public void setPlaylink(String playlink) {
        this.playlink = playlink;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getMilitime() {
        return militime;
    }

    public void setMilitime(String militime) {
        this.militime = militime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
        dest.writeString(banner);
        dest.writeString(icon);
        dest.writeString(playlink);
        dest.writeString(appname);
        dest.writeInt(priority);
        dest.writeString(militime);
    }
}
