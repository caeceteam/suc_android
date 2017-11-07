package com.example.suc.suc_android_solution.Maps;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.math.BigDecimal;

/**
 * Created by efridman on 6/11/17.
 */

public class MapMarkerViewModel implements ClusterItem, Parcelable {

    public static final String CENTER_MARKER = "center_marker";

    private final LatLng position;

    @DrawableRes
    private int pinId;

    @DrawableRes
    private int iconId;
    private String title;

    private String description;
    private BigDecimal price;
    private String priceDescription;
    private String action;
    private String alternativeName;

    /**
     * Reference source object for action purposes
     * Object used to populate this model, and needed later in the flow
     */
    private Parcelable ref;

    public MapMarkerViewModel(double latitude, double longitude) {
        this.position = new LatLng(latitude, longitude);
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    /**
     * @return the pin resource to use in the map
     */
    @DrawableRes
    public int getPinId() {
        return pinId;
    }

    /**
     * Set the icon drawable to show for this marker
     *
     * @param pinId the icon drawable id to render in the map
     */
    public void setPinId(@DrawableRes int pinId) {
        this.pinId = pinId;
    }

    /**
     * @return an icon to show in the description of the pin
     */
    @DrawableRes
    public int getIconId() {
        return iconId;
    }

    /**
     * Set the icon to use in the marker description
     *
     * @param iconId the icon drawable
     */
    public void setIconId(@DrawableRes int iconId) {
        this.iconId = iconId;
    }

    /**
     * @return the title to use in the description
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for {@link #title}
     *
     * @param title the title to display
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets {@link #title} for center marker
     */
    public void setCenterTitle() {
        this.title = CENTER_MARKER;
    }

    /**
     * Determinates if this is a center marker model
     */
    public boolean isCenterMarker() {
        return title.equals(CENTER_MARKER);
    }

    /**
     * @return the text to use as description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for {@link #description}
     *
     * @param description the description to display
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the price value
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Setter for {@link #price}
     *
     * @param price the price to display
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * @return the price description
     */
    public String getPriceDescription() {
        return priceDescription;
    }

    /**
     * Setter for {@link #priceDescription}
     *
     * @param priceDescription the price description
     */
    public void setPriceDescription(String priceDescription) {
        this.priceDescription = priceDescription;
    }

    /**
     * @return the text to use as action
     */
    public String getAction() {
        return action;
    }

    /**
     * Setter for {@link #action}
     *
     * @param action the action to display
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * @return the object the reference this model
     */
    public Parcelable getRef() {
        return ref;
    }

    /**
     * Setter for {@link #ref}
     *
     * @param ref the object referenced
     */
    public void setRef(Parcelable ref) {
        this.ref = ref;
    }

    /**
     * @return the name to use in the alternative name
     */
    public String getAlternativeName() {
        return alternativeName;
    }

    /**
     * Setter for {@link #alternativeName}
     *
     * @param alternativeName the alternative name to display
     */
    public void setAlternativeName(String alternativeName) {
        this.alternativeName = alternativeName;
    }

    /*********
     * Parcelable Implementation
     **********/
    protected MapMarkerViewModel(Parcel parcel) {
        // Recover from parcel
        position = parcel.readParcelable(LatLng.class.getClassLoader());
        pinId = parcel.readInt();
        iconId = parcel.readInt();
        title = parcel.readString();
        description = parcel.readString();
        price = (BigDecimal) parcel.readSerializable();
        action = parcel.readString();
        alternativeName = parcel.readString();
    }

    public static final Creator<MapMarkerViewModel> CREATOR = new Creator<MapMarkerViewModel>() {
        @Override
        public MapMarkerViewModel createFromParcel(Parcel parcel) {
            return new MapMarkerViewModel(parcel);
        }
        @Override
        public MapMarkerViewModel[] newArray(int size) {
            return new MapMarkerViewModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(position, flags);
        dest.writeInt(pinId);
        dest.writeInt(iconId);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeSerializable(price);
        dest.writeString(action);
        dest.writeString(alternativeName);
        dest.writeParcelable(ref, flags);
    }
    /*********************************************/

}
