package ataxx;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class MoveTest {


    @Test
    public void testIsCloneTrue(){
        assertTrue(Move.isClone("c1","d2"));
        assertTrue(Move.isClone("b1","b2"));
    }

    @Test
    public void testIsCloneFalse(){
        assertFalse(Move.isClone("c1","c1"));
        assertFalse(Move.isClone("c1","c3"));
    }


    @Test
    public void testIsJumpTrue(){
        assertTrue(Move.isJump("c1","d3"));
        assertTrue(Move.isJump("c1","c3"));
    }

    @Test
    public void testIsJumpFalse(){
        assertFalse(Move.isJump("c4","d3"));
        assertFalse(Move.isJump("b2","b5"));
    }


}
