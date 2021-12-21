# cxf-jaxws

This is fork of [https://github.com/code-not-found/cxf-jaxws.git](https://github.com/code-not-found/cxf-jaxws.git) with a lot of customization and modifications.

Some project have been modernized and some completely revised.
From previous: `cxf-jaxws-digital-signature` project, two additional project have been created:

- `cxf-jaxws-digital-signature-service` -  contains Web Service that verifies digitally signed SOAP message and return digitally signed result
- `cxf-jaxws-digital-signature-client` -  contains Web Service that accepts unsigned SOAP message, signs it and sends it to `cxf-jaxws-digital-signature-service` Web Service
