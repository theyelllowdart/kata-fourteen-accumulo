[![Build Status](https://travis-ci.org/theyelllowdart/kata-fourteen-accumulo.png?branch=master)](https://travis-ci.org/theyelllowdart/kata-fourteen-accumulo)
Kata-fourteen-accumulo
======================

######Design Diagrams:
* [Text Ingestion](https://cacoo.com/diagrams/yAXhWET7O26g9E2t-21FDE.png)
* [Text Generation](https://cacoo.com/diagrams/yAXhWET7O26g9E2t-B12E3.png)

######Run instructions:
1. mvn install
2. cd web
3. mvn jetty:run
4. curl --verbose --request PUT --data "I wish I may I wish I might" http://localhost:8081/rest/ingest
5. curl --verbose --request GET http://localhost:8081/rest/generator?maxLength=200

######Use Real Accumulo/Zookeeper
* Copy uber-jars/accumulo-iterators-jar/target/accumulo-iterators-jar-1.0-SNAPSHOT.jar into $ACCUMULO_HOME/lib/ext
* Specify an overriding web.xml with these context-parms (sample values listed below)

```
<context-param>
  <param-name>kata-fourteen.accumulo.connector.type</param-name>
  <param-value>ZOOKEEPER</param-value>
</context-param>
<context-param>
  <param-name>kata-fourteen.accumulo.instance</param-name>
  <param-value>instance</param-value>
</context-param>
<context-param>
  <param-name>kata-fourteen.accumulo.username</param-name>
  <param-value>root</param-value>
</context-param>
<context-param>
  <param-name>kata-fourteen.accumulo.password</param-name>
  <param-value>secret</param-value>
</context-param>
<context-param>
  <param-name>kata-fourteen.zookeeper.connection</param-name>
  <param-value>localhost:2181</param-value>
</context-param>
<context-param>
  <param-name>kata-fourteen.ngram.size</param-name>
  <param-value>2</param-value>
</context-param>
<context-param>
  <param-name>kata-fourteen.ngram.table.name</param-name>
  <param-value>ngram</param-value>
</context-param>
```
