package serverSideMP;

import serverSide.ClientControl;
import serverSide.Server;

public class ServerMP extends Server {
    @Override
    public int getPort() {
        return 0;
    }

    @Override
    public void ready(ClientControl clientControl) {

    }

    @Override
    public void sendToOtherClients(String s, ClientControl cc) {

    }

    @Override
    public void broadcast(String s) {

    }

    @Override
    public int getNOC() {
        return 0;
    }
}
