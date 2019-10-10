package com.shollmann.events.ui.viewholder;

import android.content.Intent;

public class CatViewHolder {
    private String name;

    private Integer pic;

    public CatViewHolder(String n) {
        name = n;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Integer getPic() {
        return pic;
    }

    public void setPic(Integer pic) {
        this.pic = pic;
    }
}
