/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.setko.UriHandlers;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.FOUND;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.util.CharsetUtil;
import ua.setko.statisticsHandlers.StatisticsHandler;

/**
 *
 * @author АРТЁМ
 */
public class RedirectUriHandler implements UriHandlerInterface {

    @Override
    public FullHttpResponse process(String uri) {
        String url = uri.substring(uri.indexOf("=") + 1, uri.length());

        StatisticsHandler.addURLRedirection(url);
        // \\S*   – любое кол-во  не пробельных символов
        if (!url.matches("http://\\S*")) {
            url = "http://" + url;
        }

        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1, FOUND, Unpooled.copiedBuffer(url, CharsetUtil.UTF_8));
        //The Location response-header field is used to redirect the recipient to a location other than the Request-URI for completion.
        response.headers().set(HttpHeaders.Names.LOCATION, url);
        response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
       
        return response;
    }
}
