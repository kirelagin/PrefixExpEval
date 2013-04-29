package itmo.evaluator;

import java.net.MalformedURLException;
import java.util.ArrayList;

public class WorkersLauncher {
    public static void main(String[] args) {
        if (args.length != 5) {
            System.out.println("Required parameters: <url> <service_host> <service_name> <pworkers_count> <mworkers_count>");
            return;
        }
        String url = args[0];
        String serviceHost = args[1];
        String serviceName = args[2];
        int pWorkersCount = Integer.parseInt(args[3]);
        int mWorkersCount = Integer.parseInt(args[4]);

        ArrayList<Thread> threads = new ArrayList<Thread>();
        try {
            for (int i = 0; i < pWorkersCount; ++i) {
                threads.add(new Thread(new PWorker(url, serviceHost, serviceName)));
            }
            for (int i = 0; i < mWorkersCount; ++i) {
                threads.add(new Thread(new MWorker(url, serviceHost, serviceName)));
            }
            for (Thread thread : threads) {
                thread.start();
            }
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
