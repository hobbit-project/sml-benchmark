//package com.agtinternational.hobbit.sml.deployment.sml;
//
//import com.agtinternational.hobbit.sdk.CommonConstants;
//import com.agtinternational.hobbit.sdk.docker.Dockerizer;
//import com.agtinternational.hobbit.sml.common.SMLConstants;
//import com.spotify.docker.client.exceptions.DockerCertificateException;
//import com.spotify.docker.client.exceptions.DockerException;
//
//import java.io.IOException;
//
///**
// * @author Roman Katerinenko
// */
//public class AnalyticsBenchmarkDockerDeployment {
//    public static void main(String[] args) throws InterruptedException, DockerException, DockerCertificateException, IOException {
//        Dockerizer dockerizer = new AnalyticsBenchmarkDockerBuilder()
//                .benchmarkOutputFormat(SMLConstants.FORMAT_RDF)
//                .hobbitSessionId(CommonConstants.HOBBIT_SESSION_ID)
//                .systemUri(CommonConstants.SYSTEM_URI)
//                .build();
//        dockerizer.removeImagesAndContainers();
//        dockerizer.buildImage();
//        dockerizer.createContainer();
//    }
//}