package org.flowerplatform.js_client.server.test;

import org.codehaus.jackson.annotate.JsonTypeInfo;

//@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
public class TestBean {

    private String name = "Simple Test Bean";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
