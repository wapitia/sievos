package org.sievos.kern
package test

import java.io.PrintWriter

import org.junit.Before
import org.junit.Test
import org.sievos.kern.CSQ.csq
import org.sievos.kern.Kern._
import org.sievos.kern.Logical._
import java.io.File


class KernTest {

  var testoutdir: File = _

  @Before
  def mkTestOutEnv {
    testoutdir = new File("target/test-output")
    testoutdir.mkdirs
  }

  @Test
  def test1() {
    val outfile: File = new File(testoutdir, "out")
    val pw: PrintWriter = new PrintWriter(outfile)
    val ln = N0 :: N1 :: N2 :: N3 :: Nil
    ln.foreach( n => print(n,pw))
    pw.println("--------   --------------")
    val ln2 = F0 :: F1 :: F2 :: F3 :: Nil
    ln2.foreach( n => print(n,pw))
    pw.close()
  }

  def print(n:N, pw: PrintWriter) {
      val halfadd = z(r(z(n)))
      val q = csq(halfadd)
      pw.println("%s  %s => %s".format(n,Visual.toString(n),q))
  }

}

object KernTest extends App {

}