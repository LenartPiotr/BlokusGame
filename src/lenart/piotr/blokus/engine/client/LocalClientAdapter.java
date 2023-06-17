package lenart.piotr.blokus.engine.client;

import lenart.piotr.blokus.basic.ICallback0;
import lenart.piotr.blokus.basic.ICallback1;
import lenart.piotr.blokus.network.Client;

import java.util.HashMap;
import java.util.Map;

public class LocalClientAdapter {

    private final Client clientToHost;
    private final Client hostToClient;

    public LocalClientAdapter(String nick) {
        clientToHost = new Client();
        hostToClient = new Client();

        clientToHost.setDestination(hostToClient);
        hostToClient.setDestination(clientToHost);

        hostToClient.on("getName", data -> {
            hostToClient.invoke("setName", nick);
        });
    }

    public IClient getClient() {
        return clientToHost;
    }

    public IClient getHost() {
        return hostToClient;
    }

    public static class Client implements IClient {

        private Client dest;
        private final Map<String, ICallback1<Object>> listeners;

        public Client() {
            listeners = new HashMap<>();
        }

        private void setDestination(Client second) {
            dest = second;
        }

        @Override
        public void on(String key, ICallback1<Object> callback) {
            listeners.put(key, callback);
        }

        @Override
        public void off(String key) {
            listeners.remove(key);
        }

        @Override
        public void invoke(String key, Object obj) {
            dest.run(key, obj);
        }

        public void run(String key, Object obj) {
            if (listeners.containsKey(key)) {
                listeners.get(key).run(obj);
            }
        }

        @Override
        public boolean isActive() {
            return true;
        }

        @Override
        public void onDisconnect(ICallback0 callback) { }
    }
}
