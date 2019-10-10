
package com.shollmann.events.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Address {

//    @SerializedName("address_1")
//    @Expose
//    private String address_1;
//    @SerializedName("address_2")
//    @Expose
//    private String address_2;
//    @SerializedName("city")
//    @Expose
//    private String city;
//    @SerializedName("postal_code")
//    @Expose
//    private String postal_code;
//    @SerializedName("region")
//    @Expose
//    private String region;
//    @SerializedName("localized_address_display")
//    @Expose
//    private String localized_address_display;
//    @SerializedName("localized_area_display")
//    @Expose
//    private String localized_area_display;
    @SerializedName("longitude")
    @Expose
    private String longitude;
//    @SerializedName("localized_multi_line_address_display")
//    @Expose
//    private String localized_multi_line_address_display;
    @SerializedName("latitude")
    @Expose
    private String latitude;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

}
