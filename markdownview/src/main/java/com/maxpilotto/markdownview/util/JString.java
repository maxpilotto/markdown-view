package com.maxpilotto.markdownview.util;

/**
 * Improved version of the class String
 * Created on 17/06/2019 at 17:12
 *
 * @author Max Pilotto (github.com/maxpilotto, maxpilotto.com)
 */
public class JString {
    private String value;

    public JString() {
        this.value = null;
    }

    public void set(String value){
        this.value = value;
    }

    public void append(String value){
        this.value += value;
    }

    public String str(){
        return value;
    }
}
