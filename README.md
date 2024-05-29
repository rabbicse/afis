# AFIS
Automated Fingerprint Identification System using Java, Spring Boot, and Digital Persona U.are.U SDK. Utilizes MINEX templates for matching and leverages cluster computing for faster identification.

## Containerization
To build docker image run the following command

```
docker build --progress=plain --platform linux/amd64 -t afis .
```

To run on docker engine or docker desktop run the following command

```
docker run --name afis -p 8089:8089 -p 8000:8000 -t afis
```