package enUcuz.interfaces;

import java.io.IOException;
import java.net.SocketTimeoutException;

public interface IScrappable {
    void scrap() throws IOException, SocketTimeoutException;
}
