package ua.setko.statisticsHandlers;

import ua.setko.UriHandlers.HelloUriHandler;
import ua.setko.UriHandlers.UriHandlerInterface;
import ua.setko.UriHandlers.RedirectUriHandler;
import ua.setko.UriHandlers.NotFoundUriHandler;
import ua.setko.UriHandlers.StatisticsUriHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import ua.setko.util.ConnectionNodeUnit;
import java.net.InetSocketAddress;
import static io.netty.channel.ChannelHandler.Sharable;

/**
 * @Author Artem Setko on 16.12.15.
 * <p/>
 * Main FullHttpRequests handler
 * <p/>
 */
@Sharable
public class StatisticsHttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final NotFoundUriHandler NOT_FOUND_URI_HANDLER = new NotFoundUriHandler();
    private static final HelloUriHandler HELLO_URI_HANDLER = new HelloUriHandler();
    private static final RedirectUriHandler REDIRECT_URI_HANDLER = new RedirectUriHandler();
    private static final StatisticsUriHandler STATUS_URI_HANDLER = new StatisticsUriHandler();

   /*
    * channelRead0 is called for each message of type FullHttpRequest
    * ctx - the ChannelHandlerContext which this SimpleChannelInboundHandler belongs to
    * fullHttpRequest - Combinate the HttpRequest and FullHttpMessage, so the request is a complete HTTP request.
    */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest) {
        long time = System.nanoTime();

        String requestIP = (((InetSocketAddress) ctx.channel().remoteAddress()).getHostString());
        String URI = fullHttpRequest.getUri();

        FullHttpResponse response = writeResponse(URI);
        //close the connection immediately because no more requests can be sent from the browser
        ctx.write(response).addListener(ChannelFutureListener.CLOSE);

        StatisticsHandler.addRequestsCounter(requestIP, URI);
        StatisticsHandler.addLogUnit(formStats(requestIP, URI, time, fullHttpRequest, response));
    }

    private ConnectionNodeUnit formStats(String requestIP, String URI, long time, FullHttpRequest fullHttpRequest, FullHttpResponse response) {
        //to form statistics
        ByteBuf buffer = Unpooled.copiedBuffer(fullHttpRequest.toString().getBytes());
        int receivedBytes = buffer.readableBytes() + fullHttpRequest.content().readableBytes();
        int sentBytes = response.content().writerIndex();
        long time0 = System.nanoTime() - time;
        double time1 = time0 / (double) 1000000000;
        long speed = Math.round((sentBytes + receivedBytes) / time1);
        ConnectionNodeUnit logUnit = new ConnectionNodeUnit(requestIP, URI, sentBytes, receivedBytes, speed);
        return logUnit;
    }

    //Define required handler
    private FullHttpResponse writeResponse(String uri) {
        UriHandlerInterface handler;
        if (uri.matches("/redirect\\?url=\\S*")) {
            handler = REDIRECT_URI_HANDLER;
        } else {
            switch (uri) {
                case "/hello":
                    handler = HELLO_URI_HANDLER;
                    break;
                case "/status":
                    handler = STATUS_URI_HANDLER;
                    break;
                default:
                    handler = NOT_FOUND_URI_HANDLER;
            }
        }

        return handler.process(uri);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //Request to flush all pending messages via this ChannelOutboundInvoker.
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        /*
        * Request to close the Channel and notify the ChannelFuture once the operation completes, 
        * either because the operation was successful or because of an error. 
        * After it is closed it is not possible to reuse it again.
        */
        ctx.close();
    }
}
