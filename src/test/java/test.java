import com.happysnaker.utils.IOUtil;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class test {
    public static void main(String[] args) throws IOException {
        IOUtil.sendAndGetResponseMap(new URL("http://api.lolicon.app/setu/v2?r18=1"), "GET", null, null).get("data");
    }
}
