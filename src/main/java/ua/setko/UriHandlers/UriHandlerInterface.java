package ua.setko.UriHandlers;

/**
 * @Author Artem Setko on 16.12.15.
 */
import io.netty.handler.codec.http.FullHttpResponse;

public interface UriHandlerInterface{
    /**
     *FullHttpResponse - Combination of a HttpResponse and FullHttpMessage. So it represent a complete http response.
     * @param uri
     * @return
     */
    FullHttpResponse process(String uri);


}
