package com.yjz.discard.server;

import com.yjz.discard.handler.DiscardServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 丢弃发来的任何消息
 */
public class DiscardServer {
    private int port;

    public DiscardServer(int port) {
        this.port = port;
    }

    /**
     * NioEventLoopGroup是一个处理 I/O 操作的多线程事件循环。NettyEventLoopGroup为不同类型的传输提供了不同的实现。我们在此示例中实现了一个服务器端应用程序，因此NioEventLoopGroup将使用两个。第一个，通常称为“老板”，接受传入连接。第二个，通常称为“worker”，一旦老板接受连接并向 worker 注册接受的连接，就会处理已接受连接的流量。使用了多少线程以及它们如何映射到创建Channel的线程取决于EventLoopGroup实现，甚至可以通过构造函数进行配置。
     * ServerBootstrap是一个设置服务器的辅助类。您可以直接使用 a 来设置服务器Channel。但是请注意，这是一个繁琐的过程，在大多数情况下您不需要这样做。
     * 在这里，我们指定使用NioServerSocketChannel用于实例化新Channel接受传入连接的类。
     * 此处指定的处理程序将始终由新接受的Channel. 是一个特殊的ChannelInitializer处理程序，旨在帮助用户配置一个新的Channel. 您很可能希望通过添加一些处理程序来配置ChannelPipeline新Channel的，例如DiscardServerHandler实现您的网络应用程序。随着应用程序变得复杂，您可能会向管道添加更多处理程序，并最终将这个匿名类提取到顶级类中。
     * 您还可以设置特定于Channel实现的参数。我们正在编写一个 TCP/IP 服务器，因此我们可以设置套接字选项，例如tcpNoDelay和keepAlive。请参阅 apidocsChannelOption和具体实现以获得有关支持的 sChannelConfig的概述。ChannelOption
     * 你注意到了option()吗childOption()？option()适用于NioServerSocketChannel接受传入连接的。childOption()是为Channelparent 接受的 s ServerChannel，NioSocketChannel在这种情况下。
     * 我们准备好了。剩下的就是绑定到端口并启动服务器。在这里，我们绑定到8080机器中所有NIC（网络接口卡）的端口。您现在可以bind()根据需要多次调用该方法（使用不同的绑定地址。）
     * @throws Exception
     */
    public void run() throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup();//1
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();//2
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)//3
                    .childHandler(new ChannelInitializer<SocketChannel>() {//4
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline().addLast(new DiscardServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG,128)//5
                    .childOption(ChannelOption.SO_KEEPALIVE, true);//6
            ChannelFuture channelFuture = b.bind(port).sync();
            channelFuture.channel().closeFuture().sync();//7
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();

        }
    }

    public static void main(String[] args) throws Exception{
        int port = 8080;
        if (args.length >0) {
            port = Integer.parseInt(args[0]);
        }
        new DiscardServer(port).run();;
    }
}