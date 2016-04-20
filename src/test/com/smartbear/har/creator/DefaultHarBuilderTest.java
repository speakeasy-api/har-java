package com.smartbear.har.creator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartbear.har.builder.HarEntryBuilder;
import com.smartbear.har.builder.HarLogBuilder;
import com.smartbear.har.model.HarLog;
import com.smartbear.har.model.HarLogRoot;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DefaultHarBuilderTest {

    private File tempHarFile;
    private HarBuilder harBuilder;

    @Before
    public void setUp() throws Exception {
        tempHarFile = File.createTempFile("virt", ".har");
        harBuilder = new DefaultHarBuilder(tempHarFile, new HarLogBuilder().withComment("Test Har").build());
    }

    @After
    public void tearDown() throws Exception {
        tempHarFile.deleteOnExit();
    }

    @Test
    public void harBuilderAppendsMultipleEntriesToHarLog() throws Exception {
        harBuilder.addEntry(new HarEntryBuilder().withComment("Test Entry").build());
        harBuilder.addEntry(new HarEntryBuilder().withComment("Another Test Entry").build());
        harBuilder.closeHar();

        HarLog harLog = getHarLog();

        assertThat(harLog.getEntries().size(), is(2));
    }

    private HarLog getHarLog() throws java.io.IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(tempHarFile, HarLogRoot.class).getLog();
    }
}