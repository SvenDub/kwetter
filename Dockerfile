FROM oracle/glassfish

RUN curl https://repo1.maven.org/maven2/mysql/mysql-connector-java/5.1.45/mysql-connector-java-5.1.45.jar -o $GLASSFISH_HOME/glassfish/lib/mysql-connector-java-5.1.45.jar

COPY config/glassfish-resources.xml /tmp/glassfish-resources.xml
RUN asadmin start-domain domain1 \
    && asadmin add-resources /tmp/glassfish-resources.xml \
    && asadmin set server.network-config.network-listeners.network-listener.http-listener-1.port=60000 \
    && asadmin set server.network-config.network-listeners.network-listener.admin-listener.port=60001 \
    && asadmin stop-domain domain1
COPY target/kwetter.war $GLASSFISH_HOME/glassfish/domains/domain1/autodeploy/kwetter.war

EXPOSE 60000
EXPOSE 60001
