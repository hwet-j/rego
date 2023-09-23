package com.clipclap.rego.model.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class response {

    public static class Header {
        private String resultCode;
        private String resultMsg;

        // getters and setters
    }

    public static class Item {
        private String cityChn;
        private String cityCode;
        private String cityEng;
        private String cityJpn;
        private String cityKor;

        // getters and setters
    }

    public static class Body {
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "item")
        private List<Item> items;
        private int numOfRows;
        private int pageNo;
        private int totalCount;

        // getters and setters
    }

    private Header header;
    private Body body;

    // getters and setters
}
