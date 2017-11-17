FROM java
RUN mkdir -p /usr/src/debs
ADD /target/sml-benchmark-1.0-SNAPSHOT.jar /usr/src/debs/
#ADD /target/original-wm-data-gen-1.0-SNAPSHOT.jar /usr/src/debs/
WORKDIR /usr/src/debs
#CMD ["java", "-cp", "sml-benchmark-1.0-SNAPSHOT.jar:original-wm-data-gen-1.0-SNAPSHOT.jar", "SMLBenchmarkRunner"]
CMD ["java", "-cp", "sml-benchmark-1.0-SNAPSHOT.jar", "SMLBenchmarkRunner"]
