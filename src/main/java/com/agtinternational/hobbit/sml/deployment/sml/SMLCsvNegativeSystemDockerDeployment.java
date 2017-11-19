//package com.agtinternational.hobbit.sml.deployment.sml;
//
//import com.agtinternational.hobbit.sdk.CommonConstants;
//import com.agtinternational.hobbit.sdk.docker.*;
//import com.spotify.docker.client.exceptions.DockerCertificateException;
//import com.spotify.docker.client.exceptions.DockerException;
//
//import java.io.IOException;
//
///**
// * @author Roman Katerinenko
// */
//public class SMLCsvNegativeSystemDockerDeployment {
//    public static void main(String[] args) throws InterruptedException, DockerException, DockerCertificateException, IOException {
//        String emptyParameters = "{}";
//        Dockerizer dockerizer = new SMLCsvNegativeSystemBuilder()
//                .parameters(emptyParameters)
//                .hobbitSessionId(CommonConstants.HOBBIT_SESSION_ID)
//                .systemUri(CommonConstants.SYSTEM_URI)
//                .build();
//        dockerizer.removeImagesAndContainers();
//        dockerizer.buildImage();
//        dockerizer.createContainer();
//    }
//}