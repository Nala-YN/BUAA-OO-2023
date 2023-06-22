'''
Copyright (C) 2022 BUAA
Author: Hyggge, <czh20020503@buaa.edu.com>

You can use this program to generate test data, which can be used in homework9 of BUAA-OO
'''


import random
import sys
import names

instr_list = ['ap', 'ar', 'qv','qci', 'qbs', 'qts','qtsok']
person_id_set = set()
group_id_set = set()
link_map = {}

def rd(a,b):
    return random.randint(a,b)
def get_unexist_id(id_set) :
    id = random.randint(-2147483648, 2147483647)
    while (id in id_set) :
        id = random.randint(-2147483648, 2147483647)
    return str(id)

def get_exist_id(id_set) :
    id = random.choice(list(id_set))
    return str(id)

def get_name() :
    name = names.get_first_name()
    while (len(name) > 9) :
        name = names.get_first_name()
    return name

def get_age() :
    age = random.randint(0, 190)
    return str(age)

def get_value() :
    value = random.randint(1, 90)
    return str(value)


def add_person(instr) :
    prob = random.uniform(0, 1)
    id = get_unexist_id(person_id_set)
    if (prob < 0.4) :
        if (person_id_set) :
            id = get_exist_id(person_id_set)
    else :
        id = get_unexist_id(person_id_set)
        person_id_set.add(id)
        link_map[id] = []
    return instr + " " + id + " " + get_name() + " " + get_age()
def add_personreal():
    id = get_unexist_id(person_id_set)
    person_id_set.add(id)
    link_map[id] = []
    return "ap " + id + " " + get_name() + " " + get_age()
def add_relation(instr) :
    prob = random.uniform(0, 1)
    id1 = get_unexist_id(person_id_set)
    id2 = get_unexist_id(person_id_set)
    if (prob < 0.2) :
        id1 = get_unexist_id(person_id_set)
        id2 = str(random.randint(-2147483648, 2147483647))
    elif (prob < 0.4) :
        if (person_id_set) :
            id1 = get_exist_id(person_id_set)
        id2 = get_unexist_id(person_id_set)
    elif (prob < 0.6) :
        if (person_id_set) :
            id1 = get_exist_id(person_id_set)
            if (link_map[id1]) :
                id2 = random.choice(link_map[id1])
            else :
                id2 = id1
        
    else :
        if person_id_set:
            id1 = get_exist_id(person_id_set)
            id2 = get_exist_id(person_id_set)
            link_map[id1].append(id2)
            link_map[id2].append(id1)

    return instr + " " + id1 + " " + id2 + " " + get_value()
def add_relationreal():
    if person_id_set:
        id1 = get_exist_id(person_id_set)
        id2 = get_exist_id(person_id_set)
        while (len(link_map[id1]) + 1 >= len(person_id_set)):
            id1 = get_exist_id(person_id_set)
            id2 = get_exist_id(person_id_set)
        while (id2 in link_map[id1] or id2 == id1):
            id2 = get_exist_id(person_id_set)
        link_map[id1].append(id2)
        link_map[id2].append(id1)
    return "ar " + id1 + " " + id2 + " " + get_value()
def query_value(instr) :
    prob = random.uniform(0, 1)
    id1 = get_unexist_id(person_id_set)
    id2 = get_unexist_id(person_id_set)
    if (prob < 0.2) :
        id1 = get_unexist_id(person_id_set)
        id2 = str(random.randint(-2147483648, 2147483647))
    elif (prob < 0.4) :
        if (person_id_set) :
            id1 = get_exist_id(person_id_set)
        id2 = get_unexist_id(person_id_set)
    elif (prob < 0.6) :
        if (person_id_set) :
            id1 = get_exist_id(person_id_set)
            id2 = get_exist_id(person_id_set)
            if (len(link_map[id1]) + 1 < len(person_id_set)) :
                while (id2 in link_map[id1]) :
                    id2 = get_exist_id(person_id_set)
    else :
        if (person_id_set) :
            id1 = get_exist_id(person_id_set)
            if (link_map[id1]) :
                id2 = random.choice(link_map[id1])
            else :
                id2 = id1 
    return instr + " " + id1 + " " + id2 

def query_circle(instr) :
    prob = random.uniform(0, 1)
    id1 = get_unexist_id(person_id_set)
    id2 = get_unexist_id(person_id_set)
    if (prob < 0.2) :
        id1 = get_unexist_id(person_id_set)
        id2 = str(random.randint(-2147483648, 2147483647))
    elif (prob < 0.4) :
        if (person_id_set) :
            id1 = get_exist_id(person_id_set)
        id2 = get_unexist_id(person_id_set)
    else :
        if (person_id_set) :
            id1 = get_exist_id(person_id_set)
            id2 = get_exist_id(person_id_set)
    return instr + " " + id1 + " " + id2
def query_circlereal():
    if (person_id_set) :
        id1 = get_exist_id(person_id_set)
        id2 = get_exist_id(person_id_set)
    return  "qci " + id1 + " " + id2
class PV:
    person=0
    value=0
    def __init__(self,person,value):
        self.person=person
        self.value=value
def get_qtsok():
    person_num=rd(0,10)
    bfdata='qtsok'+' '+str(person_num)
    person_set={}
    person_acq={}
    for i in range(person_num):
        person=rd(-30, 30)
        while person in person_set.values():
            person=rd(-30, 30)
        bfdata=bfdata+' '+str(person)
        person_set[i]=person
        person_acq[i]=set()
    for i in range(person_num):
        acqnum=rd(0,i)
        if i==0:
            continue
        for j in range(acqnum):
            acperson = rd(0, i - 1)
            flag=False
            while flag==False:
                flag=True
                acperson = rd(0, i - 1)
                for v in person_acq[i]:
                    if acperson==v.person:
                        flag=False
                        break
            value=rd(0, 100)
            person_acq[i].add(PV(acperson,value))
            person_acq[acperson].add(PV(i,value))
    for i in range(person_num):
        bfdata=bfdata+" "+str(len(person_acq[i]))
        for key in person_acq[i]:
            bfdata=bfdata+' '+str(person_set[key.person])+' '+str(key.value)
    error_type=rd(1,5)
    if error_type==0:
        person_num=person_num+1
        p=rd(-30, 30)
        while p in person_set.values():
            p=rd(-30, 30)
        person_set[person_num-1]=p
    elif error_type==1:
        if person_num>0:
            person=rd(0,person_num-1)
            if len(person_acq[person])>0:
                pv=person_acq[person].pop()
                for k in person_acq[pv.person]:
                    if k.person==person:
                        person_acq[pv.person].remove(k)
                        break
    elif error_type==2:
        if  person_num>=1:
            person_acq[rd(0,person_num-1)]=set()
    elif error_type==3:
        if person_num>0:
            person = rd(0, person_num - 1)
            if len(person_acq[person])>0:
                person_acq[person].pop()
    elif error_type==5:
        if person_num>0:
            person=rd(0,person_num-1)
            person_acq[person].add(PV(person_num+10,rd(0,114514)))
            person_set[person_num+10]=rd(0,114514)
    bfdata=bfdata+" "+str(person_num)
    for i in range(person_num):
        bfdata=bfdata+" "+str(person_set[i])
    for i in range(person_num):
        bfdata=bfdata+" "+str(len(person_acq[i]))
        for pv in person_acq[i]:
            bfdata=bfdata+" "+str(person_set[pv.person])+" "+str(pv.value)
    if error_type==4:
        cnt=0
        for i in range(person_num):
            for j in person_acq[i]:
                for k in person_acq[j.person]:
                    for m in person_acq[k.person]:
                        if m.person==i:
                            cnt=cnt+1
                            break
        bfdata=bfdata+" "+str(cnt/6)
    else:
        bfdata=bfdata+" "+str(0)
    return bfdata
instr_cnt=0
def get_instr() :
    global instr_cnt
    instr = random.choice(instr_list)
    if instr_cnt>10:
        while instr=='qtsok':
            instr=random.choice(instr_list)
    if (instr == 'ap') :
        return add_person(instr)
    elif (instr == 'ar') :
        return add_relation(instr)
    elif (instr == 'qv') :
        return query_value(instr)
    elif (instr == 'qps') :
        return instr
    elif (instr == 'qci') :
        return query_circle(instr)
    elif (instr == 'qbs') :
        return instr
    elif instr=='qts':
        return instr
    elif instr=='qtsok':
        instr_cnt=instr_cnt+1
        return get_qtsok()
def get_choumi():
    for i in range(6000):
        print(add_personreal())
    #for i in range():
    #    print(add_relationreal())
def get_line():
    for i in range(2500):
        print('ap'+' '+str(i)+' '+str(i)+' 1')
    for i in range(2499):
        print('ar' +' '+ str(i) + ' ' + str(i+1) + ' 1')
    for i in range(5000):
        print('qci'+' '+str(rd(0,20000))+' '+str(rd(0,20000)))
def get_xishu():
    for i in range(100):
        print(add_personreal())
    for i in range(700):
        print(add_relationreal())
def get_qts():
    for i in range(4000):
        print('qcs')
def get_blocksum():
    for i in range(50000):
        print('qbs')
def get_circle():
    for i in range(50000):
        print(query_circlereal())
if __name__ == '__main__':
    # f = open("data.txt", "w")
    # sys.stdout = f
    get_choumi()
    get_qts()