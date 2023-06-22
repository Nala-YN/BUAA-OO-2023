package com.oocourse.spec1.main;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;

public class Runner {

    private String[] cmds;
    private Network network;
    private Class<? extends Person> personClass;
    private Class<? extends Network> networkClass;
    private Constructor<? extends Person> personConstructor;
    private Constructor<? extends Network> networkConstructor;
    private Scanner cin;

    public Runner(Class<? extends Person> personClass, Class<? extends Network> networkClass) throws NoSuchMethodException, SecurityException {
        this.personClass = personClass;
        this.networkClass = networkClass;
        personConstructor = this.personClass.getConstructor(
                int.class, String.class, int.class);
        networkConstructor = this.networkClass.getConstructor();
        cin = new Scanner(System.in);
    }

    public void run()
            throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        this.network = (Network) this.networkConstructor.newInstance();
        while (cin.hasNextLine()) {
            String cmd = cin.nextLine();
            cmds = cmd.split(" ");
            if (cmds[0].equals("ap")) {
                addPerson();
            } else if (cmds[0].equals("ar")) {
                addRelation();
            } else if (cmds[0].equals("qv")) {
                queryValue();
            } else if (cmds[0].equals("qci")) {
                queryCircle();
            } else if (cmds[0].equals("qbs")) {
                queryBlockSum();
            } else if (cmds[0].equals("qts")) {
                queryTripleSum();
            } else if (cmds[0].equals("qtsok")) {
                queryTripleSumOKTest();
            } else {
//                throw new AssertionError();
            }
        }
        cin.close();
    }

    private HashMap<Integer, HashMap<Integer, Integer>> getModifyRelationOKData(ArrayList<String> cmdList, boolean needCheck) throws Exception {
        HashMap<Integer, HashMap<Integer, Integer>> ret = new HashMap<>();
        int n = Integer.parseInt(cmdList.remove(0));
        if (n < 0 || n > 100) {
            throw new Exception("Invalid people number!");
        }
        ArrayList<Integer> idList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int id = Integer.parseInt(cmdList.remove(0));
            ret.put(id, new HashMap<>());
            idList.add(id);
        }
        for (int i = 0; i < n; i++) {
            int cnt = Integer.parseInt(cmdList.remove(0));
            if (cnt < 0 || cnt > n - 1) {
                throw new Exception("person " + idList.get(i) + ": The number of acquaintances is invalid!");
            }
            HashMap<Integer, Integer> map = ret.get(idList.get(i));
            for (int j = 1; j <= cnt; j++) {
                int id = Integer.parseInt(cmdList.remove(0));
                int value = Integer.parseInt(cmdList.remove(0));
                if (!ret.containsKey(id) || id == idList.get(i) || map.containsKey(id)) {
                    throw new Exception("person " + idList.get(i) + " acquaintance " + id + " is invalid!");
                }
                map.put(id, value);
            }
        }

        if (needCheck) {
            for (Integer id1 : ret.keySet()) {
                HashMap<Integer, Integer> map = ret.get(id1);
                for (Integer id2 : map.keySet()) {
                    int value = map.get(id2);
                    if (value <= 0 || ret.get(id2).get(id1) != value) {
                        throw new Exception("beforeData is invalid!");
                    }
                }
            }
        }
        return ret;
    }

    private void queryTripleSumOKTest() {
        HashMap<Integer, HashMap<Integer, Integer>> beforeData;
        HashMap<Integer, HashMap<Integer, Integer>> afterData;
        int result;
        ArrayList<String> cmdList = new ArrayList<>(Arrays.asList(cmds));
        cmdList.remove(0);
        try {
            beforeData = getModifyRelationOKData(cmdList, true);
            afterData = getModifyRelationOKData(cmdList, false);
            result = Integer.parseInt(cmdList.remove(0));
            Network tmpNetwork = (Network) this.networkConstructor.newInstance();
            System.out.println(tmpNetwork.queryTripleSumOKTest(beforeData, afterData, result));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void queryTripleSum() {
        System.out.println(network.queryTripleSum());
    }


    private void queryBlockSum() {
        System.out.println(network.queryBlockSum());
    }
    private void queryCircle() {
        int id1 = Integer.parseInt(cmds[1]);
        int id2 = Integer.parseInt(cmds[2]);
        boolean ret = false;
        try {
            System.out.println(network.isCircle(id1, id2));
        } catch (PersonIdNotFoundException e) {
            e.print();
        }
    }


    private void queryValue() {
        int id1 = Integer.parseInt(cmds[1]);
        int id2 = Integer.parseInt(cmds[2]);
        int ret = 0;
        try {
            ret = network.queryValue(id1, id2);
        } catch (PersonIdNotFoundException e) {
            e.print();
            return;
        } catch (RelationNotFoundException e) {
            e.print();
            return;
        }
        System.out.println(ret);
    }

    private void addRelation() {
        int id1 = Integer.parseInt(cmds[1]);
        int id2 = Integer.parseInt(cmds[2]);
        int value = Integer.parseInt(cmds[3]);
        try {
            network.addRelation(id1, id2, value);
        } catch (PersonIdNotFoundException e) {
            e.print();
            return;
        } catch (EqualRelationException e) {
            e.print();
            return;
        }
        System.out.println(String.format("Ok"));
    }

    private void addPerson()
            throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        int id = Integer.parseInt(cmds[1]);
        String name = cmds[2];
        int age = Integer.parseInt(cmds[3]);
        try {
            network.addPerson((Person) this.personConstructor.newInstance(
                    id, name, age));
        } catch (EqualPersonIdException e) {
            e.print();
            return;
        }
        System.out.println(String.format("Ok"));
    }
}
