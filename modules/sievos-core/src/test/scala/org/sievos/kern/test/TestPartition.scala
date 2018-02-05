package org.sievos.kern
package test

import org.junit.Test



class TestPartition {
  
//  var testoutdir: File = _
  
  object tps {
      val p0 = Part[String]()
      val p1 = Part("A")
      val p1b = Part("B")
      val p2 = p0 | p1
      val p3 = p1 | p0 | p1
      val p3b = Part[String]() |+ "L" |+ "M" |+ "N" |+ "O" |+ "P"
      val p4 = p1 |+ "C"
      val p5 = p1b | Part[String](p3,p4) | Part[String]("D")
      val partViewTuples =  List( 
            (p0, "(|)"), 
            (p1, "A") ,
            (p2, "A"),
            (p3, "A|A"),
            (p3b,"L|M|N|O|P"),
            (p4, "A|C"),
            (p5, "B|(A|A|(A|C))|D") )
  }
  
  @Test
  def testPart01 {
    
    println; println("testPart01")
    tps.p1.ctxVisit( (i: String, pc: List[Int]) => println(i) )
    tps.p2.ctxVisit( (i: String, pc: List[Int]) => println(i) )
  }
  
  @Test
  def testPart02 {
    
    println; println("testPart02")
    tps.partViewTuples.foreach( ptup => ptup match { 
        case (part,str) => {
          val matstr = if (part.toString() == str) "Matches  " else "DOES NOT MATCH! "
          println(matstr + part + " => " + str)
        }
        case _ => 
          println("Unknown " + ptup)
      }
    )
  }

  @Test
  def testBalR1 {
    
    println; println("testBalR1")
//    tps.partViewTuples.map( case (part,str) => part)
    List(tps.p5)    
    .foreach( part => {
          val br = part.skewR
          println("%20s => balR=%s".format(part,br))
      }
    )
  }

  
  @Test
  def testPart03 {
    
    println; println("testPart03 combineR")
    var px = tps.p5
    var ix = 0
    println(" => " + px)      
    while (px.isSplit && ix < 10) {
      px = px.combineR( (li,ri) => li + ri )
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
      px = px.combineL( (li,ri) => li + ri )
      println(" => " + px)      
      ix = ix + 1
    }
  }

 
}