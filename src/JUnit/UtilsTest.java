package JUnit;

import static org.junit.Assert.*;

import org.junit.Test;

import static Utils.Utils.*;

public class UtilsTest {
	@Test
	public void testScaleValueToRange() {
		assertTrue(scaleValueToRange(2, 0, 4, 0, 10) == 5);
		assertTrue(scaleValueToRange(1, 0, 1, 0, 2) == 2);
		assertTrue(scaleValueToRange(0, 0, 1, 0, 2) == 0);
	}

	@Test
	public void testParseNumber() {
		assertTrue("1".equals(parseNumber("1:abc")));
		assertTrue("2".equals(parseNumber("2:abc")));
		assertFalse("2".equals(parseNumber("1:abc")));
		assertTrue("1".equals(parseNumber("1:a")));
		assertTrue("".equals(parseNumber("")));
	}

	@Test
	public void testParseSequence() {
		assertTrue("abc".equals(parseSequence("1:abc")));
		assertTrue("abc".equals(parseSequence("2:abc")));
		assertFalse("abcd".equals(parseSequence("1:abc")));
		assertTrue("a".equals(parseSequence("3:a")));
		assertTrue("".equals(parseSequence("")));
	}

}
