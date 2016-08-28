package org.lmars.dm.main.agent;

import static org.lmars.dm.tcp.TcpFrameHelper.sendFrame;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.net.NetSocket;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.lmars.dm.conf.Config;
import org.lmars.dm.message.ErrorResponse;
import org.lmars.dm.message.ErrorResponse2;
import org.lmars.dm.message.LoginResponse;
import org.lmars.dm.message.OKResponse;
import org.lmars.dm.tcp.TcpFrameParser;
import org.lmars.dm.tcp.TcpRequest;
import org.lmars.dm.ua.UserAuthorityInfo;
import org.lmars.dm.ua.UserAuthorityService;
import org.lmars.dm.util.BaseServer;
import org.lmars.dm.util.ConfigureFileParser;

public class StreamService extends BaseServer {
    
    private static String cfgFile;
    private static String bootstrapServers;
    private static int serverPort;
    private static int vertxServerNumber;
    private static String identifyUrl;
    private static String queryUrl;

    public static void main(String args[]) {
        if(args.length>0) {
            cfgFile = args[0];
        } else {
            cfgFile = "C:\\Users\\Junia\\Documents\\NetBeansProjects\\DMessage\\conf\\stream.cfg";
        }
        BaseServer.create(StreamService.class, Config.stream_server_worker_poor_size, Config.stream_max_event_loop_execute_time);
    }
    
    public void readConfig() throws Exception{
		//加载配置信息
		ConfigureFileParser cfg = new ConfigureFileParser();
		Map<String,Map<String,String>> sections = cfg.parse2(cfgFile);
                
		//kafka
		Map<String,String> kafka_conf = sections.get("kafka");
                bootstrapServers = kafka_conf.get("servers");
                
                //stream server
		Map<String,String> server_conf = sections.get("server");
                serverPort = Integer.parseInt(server_conf.get("port"));
                vertxServerNumber = Integer.parseInt(server_conf.get("server_number"));
                
                //right server
		Map<String,String> right_conf = sections.get("right");
                identifyUrl = server_conf.get("query_url");
                queryUrl = server_conf.get("identify_url");
	}

    @Override
    public void start() throws Exception {
        
        readConfig();

        forwarder.startUp(bootstrapServers);

        for (int i = 0; i < vertxServerNumber; i++) {
            NetServerOptions netServerOptions = new NetServerOptions();
            NetServer server = vertx.createNetServer(netServerOptions);
            server.connectHandler(s -> {
                SessionLocalHolder holder = new SessionLocalHolder(s);
                holder.doWork();
            });
            listen(server, serverPort, res -> {
                log.info("[Stream Server Worker] Running in " + Thread.currentThread().getName() + ", listening on " + serverPort);
            });
        }
    }

    private static final Logger log = LoggerFactory.getLogger(StreamService.class);

    //session表
    private Map<String, SessionLocalHolder> sessions = new ConcurrentHashMap<String, SessionLocalHolder>();
    //session的超时时间
    private int maxUnactiveTimeOut = 30000;
    //转发消息
    private MessageForwarder forwarder = new MessageForwarder();

    public class SessionLocalHolder {

        //用户名
        public String userName = null;
        //socket信息
        public NetSocket socket = null;
        //是否有效标志
        private boolean isClose = false;
        //超时ID
        private long timeoutID = -1;
        //更新时间
        public long timeStamp = -1;
        //是否登录
        public boolean isLogin = false;
        //UUID
        public String uuid = null;
        //更多权限信息
        public JsonObject more = new JsonObject();
        public TcpFrameParser parser = null;

        public SessionLocalHolder(NetSocket s) {
            socket = s;
            timeStamp = System.currentTimeMillis();
            uuid = UUID.randomUUID().toString();
        }

        public void checkLive() {
            synchronized (this) {
                if (isClose) {
                    return;
                }

                if (timeStamp != -1) {
                    long cur_t = System.currentTimeMillis();
                    long pass_time = cur_t - timeStamp;
                    if (pass_time < maxUnactiveTimeOut / 2) {
                        return;
                    }
                }

                if (timeoutID != -1) {
                    vertx.cancelTimer(timeoutID);
                }
                timeStamp = System.currentTimeMillis();
                timeoutID = vertx.setTimer(maxUnactiveTimeOut, tid -> {
                    vertx.executeBlocking(f -> {
                        close();
                    }, true, null);
                });
            }
        }

        public void close() {
            synchronized (this) {
                if (isClose) {
                    return;
                }
                try {
                    sessions.remove(uuid);
                    if (timeoutID != -1) {
                        vertx.cancelTimer(timeoutID);
                    }
                    socket.close();
                    isClose = true;
                } catch (Exception ignore) {

                }
            }
        }

        private void processLoginMessage(TcpRequest msg) throws Exception {

            if (!isLogin) {
                JsonObject loginInfo = new JsonObject(msg.getContent());
                String userName = loginInfo.getString(Config.param_username);
                String password = loginInfo.getString(Config.param_password);
                UserAuthorityService UA = new UserAuthorityService();
                JsonObject object = new JsonObject();
                object.put(Config.user_authority_username, userName);
                object.put(Config.user_authority_password, password);
                object.put(Config.user_authority_type, 0);
                String data = object.toString();
                UserAuthorityInfo checkout = UA.login(identifyUrl, data);
                if (checkout.status) {
                    isLogin = true;
                    this.userName = userName;
                    sendFrame(new LoginResponse(true, this.uuid).toJson(), socket);
                } else {
                    sendFrame(new ErrorResponse("登录错误").toJson(), socket);//返回登录失败的信息  
                    throw new Exception();
                }
            } else {
                sendFrame(new ErrorResponse("repeatedly login").toJson(), socket);//返回重复登录信息  
            }
        }

        private void processSendMessage(TcpRequest msg) throws Exception {

            if (!isLogin) {
                sendFrame(new ErrorResponse("login first,please").toJson(), socket);
                return;
            }

            //System.out.println(msg.getContent());
            forwarder.processMessage(msg.getTopic(), msg.getContent());

            sendFrame(new OKResponse().toJson(), socket);

        }

        private void processSubscribeMessage(TcpRequest msg) throws Exception {
            if (!isLogin) {
                sendFrame(new ErrorResponse("login first,please").toJson(), socket);
                return;
            }

        }

        public void doWork() {
            sessions.put(uuid, this);
            parser = new TcpFrameParser(res -> {
                try {
                    if (res == null || res.failed()) {
                        // could not parse the message properly
                        sendFrame(new ErrorResponse2().set(res.cause().toString()).create(), socket);
                        log.error(res.cause());
                        return;
                    }
                    checkLive();

                    TcpRequest msg = (TcpRequest) res.result();
                    switch (msg.getMessageType()) {
                        case Config.Message_Login:
                            processLoginMessage(msg);
                            break;
                        case Config.Message_Send:
                            processSendMessage(msg);
                            break;
                        case Config.Message_Subscribe:
                            processSubscribeMessage(msg);
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    close();
                }
            });

            socket.handler(parser::handle);
            socket.exceptionHandler(t -> {
                close();
            });
            socket.endHandler(v -> {
                close();
            });
        }
    }

    private void listen(NetServer server) {
        server.listen();
    }

    private void listen(NetServer server, int port) {
        server.listen(port);
    }

    private void listen(NetServer server, int port, String address) {
        server.listen(port, address);
    }

    private void listen(NetServer server, Handler<AsyncResult<String>> handler) {
        server.listen(res -> {
            if (res.failed()) {
                handler.handle(Future.failedFuture(res.cause()));
            } else {
                handler.handle(Future.succeededFuture("ok"));
            }
        });
    }

    private void listen(NetServer server, int port, String address, Handler<AsyncResult<String>> handler) {
        server.listen(port, address, res -> {
            if (res.failed()) {
                handler.handle(Future.failedFuture(res.cause()));
            } else {
                handler.handle(Future.succeededFuture("ok"));
            }
        });
    }

    private void listen(NetServer server, int port, Handler<AsyncResult<String>> handler) {
        server.listen(port, res -> {
            if (res.failed()) {
                handler.handle(Future.failedFuture(res.cause()));
            } else {
                handler.handle(Future.succeededFuture("ok"));
            }
        });
    }

    private void close(NetServer server, Handler<AsyncResult<Void>> handler) {
        server.close(handler);
    }

    private void close(NetServer server) {
        server.close();
    }

}
