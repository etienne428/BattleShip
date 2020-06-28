package clientSide;

import java.io.*;

public class Listener extends Thread {
    private final Client client;
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private final BufferedReader bufferedReader;
    private final boolean serverListener;

    public Listener(Client client, OutputStream outputStream) {
        this.client = client;

        serverListener = false;
        this.inputStream = System.in;
        this.outputStream = outputStream;
        bufferedReader = new BufferedReader((new InputStreamReader(inputStream)));
    }

    public Listener(Client client, InputStream inputStream, OutputStream outputStream) {
        this.client = client;

        serverListener = true;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
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
