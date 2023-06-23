Tomcat CachingRealm
========================
![travis-ci](https://travis-ci.org/shopping24/tomcat-cache-realm.svg)

This projects adds a cache to your authentication Realm in Tomcat. In high-throughput environments
it might be a good idea to cache the underlying authentication infrastructure (LDAP/JDBC). 

## Use this project

Download the project from [Maven Central](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22tomcat-cache-realm%22) 
and drop the `jar-with-dependencies` into your Tomcat `/lib` folder. Then wrap your existing authentication 
realm(s) with the caching realm (in your `server.xml` or `context.xml`):

    <Realm className="com.s24.tomcat.CachingRealm">
       <Realm className="org.apache.catalina.realm.JNDIRealm" 
           connectionURL="..."
    </Realm>

By default, successful authentications are cached for 5 minutes. Use the `cacheSettings` property
to supply [Google Guava cache builder specs](https://github.com/google/guava/blob/master/guava/src/com/google/common/cache/CacheBuilderSpec.java)
to configure cache size and retention time. 

**NOTE**
This is currently being utilized in our Grouper tomcat environment to improve throughput on webservices and reduce load LDAP.

## Building the project

This should install the current version into your local repository

    $ mvn clean verify
    
### Releasing the project to maven central
    
Define new versions
    
    $ export NEXT_VERSION=<version>
    $ export NEXT_DEVELOPMENT_VERSION=<version>-SNAPSHOT

Then execute the release chain

    $ mvn org.codehaus.mojo:versions-maven-plugin:2.0:set -DgenerateBackupPoms=false -DnewVersion=$NEXT_VERSION
    $ git commit -a -m "pushes to release version $NEXT_VERSION"
    $ mvn -P release
    
Then, increment to next development version:
    
    $ git tag -a v$NEXT_VERSION -m "`curl -s http://whatthecommit.com/index.txt`"
    $ mvn org.codehaus.mojo:versions-maven-plugin:2.0:set -DgenerateBackupPoms=false -DnewVersion=$NEXT_DEVELOPMENT_VERSION
    $ git commit -a -m "pushes to development version $NEXT_DEVELOPMENT_VERSION"
    $ git push origin tag v$NEXT_VERSION && git push origin

## Contributing

We're looking forward to your comments, issues and pull requests!

## License

This project is licensed under the [Apache License, Version 2](http://www.apache.org/licenses/LICENSE-2.0.html).
