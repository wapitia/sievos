package org.sievos.kern
package test

import org.junit.Test



class TestPartition {
  
//  var testoutdir: File = _
  
  object tps {
      val p0 = Part[Int]()
      val p1 = Part(3)
      val p1b = Part(7)
      val p2 = p0 | p1
      val p3 = p1 | p0 | p1
      val p4 = p1 |+ 9
      val p5 = p1b | Part[Int](p3,p4) | Part[Int](10)
      val partViewTuples =  List( 
            (p0, "(|)"), 
            (p1, "3") ,
            (p2, "3"),
            (p3, "3|3"),
            (p4, "3|9"),
            (p5, "(7|((3|3)|(3|9)))|10") )
  }
  
  @Test
  def testPart01 {
    
    println; println("testPart01")
    tps.p1.visit(PartCtx[Int](), 
      (i: Int, pc: PartCtx[Int]) => println(i))
    tps.p2.visit(PartCtx[Int](), 
      (i: Int, pc: PartCtx[Int]) => println(i))
  }
  
  @Test
  def testPart02 {
    
    println; println("testPart02")
    tps.partViewTuples.foreach( ptup => ptup match { 
      case (part,str) if part == str => println("matches " + part + " => " + str)
      case (part,str)                => println("DOES NOT MATCH! " + part + " => " + str)
    } )
          
  }

  @Test
  def testPart03 {
    
    println; println("testPart03 combineR")
    var px = tps.p5
    var ix = 0
    println(" => " + px)      
    while (px.isSplit && ix < 10) {
      px = px.combineR( (li,ri) => li * 100 + ri )
      println(" => " + px)      
      ix = ix + 1
    }
  }

  @Test
  def testPart03L {
    
    println; println("testPart03L combineL")
    var px = tps.p5
    var ix = 0
    println(" => " + px)      
    while (px.isSplit && ix < 10) {
      px = px.combineL( (li,ri) => li * 100 + ri )
      println(" => " + px)      
      ix = ix + 1
    }
  }

}