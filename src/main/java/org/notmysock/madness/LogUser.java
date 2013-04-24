package org.notmysock.madness;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LogUser {

  Log LOG = LogFactory.getLog(LogUser.class);
  
  public void benchmark() {
    long t1 = System.currentTimeMillis();
    int i;
    for(i = 0; i < 4*1024*1024; i++) {
      LOG.debug("hello" + "world!");
    }
    long t2 = System.currentTimeMillis();
    System.out.printf("We took %d ms to do %d M loops\n", (t2 - t1), i/(1024*1024));
  }
  public static void main(String[] args) {
    LogUser test = new LogUser();
    test.LOG.debug("XX");
    test.benchmark();
  }
}
