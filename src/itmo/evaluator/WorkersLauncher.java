package itmo.evaluator;

import java.net.MalformedURLException;
import java.util.ArrayList;

public class WorkersLauncher {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Required parameters: <url> <pworkers_count> <mworkers_count>");
            return;
        }
        String url = args[0];
        int pWorkersCount = Integer.parseInt(args[1]);
        int mWorkersCount = Integer.parseInt(args[2]);

        ArrayList<Thread> threads = new ArrayList<Thread>();
        try {
            for (int i = 0; i < pWorkersCount; ++i) {
                threads.add(new Thread(new PWorker(url)));
            }
            for (int i = 0; i < mWorkersCount; ++i) {
                threads.add(new Thread(new MWorker(url)));
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
