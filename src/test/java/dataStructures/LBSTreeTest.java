package dataStructures;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class LBSTreeTest {

    private LBSTree<Integer, String> tree;

    @BeforeEach
    public void setUp() {
        tree = new LBSTree<Integer, String>();
    }

    protected void populate(LBSTree<Integer, String> tree) {
        tree.insert(4, "4");
        tree.insert(3, "3");
        tree.insert(9, "9");
        tree.insert(6, "6");
        tree.insert(1, "1");
        tree.insert(7, "7");
        tree.insert(2, "2");
        tree.insert(5, "5");
        tree.insert(8, "8");
    }

    @Test
    public void testConstructorEmpty() {
        LBSTree<Integer, String> tree = new LBSTree<Integer, String>();
        assertTrue(tree.isEmpty());
        assertNull(tree.getKey());
        assertNull(tree.getInfo());
        assertNull(tree.getLeft());
        assertNull(tree.getRight());
    }

    @Test
    public void testConstructorKeyAndData() {
        LBSTree<Integer, String> tree = new LBSTree<Integer, String>(1, "1");
        assertFalse(tree.isEmpty());
        assertEquals(1, tree.getKey());
        assertEquals("1", tree.getInfo());
        assertNull(tree.getLeft());
        assertNull(tree.getRight());
    }

    @Test
    public void testInsert() {
        tree.insert(1, "1");
        tree.insert(2, "2");
        assertFalse(tree.isEmpty());
        assertTrue("1".equals(tree.getInfo()));
        assertTrue("2".equals(tree.getRight().getInfo()));
    }

    @Test
    public void testIterator() {
        populate(tree);
        Iterator<String> iterator = tree.iterator();
        List<String> expectedList = Arrays.asList("4", "3", "1", "2", "9", "6", "5", "7", "8");
        for (String expected : expectedList) {
            assertTrue(iterator.hasNext());
            assertEquals(expected, iterator.next());
        }
    }

    @Test
    public void testIteratorPreOrder() {
        populate(tree);
        Iterator<String> iterator = tree.iteratorPreOrder();
        List<String> expectedList = Arrays.asList("4", "3", "1", "2", "9", "6", "5", "7", "8");
        for (String expected : expectedList) {
            assertTrue(iterator.hasNext());
            assertEquals(expected, iterator.next());
        }
    }

    @Test
    public void testIteratorInOrder() {
        populate(tree);
        Iterator<String> iterator = tree.iteratorInOrder();
        List<String> expectedList = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9");
        for (String expected : expectedList) {
            assertTrue(iterator.hasNext());
            assertEquals(expected, iterator.next());
        }
    }

    @Test
    public void testIteratorPostOrder() {
        populate(tree);
        Iterator<String> iterator = tree.iteratorPostOrder();
        List<String> expectedList = Arrays.asList("2", "1", "3", "5", "8", "7", "6", "9", "4");
        for (String expected : expectedList) {
            assertTrue(iterator.hasNext());
            assertEquals(expected, iterator.next());
        }
    }

    @Test
    public void testToStringPreOrder() {
        populate(tree);
        assertEquals("4, 3, 1, 2, 9, 6, 5, 7, 8", tree.toStringPreOrder());
    }
}
