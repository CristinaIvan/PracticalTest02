package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

public class CommunicationThread extends Thread{
    private ServerThread serverThread;
    private Socket socket;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }
    public void run() {
        if (socket == null) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
            return;
        }
        try {
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Buffered Reader / Print Writer are null!");
                return;
            }
            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (hour/minute!");
            String request = bufferedReader.readLine();
            if (request == null || request.isEmpty()) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error receiving parameters from client (request)!");
                return;
            }
            HashMap<String, AlarmInformation> data = serverThread.getData();
            String ip = socket.getInetAddress().toString();
            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Received " + request);
            if (request.substring(0, 3).equals("set")) {
                if (data.containsKey(ip)) {
                    Log.i(Constants.TAG, "[COMMUNICATION THREAD] Ovverwrite alarm...");
                }
                String[] vals = request.split(",");
                data.put(ip, new AlarmInformation(vals[1], vals[2]));
                Log.i(Constants.TAG, "[COMMUNICATION THREAD] Put alarm " + vals[1] + "," + vals[2] + " for " + ip);
                printWriter.println("Alarm set");
                printWriter.flush();
            } else if (request.equals("reset")) {
                if (data.containsKey(ip)) {
                    Log.i(Constants.TAG, "[COMMUNICATION THREAD] Delete alarm...");
                    data.remove(ip);
                    printWriter.println("Alarm deleted");
                    printWriter.flush();
                } else {
                    printWriter.println("No alarm for this ip");
                    printWriter.flush();
                }
            } else if (request.equals("poll")) {
                if (data.containsKey(ip)) {
                    Log.i(Constants.TAG, "[COMMUNICATION THREAD] Verify if alarm is active...");
                    Socket new_socket = new Socket(Constants.WEB_SERVICE_ADDRESS, Constants.WEB_SERVICE_PORT);
                    BufferedReader new_bufferedReader = Utilities.getReader(new_socket);
                    String dayTimeProtocol = new_bufferedReader.readLine();
                    Log.d(Constants.TAG, "[COMMUNICATION THREAD] The server returned: " + dayTimeProtocol);

                } else {
                    printWriter.println("No alarm for this ip");
                    printWriter.flush();
                }
            }
        }catch (IOException ioException) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
//        } catch (JSONException jsonException) {
//            Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + jsonException.getMessage());
//            if (Constants.DEBUG) {
//                jsonException.printStackTrace();
//            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }
}
