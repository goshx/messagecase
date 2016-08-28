package org.lmars.dm.tcp;


import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.streams.WriteStream;

/**
 * Helper class to format and send frames over a socket
 * @author Paulo Lopes
 */
public class TcpFrameHelper {

  private TcpFrameHelper() {}
  
  public static void sendFrame(JsonObject payload, WriteStream<Buffer> stream) {
    // encode
    byte[] data = payload.toString().getBytes();
    stream.write(Buffer.buffer().appendInt(data.length).appendBytes(data));
  }
  
}

