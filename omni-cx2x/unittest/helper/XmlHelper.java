/*
 * This file is released under terms of BSD license
 * See LICENSE file for more information
 */
package helper;

import claw.tatsu.xcodeml.xnode.Xname;
import claw.tatsu.xcodeml.xnode.common.*;
import claw.tatsu.xcodeml.xnode.fortran.XbasicType;
import claw.tatsu.xcodeml.xnode.fortran.XfunctionDefinition;
import claw.tatsu.xcodeml.xnode.fortran.XfunctionType;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.StringReader;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Helper class containing static methods for the unit tests.
 *
 * @author clementval
 */

public class XmlHelper {

  public static XcodeProgram getDummyXcodeProgram() {
    File f = new File(TestConstant.TEST_DATA);
    assertTrue(f.exists());
    XcodeProgram xcodeml = XcodeProgram.createFromFile(TestConstant.TEST_DATA);
    assertNotNull(xcodeml);
    return xcodeml;
  }

  private static Document loadXMLFromString(String xml) {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      InputSource is = new InputSource(new StringReader(xml));
      return builder.parse(is);
    } catch(Exception ex) {
      return null;
    }
  }

  private static Xnode getElementFromString(String xml) {
    Document doc = loadXMLFromString(xml);
    if(doc != null) {
      return new Xnode(doc.getDocumentElement());
    }
    return null;
  }

  public static Xid createXidFromString(String xml) {
    Xnode n = XmlHelper.getElementFromString(xml);
    assertNotNull(n);
    return new Xid(n);
  }

  public static XbasicType createXbasicTypeFromString(String xml) {
    Xnode n = XmlHelper.getElementFromString(xml);
    assertNotNull(n);
    return new XbasicType(n);
  }

  public static XfunctionType createXfctTypeFromString(String xml) {
    Xnode n = XmlHelper.getElementFromString(xml);
    assertNotNull(n);
    return new XfunctionType(n);
  }

  public static XsymbolTable createXglobalSymbolFromString(String xml) {
    Xnode n = XmlHelper.getElementFromString(xml);
    assertNotNull(n);
    return new XsymbolTable(n);
  }

  public static XsymbolTable createXSymbolTableFromString(String xml) {
    Xnode n = XmlHelper.getElementFromString(xml);
    assertNotNull(n);
    return new XsymbolTable(n);
  }

  public static XtypeTable createXtypeTableFromString(String xml) {
    Xnode n = XmlHelper.getElementFromString(xml);
    assertNotNull(n);
    return new XtypeTable(n);
  }

  public static XfunctionDefinition createXfunctionDefinitionFromString(
      String xml)
  {
    Xnode n = XmlHelper.getElementFromString(xml);
    assertNotNull(n);
    return new XfunctionDefinition(n);
  }

  public static XglobalDeclTable createGlobalDeclTable(String xml) {
    Xnode n = XmlHelper.getElementFromString(xml);
    assertNotNull(n);
    return new XglobalDeclTable(n);
  }

  public static Xnode createXvarDecl(String xml) {
    Xnode n = XmlHelper.getElementFromString(xml);
    assertNotNull(n);
    return n;
  }

  public static XdeclTable createXdeclTable(String xml) {
    Xnode n = XmlHelper.getElementFromString(xml);
    assertNotNull(n);
    return new XdeclTable(n);
  }

  public static Xnode createXpragma() {
    String xml = "<" + Xname.F_PRAGMA_STMT + "></" +
        Xname.F_PRAGMA_STMT + ">";
    Xnode n = XmlHelper.getElementFromString(xml);
    assertNotNull(n);
    return n;
  }

  public static Xnode createXnode(String xml) {
    Xnode n = XmlHelper.getElementFromString(xml);
    assertNotNull(n);
    return n;
  }
}
