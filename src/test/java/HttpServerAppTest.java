import com.sun.net.httpserver.HttpExchange;
import org.example.Main;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class HttpServerAppTest {

        @Test
        public void testHandle() throws IOException {
            Main.RequestHandler handler = new Main.RequestHandler();
            // mock HttpExchange object
            HttpExchange exchange = Mockito.mock(HttpExchange.class);
            // mock InputStream object
            InputStream inputStream = Mockito.mock(InputStream.class);
            // mock OutputStream object
            OutputStream outputStream = Mockito.mock(OutputStream.class);

            // setup mock objects
            Mockito.when(exchange.getRequestMethod()).thenReturn("POST");
            Mockito.when(exchange.getRequestBody()).thenReturn(inputStream);
            Mockito.when(inputStream.readAllBytes()).thenReturn("1 end".getBytes());
            Mockito.when(exchange.getResponseBody()).thenReturn(outputStream);

            // call handle method
            handler.handle(exchange);

            // verify interactions with mock objects
            Mockito.verify(exchange).getRequestMethod();
            Mockito.verify(exchange).getRequestBody();
            Mockito.verify(inputStream).readAllBytes();
            Mockito.verify(exchange).sendResponseHeaders(200, 5);
            Mockito.verify(exchange).getResponseBody();
            Mockito.verify(outputStream).write("1 end".getBytes());
            Mockito.verify(outputStream).close();
        }

}
