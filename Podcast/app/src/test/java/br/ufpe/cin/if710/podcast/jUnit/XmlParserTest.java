package br.ufpe.cin.if710.podcast.jUnit;

import static org.mockito.Mockito.*;

import junit.framework.Assert;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.xmlpull.v1.XmlPullParserException;

import org.powermock.modules.junit4.PowerMockRunner;



import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import br.ufpe.cin.if710.podcast.domain.ItemFeed;
import br.ufpe.cin.if710.podcast.domain.XmlFeedParser;


/**
 * Created by lucas on 10/12/17.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest(XmlFeedParser.class)
public class XmlParserTest {

    private final String FEED_URL = "http://leopoldomt.com/if710/fronteirasdaciencia.xml";
    private List<ItemFeed> items = new LinkedList<>();

    @Mock
    private XmlFeedParser parserMock;


    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        items.add(new ItemFeed("StarWars", "lucasfilm.com", "13/03/1995", "Lucas films - Star wars", "lucasfilm.com/download"));
        items.add(new ItemFeed("StarWars", "lucasfilm.com", "11/03/1995", "Lucas films - Star wars", "lucasfilm.com/download"));
        items.add(new ItemFeed("StarWars", "lucasfilm.com", "12/03/1995", "Lucas films - Star wars", "lucasfilm.com/download"));
    }


    @Test
    public void xmlFeedParserSuccessTest() throws IOException, XmlPullParserException{
        PowerMockito.mockStatic(XmlFeedParser.class);

        when(parserMock.parse(anyString())).thenReturn(items);
        List<ItemFeed> parser = XmlFeedParser.parse(FEED_URL);

        Assert.assertNotNull("Success", parser);
    }

    @Test
    public void xmlFeedParserFailTest() throws IOException, XmlPullParserException{
        PowerMockito.mockStatic(XmlFeedParser.class);

        when(parserMock.parse(anyString())).thenReturn(null);
        List<ItemFeed> parser = XmlFeedParser.parse("fail_feed");

        Assert.assertNull("Fail", parser);
    }

}
