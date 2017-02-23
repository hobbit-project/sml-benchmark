FROM java
RUN mkdir -p /usr/src/debs
WORKDIR /usr/src/debs
ADD ./target/sml-benchmark-1.0-SNAPSHOT.jar /usr/src/debs
CMD ["java", "-cp", "sml-benchmark-1.0-SNAPSHOT.jar", "com.agtinternational.hobbit.ShellFacade"]