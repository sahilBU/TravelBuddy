
package com.shollmann.events.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;

public class EventTM {

    @SerializedName("name")
    @Expose
    private Text name;
    @SerializedName("dates")
    @Expose
    private Dates dates;
    @SerializedName("keyword")
    @Expose
    private String attractionId;
    @SerializedName("attractionId")
    @Expose
    private String venueId;
    @SerializedName("venueId")
    @Expose
    private String start;
    @SerializedName("start")
    @Expose
    private Start chicken;
    @SerializedName("end")
    @Expose
    private End end;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("changed")
    @Expose
    private String changed;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("capacity_is_custom")
    @Expose
    private Boolean capacityIsCustom;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("listed")
    @Expose
    private Boolean listed;
    @SerializedName("shareable")
    @Expose
    private Boolean shareable;
    @SerializedName("online_event")
    @Expose
    private Boolean onlineEvent;
    @SerializedName("tx_time_limit")
    @Expose
    private Integer txTimeLimit;
    @SerializedName("hide_start_date")
    @Expose
    private Boolean hideStartDate;
    @SerializedName("hide_end_date")
    @Expose
    private Boolean hideEndDate;
    @SerializedName("locale")
    @Expose
    private String locale;
    @SerializedName("is_locked")
    @Expose
    private Boolean isLocked;
    @SerializedName("privacy_setting")
    @Expose
    private String privacySetting;
    @SerializedName("is_series")
    @Expose
    private Boolean isSeries;
    @SerializedName("is_series_parent")
    @Expose
    private Boolean isSeriesParent;
    @SerializedName("is_reserved_seating")
    @Expose
    private Boolean isReservedSeating;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("is_free")
    @Expose
    private Boolean isFree;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("images")
    @Expose
    private Array images;
    @SerializedName("event")
    @Expose
    private String event;
    @SerializedName("category_id")
    @Expose
    private String id;
    @SerializedName("id")
    @Expose
    private String subcategoryId;
    @SerializedName("description")
    @Expose
    private Text description;
    @SerializedName("resource_uri")
    @Expose
    private String resourceUri;
    @SerializedName("logo")
    @Expose
    private Logo logo;

    public Text getName() {
        return name;
    }

    public void setName(Text name) {
        this.name = name;
    }

    public Text getDescription() {
        return description;
    }

    public void setDescription(Text description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
//
//    public String getVanityUrl() {
//        return vanityUrl;
//    }
//
//    public void setVanityUrl(String vanityUrl) {
//        this.vanityUrl = vanityUrl;
//    }
//
//    public Start getStart() {
//        return start;
//    }
//
//    public void setStart(Start start) {
//        this.start = start;
//    }

    public End getEnd() {
        return end;
    }

    public void setEnd(End end) {
        this.end = end;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getChanged() {
        return changed;
    }

    public void setChanged(String changed) {
        this.changed = changed;
    }

    public String getType() {
        return type;
    }

    public void setType(Integer capacity) {
        this.type = type;
    }

    public Boolean getCapacityIsCustom() {
        return capacityIsCustom;
    }

    public void setCapacityIsCustom(Boolean capacityIsCustom) {
        this.capacityIsCustom = capacityIsCustom;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Boolean getListed() {
        return listed;
    }

    public void setListed(Boolean listed) {
        this.listed = listed;
    }

    public Boolean getShareable() {
        return shareable;
    }

    public void setShareable(Boolean shareable) {
        this.shareable = shareable;
    }

    public Boolean getOnlineEvent() {
        return onlineEvent;
    }

    public void setOnlineEvent(Boolean onlineEvent) {
        this.onlineEvent = onlineEvent;
    }

    public Integer getTxTimeLimit() {
        return txTimeLimit;
    }

    public void setTxTimeLimit(Integer txTimeLimit) {
        this.txTimeLimit = txTimeLimit;
    }

    public Boolean getHideStartDate() {
        return hideStartDate;
    }

    public void setHideStartDate(Boolean hideStartDate) {
        this.hideStartDate = hideStartDate;
    }

    public Boolean getHideEndDate() {
        return hideEndDate;
    }

    public void setHideEndDate(Boolean hideEndDate) {
        this.hideEndDate = hideEndDate;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public Boolean getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }

    public String getPrivacySetting() {
        return privacySetting;
    }

    public void setPrivacySetting(String privacySetting) {
        this.privacySetting = privacySetting;
    }

    public Boolean getIsSeries() {
        return isSeries;
    }

    public void setIsSeries(Boolean isSeries) {
        this.isSeries = isSeries;
    }

    public Boolean getIsSeriesParent() {
        return isSeriesParent;
    }

    public void setIsSeriesParent(Boolean isSeriesParent) {
        this.isSeriesParent = isSeriesParent;
    }

    public Boolean getIsReservedSeating() {
        return isReservedSeating;
    }

    public void setIsReservedSeating(Boolean isReservedSeating) {
        this.isReservedSeating = isReservedSeating;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Boolean getIsFree() {
        return isFree;
    }

    public void setIsFree(Boolean isFree) {
        this.isFree = isFree;
    }

//    public String getLogoId() {
//        return logoId;
//    }
//
//    public void setLogoId(String logoId) {
//        this.logoId = logoId;
//    }
//
//    public String getOrganizerId() {
//        return organizerId;
//    }
//
//    public void setOrganizerId(String organizerId) {
//        this.organizerId = organizerId;
//    }
//
//    public String getVenueId() {
//        return venueId;
//    }
//
//    public void setVenueId(String venueId) {
//        this.venueId = venueId;
//    }
//
//    public String getCategoryId() {
//        return categoryId;
//    }
//
//    public void setCategoryId(String categoryId) {
//        this.categoryId = categoryId;
//    }
//
//    public String getSubcategoryId() {
//        return subcategoryId;
//    }
//
//    public void setSubcategoryId(String subcategoryId) {
//        this.subcategoryId = subcategoryId;
//    }
//
//    public String getFormatId() {
//        return formatId;
//    }
//
//    public void setFormatId(String formatId) {
//        this.formatId = formatId;
//    }

    public String getResourceUri() {
        return resourceUri;
    }

    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }

    public Logo getLogo() {
        return logo;
    }

    public void setLogo(Logo logo) {
        this.logo = logo;
    }

}
