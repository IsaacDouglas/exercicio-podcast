package br.ufpe.cin.if710.podcast.jUnit;

import android.content.ContentValues;
import android.net.Uri;

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

import br.ufpe.cin.if710.podcast.db.PodcastProvider;
import br.ufpe.cin.if710.podcast.db.PodcastProviderContract;
import br.ufpe.cin.if710.podcast.domain.ItemFeed;
import br.ufpe.cin.if710.podcast.domain.XmlFeedParser;

/**
 * Created by lucas on 11/12/17.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Uri.class})
public class DatabaseTest {

    @Mock
    private ContentValues content;

    @Mock
    private PodcastProvider provider;

    @Mock
    private Uri success;

    @Mock
    private Uri uri;

    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        content.put(PodcastProviderContract.TITLE, "StarWars");
        content.put(PodcastProviderContract.LINK, "www.lucasfilm.com/podcast");
        content.put(PodcastProviderContract.DATE, "13/03/1995");
        content.put(PodcastProviderContract.DESCRIPTION, "Lucas Films - StarWars");
        content.put(PodcastProviderContract.URI , "algumacoisaaqui");
    }

    @Test
    public void insertSuccess() throws Exception{
        when(provider.insert(uri, content)).thenReturn(success);
        doNothing().when(content).put(anyString(), anyString());

        Uri result = provider.insert(uri, content);

        Assert.assertNotNull("success", result);

    }

    @Test
    public void insertFail() throws Exception {
        when(provider.insert(uri, content)).thenReturn(null);
        doNothing().when(content).put(anyString(), anyString());

        Uri result = provider.insert(uri, content);

        Assert.assertNull("fail", result);
    }

}
