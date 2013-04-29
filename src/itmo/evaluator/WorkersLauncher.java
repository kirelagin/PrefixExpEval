package itmo.evaluator;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

        ExecutorService executor = Executors.newFixedThreadPool(pWorkersCount + mWorkersCount);
        try {
            for (int i = 0; i < pWorkersCount; ++i) {
                executor.execute(new PWorker(url, serviceHost, serviceName));
            }
            for (int i = 0; i < mWorkersCount; ++i) {
                executor.execute(new MWorker(url, serviceHost, serviceName));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
