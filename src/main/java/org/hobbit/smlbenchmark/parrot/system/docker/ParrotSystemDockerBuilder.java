package org.hobbit.smlbenchmark.parrot.system.docker;

import org.hobbit.smlbenchmark.common.docker.SMLDockersBuilder;

import static org.hobbit.core.Constants.SYSTEM_PARAMETERS_MODEL_KEY;

/**
 * @author Roman Katerinenko
 */
public class ParrotSystemDockerBuilder extends SMLDockersBuilder {

//    public static void main(String[] args) throws InterruptedException, DockerException, DockerCertificateException, IOException {
//        String emptyParameters = "{}";
//        AbstractDockerizer dockerizer = new ParrotSystemDockerBuilder()
//                .parameters(emptyParameters)
//                .hobbitSessionId(CommonConstants.HOBBIT_SESSION_ID)
//                .systemUri(CommonConstants.SYSTEM_URI)
//                .build();
//        dockerizer.removeImagesAndContainers();
//        dockerizer.buildImage();
//        dockerizer.createContainer();
//    }

    public ParrotSystemDockerBuilder() throws Exception {
        super("ParrotSystemDockerizer");
        imageName("git.project-hobbit.eu:4567/rkaterinenko/debsparrotsystem");
        containerName("cont_name_debsparrotsystem");
        runnerClass(ParrotSystemRunner.class);
    }

    public ParrotSystemDockerBuilder parameters(String parameters) {
        addEnvironmentVariable(SYSTEM_PARAMETERS_MODEL_KEY, parameters);
        return this;
    }
}