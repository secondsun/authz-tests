Build the image, then run the image

ex
```
docker build --tag authz-keycloak .
docker run -p 8080:8080 -p 9990:9990 -it --rm authz-keycloak
```
