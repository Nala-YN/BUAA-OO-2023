package com.oocourse.spec3.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.oocourse.spec3.exceptions.PersonIdNotFoundException;
import com.oocourse.spec3.exceptions.AcquaintanceNotFoundException;
import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;
import com.oocourse.spec3.exceptions.EqualEmojiIdException;
import com.oocourse.spec3.exceptions.EqualGroupIdException;
import com.oocourse.spec3.exceptions.EqualMessageIdException;
import com.oocourse.spec3.exceptions.EqualPersonIdException;
import com.oocourse.spec3.exceptions.EqualRelationException;
import com.oocourse.spec3.exceptions.GroupIdNotFoundException;
import com.oocourse.spec3.exceptions.MessageIdNotFoundException;
import com.oocourse.spec3.exceptions.PathNotFoundException;
import com.oocourse.spec3.exceptions.RelationNotFoundException;

public interface Network {

    /*@ public instance model non_null Person[] people;
      @ public instance model non_null Group[] groups;
      @ public instance model non_null Message[] messages;
      @ public instance model non_null int[] emojiIdList;
      @ public instance model non_null int[] emojiHeatList;
      @*/

    /*@ invariant people != null && (\forall int i,j; 0 <= i && i < j && j < people.length; !people[i].equals(people[j]));*/

    //@ ensures \result == (\exists int i; 0 <= i && i < people.length; people[i].getId() == id);
    public /*@ pure @*/ boolean contains(int id);

    /*@ public normal_behavior
      @ requires contains(id);
      @ ensures (\exists int i; 0 <= i && i < people.length; people[i].getId() == id &&
      @         \result == people[i]);
      @ also
      @ public normal_behavior
      @ requires !contains(id);
      @ ensures \result == null;
      @*/
    public /*@ pure @*/ Person getPerson(int id);

    /*@ public normal_behavior
      @ requires !(\exists int i; 0 <= i && i < people.length; people[i].equals(person));
      @ assignable people[*];
      @ ensures people.length == \old(people.length) + 1;
      @ ensures (\forall int i; 0 <= i && i < \old(people.length);
      @          (\exists int j; 0 <= j && j < people.length; people[j] == (\old(people[i]))));
      @ ensures (\exists int i; 0 <= i && i < people.length; people[i] == person);
      @ also
      @ public exceptional_behavior
      @ signals (EqualPersonIdException e) (\exists int i; 0 <= i && i < people.length;
      @                                     people[i].equals(person));
      @*/
    public void addPerson(/*@ non_null @*/Person person) throws EqualPersonIdException;

    /*@ public normal_behavior
      @ requires contains(id1) && contains(id2) && !getPerson(id1).isLinked(getPerson(id2));
      @ assignable people[*];
      @ ensures people.length == \old(people.length);
      @ ensures (\forall int i; 0 <= i && i < \old(people.length);
      @          (\exists int j; 0 <= j && j < people.length; people[j] == \old(people[i])));
      @ ensures (\forall int i; 0 <= i && i < people.length && \old(people[i].getId()) != id1 &&
      @     \old(people[i].getId()) != id2; \not_assigned(people[i]));
      @ ensures getPerson(id1).isLinked(getPerson(id2)) && getPerson(id2).isLinked(getPerson(id1));
      @ ensures getPerson(id1).queryValue(getPerson(id2)) == value;
      @ ensures getPerson(id2).queryValue(getPerson(id1)) == value;
      @ ensures (\forall int i; 0 <= i && i < \old(getPerson(id1).acquaintance.length);
      @         \old(getPerson(id1).acquaintance[i]) == getPerson(id1).acquaintance[i] &&
      @          \old(getPerson(id1).value[i]) == getPerson(id1).value[i]);
      @ ensures (\forall int i; 0 <= i && i < \old(getPerson(id2).acquaintance.length);
      @         \old(getPerson(id2).acquaintance[i]) == getPerson(id2).acquaintance[i] &&
      @          \old(getPerson(id2).value[i]) == getPerson(id2).value[i]);
      @ ensures getPerson(id1).value.length == getPerson(id1).acquaintance.length;
      @ ensures getPerson(id2).value.length == getPerson(id2).acquaintance.length;
      @ ensures \old(getPerson(id1).value.length) == getPerson(id1).acquaintance.length - 1;
      @ ensures \old(getPerson(id2).value.length) == getPerson(id2).acquaintance.length - 1;
      @ also
      @ public exceptional_behavior
      @ assignable \nothing;
      @ requires !contains(id1) || !contains(id2) || getPerson(id1).isLinked(getPerson(id2));
      @ signals (PersonIdNotFoundException e) !contains(id1);
      @ signals (PersonIdNotFoundException e) contains(id1) && !contains(id2);
      @ signals (EqualRelationException e) contains(id1) && contains(id2) &&
      @         getPerson(id1).isLinked(getPerson(id2));
      @*/
    public void addRelation(int id1, int id2, int value) throws
            PersonIdNotFoundException, EqualRelationException;

    /*@ public normal_behavior
      @ requires contains(id1) && contains(id2) && id1 != id2 && getPerson(id1).isLinked(getPerson(id2))
                && getPerson(id1).queryValue(getPerson(id2)) + value > 0;
      @ assignable people;
      @ 1 ensures people.length == \old(people.length);
      @ 2 ensures (\forall int i; 0 <= i && i < \old(people.length);
      @          (\exists int j; 0 <= j && j < people.length; people[j].getId() == \old(people[i]).getId()));
      @ 3 ensures (\forall int i; 0 <= i && i < people.length && \old(people[i].getId()) != id1 &&
      @     \old(people[i].getId()) != id2; \not_assigned(people[i]));
      @ 4 ensures getPerson(id1).isLinked(getPerson(id2)) && getPerson(id2).isLinked(getPerson(id1));
      @ 5 ensures getPerson(id1).queryValue(getPerson(id2)) == \old(getPerson(id1).queryValue(getPerson(id2))) + value;
      @ 6 ensures getPerson(id2).queryValue(getPerson(id1)) == \old(getPerson(id2).queryValue(getPerson(id1))) + value;
      @ 7 ensures getPerson(id1).acquaintance.length == \old(getPerson(id1).acquaintance.length);
      @ 8 ensures getPerson(id2).acquaintance.length == \old(getPerson(id2).acquaintance.length);
      @ 9 ensures (\forall int i; 0 <= i && i < getPerson(id1).acquaintance.length; getPerson(id1).acquaintance[i].equals(\old(getPerson(id1).acquaintance[i]));
      @ 10 ensures (\forall int i; 0 <= i && i < getPerson(id2).acquaintance.length; getPerson(id2).acquaintance[i].equals(\old(getPerson(id2).acquaintance[i]));
      @ 11 ensures (\forall int i; 0 <= i && i < getPerson(id1).acquaintance.length && getPerson(id1).acquaintance[i].getId() != id2;
      @             getPerson(id1).value[i] == \old(getPerson(id1).value[i]));
      @ 12 ensures (\forall int i; 0 <= i && i < getPerson(id2).acquaintance.length && getPerson(id2).acquaintance[i].getId() != id1;
      @             getPerson(id2).value[i] == \old(getPerson(id2).value[i]));
      @ 13 ensures getPerson(id1).value.length == getPerson(id1).acquaintance.length;
      @ 14 ensures getPerson(id2).value.length == getPerson(id2).acquaintance.length;
      @ also
      @ public normal_behavior
      @ requires contains(id1) && contains(id2) && id1 != id2 && getPerson(id1).isLinked(getPerson(id2))
      @         && getPerson(id1).queryValue(getPerson(id2)) + value <= 0;
      @ 1 ensures people.length == \old(people.length);
      @ 2 ensures (\forall int i; 0 <= i && i < \old(people.length);
      @          (\exists int j; 0 <= j && j < people.length; people[j] == \old(people[i])));
      @ 3 ensures (\forall int i; 0 <= i && i < people.length && \old(people[i].getId()) != id1 &&
      @     \old(people[i].getId()) != id2; \not_assigned(people[i]));
      @ 15 ensures !getPerson(id1).isLinked(getPerson(id2)) && !getPerson(id2).isLinked(getPerson(id1));
      @ 16 ensures \old(getPerson(id1).value.length) == getPerson(id1).acquaintance.length + 1;
      @ 17 ensures \old(getPerson(id2).value.length) == getPerson(id2).acquaintance.length + 1;
      @ 18 ensures getPerson(id1).value.length == getPerson(id1).acquaintance.length;
      @ 19 ensures getPerson(id2).value.length == getPerson(id2).acquaintance.length;
      @ 20 ensures (\forall int i; 0 <= i && i < getPerson(id1).acquaintance.length;
      @         \old(getPerson(id1).acquaintance[i]) == getPerson(id1).acquaintance[i] &&
      @          \old(getPerson(id1).value[i]) == getPerson(id1).value[i]);
      @ 21 ensures (\forall int i; 0 <= i && i < getPerson(id2).acquaintance.length;
      @         \old(getPerson(id2).acquaintance[i]) == getPerson(id2).acquaintance[i] &&
      @          \old(getPerson(id2).value[i]) == getPerson(id2).value[i]);
      @ also
      @ public exceptional_behavior
      @ assignable \nothing;
      @ requires !contains(id1) || !contains(id2) || id1 == id2 || !getPerson(id1).isLinked(getPerson(id2));
      @ signals (PersonIdNotFoundException e) !contains(id1);
      @ signals (PersonIdNotFoundException e) contains(id1) && !contains(id2);
      @ signals (EqualPersonIdException e) contains(id1) && contains(id2) && id1 == id2;
      @ signals (RelationNotFoundException e) contains(id1) && contains(id2) && id1 != id2 &&
      @         !getPerson(id1).isLinked(getPerson(id2));
      @*/
    public void modifyRelation(int id1, int id2, int value) throws PersonIdNotFoundException,
            EqualPersonIdException, RelationNotFoundException;

    /*@ public normal_behavior
      @ requires contains(id1) && contains(id2) && getPerson(id1).isLinked(getPerson(id2));
      @ ensures \result == getPerson(id1).queryValue(getPerson(id2));
      @ also
      @ public exceptional_behavior
      @ signals (PersonIdNotFoundException e) !contains(id1);
      @ signals (PersonIdNotFoundException e) contains(id1) && !contains(id2);
      @ signals (RelationNotFoundException e) contains(id1) && contains(id2) &&
      @         !getPerson(id1).isLinked(getPerson(id2));
      @*/
    public /*@ pure @*/ int queryValue(int id1, int id2) throws
            PersonIdNotFoundException, RelationNotFoundException;


    /*@ public normal_behavior
      @ requires contains(id1) && contains(id2);
      @ ensures \result == (\exists Person[] array; array.length >= 2;
      @                     array[0].equals(getPerson(id1)) &&
      @                     array[array.length - 1].equals(getPerson(id2)) &&
      @                      (\forall int i; 0 <= i && i < array.length - 1;
      @                      array[i].isLinked(array[i + 1]) == true));
      @ also
      @ public exceptional_behavior
      @ signals (PersonIdNotFoundException e) !contains(id1);
      @ signals (PersonIdNotFoundException e) contains(id1) && !contains(id2);
      @*/
    public /*@ pure @*/ boolean isCircle(int id1, int id2) throws PersonIdNotFoundException;

    /*@ ensures \result ==
      @         (\sum int i; 0 <= i && i < people.length &&
      @         (\forall int j; 0 <= j && j < i; !isCircle(people[i].getId(), people[j].getId()));
      @         1);
      @*/
    public /*@ pure @*/ int queryBlockSum();

    /*@ ensures \result ==
      @         (\sum int i; 0 <= i && i < people.length;
      @             (\sum int j; i < j && j < people.length;
      @                 (\sum int k; j < k && k < people.length
      @                     && getPerson(people[i].getId()).isLinked(getPerson(people[j].getId()))
      @                     && getPerson(people[j].getId()).isLinked(getPerson(people[k].getId()))
      @                     && getPerson(people[k].getId()).isLinked(getPerson(people[i].getId()));
      @                     1)));
      @*/
    public /*@ pure @*/ int queryTripleSum();

    /*@ public normal_behavior
      @ requires !(\exists int i; 0 <= i && i < groups.length; groups[i].equals(group));
      @ assignable groups[*];
      @ ensures groups.length == \old(groups.length) + 1;
      @ ensures (\forall int i; 0 <= i && i < \old(groups.length);
      @          (\exists int j; 0 <= j && j < groups.length; groups[j] == (\old(groups[i]))));
      @ ensures (\exists int i; 0 <= i && i < groups.length; groups[i] == group);
      @ also
      @ public exceptional_behavior
      @ signals (EqualGroupIdException e) (\exists int i; 0 <= i && i < groups.length;
      @                                     groups[i].equals(group));
      @*/
    public void addGroup(/*@ non_null @*/Group group) throws EqualGroupIdException;

    /*@ public normal_behavior
      @ requires (\exists int i; 0 <= i && i < groups.length; groups[i].getId() == id);
      @ ensures (\exists int i; 0 <= i && i < groups.length; groups[i].getId() == id &&
      @         \result == groups[i]);
      @ also
      @ public normal_behavior
      @ requires (\forall int i; 0 <= i && i < groups.length; groups[i].getId() != id);
      @ ensures \result == null;
      @*/
    public /*@ pure @*/ Group getGroup(int id);

    /*@ public normal_behavior
      @ requires (\exists int i; 0 <= i && i < groups.length; groups[i].getId() == id2) &&
      @           (\exists int i; 0 <= i && i < people.length; people[i].getId() == id1) &&
      @            getGroup(id2).hasPerson(getPerson(id1)) == false &&
      @             getGroup(id2).people.length <= 1111;
      @ assignable getGroup(id2).people[*];
      @ ensures (\forall Person i; \old(getGroup(id2).hasPerson(i));
      @          getGroup(id2).hasPerson(i));
      @ ensures \old(getGroup(id2).people.length) == getGroup(id2).people.length - 1;
      @ ensures getGroup(id2).hasPerson(getPerson(id1));
      @ also
      @ public normal_behavior
      @ requires (\exists int i; 0 <= i && i < groups.length; groups[i].getId() == id2) &&
      @           (\exists int i; 0 <= i && i < people.length; people[i].getId() == id1) &&
      @            getGroup(id2).hasPerson(getPerson(id1)) == false &&
      @             getGroup(id2).people.length > 1111;
      @ assignable \nothing;
      @ also
      @ public exceptional_behavior
      @ signals (GroupIdNotFoundException e) !(\exists int i; 0 <= i && i < groups.length;
      @          groups[i].getId() == id2);
      @ signals (PersonIdNotFoundException e) (\exists int i; 0 <= i && i < groups.length;
      @          groups[i].getId() == id2) && !(\exists int i; 0 <= i && i < people.length;
      @           people[i].getId() == id1);
      @ signals (EqualPersonIdException e) (\exists int i; 0 <= i && i < groups.length;
      @          groups[i].getId() == id2) && (\exists int i; 0 <= i && i < people.length;
      @           people[i].getId() == id1) && getGroup(id2).hasPerson(getPerson(id1));
      @*/
    public void addToGroup(int id1, int id2) throws GroupIdNotFoundException,
            PersonIdNotFoundException, EqualPersonIdException;


    /*@ public normal_behavior
      @ requires (\exists int i; 0 <= i && i < groups.length; groups[i].getId() == id);
      @ ensures \result == getGroup(id).getValueSum();
      @ also
      @ public exceptional_behavior
      @ signals (GroupIdNotFoundException e) !(\exists int i; 0 <= i && i < groups.length;
      @          groups[i].getId() == id);
      @*/
    public /*@ pure @*/ int queryGroupValueSum(int id) throws GroupIdNotFoundException;

    /*@ public normal_behavior
      @ requires (\exists int i; 0 <= i && i < groups.length; groups[i].getId() == id);
      @ ensures \result == getGroup(id).getAgeVar();
      @ also
      @ public exceptional_behavior
      @ signals (GroupIdNotFoundException e) !(\exists int i; 0 <= i && i < groups.length;
      @          groups[i].getId() == id);
      @*/
    public /*@ pure @*/ int queryGroupAgeVar(int id) throws GroupIdNotFoundException;

    /*@ public normal_behavior
      @ requires (\exists int i; 0 <= i && i < groups.length; groups[i].getId() == id2) &&
      @           (\exists int i; 0 <= i && i < people.length; people[i].getId() == id1) &&
      @            getGroup(id2).hasPerson(getPerson(id1)) == true;
      @ assignable getGroup(id2).people[*];
      @ ensures (\forall Person i; getGroup(id2).hasPerson(i);
      @          \old(getGroup(id2).hasPerson(i)));
      @ ensures \old(getGroup(id2).people.length) == getGroup(id2).people.length + 1;
      @ ensures getGroup(id2).hasPerson(getPerson(id1)) == false;
      @ also
      @ public exceptional_behavior
      @ signals (GroupIdNotFoundException e) !(\exists int i; 0 <= i && i < groups.length;
      @          groups[i].getId() == id2);
      @ signals (PersonIdNotFoundException e) (\exists int i; 0 <= i && i < groups.length;
      @          groups[i].getId() == id2) && !(\exists int i; 0 <= i && i < people.length;
      @           people[i].getId() == id1);
      @ signals (EqualPersonIdException e) (\exists int i; 0 <= i && i < groups.length;
      @          groups[i].getId() == id2) && (\exists int i; 0 <= i && i < people.length;
      @           people[i].getId() == id1) && !getGroup(id2).hasPerson(getPerson(id1));
      @*/
    public void delFromGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException;

    //@ ensures \result == (\exists int i; 0 <= i && i < messages.length; messages[i].getId() == id);
    public /*@ pure @*/ boolean containsMessage(int id);

    /*@ public normal_behavior
      @ requires !(\exists int i; 0 <= i && i < messages.length; messages[i].equals(message)) &&
      @           (message instanceof EmojiMessage) ==> containsEmojiId(((EmojiMessage) message).getEmojiId()) &&
      @            (message.getType() == 0) ==> (message.getPerson1() != message.getPerson2());
      @ assignable messages[*];
      @ ensures messages.length == \old(messages.length) + 1;
      @ ensures (\forall int i; 0 <= i && i < \old(messages.length);
      @          (\exists int j; 0 <= j && j < messages.length; messages[j].equals(\old(messages[i]))));
      @ ensures (\exists int i; 0 <= i && i < messages.length; messages[i].equals(message));
      @ also
      @ public exceptional_behavior
      @ signals (EqualMessageIdException e) (\exists int i; 0 <= i && i < messages.length;
      @                                     messages[i].equals(message));
      @ signals (EmojiIdNotFoundException e) !(\exists int i; 0 <= i && i < messages.length;
      @                                       messages[i].equals(message)) &&
      @                                       (message instanceof EmojiMessage) &&
      @                                       !containsEmojiId(((EmojiMessage) message).getEmojiId());
      @ signals (EqualPersonIdException e) !(\exists int i; 0 <= i && i < messages.length;
      @                                     messages[i].equals(message)) &&
      @                                     ((message instanceof EmojiMessage) ==>
      @                                     containsEmojiId(((EmojiMessage) message).getEmojiId())) &&
      @                                     message.getType() == 0 && message.getPerson1() == message.getPerson2();
      @*/
    public void addMessage(Message message) throws
            EqualMessageIdException, EmojiIdNotFoundException, EqualPersonIdException;

    /*@ public normal_behavior
      @ requires containsMessage(id);
      @ ensures (\exists int i; 0 <= i && i < messages.length; messages[i].getId() == id &&
      @         \result == messages[i]);
      @ public normal_behavior
      @ requires !containsMessage(id);
      @ ensures \result == null;
      @*/
    public /*@ pure @*/ Message getMessage(int id);

    /*@ public normal_behavior
      @ requires containsMessage(id) && getMessage(id).getType() == 0 &&
      @          getMessage(id).getPerson1().isLinked(getMessage(id).getPerson2()) &&
      @          getMessage(id).getPerson1() != getMessage(id).getPerson2();
      @ assignable messages[*], emojiHeatList[*];
      @ assignable getMessage(id).getPerson1().socialValue, getMessage(id).getPerson1().money;
      @ assignable getMessage(id).getPerson2().messages, getMessage(id).getPerson2().socialValue, getMessage(id).getPerson2().money;
      @ ensures !containsMessage(id) && messages.length == \old(messages.length) - 1 &&
      @         (\forall int i; 0 <= i && i < \old(messages.length) && \old(messages[i].getId()) != id;
      @         (\exists int j; 0 <= j && j < messages.length; messages[j].equals(\old(messages[i]))));
      @ ensures \old(getMessage(id)).getPerson1().getSocialValue() ==
      @         \old(getMessage(id).getPerson1().getSocialValue()) + \old(getMessage(id)).getSocialValue() &&
      @         \old(getMessage(id)).getPerson2().getSocialValue() ==
      @         \old(getMessage(id).getPerson2().getSocialValue()) + \old(getMessage(id)).getSocialValue();
      @ ensures (\old(getMessage(id)) instanceof RedEnvelopeMessage) ==>
      @         (\old(getMessage(id)).getPerson1().getMoney() ==
      @         \old(getMessage(id).getPerson1().getMoney()) - ((RedEnvelopeMessage)\old(getMessage(id))).getMoney() &&
      @         \old(getMessage(id)).getPerson2().getMoney() ==
      @         \old(getMessage(id).getPerson2().getMoney()) + ((RedEnvelopeMessage)\old(getMessage(id))).getMoney());
      @ ensures (!(\old(getMessage(id)) instanceof RedEnvelopeMessage)) ==> (\not_assigned(people[*].money));
      @ ensures (\old(getMessage(id)) instanceof EmojiMessage) ==>
      @         (\exists int i; 0 <= i && i < emojiIdList.length && emojiIdList[i] == ((EmojiMessage)\old(getMessage(id))).getEmojiId();
      @         emojiHeatList[i] == \old(emojiHeatList[i]) + 1);
      @ ensures (!(\old(getMessage(id)) instanceof EmojiMessage)) ==> \not_assigned(emojiHeatList);
      @ ensures (\forall int i; 0 <= i && i < \old(getMessage(id).getPerson2().getMessages().size());
      @          \old(getMessage(id)).getPerson2().getMessages().get(i+1) == \old(getMessage(id).getPerson2().getMessages().get(i)));
      @ ensures \old(getMessage(id)).getPerson2().getMessages().get(0).equals(\old(getMessage(id)));
      @ ensures \old(getMessage(id)).getPerson2().getMessages().size() == \old(getMessage(id).getPerson2().getMessages().size()) + 1;
      @ also
      @ public normal_behavior
      @ requires containsMessage(id) && getMessage(id).getType() == 1 &&
      @           getMessage(id).getGroup().hasPerson(getMessage(id).getPerson1());
      @ assignable people[*].socialValue, people[*].money, messages, emojiHeatList;
      @ ensures !containsMessage(id) && messages.length == \old(messages.length) - 1 &&
      @         (\forall int i; 0 <= i && i < \old(messages.length) && \old(messages[i].getId()) != id;
      @         (\exists int j; 0 <= j && j < messages.length; messages[j].equals(\old(messages[i]))));
      @ ensures (\forall Person p; \old(getMessage(id)).getGroup().hasPerson(p); p.getSocialValue() ==
      @         \old(p.getSocialValue()) + \old(getMessage(id)).getSocialValue());
      @ ensures (\forall int i; 0 <= i && i < people.length && !\old(getMessage(id)).getGroup().hasPerson(people[i]);
      @          \old(people[i].getSocialValue()) == people[i].getSocialValue());
      @ ensures (\old(getMessage(id)) instanceof RedEnvelopeMessage) ==>
      @          (\exists int i; i == ((RedEnvelopeMessage)\old(getMessage(id))).getMoney()/\old(getMessage(id)).getGroup().getSize();
      @           \old(getMessage(id)).getPerson1().getMoney() ==
      @           \old(getMessage(id).getPerson1().getMoney()) - i*(\old(getMessage(id)).getGroup().getSize() - 1) &&
      @           (\forall Person p; \old(getMessage(id)).getGroup().hasPerson(p) && p != \old(getMessage(id)).getPerson1();
      @           p.getMoney() == \old(p.getMoney()) + i));
      @ ensures (\old(getMessage(id)) instanceof RedEnvelopeMessage) ==>
      @          (\forall int i; 0 <= i && i < people.length && !\old(getMessage(id)).getGroup().hasPerson(people[i]);
      @           \old(people[i].getMoney()) == people[i].getMoney());
      @ ensures (!(\old(getMessage(id)) instanceof RedEnvelopeMessage)) ==> (\not_assigned(people[*].money));
      @ ensures (\old(getMessage(id)) instanceof EmojiMessage) ==>
      @         (\exists int i; 0 <= i && i < emojiIdList.length && emojiIdList[i] == ((EmojiMessage)\old(getMessage(id))).getEmojiId();
      @          emojiHeatList[i] == \old(emojiHeatList[i]) + 1);
      @ ensures (!(\old(getMessage(id)) instanceof EmojiMessage)) ==> \not_assigned(emojiHeatList);
      @ also
      @ public exceptional_behavior
      @ signals (MessageIdNotFoundException e) !containsMessage(id);
      @ signals (RelationNotFoundException e) containsMessage(id) && getMessage(id).getType() == 0 &&
      @          !(getMessage(id).getPerson1().isLinked(getMessage(id).getPerson2()));
      @ signals (PersonIdNotFoundException e) containsMessage(id) && getMessage(id).getType() == 1 &&
      @          !(getMessage(id).getGroup().hasPerson(getMessage(id).getPerson1()));
      @*/
    public void sendMessage(int id) throws
            RelationNotFoundException, MessageIdNotFoundException, PersonIdNotFoundException;

    /*@ public normal_behavior
      @ requires contains(id);
      @ ensures \result == getPerson(id).getSocialValue();
      @ also
      @ public exceptional_behavior
      @ signals (PersonIdNotFoundException e) !contains(id);
      @*/
    public /*@ pure @*/ int querySocialValue(int id) throws PersonIdNotFoundException;

    /*@ public normal_behavior
      @ requires contains(id);
      @ ensures \result == getPerson(id).getReceivedMessages();
      @ also
      @ public exceptional_behavior
      @ signals (PersonIdNotFoundException e) !contains(id);
      @*/
    public /*@ pure @*/ List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException;

    //@ ensures \result == (\exists int i; 0 <= i && i < emojiIdList.length; emojiIdList[i] == id);
    public /*@ pure @*/ boolean containsEmojiId(int id);

    /*@ public normal_behavior
      @ requires !(\exists int i; 0 <= i && i < emojiIdList.length; emojiIdList[i] == id);
      @ assignable emojiIdList[*], emojiHeatList[*];
      @ ensures (\exists int i; 0 <= i && i < emojiIdList.length; emojiIdList[i] == id && emojiHeatList[i] == 0);
      @ ensures emojiIdList.length == \old(emojiIdList.length) + 1 &&
      @          emojiHeatList.length == \old(emojiHeatList.length) + 1;
      @ ensures (\forall int i; 0 <= i && i < \old(emojiIdList.length);
      @          (\exists int j; 0 <= j && j < emojiIdList.length; emojiIdList[j] == \old(emojiIdList[i]) &&
      @          emojiHeatList[j] == \old(emojiHeatList[i])));
      @ also
      @ public exceptional_behavior
      @ signals (EqualEmojiIdException e) (\exists int i; 0 <= i && i < emojiIdList.length;
      @                                     emojiIdList[i] == id);
      @*/
    public void storeEmojiId(int id) throws EqualEmojiIdException;

    /*@ public normal_behavior
      @ requires contains(id);
      @ ensures \result == getPerson(id).getMoney();
      @ also
      @ public exceptional_behavior
      @ signals (PersonIdNotFoundException e) !contains(id);
      @*/
    public /*@ pure @*/ int queryMoney(int id) throws PersonIdNotFoundException;

    /*@ public normal_behavior
      @ requires (\exists int i; 0 <= i && i < emojiIdList.length; emojiIdList[i] == id);
      @ ensures (\exists int i; 0 <= i && i < emojiIdList.length; emojiIdList[i] == id &&
      @          \result == emojiHeatList[i]);
      @ also
      @ public exceptional_behavior
      @ requires !(\exists int i; 0 <= i && i < emojiIdList.length; emojiIdList[i] == id);
      @ signals_only EmojiIdNotFoundException;
      @*/
    public /*@ pure @*/ int queryPopularity(int id) throws EmojiIdNotFoundException;

    /*@ public normal_behavior
      @ assignable emojiIdList, emojiHeatList, messages;
      @ 1 ensures (\forall int i; 0 <= i && i < \old(emojiIdList.length);
      @          (\old(emojiHeatList[i] >= limit) ==>
      @          (\exists int j; 0 <= j && j < emojiIdList.length; emojiIdList[j] == \old(emojiIdList[i]))));
      @ 2 ensures (\forall int i; 0 <= i && i < emojiIdList.length;
      @          (\exists int j; 0 <= j && j < \old(emojiIdList.length);
      @          emojiIdList[i] == \old(emojiIdList[j]) && emojiHeatList[i] == \old(emojiHeatList[j])));
      @ 3 ensures emojiIdList.length ==
      @          (\num_of int i; 0 <= i && i < \old(emojiIdList.length); \old(emojiHeatList[i] >= limit));
      @ 4 ensures emojiIdList.length == emojiHeatList.length;
      @ 5 ensures (\forall int i; 0 <= i && i < \old(messages.length);
      @          (\old(messages[i]) instanceof EmojiMessage &&
      @           containsEmojiId(\old(((EmojiMessage)messages[i]).getEmojiId()))  ==> \not_assigned(\old(messages[i])) &&
      @           (\exists int j; 0 <= j && j < messages.length; messages[j].equals(\old(messages[i])))));
      @ 6 ensures (\forall int i; 0 <= i && i < \old(messages.length);
      @          (!(\old(messages[i]) instanceof EmojiMessage) ==> \not_assigned(\old(messages[i])) &&
      @           (\exists int j; 0 <= j && j < messages.length; messages[j].equals(\old(messages[i])))));
      @ 7 ensures messages.length == (\num_of int i; 0 <= i && i < \old(messages.length);
      @          (\old(messages[i]) instanceof EmojiMessage) ==>
      @           (containsEmojiId(\old(((EmojiMessage)messages[i]).getEmojiId()))));
      @ 8 ensures \result == emojiIdList.length;
      @*/
    public int deleteColdEmoji(int limit);

    /*@ public normal_behavior
      @ requires contains(personId);
      @ assignable getPerson(personId).messages[*];
      @ ensures (\forall Message i; \old(getPerson(personId).getMessages().contains(i));
      @           !(i instanceof NoticeMessage) ==>
      @             (\exists Message j; getPerson(personId).getMessages().contains(j); i.equals(j)));
      @ ensures getPerson(personId).messages.length ==
      @           (\num_of int i; 0 <= i && i < \old(getPerson(personId).messages.length);
      @             !(\old(getPerson(personId).getMessages().get(i)) instanceof NoticeMessage));
      @ ensures (\forall int i; 0 <= i && i < getPerson(personId).getMessages().size();
      @           (\forall int j; i < j && j < getPerson(personId).getMessages().size();
      @                 \old(getPerson(personId).getMessages()).indexOf(
      @                   getPerson(personId).getMessages().get(i)) <
      @                 \old(getPerson(personId).getMessages()).indexOf(
      @                   getPerson(personId).getMessages().get(j))));
      @ also
      @ public exceptional_behavior
      @ signals (PersonIdNotFoundException e) !contains(personId);
      @*/
    public void clearNotices(int personId) throws PersonIdNotFoundException;

    /*@ public normal_behavior
      @ requires contains(id) && getPerson(id).acquaintance.length != 0;
      @ ensures \result == (\min int bestId;
      @         (\exists int i; 0 <= i && i < getPerson(id).acquaintance.length &&
      @             getPerson(id).acquaintance[i].getId() == bestId;
      @             (\forall int j; 0 <= j && j < getPerson(id).acquaintance.length;
      @                 getPerson(id).value[j] <= getPerson(id).value[i]));
      @         bestId);
      @ public exceptional_behavior
      @ signals (PersonIdNotFoundException e) !contains(id);
      @ signals (AcquaintanceNotFoundException e) contains(id) &&
      @         getPerson(id).acquaintance.length == 0;
      @*/
    public /*@ pure @*/ int queryBestAcquaintance(int id) throws
            PersonIdNotFoundException, AcquaintanceNotFoundException;


    /*@ ensures \result ==
      @         (\sum int i, j; 0 <= i && i < j && j < people.length
      @                         && people[i].acquaintance.length > 0 && queryBestAcquaintance(people[i].getId()) == people[j].getId()
      @                         && people[j].acquaintance.length > 0 && queryBestAcquaintance(people[j].getId()) == people[i].getId();
      @                         1);
      @*/
    public /*@ pure @*/ int queryCoupleSum();


    /*@ public normal_behavior
      @ requires contains(id) && (\exists Person[] path;
      @         path.length >= 4;
      @         path[0].equals(getPerson(id)) &&
      @         path[path.length - 1].equals(getPerson(id)) &&
      @         (\forall int i; 1 <= i && i < path.length; path[i - 1].isLinked(path[i])) &&
      @         (\forall int i, j; 1 <= i && i < j && j < path.length; !path[i].equals(path[j])));
      @ ensures (\exists Person[] pathM;
      @         pathM.length >= 4 &&
      @         pathM[0].equals(getPerson(id)) &&
      @         pathM[pathM.length - 1].equals(getPerson(id)) &&
      @         (\forall int i; 1 <= i && i < pathM.length; pathM[i - 1].isLinked(pathM[i])) &&
      @         (\forall int i, j; 1 <= i && i < j && j < pathM.length; !pathM[i].equals(pathM[j]));
      @         (\forall Person[] path;
      @         path.length >= 4 &&
      @         path[0].equals(getPerson(id)) &&
      @         path[path.length - 1].equals(getPerson(id)) &&
      @         (\forall int i; 1 <= i && i < path.length; path[i - 1].isLinked(path[i])) &&
      @         (\forall int i, j; 1 <= i && i < j && j < path.length; !path[i].equals(path[j]));
      @         (\sum int i; 1 <= i && i < path.length; path[i - 1].queryValue(path[i])) >=
      @         (\sum int i; 1 <= i && i < pathM.length; pathM[i - 1].queryValue(pathM[i]))) &&
      @         \result==(\sum int i; 1 <= i && i < pathM.length; pathM[i - 1].queryValue(pathM[i])));
      @ also
      @ public exceptional_behavior
      @ signals (PersonIdNotFoundException e) !contains(id);
      @ signals (PathNotFoundException e) contains(id) && !(\exists Person[] path;
      @         path.length >= 4;
      @         path[0].equals(getPerson(id)) &&
      @         path[path.length - 1].equals(getPerson(id)) &&
      @         (\forall int i; 1 <= i && i < path.length; path[i - 1].isLinked(path[i])) &&
      @         (\forall int i, j; 1 <= i && i < j && j < path.length; !path[i].equals(path[j])));
      @*/
    public int queryLeastMoments(int id) throws PersonIdNotFoundException, PathNotFoundException;


    public int deleteColdEmojiOKTest(int limit, ArrayList<HashMap<Integer, Integer>> beforeData, ArrayList<HashMap<Integer, Integer>> afterData, int result);

}
