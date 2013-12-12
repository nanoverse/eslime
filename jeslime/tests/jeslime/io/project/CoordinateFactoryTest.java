package jeslime.io.project;

import io.project.CoordinateFactory;
import org.dom4j.Element;
import org.dom4j.tree.BaseElement;

import structural.Flags;
import structural.identifiers.Coordinate;
import jeslime.EslimeTestCase;

public class CoordinateFactoryTest extends EslimeTestCase {

	public void testInstantiateFromElement() {
		Element ce = new BaseElement("coordinate");
		Element ve = new BaseElement("vector");
		Element de = new BaseElement("displacement");
		
		addAttributes(ce, 1, 2, 3);
		addAttributes(ve, 2, 3, 4, 5);
		addAttributes(de, 4, 5, 6);

		doTest(ce, 1, 2, 3 | Flags.PLANAR);
		doTest(ve, 2, 3, 4, 5 | Flags.VECTOR);
		doTest(de, 4, 5, 6 | Flags.VECTOR | Flags.PLANAR);
	}
	
	private void doTest(Element o, int x, int y, int z, int flags) {
		Coordinate c = CoordinateFactory.instantiate(o);
		assertEquals(x, c.x());
		assertEquals(y, c.y());
		assertEquals(z, c.z());
		assertEquals(flags, c.flags());
	}
	
	private void doTest(Object e, int x, int y, int z, int flags) {
		Coordinate c = CoordinateFactory.instantiate(e);
		assertEquals(x, c.x());
		assertEquals(y, c.y());
		assertEquals(z, c.z());
		assertEquals(flags, c.flags());
	}
	private void doTest(Element e, int x, int y, int flags) {
		Coordinate c = CoordinateFactory.instantiate(e);
		assertEquals(x, c.x());
		assertEquals(y, c.y());
		assertEquals(flags, c.flags());
	}

	private void addAttributes(Element e, int x, int y, int flags) {
		e.addAttribute("x", Integer.toString(x));
		e.addAttribute("y", Integer.toString(y));
		e.addAttribute("flags", Integer.toString(flags));
	}
	
	private void doTest(Object o, int x, int y, int flags) {
		Coordinate c = CoordinateFactory.instantiate(o);
		assertEquals(x, c.x());
		assertEquals(y, c.y());
		assertEquals(flags, c.flags());		
	}
	
	private void addAttributes(Element e, int x, int y, int z, int flags) {
		addAttributes(e, x, y, flags);
		e.addAttribute("z", Integer.toString(z));
	}
	
	public void testInstantiateFromObject() {
		Element ce = new BaseElement("coordinate");
		Element ve = new BaseElement("vector");
		Element de = new BaseElement("displacement");
		
		addAttributes(ce, 1, 2, 3);
		addAttributes(ve, 2, 3, 4, 5);
		addAttributes(de, 4, 5, 6);

		doTest((Object) ce, 1, 2, 3 | Flags.PLANAR);
		doTest((Object) ve, 2, 3, 4, 5 | Flags.VECTOR);
		doTest((Object) de, 4, 5, 6 | Flags.PLANAR | Flags.VECTOR);
	}

}
