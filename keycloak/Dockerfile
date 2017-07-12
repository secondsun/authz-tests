FROM fedora:25

RUN dnf install -y java wget
RUN wget https://downloads.jboss.org/keycloak/3.2.0.Final/keycloak-3.2.0.Final.tar.gz
RUN tar -zvxf keycloak-3.2.0.Final.tar.gz

ADD authz_demo.json /keycloak-3.2.0.Final/bin

EXPOSE 8080
EXPOSE 9990
EXPOSE 8443

WORKDIR /keycloak-3.2.0.Final/bin

CMD ["./standalone.sh", "-b=0.0.0.0", "-Dkeycloak.migration.action=import", "-Dkeycloak.migration.provider=singleFile",  "-Dkeycloak.migration.file=authz_demo.json", "-Dkeycloak.migration.strategy=IGNORE_EXISTING"]
