/*
 * This file is released under terms of BSD license
 * See LICENSE file for more information
 */

package cx2x.translator.misc;


import static org.junit.Assert.*;

import cx2x.translator.pragma.ClawMapping;
import cx2x.xcodeml.exception.IllegalDirectiveException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


/**
 * @author clementval
 */
public class UtilityTest {
  @Test
  public void JoinArrayTest(){
    String[] a = {"a", "b", "c"};
    assertEquals("a,b,c", Utility.join(",", a));
    String[] b = {"a"};
    assertEquals("a", Utility.join(",", b));
  }

  @Test
  public void JoinListTest(){
    List<String> a = new ArrayList<>();
    a.add("a"); a.add("b"); a.add("c");
    assertEquals("a,b,c", Utility.join(",", a));
    List<String> b = new ArrayList<>();
    b.add("a");
    assertEquals("a", Utility.join(",", b));
    ClawMapping cm = null;
    try {
      cm = new ClawMapping("a,b:j1/k1,j2");
    } catch (IllegalDirectiveException e) {
      fail();
    }
    assertNotNull(cm);
    assertEquals("a,b:j1/k1,j2", cm.toString());
  }
}