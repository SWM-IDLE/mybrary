package kr.mybrary.bookservice.mybook.persistence.tag;

import lombok.Getter;

@Getter
public enum QuoteColor {

    LAVENDER("#6F5DDE"),
    SHAMROCK_GREEN("#2CCD80"),
    AMBER("#FFB525"),
    PINK_SALMON("#FA9993"),
    ORCHID("#EC6DD0");

    private String hexCode;

    QuoteColor(String hexCode) {
        this.hexCode = hexCode;
    }

}
