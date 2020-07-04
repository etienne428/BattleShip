package serverSide;

public abstract class Server extends Thread {

    public Server(int port) {

    }

    protected Server() {
    }

    public abstract int getPort();

    public abstract void ready(ClientControl clientControl);

    public abstract void sendToOtherClients(String s, ClientControl cc);

    public abstract void broadcast(String s);

    public abstract int getNOC();
}
