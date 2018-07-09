package com;

import com.ps.Page.Page;
import org.junit.Test;

public class Page_Test {
    private static final int NUMBER_URL_PAGE = 5;

    private Page page = new Page.Builder(
            "56FD43F9-F50B-B646-FFA5-54A071D11398",
            "",
            "",
            "",
            "",
            null,
            null,
            NUMBER_URL_PAGE
    ).build();

    @Test
    public void setPathYouTube() throws Exception {
        page.setPathYouTube(
                "/url?q=https://www.youtube.com/watch%3Fv%3DUwoOfibZoYA&sa=U&ved=0ahUKEwi54Py2_OjXAhUhSJoKHUdvB_wQtwIIFzAA&usg=AOvVaw3eyJFRRnFSZWJIFSiWIF5q");

        System.out.println(page.getIdYouTube());
    }

}