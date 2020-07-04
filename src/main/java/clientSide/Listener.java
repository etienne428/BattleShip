package clientSide;

import java.io.*;

public class Listener extends Thread {
    private final Client client;
    private final InputStream inputStream;
    private OutputStream outputStream = null;
    private final BufferedReader bufferedReader;
    private final boolean serverListener;

    /**
     * Console listener.
     * Read the input from console
     *
     * @param client         the client, in case of QUIT
     * @param outputStream   the target of the messages
     */
    public Listener(Client client, OutputStream outputStream) {
        this.client = client;

        serverListener = false;
        this.inputStream = System.in;
        this.outputStream = outputStream;
        bufferedReader = new BufferedReader((new InputStreamReader(inputStream)));
    }

    /**
     * Server listener.
     * Read the input from server and send them to the client
     *
     * @param client        the client
     * @param inputStream   the inputStream (the server)
     */
    public Listener(Client client, InputStream inputStream) {
        this.client = client;

        serverListener = true;
        this.inputStream = inputStream;
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    }

    @Override
    public void run() {
        while (true) {
            try {
                String s = bufferedReader.readLine();
                if (serverListener) {
                    if (s.startsWith("QUIT")) {
                        Client.quit();
                        break;
                    }
                    client.performAction(s);
                } else {
                    outputStream.write(s.getBytes());
                    outputStream.write("\r\n".getBytes());
                    if (s.startsWith("QUIT")) {
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
