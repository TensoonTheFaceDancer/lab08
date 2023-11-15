package it.unibo.deathnote;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.deathnote.api.DeathNote;
import it.unibo.deathnote.impl.DeathNoteImpl;

class TestDeathNote {

    public static final String LIGHT = "Light";
    public static final String MISA = "Misa";
    public static final String NEAR = "Near";

    private DeathNoteImpl myDeathNote;

    @BeforeEach
    public void setup() {
        myDeathNote = new DeathNoteImpl();
    }

    //Rule number 0 and negative rules do not exist in the DeathNote rules.
    @Test
    public void testRulesImplementation() {
        try {
            myDeathNote.getRule(0);
            myDeathNote.getRule(-1);
            fail();
        } catch (IllegalArgumentException e) {
            Assertions.assertTrue(e.getMessage().length() > 0);
        }
    }

    //No rule is empty or null in the DeathNote rules.
    @Test
    public void testRulesNeitherNullNorEmpty() {
        for (String rule : DeathNote.RULES) {
            Assertions.assertNotEquals("", rule);
            Assertions.assertNotNull(rule);
        }
    }

    //The human whose name is written in the DeathNote will eventually die.
    @Test
    public void testHumanShallEventuallyDie() {
        Assertions.assertFalse(myDeathNote.isNameWritten(LIGHT));
        myDeathNote.writeName(LIGHT);
        Assertions.assertTrue(myDeathNote.isNameWritten(LIGHT));
        Assertions.assertFalse(myDeathNote.isNameWritten(MISA));
        Assertions.assertFalse(myDeathNote.isNameWritten(""));
    }

    /*
     * Only if the cause of death is written within the next 40 milliseconds of writing the person's name, it will
     * happen.
     */
    @Test
    public void testDeathHappensInTime() {
        try {
            myDeathNote.writeDeathCause("Looked funny");
            fail();
        } catch (IllegalStateException e) {
            Assertions.assertEquals("No name has been written in this note", e.getMessage());
        }
        myDeathNote.writeName(LIGHT);
        Assertions.assertEquals("heart attack", myDeathNote.getDeathCause(LIGHT));
        myDeathNote.writeName(NEAR);
        Assertions.assertTrue(myDeathNote.writeDeathCause("karting accident"));
        Assertions.assertEquals("karting accident", myDeathNote.getDeathCause(NEAR));
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) { }
        Assertions.assertFalse(myDeathNote.writeDeathCause("skiing accident"));
    }

    /*
     * Only if the cause of death is written within the next 6 seconds and 40 milliseconds of writing the death's
     * details, it will happen.
     */
    @Test
    public void testDetailsTrueInTime() {
        try {
            myDeathNote.writeDetails("Ate without Youtube");
            fail();
        } catch (IllegalStateException e) {
            Assertions.assertEquals("No name has been written in this note", e.getMessage());
        }
        myDeathNote.writeName(LIGHT);
        Assertions.assertEquals("", myDeathNote.getDeathDetails(LIGHT));
        Assertions.assertTrue(myDeathNote.writeDetails("ran for too long"));
        Assertions.assertEquals("ran for too long", myDeathNote.getDeathDetails(LIGHT));
        myDeathNote.writeName(MISA);
        try {
            Thread.sleep(6100);
        } catch (InterruptedException e) { }
        Assertions.assertFalse(myDeathNote.writeDetails("slept too much"));
    }

}