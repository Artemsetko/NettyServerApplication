package ua.setko.UriHandlers;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.util.CharsetUtil;

/**
 *
 * @author АРТЁМ
 */
public class NotFoundUriHandler implements UriHandlerInterface {
    private static final String ANS_NOT_FOUND = "<!DOCTYPE html><html><body><h2>404 NOT FOUND!</h2></body></html>";

    @Override
    public FullHttpResponse process(String uri) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1, NOT_FOUND, Unpooled.copiedBuffer(ANS_NOT_FOUND, CharsetUtil.UTF_8)
        );
        response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
        return response;
    }
}
