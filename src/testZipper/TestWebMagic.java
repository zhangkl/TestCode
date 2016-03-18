package testZipper;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

public class TestWebMagic implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);


    @Override
    public void process(Page page) {
        System.out.println("自打："+page.getRawText());
        System.out.println("自打："+page.getStatusCode());

    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Site.me().addCookie("__jsluid", "916d711271d073d1111d973685484c88");
        Site.me().addCookie("_gscu_1049835508", "56465253tvy5ai14");
        Site.me().addCookie("_gscbrs_1049835508", "1");
        Site.me().addCookie("wafenterurl", "/image.jsp");
        Site.me().addCookie("COLLCK", "1365137733");
        Site.me().addCookie("route", "82bc4860a319795dba2951a52df9911c");
        Site.me().addCookie("JSESSIONID", "E86C3B26FAA4546B98C781AD02A713E8");
        Site.me().addCookie("wzwsconfirm", "edaa355c3a721ae6783aeba2ef164cdc");
        Site.me().addCookie("wzwstemplate", "MTA=");
        Site.me().addCookie("ccpassport", "397842e355ae3596265c5fa779623436");
        Site.me().addCookie("wzwschallenge", "-1");
        Site.me().setUserAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36");
        Site.me().setCharset("utf-8");
        Site.me().setTimeOut(3000);
        Site.me().addHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*", "q=0.8");
//        Spider.create(new TestWebMagic()).addUrl("http://shixin.court.gov.cn/static/javascript/index.js")
        Spider.create(new TestWebMagic()).addUrl("http://shixin.court.gov.cn/index_publish_new.jsp")
                .addPipeline(new ConsolePipeline())
                .thread(1)
                .run();
        //ResultItems result = Spider.create(new TestWebMagic()).get("http://shixin.court.gov.cn");
    }
}