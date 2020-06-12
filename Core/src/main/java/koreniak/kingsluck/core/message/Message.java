package koreniak.kingsluck.core.message;

public class Message {
    private Object object;
    private MessageType type;

    public Message(Object object, MessageType type) {
        this.object = object;
        this.type = type;
    }

    public Object getObject() {
        return object;
    }

    public MessageType getType() {
        return type;
    }
}
