//package com.agtinternational.hobbit.sml.deployment.parrot;
//
//import com.agtinternational.hobbit.sml.systems.ParrotSystemRunner;
//import com.agtinternational.hobbit.sdk.CommonConstants;
//import com.agtinternational.hobbit.sdk.docker.*;
//import com.spotify.docker.client.exceptions.DockerCertificateException;
//import com.spotify.docker.client.exceptions.DockerException;
//
//import java.io.IOException;
//
//import static org.hobbit.core.Constants.SYSTEM_PARAMETERS_MODEL_KEY;
//
///**
// * @author Roman Katerinenko
// */
//public class ParrotSystemDockerDeployment extends ParrotDockersBuilder {
//
//    public static void main(String[] args) throws InterruptedException, DockerException, DockerCertificateException, IOException {
//        String emptyParameters = "{}";
//        Dockerizer dockerizer = new ParrotSystemDockerDeployment()
//                .parameters(emptyParameters)
//                .hobbitSessionId(CommonConstants.HOBBIT_SESSION_ID)
//                .systemUri(CommonConstants.SYSTEM_URI)
//                .build();
//        dockerizer.removeImagesAndContainers();
//        dockerizer.buildImage();
//        dockerizer.createContainer();
//    }
//
//    public ParrotSystemDockerDeployment() {
//        super("ParrotSystemDockerizer");
//        imageName("git.project-hobbit.eu:4567/rkaterinenko/debsparrotsystem");
//        containerName("cont_name_debsparrotsystem");
//        runnerClass(ParrotSystemRunner.class);
//    }
//
//    public ParrotSystemDockerDeployment parameters(String parameters) {
//        addEnvironmentVariable(SYSTEM_PARAMETERS_MODEL_KEY, parameters);
//        return this;
//    }
//}