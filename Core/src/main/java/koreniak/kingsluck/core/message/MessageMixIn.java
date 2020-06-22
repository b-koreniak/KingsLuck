package koreniak.kingsluck.core.message;


import com.fasterxml.jackson.annotation.JsonTypeInfo;

public class MessageMixIn {
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
    private Object object;
}
