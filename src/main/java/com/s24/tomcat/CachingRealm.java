package com.s24.tomcat;

import static com.google.common.base.Preconditions.checkNotNull;

import java.security.Principal;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.catalina.realm.CombinedRealm;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * A caching tomcat realm.
 * 
 * @author Shopping24 GmbH, Torsten Bøgh Köster (@tboeghk)
 * modified by Anne von Raven
 */
public class CachingRealm extends CombinedRealm {

   // default cache
   private Cache<String, Principal> authCache = CacheBuilder.newBuilder()
         .expireAfterWrite(5 , TimeUnit.MINUTES)
         .build();

   /**
    * {@inheritDoc}
    * 
    * Caches the authentication given for the configured timeframe.
    */
   @Override
   public Principal authenticate(String username, String credentials) {
      checkNotNull(username, "Pre-condition violated: username must not be null.");

      // compute cache key
      String key = username + ":" + credentials;
      
      try {
         return authCache.get(key, () -> {
            Principal auth = authenticateInternal(username, credentials);
            // Guava cache does not accept null return values - recommendation is to throw an exception instead
            if (auth == null) throw new ExecutionException(new Throwable("Invalid credentials"));
            return auth;
         });
      } catch (ExecutionException e) {
         // AvR 2019-05-21 is this really needed? If something went wrong above, it is quite certainly due to wrong credentials
         return authenticateInternal(username, credentials);
      }
   }
   
   protected Principal authenticateInternal(String username, String credentials) {
      return super.authenticate(username, credentials);
   }

   public void setCacheSettings(String settings) {
      checkNotNull(settings, "Pre-condition violated: settings must not be null.");

      authCache = CacheBuilder.from(settings).build();
   }

}
