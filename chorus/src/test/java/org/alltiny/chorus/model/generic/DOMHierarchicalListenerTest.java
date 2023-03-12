package org.alltiny.chorus.model.generic;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DOMHierarchicalListenerTest {

    /**
     * In the test a hierarchical listener is added to a filled model.
     * The listener should be triggered.
     */
    @Test
    public void testInitialization() {
        final Train train = new Train().setNumber("AB-12")
            .addCoach(new Coach().setNumber("1")
                .addPassenger(new Passenger().setName("Frank"))
                .addPassenger(new Passenger().setName("Melina")));

        final List<String> seenNames = new ArrayList<>();

        train.addListener(
            new DOMHierarchicalListener<>(
                new DOMHierarchicalListener.PropertyOnMap<>(Train.class, Train.COACHES),
            new DOMHierarchicalListener<>(
                new DOMHierarchicalListener.AnyItemInList<>(),
            new DOMHierarchicalListener<>(
                new DOMHierarchicalListener.PropertyOnMap<>(Coach.class, Coach.PASSENGERS),
            new DOMHierarchicalListener<>(
                new DOMHierarchicalListener.AnyItemInList<>(),
            new DOMHierarchicalListener<>(
                new DOMHierarchicalListener.PropertyOnMap<>(Passenger.class, Passenger.NAME),
                new DOMHierarchicalListener.Callback<String,String>() {
                    @Override
                    public void added(String name, String property, Context<?> context) {
                        seenNames.add(name);
                    }

                    @Override
                    public void changed(String name, String property, Context<?> context) {
                        Assert.fail("should not have been called in this test");
                    }

                    @Override
                    public void removed(String name, String property, Context<?> context) {
                        Assert.fail("should not have been called in this test");
                    }
                })
            )))));

        Assert.assertEquals("passenger names are",
            Arrays.asList("Frank", "Melina"),
            seenNames);
    }

    /**
     * In the test a part of the model is replaced.
     * The listener should be triggered.
     */
    @Test
    public void testReplacement() {
        final Train train = new Train().setNumber("AB-12")
            .addCoach(new Coach().setNumber("1")
                .addPassenger(new Passenger().setName("Frank"))
                .addPassenger(new Passenger().setName("Melina")));

        final List<String> seenNames = new ArrayList<>();

        train.addListener(
            new DOMHierarchicalListener<>(
                new DOMHierarchicalListener.PropertyOnMap<>(Train.class, Train.COACHES),
            new DOMHierarchicalListener<>(
                new DOMHierarchicalListener.AnyItemInList<>(),
            new DOMHierarchicalListener<>(
                new DOMHierarchicalListener.PropertyOnMap<>(Coach.class, Coach.PASSENGERS),
            new DOMHierarchicalListener<>(
                new DOMHierarchicalListener.AnyItemInList<>(),
            new DOMHierarchicalListener<>(
                new DOMHierarchicalListener.PropertyOnMap<>(Passenger.class, Passenger.NAME),
                new DOMHierarchicalListener.Callback<String,String>() {
                    @Override
                    public void added(String name, String property, Context<?> context) {
                        seenNames.add(name);
                    }

                    @Override
                    public void changed(String name, String property, Context<?> context) {
                        Assert.fail("should not have been called in this test");
                    }

                    @Override
                    public void removed(String name, String property, Context<?> context) {
                        seenNames.remove(name);
                    }
                })
            )))));

        train.setCoach(0, new Coach()
            .setNumber("x")
            .addPassenger(new Passenger().setName("Holmes")));

        Assert.assertEquals("passenger names are",
            Arrays.asList("Holmes"),
            seenNames);
    }

    public static class Train extends DOMMap<Train,Object> {

        private static final String NUMBER = "number";
        private static final String COACHES = "coaches";

        public Train() {
            put(COACHES, new DOMList<DOMList<?,Coach>,Coach>());
        }

        public String getNumber() {
            return (String)get(NUMBER);
        }

        public Train setNumber(String number) {
            put(NUMBER, number);
            return this;
        }

        public List<Coach> getCoaches() {
            return (List<Coach>)get(COACHES);
        }

        public Train addCoach(Coach coach) {
            getCoaches().add(coach);
            return this;
        }

        public Train setCoach(int index, Coach coach) {
            getCoaches().set(index, coach);
            return this;
        }
    }

    public static class Coach extends DOMMap<Coach,Object> {

        private static final String NUMBER = "number";
        private static final String PASSENGERS = "passengers";

        public Coach() {
            put(PASSENGERS, new DOMList<DOMList<?,Passenger>,Passenger>());
        }

        public String getNumber() {
            return (String)get(NUMBER);
        }

        public Coach setNumber(String number) {
            put(NUMBER, number);
            return this;
        }

        public List<Passenger> getPassengers() {
            return (List<Passenger>)get(PASSENGERS);
        }

        public Coach addPassenger(Passenger passenger) {
            getPassengers().add(passenger);
            return this;
        }
    }

    public static class Passenger extends DOMMap<Passenger,Object> {

        private static final String NAME = "name";

        public String getName() {
            return (String)get(NAME);
        }

        public Passenger setName(String name) {
            put(NAME, name);
            return this;
        }
    }
}
