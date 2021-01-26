
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.IOException;
import java.net.http.HttpClient;

public class TestJsd {
    @Test
    public void m() throws IOException {
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setDoNotTrackEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(true); //启用JS解释器，默认为true
        webClient.getOptions().setCssEnabled(false); //禁用css支持
        webClient.getOptions().setThrowExceptionOnScriptError(false); //js运行错误时，是否抛出异常
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setTimeout(20000);
        HtmlPage page = webClient.getPage("http://22ttyy.com/vod/SWAG/61264/index_1_1.html");
        //我认为这个最重要
        String pageXml = page.asXml(); //以xml的形式获取响应文本
        //System.out.println("pageXml = " + pageXml);
        /**jsoup解析文档*/
        Document doc = Jsoup.parse(pageXml);
        //.out.println(doc.html());
        System.out.println("finish");
    }
    @Test
    public void n(){
    }
}
