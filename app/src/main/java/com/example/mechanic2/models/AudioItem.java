package com.example.mechanic2.models;

import java.io.Serializable;

public class AudioItem  implements Serializable {
    private Integer audioResId;
    private String audioAddress;

    public Integer getAudioResId() {
        return audioResId;
    }

    public void setAudioResId(Integer audioResId) {
        this.audioResId = audioResId;
    }

    public String getAudioAddress() {
        return audioAddress;
    }

    public void setAudioAddress(String audioAddress) {
        this.audioAddress = audioAddress;
    }
}
