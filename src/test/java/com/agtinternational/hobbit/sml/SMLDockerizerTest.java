package com.agtinternational.hobbit.sml;

import com.agtinternational.hobbit.sml.common.SMLConstants;
import com.agtinternational.hobbit.sml.deployment.sml.SMLCsvSystemDockerBuilder;
import com.agtinternational.hobbit.sdk.CommonConstants;
import com.agtinternational.hobbit.sdk.docker.*;
import com.agtinternational.hobbit.sml.deployment.sml.SMLBenchmarkDockerBuilder;
import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class SMLDockerizerTest {

    @Test
    public void checkSystemStartStop(){

        Dockerizer dockerizer = new SMLCsvSystemDockerBuilder()
                .hobbitSessionId(CommonConstants.HOBBIT_SESSION_ID)
                .systemUri(CommonConstants.SYSTEM_URI)
                //.useCachedContainer()
                .build();
        try {
            dockerizer.run();
            dockerizer.stop();
        }
        catch (Exception e){
            fail(e.getMessage());
        }

        assertNull(dockerizer.anyExceptions());
    }

    @Test
    public void checkBenchmarkStartStop(){

        Dockerizer dockerizer = new SMLBenchmarkDockerBuilder()
                .benchmarkOutputFormat(SMLConstants.FORMAT_RDF)
                .hobbitSessionId(CommonConstants.HOBBIT_SESSION_ID)
                .systemUri(CommonConstants.SYSTEM_URI)
                .useCachedContainer()
                .skipLogsReading()
                .build();
        try {
            dockerizer.run();
            dockerizer.stop();
        }
        catch (Exception e){
            fail(e.getMessage());
        }

        assertNull(dockerizer.anyExceptions());
    }
}
