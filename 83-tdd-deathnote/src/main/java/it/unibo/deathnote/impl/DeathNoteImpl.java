package it.unibo.deathnote.impl;

import java.util.LinkedList;

import it.unibo.deathnote.api.DeathNote;

public class DeathNoteImpl implements DeathNote {

    private final LinkedList<Entry> entryList = new LinkedList<>();

    private static class Entry {

        private final String name;
        private String deathCause;
        private String deathDetails;
        private long timeOfWriting;

        public Entry(final String name) {
            this.name = name;
        }

    }

    @Override
    public String getRule(int ruleNumber) throws IllegalArgumentException {
        if(ruleNumber < 1 || ruleNumber > RULES.size()) {
            throw new IllegalArgumentException("Rules range from to " + DeathNote.RULES.size());
        }
        return RULES.get(ruleNumber - 1);
    }

    @Override
    public void writeName(String name) throws NullPointerException {
        if(name == null) {
            throw new NullPointerException("No name was passed");
        }
        entryList.add(new Entry(name));
        entryList.getLast().timeOfWriting = System.currentTimeMillis();
    }

    @Override
    public boolean writeDeathCause(String cause) throws IllegalStateException {
        if(entryList.isEmpty()) {
            throw new IllegalStateException("No name has been written in this note");
        }
        if(cause == null) {
            throw new IllegalStateException("No cause of death was passed");
        }
        final var now = System.currentTimeMillis();
        if(now - entryList.getLast().timeOfWriting <= 40) {
            entryList.getLast().deathCause = cause;
            return true;
        }
        return false;
    }
    
    @Override
    public boolean writeDetails(String details) {
        if(entryList.isEmpty()) {
            throw new IllegalStateException("No name has been written in this note");
        }
        if(details == null) {
            throw new IllegalStateException("No details were passed");
        }
        final var now = System.currentTimeMillis();
        if(now - entryList.getLast().timeOfWriting <= 6040) {
            entryList.getLast().deathDetails = details;
            return true;
        }
        return false;
    }

    @Override
    public String getDeathCause(String name) throws IllegalArgumentException {
        final var iter = entryList.iterator();
        while(iter.hasNext()) {
            var curr = iter.next();
            if(curr.name == name) {
                return curr.deathCause != null ? curr.deathCause : "heart attack";
            }
        }
        throw new IllegalArgumentException("No such name was found in this note");
    }
    
    @Override
    public String getDeathDetails(String name) {
        final var iter = entryList.iterator();
        while(iter.hasNext()) {
            final Entry curr = iter.next();
            if(curr.name == name) {
                return curr.deathDetails != null ? curr.deathDetails : "";
            }
        }
        throw new IllegalArgumentException("No such name was found in this note");
    }

    @Override
    public boolean isNameWritten(String name) {
        final var iter = entryList.iterator();
        while(iter.hasNext()) {
            if(iter.next().name == name) {
                return true;
            }
        }
        return false;
    }
    
}
