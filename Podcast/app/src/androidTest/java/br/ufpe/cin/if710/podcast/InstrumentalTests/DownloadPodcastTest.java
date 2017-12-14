package br.ufpe.cin.if710.podcast.InstrumentalTests;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.test.InstrumentationTestCase;

/**
 * Created by lucas on 13/12/17.
 */

public class DownloadPodcastTest extends  InstrumentationTestCase {

    private UiDevice device;

    @Override
    public void setUp() throws Exception {
        device = UiDevice.getInstance(getInstrumentation());
        device.pressHome();
        device.wait(Until.hasObject(By.desc("Apps")), 5000);

        UiObject2 appsButton = device.findObject(By.descContains("Apps"));
        appsButton.click();
        device.wait(Until.hasObject(By.text("Podcast")), 5000);

        UiObject2 podcasthelper = device.findObject(By.text("Podcast"));
        podcasthelper.click();
        device.wait(Until.hasObject(By.text("Open navigation drawer")), 3000);

        UiObject2 botaoDownload = device.findObject(By.text("DOWNLOAD"));
        botaoDownload.click();
        device.wait(Until.hasObject(By.text("PLAY")), 5000);

        UiObject2 botaoPlay = device.findObject(By.text("PLAY"));
        botaoPlay.click();

        super.setUp();

    }

    public void testName() throws Exception {

    }

}
