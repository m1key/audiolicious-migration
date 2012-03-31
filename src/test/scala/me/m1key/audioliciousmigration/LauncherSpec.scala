package me.m1key.audioliciousmigration

import org.specs._
import org.specs.runner._
import org.junit.runner.RunWith

@RunWith(classOf[JUnitSuiteRunner])
class LauncherSpec extends Specification with JUnit {
	
	"2 + 2" should {	
		"equal 4" in {
			2 + 2 mustBe 4
		}
	}

}