package ua.setko.UriHandlers;

/**
 * @Author Artem Setko on 16.12.15.
 */
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.CharsetUtil;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HelloUriHandler implements UriHandlerInterface {

    private static final int TIMEOUT = 10000;
    private static final String ANS_HELLO_WORLD = "<!DOCTYPE html><html><head>\n"
            + "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n"
            + "  <title>Hello World page</title>\n"
            + " </head><body><h1>Hello World</h1></body></html>";

    /**
     *
     * @param uri
     * @return FullHttpResponse
     */
    @Override
    public FullHttpResponse process(String uri) {
        try {
            Thread.sleep(TIMEOUT);
        } catch (InterruptedException e) {
        }
        /**
         *  DefaultFullHttpResponse - Default implementation of a
         *  FullHttpResponse. HTTP_1_1 - The version of HTTP or its derived
         * protocols, such as RTSP and ICAP. 
         *  Response status - 200 OK
         *  io.netty.buffer - Abstraction of a byte buffer - the fundamental data
         * structure to represent a low-level binary and text message.
         *  copiedBuffer(CharSequence string, Charset charset) - Creates a new
         * big-endian buffer whose content is the specified string encoded in
         * the specified charset.
         */
        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1, OK, Unpooled.copiedBuffer(ANS_HELLO_WORLD, CharsetUtil.UTF_8)
        );
        return response;
    }
}
