package com.jike.wlw.base;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @title: Parameter
 * @Author RS
 * @Date: 2023/3/15 10:35
 * @Version 1.0
 */
@Getter
@Setter
public class Parameter implements Serializable {

    private static final long serialVersionUID = 4651952866543862053L;

    public Parameter() {
        super();
    }

    public Parameter(String name, String value) {
        this();
        this.name = name;
        this.value = value;
    }

    private String name;
    private String value;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Parameter other = (Parameter) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }
}

