//package com.agtinternational.hobbit.sml.deployment.parrot;
//
//import com.agtinternational.hobbit.sdk.CommonConstants;
//import com.agtinternational.hobbit.sml.systems.ParrotNegativeSystemRunner;
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
//public class ParrotNegativeSystemDockerDeployment extends ParrotDockersBuilder {
//
//    public static void main(String[] args) throws InterruptedException, DockerException, DockerCertificateException, IOException {
//        String emptyParameters = "{}";
//        Dockerizer dockerizer = new ParrotNegativeSystemDockerDeployment()
//                .parameters(emptyParameters)
//                .hobbitSessionId(CommonConstants.HOBBIT_SESSION_ID)
//                .systemUri(CommonConstants.SYSTEM_URI)
//                .build();
//        dockerizer.removeImagesAndContainers();
//        dockerizer.buildImage();
//        dockerizer.createContainer();
//    }
//
//    public ParrotNegativeSystemDockerDeployment() {
//        super("ParrotNegativeSystemDockerizer");
//        imageName("git.project-hobbit.eu:4567/rkaterinenko/debsparrotsystemnegative");
//        containerName("cont_name_debsparrotsystemnegative");
//        runnerClass(ParrotNegativeSystemRunner.class);
//    }
//
//    public ParrotNegativeSystemDockerDeployment parameters(String parameters) {
//        addEnvironmentVariable(SYSTEM_PARAMETERS_MODEL_KEY, parameters);
//        return this;
//    }
//}