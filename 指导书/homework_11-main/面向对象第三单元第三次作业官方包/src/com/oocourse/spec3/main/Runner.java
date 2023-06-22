package com.oocourse.spec3.main;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import com.oocourse.spec3.exceptions.MessageIdNotFoundException;
import com.oocourse.spec3.exceptions.PersonIdNotFoundException;
import com.oocourse.spec3.exceptions.AcquaintanceNotFoundException;
import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;
import com.oocourse.spec3.exceptions.EqualEmojiIdException;
import com.oocourse.spec3.exceptions.EqualGroupIdException;
import com.oocourse.spec3.exceptions.EqualMessageIdException;
import com.oocourse.spec3.exceptions.EqualPersonIdException;
import com.oocourse.spec3.exceptions.EqualRelationException;
import com.oocourse.spec3.exceptions.GroupIdNotFoundException;
import com.oocourse.spec3.exceptions.PathNotFoundException;
import com.oocourse.spec3.exceptions.RelationNotFoundException;

public class Runner {

    private String[] cmds;
    private Network network;
    private Class<? extends Person> personClass;
    private Class<? extends Network> networkClass;
    private Class<? extends Group> groupClass;
    private Class<? extends Message> messageClass;
    private Class<? extends EmojiMessage> emojiClass;
    private Class<? extends NoticeMessage> noticeClass;
    private Class<? extends RedEnvelopeMessage> redEnvelopeClass;
    private Constructor<? extends Person> personConstructor;
    private Constructor<? extends Network> networkConstructor;
    private Constructor<? extends Group> groupConstructor;
    private Constructor<? extends Message> messageConstructor0;
    private Constructor<? extends Message> messageConstructor1;
    private Constructor<? extends EmojiMessage> emojiConstructor0;
    private Constructor<? extends EmojiMessage> emojiConstructor1;
    private Constructor<? extends RedEnvelopeMessage> redEnvelopeConstructor0;
    private Constructor<? extends RedEnvelopeMessage> redEnvelopeConstructor1;
    private Constructor<? extends NoticeMessage> noticeConstructor0;
    private Constructor<? extends NoticeMessage> noticeConstructor1;
    private Scanner cin;

    public Runner(Class<? extends Person> personClass, Class<? extends Network> networkClass,
                  Class<? extends Group> groupClass, Class<? extends Message> messageClass,
                  Class<? extends EmojiMessage> emojiClass, Class<? extends NoticeMessage> noticeClass,
                  Class<? extends RedEnvelopeMessage> redEnvelopeClass) throws NoSuchMethodException, SecurityException {
        this.personClass = personClass;
        this.networkClass = networkClass;
        this.groupClass = groupClass;
        this.messageClass = messageClass;
        this.emojiClass = emojiClass;
        this.noticeClass = noticeClass;
        this.redEnvelopeClass = redEnvelopeClass;
        personConstructor = this.personClass.getConstructor(
                int.class, String.class, int.class);
        networkConstructor = this.networkClass.getConstructor();
        groupConstructor = this.groupClass.getConstructor(int.class);
        messageConstructor0 = this.messageClass.getConstructor(int.class, int.class, Person.class, Person.class);
        messageConstructor1 = this.messageClass.getConstructor(int.class, int.class, Person.class, Group.class);
        emojiConstructor0 = this.emojiClass.getConstructor(int.class, int.class, Person.class, Person.class);
        emojiConstructor1 = this.emojiClass.getConstructor(int.class, int.class, Person.class, Group.class);
        noticeConstructor0 = this.noticeClass.getConstructor(int.class, String.class, Person.class, Person.class);
        noticeConstructor1 = this.noticeClass.getConstructor(int.class, String.class, Person.class, Group.class);
        redEnvelopeConstructor0 = this.redEnvelopeClass.getConstructor(int.class, int.class, Person.class, Person.class);
        redEnvelopeConstructor1 = this.redEnvelopeClass.getConstructor(int.class, int.class, Person.class, Group.class);
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
            } else if (cmds[0].equals("ag")) {
                addGroup();
            } else if (cmds[0].equals("atg")) {
                addToGroup();
            } else if (cmds[0].equals("dfg")) {
                delFromGroup();
            } else if (cmds[0].equals("qgvs")) {
                queryGroupValueSum();
            } else if (cmds[0].equals("qgav")) {
                queryGroupAgeVar();
            } else if (cmds[0].equals("mr")) {
                modifyRelation();
            } else if (cmds[0].equals("qba")) {
                queryBestAcquaintance();
            } else if (cmds[0].equals("qcs")) {
                queryCoupleSum();
            } else if (cmds[0].equals("am")) {
                addMessage();
            } else if (cmds[0].equals("sm")) {
                sendMessage();
            } else if (cmds[0].equals("qsv")) {
                querySocialValue();
            } else if (cmds[0].equals("qrm")) {
                queryReceivedMessages();
            } else if (cmds[0].equals("arem")) {
                addRedEnvelopeMessage();
            } else if (cmds[0].equals("anm")) {
                addNoticeMessage();
            } else if (cmds[0].equals("cn")) {
                clearNotices();
            } else if (cmds[0].equals("aem")) {
                addEmojiMessage();
            } else if (cmds[0].equals("sei")) {
                storeEmojiId();
            } else if (cmds[0].equals("qp")) {
                queryPopularity();
            } else if (cmds[0].equals("dce")) {
                deleteColdEmoji();
            } else if (cmds[0].equals("qm")) {
                queryMoney();
            } else if (cmds[0].equals("qlm")) {
                queryLeastMoments();
            } else if (cmds[0].equals("dceok")) {
                deleteColdEmojiOKTest();
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

    private ArrayList<HashMap<Integer, Integer>> getDeleteColdEmojiOKData(ArrayList<String> cmdList, boolean needCheck) throws Exception {
        HashMap<Integer, Integer> emojiId2EmojiHeat = new HashMap<>();
        HashMap<Integer, Integer> messageId2EmojiId = new HashMap<>();
        int n = Integer.parseInt(cmdList.remove(0));
        int m = Integer.parseInt(cmdList.remove(0));
        if (n < 0 || n > 100) {
            throw new Exception("emoji number is invalid: " + n);
        }
        if (m < 0 || m > 100) {
            throw new Exception("message number is invalid" + m);
        }
        for (int i = 0; i < n; i++) {
            int id = Integer.parseInt(cmdList.remove(0));
            int heat = Integer.parseInt(cmdList.remove(0));
            if (emojiId2EmojiHeat.containsKey(id)) {
                throw new Exception("emojiId2EmojiHeat id is duplicate: " + id);
            }
            if (id == 0) {
                throw new Exception("emojiId can't be 0");
            }
            emojiId2EmojiHeat.put(id, heat);
        }
        for (int i = 0; i < m; i++) {
            int id = Integer.parseInt(cmdList.remove(0));
            Integer emojiId;
            if (messageId2EmojiId.containsKey(id)) {
                throw new Exception("messageId id is duplicate: " + id);
            }
            String token = cmdList.remove(0);
            if (token.equals("null")) {
                emojiId = null;
            } else {
                emojiId = Integer.parseInt(token);
            }
            if (needCheck) {
                if (emojiId != null && !emojiId2EmojiHeat.containsKey(emojiId)) {
                    throw new Exception("beforeData is invalid! emojiMessage has invalid emojiId: " + emojiId);
                }
            }
            messageId2EmojiId.put(id, emojiId);
        }
        ArrayList<HashMap<Integer, Integer>> ret = new ArrayList<>();
        ret.add(emojiId2EmojiHeat);
        ret.add(messageId2EmojiId);
        return ret;
    }

    private void deleteColdEmojiOKTest() {
        ArrayList<HashMap<Integer, Integer>> beforeData;
        ArrayList<HashMap<Integer, Integer>> afterData;
        int limit, result;
        ArrayList<String> cmdList = new ArrayList<>(Arrays.asList(cmds));
        cmdList.remove(0);
        try {
            beforeData = getDeleteColdEmojiOKData(cmdList, true);
            limit = Integer.parseInt(cmdList.remove(0));
            afterData = getDeleteColdEmojiOKData(cmdList, false);
            result = Integer.parseInt(cmdList.remove(0));
            Network tmpNetwork = (Network) this.networkConstructor.newInstance();
            int ret = tmpNetwork.deleteColdEmojiOKTest(limit, beforeData, afterData, result);
            if (ret == 0) {
                System.out.println("true");
            } else {
                System.out.println("false, first fail in ensure " + ret);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void queryCoupleSum() {
        System.out.println(network.queryCoupleSum());
    }

    private void queryTripleSum() {
        System.out.println(network.queryTripleSum());
    }


    private void clearNotices() {
        int id = Integer.parseInt(cmds[1]);
        try {
            network.clearNotices(id);
        } catch (PersonIdNotFoundException e) {
            e.print();
            return;
        }
        System.out.println("Ok");
    }

    private void queryLeastMoments() {
        int id = Integer.parseInt(cmds[1]);
        int ret = 0;
        try {
            ret = network.queryLeastMoments(id);
        } catch (PersonIdNotFoundException e) {
            e.print();
            return;
        } catch (PathNotFoundException e) {
            e.print();
            return;
        }
        System.out.println(ret);
    }

    private void queryMoney() {
        int id = Integer.parseInt(cmds[1]);
        int ret = 0;
        try {
            ret = network.queryMoney(id);
        } catch (PersonIdNotFoundException e) {
            e.print();
            return;
        }
        System.out.println(ret);
    }

    private void queryBlockSum() {
        System.out.println(network.queryBlockSum());
    }

    private void delFromGroup() {
        int id1 = Integer.parseInt(cmds[1]);
        int id2 = Integer.parseInt(cmds[2]);
        try {
            network.delFromGroup(id1, id2);
        } catch (GroupIdNotFoundException e) {
            e.print();
            return;
        } catch (PersonIdNotFoundException e) {
            e.print();
            return;
        } catch (EqualPersonIdException e) {
            e.print();
            return;
        }
        System.out.println("Ok");
    }

    private void queryGroupAgeVar() {
        int id = Integer.parseInt(cmds[1]);
        int ret = 0;
        try {
            ret = network.queryGroupAgeVar(id);
        } catch (GroupIdNotFoundException e) {
            e.print();
            return;
        }
        System.out.println(ret);
    }

    private void queryGroupValueSum() {
        int id = Integer.parseInt(cmds[1]);
        int ret = 0;
        try {
            ret = network.queryGroupValueSum(id);
        } catch (GroupIdNotFoundException e) {
            e.print();
            return;
        }
        System.out.println(ret);
    }


    private void addToGroup() {
        int id1 = Integer.parseInt(cmds[1]);
        int id2 = Integer.parseInt(cmds[2]);
        try {
            network.addToGroup(id1, id2);
        } catch (GroupIdNotFoundException e) {
            e.print();
            return;
        } catch (PersonIdNotFoundException e) {
            e.print();
            return;
        } catch (EqualPersonIdException e) {
            e.print();
            return;
        }
        System.out.println("Ok");
    }

    private void addGroup()
            throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        int id = Integer.parseInt(cmds[1]);
        try {
            network.addGroup((Group) this.groupConstructor.newInstance(id));
        } catch (EqualGroupIdException e) {
            e.print();
            return;
        }
        System.out.println("Ok");
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

    private void modifyRelation() {
        int id1 = Integer.parseInt(cmds[1]);
        int id2 = Integer.parseInt(cmds[2]);
        int value = Integer.parseInt(cmds[3]);
        try {
            network.modifyRelation(id1, id2, value);
        } catch (PersonIdNotFoundException e) {
            e.print();
            return;
        } catch (RelationNotFoundException e) {
            e.print();
            return;
        } catch (EqualPersonIdException e) {
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

    private void storeEmojiId() {
        int id = Integer.parseInt(cmds[1]);
        try {
            network.storeEmojiId(id);
        } catch (EqualEmojiIdException e) {
            e.print();
            return;
        }
        System.out.println(String.format("Ok"));
    }

    private void addMessage() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        int id = Integer.parseInt(cmds[1]);
        int socialValue = Integer.parseInt(cmds[2]);
        int type = Integer.parseInt(cmds[3]);
        int id1 = Integer.parseInt(cmds[4]);
        int id2 = Integer.parseInt(cmds[5]);
        if (type == 0) {
            if (!network.contains(id1) || !network.contains(id2)) {
                System.out.println(String.format("The person with this number does not exist"));
                return;
            }
            Person person1 = network.getPerson(id1);
            Person person2 = network.getPerson(id2);
            Message message = this.messageConstructor0.newInstance(id, socialValue, person1, person2);
            try {
                network.addMessage(message);
            } catch (EqualMessageIdException e) {
                e.print();
                return;
            } catch (EmojiIdNotFoundException e) {
                e.print();
                return;
            } catch (EqualPersonIdException e) {
                e.print();
                return;
            }
        } else if (type == 1) {
            Group group = network.getGroup(id2);
            if (group == null) {
                System.out.println("Group does not exist");
                return;
            }
            if (!network.contains(id1)) {
                System.out.println("The person with this number does not exist");
                return;
            }
            Person person1 = network.getPerson(id1);
            Message message = this.messageConstructor1.newInstance(id, socialValue, person1, group);
            try {
                network.addMessage(message);
            } catch (EqualMessageIdException e) {
                e.print();
                return;
            } catch (EmojiIdNotFoundException e) {
                e.print();
                return;
            } catch (EqualPersonIdException e) {
                e.print();
                return;
            }
        } else {
            return;
        }
        System.out.println(String.format("Ok"));
    }

    private void addRedEnvelopeMessage() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        int id = Integer.parseInt(cmds[1]);
        int money = Integer.parseInt(cmds[2]);
        int type = Integer.parseInt(cmds[3]);
        int id1 = Integer.parseInt(cmds[4]);
        int id2 = Integer.parseInt(cmds[5]);
        if (type == 0) {
            if (!network.contains(id1) || !network.contains(id2)) {
                System.out.println(String.format("The person with this number does not exist"));
                return;
            }
            Person person1 = network.getPerson(id1);
            Person person2 = network.getPerson(id2);
            RedEnvelopeMessage message = this.redEnvelopeConstructor0.newInstance(id, money, person1, person2);
            try {
                network.addMessage(message);
            } catch (EqualMessageIdException e) {
                e.print();
                return;
            } catch (EmojiIdNotFoundException e) {
                e.print();
                return;
            } catch (EqualPersonIdException e) {
                e.print();
                return;
            }
        } else if (type == 1) {
            Group group = network.getGroup(id2);
            if (group == null) {
                System.out.println("Group does not exist");
                return;
            }
            if (!network.contains(id1)) {
                System.out.println("The person with this number does not exist");
                return;
            }
            Person person1 = network.getPerson(id1);
            RedEnvelopeMessage message = this.redEnvelopeConstructor1.newInstance(id, money, person1, group);
            try {
                network.addMessage(message);
            } catch (EqualMessageIdException e) {
                e.print();
                return;
            } catch (EmojiIdNotFoundException e) {
                e.print();
                return;
            } catch (EqualPersonIdException e) {
                e.print();
                return;
            }
        } else {
            return;
        }
        System.out.println(String.format("Ok"));
    }

    private void addNoticeMessage() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        int id = Integer.parseInt(cmds[1]);
        String string = cmds[2];
        int type = Integer.parseInt(cmds[3]);
        int id1 = Integer.parseInt(cmds[4]);
        int id2 = Integer.parseInt(cmds[5]);
        if (type == 0) {
            if (!network.contains(id1) || !network.contains(id2)) {
                System.out.println(String.format("The person with this number does not exist"));
                return;
            }
            Person person1 = network.getPerson(id1);
            Person person2 = network.getPerson(id2);
            NoticeMessage message = this.noticeConstructor0.newInstance(id, string, person1, person2);
            try {
                network.addMessage(message);
            } catch (EqualMessageIdException e) {
                e.print();
                return;
            } catch (EmojiIdNotFoundException e) {
                e.print();
                return;
            } catch (EqualPersonIdException e) {
                e.print();
                return;
            }
        } else if (type == 1) {
            Group group = network.getGroup(id2);
            if (group == null) {
                System.out.println("Group does not exist");
                return;
            }
            if (!network.contains(id1)) {
                System.out.println("The person with this number does not exist");
                return;
            }
            Person person1 = network.getPerson(id1);
            NoticeMessage message = this.noticeConstructor1.newInstance(id, string, person1, group);
            try {
                network.addMessage(message);
            } catch (EqualMessageIdException e) {
                e.print();
                return;
            } catch (EmojiIdNotFoundException e) {
                e.print();
                return;
            } catch (EqualPersonIdException e) {
                e.print();
                return;
            }
        } else {
            return;
        }
        System.out.println(String.format("Ok"));
    }

    private void addEmojiMessage() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        int id = Integer.parseInt(cmds[1]);
        int emojiId = Integer.parseInt(cmds[2]);
        int type = Integer.parseInt(cmds[3]);
        int id1 = Integer.parseInt(cmds[4]);
        int id2 = Integer.parseInt(cmds[5]);
        if (type == 0) {
            if (!network.contains(id1) || !network.contains(id2)) {
                System.out.println(String.format("The person with this number does not exist"));
                return;
            }
            Person person1 = network.getPerson(id1);
            Person person2 = network.getPerson(id2);
            EmojiMessage message = this.emojiConstructor0.newInstance(id, emojiId, person1, person2);
            try {
                network.addMessage(message);
            } catch (EqualMessageIdException e) {
                e.print();
                return;
            } catch (EmojiIdNotFoundException e) {
                e.print();
                return;
            } catch (EqualPersonIdException e) {
                e.print();
                return;
            }
        } else if (type == 1) {
            Group group = network.getGroup(id2);
            if (group == null) {
                System.out.println("Group does not exist");
                return;
            }
            if (!network.contains(id1)) {
                System.out.println("The person with this number does not exist");
                return;
            }
            Person person1 = network.getPerson(id1);
            EmojiMessage message = this.emojiConstructor1.newInstance(id, emojiId, person1, group);
            try {
                network.addMessage(message);
            } catch (EqualMessageIdException e) {
                e.print();
                return;
            } catch (EmojiIdNotFoundException e) {
                e.print();
                return;
            } catch (EqualPersonIdException e) {
                e.print();
                return;
            }
        } else {
            return;
        }
        System.out.println(String.format("Ok"));
    }

    private void sendMessage() {
        int messageId = Integer.parseInt(cmds[1]);
        try {
            network.sendMessage(messageId);
        } catch (RelationNotFoundException e) {
            e.print();
            return;
        } catch (MessageIdNotFoundException e) {
            e.print();
            return;
        } catch (PersonIdNotFoundException e) {
            e.print();
            return;
        }
        System.out.println(String.format("Ok"));
    }

    private void querySocialValue() {
        int id = Integer.parseInt(cmds[1]);
        int ret = 0;
        try {
            ret = network.querySocialValue(id);
        } catch (PersonIdNotFoundException e) {
            e.print();
            return;
        }
        System.out.println(ret);
    }

    private void queryPopularity() {
        int id = Integer.parseInt(cmds[1]);
        int ret = 0;
        try {
            ret = network.queryPopularity(id);
        } catch (EmojiIdNotFoundException e) {
            e.print();
            return;
        }
        System.out.println(ret);
    }

    private void deleteColdEmoji() {
        int limit = Integer.parseInt(cmds[1]);
        System.out.println(network.deleteColdEmoji(limit));
    }

    private void queryReceivedMessages() {
        int id = Integer.parseInt(cmds[1]);
        List<Message> list = new ArrayList<>();
        try {
            list = network.queryReceivedMessages(id);
        } catch (PersonIdNotFoundException e) {
            e.print();
            return;
        }
        if (list.size() == 0) {
            System.out.println("None");
        } else {
            int i = 0;
            for (; i < list.size() - 1; i++) {
                Message message = list.get(i);
                resolve(message);
                System.out.print("; ");
            }
            Message message = list.get(i);
            resolve(message);
            System.out.println();
        }
    }

    private void queryBestAcquaintance() {
        int id = Integer.parseInt(cmds[1]);
        int ret = 0;
        try {
            ret = network.queryBestAcquaintance(id);
        } catch (PersonIdNotFoundException e) {
            e.print();
            return;
        } catch (AcquaintanceNotFoundException e) {
            e.print();
            return;
        }
        System.out.println(ret);
    }

    private void resolve(Message message) {
        if (message instanceof NoticeMessage) {
            System.out.print("notice: " + ((NoticeMessage) message).getString());
        } else if (message instanceof EmojiMessage) {
            System.out.print("Emoji: " + ((EmojiMessage) message).getEmojiId());
        } else if (message instanceof RedEnvelopeMessage) {
            System.out.print("RedEnvelope: " + ((RedEnvelopeMessage) message).getMoney());
        } else {
            System.out.print("Ordinary message: " + message.getId());
        }
    }
}
