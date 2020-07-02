package koreniak.kingsluck.client;

public class ClientFactory {
    private ClientI webSocketClient = new WebSocketClientEndpoint();

    public ClientI getDefaultClient() {
        return webSocketClient;
    }

    public ClientI getClient(ClientType type) {
        switch (type) {
            default: {
                return getDefaultClient();
            }
        }
    }

    private static ClientFactory instance;

    public static synchronized ClientFactory getInstance() {
        if (instance == null) {
            instance = new ClientFactory();
        }

        return instance;
    }

    private ClientFactory() {

    }
}
