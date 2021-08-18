package com.isuper.eden.eve.boot.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.CodeSource;
import java.util.jar.Manifest;

/** @author admin */
public class SystemVersionUtils {

  /**
   * 获取系统的版本号码
   *
   * @param clz 当前类所在的位置
   * @return {@link String}
   */
  public static String getSystemVersion(Class<?> clz) {
    String version = clz.getPackage().getImplementationVersion();
    if (StringUtils.isBlank(version)) {
      CodeSource codeSource = clz.getProtectionDomain().getCodeSource();
      InputStream openStream = null;
      if (codeSource != null) {
        try {
          URI location = codeSource.getLocation().toURI();
          URI manifestURI =
              new URI(
                  location.getScheme(),
                  location.getAuthority(),
                  location.getPath() + "/META-INF/MANIFEST.MF",
                  location.getQuery(),
                  location.getFragment());

          openStream = manifestURI.toURL().openStream();
          Manifest manifest = new Manifest(openStream);
          version = manifest.getMainAttributes().getValue("Implementation-Version");
        } catch (Throwable e) {
          // ignore
        } finally {
          if (openStream != null) {
            try {
              openStream.close();
            } catch (IOException e1) {
              // ignore
            }
          }
        }
      }
    }
    return version;
  }
}
