//package org.hobbit.smlbenchmark;
//
//import org.hobbit.smlbenchmark.sml.system.docker.SMLCsvSystemDockerBuilder;
//import org.hobbit.sdk.docker.*;
//import org.hobbit.smlbenchmark.sml.docker.SMLBenchmarkDockerBuilder;
//import org.junit.Test;
//
//import static org.junit.Assert.assertNull;
//import static org.junit.Assert.assertTrue;
//import static org.junit.Assert.fail;
//
//public class SMLDockerizerTest {
//
//    @Test
//    public void checkSystemStartStop(){
//
//        try {
//            AbstractDockerizer dockerizer = new SMLCsvSystemDockerBuilder()
//                    //.hobbitSessionId(CommonConstants.HOBBIT_SESSION_ID)
//                    //.systemUri(CommonConstants.SYSTEM_URI)
//                    //.useCachedContainer()
//                    .build();
//            dockerizer.run();
//            dockerizer.stop();
//            assertNull(dockerizer.anyExceptions());
//        }
//        catch (Exception e){
//            fail(e.getMessage());
//        }
//
//
//    }
//
//    @Test
//    public void checkBenchmarkStartStop(){
//
//        try {
//            AbstractDockerizer dockerizer = new SMLBenchmarkDockerBuilder()
//                    .benchmarkOutputFormat(SMLConstants.FORMAT_RDF)
//                    //.hobbitSessionId(CommonConstants.HOBBIT_SESSION_ID)
//                    //.systemUri(CommonConstants.SYSTEM_URI)
//                    .useCachedContainer()
//                    .skipLogsReading()
//                    .build();
//            dockerizer.run();
//            dockerizer.stop();
//            assertNull(dockerizer.anyExceptions());
//        }
//        catch (Exception e){
//            fail(e.getMessage());
//        }
//
//
//    }
//}
