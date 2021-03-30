# CMO Messaging Library

The messaging library used by Java clients to interface with CMO messaging backbone.

The following properties should be set in the Java client's application.properties:

TLS connection properties:
(see https://docs.nats.io/developing-with-nats/security/tls)

- `nats.tls_channel` : tls_channel is a boolean indicating if a TLS channel should be create (required if NATS is running TLS)
- `nats.keystore_path`
- `nats.truststore_path`
- `nats.key_password`
- `nats.store_password`

General NATS/STAN properties:
- `nats.clientid`
- `nats.clusterid`
- `nats.url` : nats://\<ID>:\<PASSWORD>@\<HOST>:4222
