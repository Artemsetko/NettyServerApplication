package ua.setko.UriHandlers;

/**
 * @Author Artem Setko on 16.12.15.
 */
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.CharsetUtil;
import ua.setko.statisticsHandlers.StatisticsHandler;
import ua.setko.util.ConnectionNodeUnit;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class StatisticsUriHandler implements UriHandlerInterface {

    //Generated stats html page
    @Override
    public FullHttpResponse process(String uri) {
        final StringBuilder buff = new StringBuilder();

        buff.append("<!DOCTYPE html><html><head>");
        buff.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" />\n");
        buff.append("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css\">\n");
        buff.append("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css\">\n");
        buff.append("<style type=\"text/css\">\n"
                + "   h4 { \n"
                + "    font-size: 120%; \n"
                + "    font-family: Verdana, Arial, Helvetica, sans-serif; \n"
                + "    color: #333366;\n"
                + "    font-weight: bold"
                + "   }\n"
                + "   caption {\n"
                + "    text-align:center;\n"
                + "   }\n"
                + "   table {"
                + "     width: 40%; "
                + "     background: #778899; "
                + "     border: 2px solid black; "
                + "     color: white "
                + "   }\n"
                + "  </style>");
        buff.append("</head>");

        buff.append("<body>");
        buff.append("<div style=\"margin: 2%  \">");
        buff.append("<h1>-=Server Stats=-</h1>");

        // The total number of requests:
        buff.append("<h4>The total number of requests: ").append(StatisticsHandler.getTotalConnectionsCounter()).append("</h4>");
        // The number of active connections:
        buff.append("<h4>The number of active connections: ").append(StatisticsHandler.getActiveConnectionsCounter()).append("</h4>");

        // Unique Requests per one IP table
        buff.append("<table class=\"table table-bordered table-condensed\"><tbody>");
        buff.append("<caption><h4>The number of unique requests per IP: </h4></caption>");
        buff.append("<tr><th>").append("IP").append("</th><th>").append("Requests").append("</th></tr>");
        StatisticsHandler.getRequestsCounter().entrySet().stream().map((pair) -> {
            buff.append("<tr><td>");
            buff.append(pair.getKey());
            return pair;
        }).map((pair) -> {
            buff.append("</td><td>");
            buff.append(pair.getValue().getCountOfUniqueRequests());
            return pair;
        }).forEach((_item) -> {
            buff.append("</td></tr>");
        });
        buff.append("</tbody></table>");

        //Counter of the requests per 1 IP

        buff.append("<table class=\"table table-bordered table-condensed\"><tbody>");
        buff.append("<caption><h4>IP requests counter:</h4></caption>");
        buff.append("<tr><th>").append("IP").append("</th><th>").append("Requests")
                .append("</th><th>").append("Last request").append("</th></tr>");
        StatisticsHandler.getRequestsCounter().entrySet().stream().map((pair) -> {
            buff.append("<tr><td>");
            buff.append(pair.getKey());
            return pair;
        }).map((pair) -> {
            buff.append("</td><td>");
            buff.append(pair.getValue().getConnectionsCounter());
            return pair;
        }).map((pair) -> {
            buff.append("</td><td>");
            buff.append(pair.getValue().getLastConnectionDate());
            return pair;
        }).forEach((_item) -> {
            buff.append("</td></tr>");
        });
        buff.append("</tbody></table>");

        //Counter of the redirection per URL;
        buff.append("<table class=\"table table-bordered table-condensed\"><tbody>");
        buff.append("<caption><h4>URL redirection counter:</h4></caption>");
        buff.append("<tr><th class=\"col-md-4\"> URL ")
                .append("</th><th class=\"col-md-1\">").append("Count").append("</th></tr>");
        StatisticsHandler.getRedirectionPerURL().entrySet().stream().map((pair) -> {
            buff.append("<tr><td>");
            buff.append(pair.getKey());
            return pair;
        }).map((pair) -> {
            buff.append("</td><td>");
            buff.append(pair.getValue());
            return pair;
        }).forEach((_item) -> {
            buff.append("</td></tr>");
        });
        buff.append("</tbody></table>");

        //Connections log
        //buff.append("<h4>Connections log :</h4>");
        buff.append("<table class=\"table table-bordered table-condensed\"><tbody>");
        buff.append("<caption><h4>Connections log:</h4></caption>");
        buff.append("<tr><th class=\"col-md-1\"> src_ip")
                .append("</th><th class=\"col-md-3\">").append("URI ")
                .append("</th><th class=\"col-md-3\">").append("timestamp ")
                .append("</th><th class=\"col-md-1\">").append("sent_bytes ")
                .append("</th><th class=\"col-md-1\">").append("received_bytes ")
                .append("</th><th class=\"col-md-1\">").append("speed (Bytes/Sec)")
                .append("</th></tr>");
        buff.append("</tbody>");
        for (ConnectionNodeUnit c : StatisticsHandler.getLog()) {
            buff.append("<tr><td>");
            buff.append(c.getIP()).append("</td><td>");
            buff.append(c.getURI()).append("</td><td>");
            buff.append(c.getTimeStamp()).append("</td><td>");
            buff.append(c.getSentBytes()).append("</td><td>");
            buff.append(c.getReceivedBytes()).append("</td><td>");
            buff.append(c.getSpeed()).append("</td></tr>");
        }
        buff.append("</tbody></table></div></body></html>");

        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1,
                OK,
                Unpooled.copiedBuffer(buff.toString(), CharsetUtil.UTF_8)
        );
        response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
        return response;
    }
}
