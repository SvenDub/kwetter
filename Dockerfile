FROM oracle/glassfish:5.0

RUN curl https://repo1.maven.org/maven2/mysql/mysql-connector-java/5.1.45/mysql-connector-java-5.1.45.jar -o $GLASSFISH_HOME/glassfish/lib/mysql-connector-java-5.1.45.jar

COPY config/glassfish-resources.xml /tmp/glassfish-resources.xml
RUN asadmin start-domain domain1 \
    && asadmin add-resources /tmp/glassfish-resources.xml \
    && asadmin create-auth-realm --classname com.sun.enterprise.security.auth.realm.jdbc.JDBCRealm \
    --property jaas-context="jdbcRealm":encoding="Base64":password-column="password":datasource-jndi="jdbc/kwetter":group-table="v_User_SecurityGroup":user-table="User":group-name-column="permission_key":digestrealm-password-enc-algorithm="none":digest-algorithm="SHA-256":user-name-column="email":group-table-user-name-column="email" \
    admin \
    && asadmin stop-domain domain1
COPY target/kwetter.war $GLASSFISH_HOME/glassfish/domains/domain1/autodeploy/kwetter.war

HEALTHCHECK --start-period=5m CMD curl -f http://localhost:8080/api/health
