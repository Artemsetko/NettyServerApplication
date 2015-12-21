package ua.setko.server;

/**
 * @Author Artem Setko on 16.12.15.
 *
 * The Server Class
 */
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {

    private final int port;

    public Server(int port) {
        this.port = port;
    }

    private EventLoopGroup parentGroup;
    private EventLoopGroup childGroup;

    public void start() throws Exception {
        /*
         *The  NioEventLoopGroup instance is created and assigned to handle the event 
         processing, such as creating new connections, receiving data, writing data, and so on. 
         */
        parentGroup = new NioEventLoopGroup(1);
        childGroup = new NioEventLoopGroup(2);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ServerInitializer());
            Channel channel = bootstrap.bind(port).sync().channel();
            channel.closeFuture().sync();
        } catch (InterruptedException ex) {

        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }

    public void stop() {
        if (parentGroup != null && childGroup != null) {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
            while (!parentGroup.isShutdown() && !childGroup.isShutdown()) {
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                }
            }
        }
    }
}
