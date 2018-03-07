FROM oracle/glassfish

RUN curl https://repo1.maven.org/maven2/mysql/mysql-connector-java/5.1.45/mysql-connector-java-5.1.45.jar -o $GLASSFISH_HOME/glassfish/lib/mysql-connector-java-5.1.45.jar

COPY config/domain.xml $GLASSFISH_HOME/glassfish/domains/domain1/config/domain.xml
COPY target/kwetter.war $GLASSFISH_HOME/glassfish/domains/domain1/autodeploy/kwetter.war
