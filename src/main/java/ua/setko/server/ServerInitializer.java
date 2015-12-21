package ua.setko.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import ua.setko.statisticsHandlers.StatisticsHandler;
import ua.setko.statisticsHandlers.StatisticsHttpHandler;

/**
 *
 * @author АРТЁМ
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    private final StatisticsHandler statisticsHandler = new StatisticsHandler(0);
    private final StatisticsHttpHandler httpHandler = new StatisticsHttpHandler();

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        /*
         * A list of ChannelHandlers which handles or intercepts inbound events and outbound operations of a Channel. 
         * ChannelPipeline implements an advanced form of the Intercepting Filter pattern 
         * to give a user full control over how an event is handled and how the ChannelHandlers in a pipeline interact with each other.
         */
        ChannelPipeline p = ch.pipeline();

        //A combination of HttpRequestDecoder and HttpResponseEncoder which enables easier server side HTTP implementation.
        p.addLast("codec", new HttpServerCodec());
        // It is useful when you don't want to take care of HTTP messages whose transfer encoding is 'chunked'
        p.addLast("aggregator", new HttpObjectAggregator(512 * 1024));
        p.addLast("statisticsHandler", statisticsHandler);
        p.addLast("handler", httpHandler);
    }
}
