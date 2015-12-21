package ua.setko.statisticsHandlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;
import ua.setko.util.ConnectionNodeUnit;
import ua.setko.util.ConcurrentLogQueue;
import ua.setko.util.RequestsCounter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static io.netty.channel.ChannelHandler.Sharable;

/**
 * @Author Artem Setko on 16.12.15.
 * <p/>
 * Statistics Handler
 */
@Sharable
public class StatisticsHandler extends ChannelTrafficShapingHandler {

    private static final AtomicInteger totalConnectionsCounter = new AtomicInteger(0);
    private static final AtomicInteger activeConnectionsCounter = new AtomicInteger(0);

    private static final ConcurrentHashMap<String, RequestsCounter> requestsCounter = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Integer> redirectionPerURL = new ConcurrentHashMap<>();

    private static final ConcurrentLogQueue<ConnectionNodeUnit> log = new ConcurrentLogQueue<>();

    public StatisticsHandler(long checkInterval) {
        super(checkInterval);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        totalConnectionsCounter.getAndIncrement();
        activeConnectionsCounter.getAndIncrement();
        super.channelRead(ctx, msg);
    }


    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        activeConnectionsCounter.getAndDecrement();
        super.handlerRemoved(ctx);
        
    }

    public static void addLogUnit(ConnectionNodeUnit unit) {
        log.add(unit);
    }

    public static void addURLRedirection(String url) {
        synchronized (redirectionPerURL) {
            if (!redirectionPerURL.containsKey(url)) {
                redirectionPerURL.put(url, 1);
            } else {
                redirectionPerURL.put(url, redirectionPerURL.get(url) + 1);
            }
        }
    }

    public static void addRequestsCounter(String requestIP, String URI) {
        RequestsCounter c;
        synchronized (requestsCounter) {
            if (!requestsCounter.containsKey(requestIP)) {
                c = new RequestsCounter(requestIP, URI);
                requestsCounter.put(requestIP, c);
            } else {
                c = requestsCounter.get(requestIP).addRequest(URI);
                requestsCounter.put(requestIP, c);
            }
        }
    }

    public static int getTotalConnectionsCounter() {
        return totalConnectionsCounter.get();
    }

    public static int getActiveConnectionsCounter() {
        return activeConnectionsCounter.get();
    }

    public static ConcurrentHashMap<String, RequestsCounter> getRequestsCounter() {
        return requestsCounter;
    }

    public static ConcurrentHashMap<String, Integer> getRedirectionPerURL() {
        return redirectionPerURL;
    }

    public static ConcurrentLogQueue<ConnectionNodeUnit> getLog() {
        return log;
    }
}
