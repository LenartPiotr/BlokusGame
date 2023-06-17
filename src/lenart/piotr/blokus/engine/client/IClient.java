package lenart.piotr.blokus.engine.client;

import lenart.piotr.blokus.basic.ICallback0;
import lenart.piotr.blokus.basic.ICallback1;

public interface IClient {
    public void on(String key, ICallback1<Object> callback);
    public void off(String key);
    public void invoke(String key, Object obj);
    public boolean isActive();
    public void onDisconnect(ICallback0 callback);
}
