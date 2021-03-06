package org.hobbit.smlbenchmark.parrot.system.docker;

import org.hobbit.smlbenchmark.common.docker.SMLDockersBuilder;

import static org.hobbit.core.Constants.SYSTEM_PARAMETERS_MODEL_KEY;

/**
 * @author Roman Katerinenko
 */
public class ParrotNegativeSystemDockerBuilder extends SMLDockersBuilder {

//    public static void main(String[] args) throws InterruptedException, DockerException, DockerCertificateException, IOException {
//        String emptyParameters = "{}";
//        AbstractDockerizer dockerizer = new ParrotNegativeSystemDockerBuilder()
//                .parameters(emptyParameters)
//                .hobbitSessionId(CommonConstants.HOBBIT_SESSION_ID)
//                .systemUri(CommonConstants.SYSTEM_URI)
//                .build();
//        dockerizer.removeImagesAndContainers();
//        dockerizer.buildImage();
//        dockerizer.createContainer();
//    }

    public ParrotNegativeSystemDockerBuilder() throws Exception {
        super("ParrotNegativeSystemDockerizer");
        imageName("git.project-hobbit.eu:4567/rkaterinenko/debsparrotsystemnegative");
        containerName("cont_name_debsparrotsystemnegative");
        runnerClass(ParrotNegativeSystemRunner.class);
    }

    public ParrotNegativeSystemDockerBuilder parameters(String parameters) {
        addEnvironmentVariable(SYSTEM_PARAMETERS_MODEL_KEY, parameters);
        return this;
    }
}