public class ThreadHandler {
    static Object lock = new Object();
    static boolean finished = false;

    //sets the finished boolean to true
    static void finishThreads()
    {
        synchronized(lock)
        {
            finished = true;
        }
    }

    //returns the status of finished boolean
    static boolean isFinished()
    {
        boolean result;
        synchronized(lock)
        {
            result = finished;
        }
        return result;
    }
}
