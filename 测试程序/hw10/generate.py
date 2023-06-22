'''
Copyright (C) 2022 BUAA
Author: Hyggge, <czh20020503@buaa.edu.com>

You can use this program to generate test data, which can be used in homework10 of BUAA-OO
'''

import random
import names


instr_list = ['ap', 'ar', 'ar', 'ar', 'ar', 'ar',  'ag', 'atg', 'dfg','mr','am', 'sm',
'ap', 'ar',  'ag', 'atg', 'dfg','mr','am', 'sm','ap', 'ar', 'ap', 'ar', 'ap', 'ar', 'ap', 'ar', 'ap', 'ar', 'ap', 'ar',
'ap', 'ar',  'mr','ap', 'ar',  'mr','ap', 'ar',  'mr','ap', 'ar',  'mr','ag', 'atg','atg','atg','atg','atg','atg','atg',
              'atg','atg','atg','atg','atg','atg','atg','atg','atg', 'dfg','mr','am', 'sm',
              'qsv', 'qrm','qv', 'qts', 'qci', 'qbs','qgvs', 'qgav', 'qba','qcs',]
#instr_list = ['ap','ar','ap','ar','ar','ar','ar','dfg','mr','am','sm','am','sm','am','sm','am','sm','am','sm','am','sm','am','sm',
#              'am','sm','am','sm','am','sm','am','sm','am','sm','am','am','am','am','am','am','am','am','am','am','am','qrm','qrm']
#instr_list = ['ap', 'ar', 'ar', 'ar', 'ar', 'ar',  'ag', 'atg', 'dfg','mr',
#'ap', 'ar',  'ag', 'atg', 'dfg','mr','ap', 'ar', 'ap', 'ar', 'ap', 'ar', 'ap', 'ar', 'ap', 'ar', 'ap', 'ar',
#'ap', 'ar',  'mr','ap', 'ar',  'mr','ap', 'ar',  'mr','ap', 'ar',  'mr','ag', 'atg', 'dfg','mr',
#              'qsv', 'qrm','qgvs', 'qgav']
#instr_list = ['ap', 'ar', 'ag', 'atg', 'dfg','am', 'sm', 'qsv', 'qrm']
#instr_list = ['ap', 'ar', 'mr','qts','qbs','qci']
#instr_list = ['ap', 'ar', 'qv', 'qci', 'qbs', 'ag', 'atg', 'dfg',
#                'qgvs', 'qgav','am', 'sm', 'qsv', 'qrm']
person_id_set = set()
group_id_set = set()
msg_id_set = set()

link_map = {}
group_map = {}
msg_map = {}
instr=''
#####################################################################################

def get_unexist_id(id_set) :
    id = random.randint(-1000, 1000)
    while (id in id_set) :
        id = random.randint(-1000, 1000)
    return str(id)

def get_exist_id(id_set) :
    id = random.choice(list(id_set))
    return str(id)
        
def get_name() :
    name = names.get_first_name()
    while (len(name) > 10) :
        name = names.get_first_name()
    return name

def get_age() :
    age = random.randint(0, 200)
    return str(age)

def get_value() :
    value = random.randint(1, 100)
    return str(value)

def get_type() :
    type =random.randint(0, 1)
    return str(type)

def get_social_value() :
    social_value = random.randint(-1000, 1000)
    return str(social_value)



########################################################################################

def get_id(id_set) :
    prob = random.uniform(0, 1)
    id = get_unexist_id(id_set)
    if (id_set):
        if (prob < 0.2) :
                if instr=='ag' or instr=='ap' or instr=='am':
                    id = get_exist_id(id_set)
                else:
                    id=get_unexist_id(id_set)
        else :
            if instr=='ag' or instr == 'ap' or instr=='am':
                id = get_unexist_id(id_set)
            else:
                id=get_exist_id(id_set)
    else:
        id=get_unexist_id(id_set)
    return id 

def get_double_person() :
    prob = random.uniform(0, 1)
    id1 = get_unexist_id(person_id_set)
    id2 = get_unexist_id(person_id_set)
    if (prob < 0.1) :
        id1 = get_unexist_id(person_id_set)
        id2 = str(random.randint(-2147483648, 2147483647))
    elif (prob < 0.2) :
        if (person_id_set) :
            id1 = get_exist_id(person_id_set)
        id2 = get_unexist_id(person_id_set)
    elif (prob < 0.7) :
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
                if (random.uniform(0, 1) < 0.2) : id2 = id1
                else : id2 = random.choice(link_map[id1])
            else :
                id2 = id1 
    return id1, id2

def get_person_group() :
    prob = random.uniform(0, 1)
    group_id = get_unexist_id(group_id_set)
    person_id = get_unexist_id(person_id_set)
    if (prob < 0.1) :
        group_id = get_unexist_id(group_id_set)
        person_id = str(random.randint(-2147483648, 2147483647))
    elif (prob < 0.2) :
        if (group_id_set):
            group_id = get_exist_id(group_id_set)
        person_id = get_unexist_id(person_id_set)
    elif (prob < 0.3) :
        for it in group_id_set:
            if (group_map[it]) :
                group_id = it
                person_id = random.choice(group_map[group_id])
                break    
    else :
        if (person_id_set and group_id_set) :
            group_id = get_exist_id(group_id_set)
            person_id = get_exist_id(person_id_set)
            if (len(group_map[group_id]) < len(person_id_set)) : 
                while (person_id in group_map[group_id]) :
                    person_id = get_exist_id(person_id_set)
    return person_id, group_id



#########################################################################################

# fuctions relative to person

def add_person(instr) :
    id = get_id(person_id_set)
    if (id not in person_id_set) :
        person_id_set.add(id)
        link_map[id] = []
    return instr + " " + id + " " + get_name() + " " + get_age()

def add_relation(instr) :
    id1, id2 = get_double_person()
    if (id1 in person_id_set and id2 in person_id_set and id2 not in link_map[id1]) :
        link_map[id1].append(id2)
        link_map[id2].append(id1)
    return instr + " " + id1 + " " + id2 + " " + get_value()

def query_value(instr) :
    id1, id2 = get_double_person() 
    return instr + " " + id1 + " " + id2 

def query_circle(instr) :
    id1, id2 = get_double_person()
    return instr + " " + id1 + " " + id2 


# functions relative to group

def add_group(instr) :
    id = get_id(group_id_set)
    if (id not in group_id_set) :
        group_id_set.add(id)
        group_map[id] = []
    return instr + " " + id     

def add_to_group(instr) :
    person_id, group_id = get_person_group() 
    if (group_id in group_id_set and person_id not in group_map[group_id]) :
        group_map[group_id].append(person_id)
    
    return instr + " " + person_id + " " + group_id
def modify_relation(instr):
    id1, id2 = get_double_person()
    if (id1 in person_id_set and id2 in person_id_set and id2 not in link_map[id1]) :
        link_map[id1].append(id2)
        link_map[id2].append(id1)
    return instr + " " + id1 + " " + id2 + " " + str(random.randint(-100,100))
def del_from_group(instr) :
    person_id, group_id = get_person_group() 
    if (group_id in group_id_set and person_id in group_map[group_id]) :
        group_map[group_id].remove(person_id)
    return instr + " " + person_id + " " + group_id

def query_group_people_sum(instr) :
    id = get_id(group_id_set)
    return instr + " " + id

def query_group_value_sum(instr) :
    id = get_id(group_id_set)
    return instr + " " + id

def query_group_age_var(instr) :
    id = get_id(group_id_set)
    return instr + " " + id


# functions relative to message

def add_message(instr) :    
    id = get_id(msg_id_set)
    type = get_type()
    if (type == '0') :
        id1, id2 = get_double_person()
        if (id not in msg_id_set and id1 in person_id_set and id2 in person_id_set and id1 != id2) :
            msg_id_set.add(id)
            msg_map[id] = (type, id1, id2)
            
        return instr + " " + id + " " + get_social_value() + " " + type + " " + id1 + " " + id2
    else :
        person_id, group_id = get_person_group()
        if (id not in msg_id_set and person_id in person_id_set and group_id in group_id_set) :
            msg_id_set.add(id)
            msg_map[id] = (type, person_id, group_id)
        return instr + " " + id + " " + get_social_value() + " " + type + " " + person_id + " " + group_id


def send_message(instr) :    
    id = get_id(msg_id_set) 
    if (id in msg_id_set) :
        type, id1, id2 = msg_map[id]
        if ((type == '0' and (id1 in link_map[id2] or id1 == id2)) or 
            (type == '1' and id1 in group_map[id2])) :
            msg_id_set.remove(id)
            msg_map[id] = None
    return instr + " " + id

def query_social_value(instr) :    
    id = get_id(person_id_set)
    return instr + " " + id
def query_best_acq(instr):
    id=get_id(person_id_set)
    return instr+" "+id
def query_received_messages(instr) :    
    id = get_id(person_id_set)
    return instr + " " + id

def query_least_connection(instr) :    
    id = get_id(person_id_set)
    return instr + " " + id
def rd(a,b):
    return random.randint(a,b)
class PV:
    person=0
    value=0
    def __init__(self,person,value):
        self.person=person
        self.value=value
def mrok():
    person_num=rd(3,5)
    bfdata='mrok'+' '+str(person_num)
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
        if i==0:
            continue
        acqnum=rd(1,i)
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
            value=rd(1, 100)
            person_acq[i].add(PV(acperson,value))
            person_acq[acperson].add(PV(i,value))
    for i in range(person_num):
        bfdata=bfdata+" "+str(len(person_acq[i]))
        for key in person_acq[i]:
            bfdata=bfdata+' '+str(person_set[key.person])+' '+str(key.value)
    all_error=rd(-1,4)
    m_person1=rd(0,person_num-1)
    m_person2=rd(0,person_num-1)
    while m_person2==m_person1:
        m_person2 = rd(0, person_num - 1)
    if all_error==-1:
        m_person = rd(0, person_num - 1)
        while len(person_acq[i])<0:
            m_person = rd(0, person_num - 1)
        pv=random.choice(tuple(person_acq[m_person]))
        if rd(0,1)==0:
            value=-pv.value-10
            for key in person_acq[m_person]:
                if key.person==pv.person:
                    person_acq[m_person].remove(key)
                    break
            for key in person_acq[pv.person]:
                if key.person==m_person:
                    person_acq[pv.person].remove(key)
                    break
        else:
            value=rd(-pv.value+1,pv.value)
            for key in person_acq[m_person]:
                if key.person==pv.person:
                    key.value=key.value+value
                    break
            for key in person_acq[pv.person]:
                if key.person==m_person:
                    key.value=key.value+value
        bfdata=bfdata+" "+str(person_set[m_person])+' '+str(person_set[pv.person])+' '+str(value)
    else:
        error_type=rd(1,5)
        if rd(0,3)==0:
            type=rd(1,2)
            if type==1:
                id1=rd(0,person_num-1)
                id2=id1
                bfdata=bfdata+" "+str(person_set[id1])+' '+str(person_set[id2])+' '+str(rd(-100,100))
            elif type==2:
                bfdata = bfdata + " " + str(rd(-100,100)) + ' ' + str(rd(-100,100)) + ' ' + str(rd(-100,100))
        else:
            m_person = rd(0, person_num - 1)
            while len(person_acq[i]) < 0:
                m_person = rd(0, person_num - 1)
            pv = random.choice(tuple(person_acq[m_person]))
            bfdata = bfdata + " " + str(person_set[m_person]) + ' ' + str(person_set[pv.person]) + ' ' + str(rd(-100, 100))
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
        elif error_type==5:
            person = rd(0, person_num - 1)
            if len(person_acq[person]) > 0:
                pv = person_acq[person].pop()
        elif error_type==2:
            if  person_num>=1:
                person_acq[rd(0,person_num-1)]=set()
        elif error_type==4:
            person_num-=person_num-1
        elif error_type==3:
            if person_num>0:
                person = rd(0, person_num - 1)
                if len(person_acq[person])>0:
                    person_acq[person].pop()
    bfdata=bfdata+" "+str(person_num)
    for i in range(person_num):
        bfdata=bfdata+" "+str(person_set[i])
    for i in range(person_num):
        bfdata=bfdata+" "+str(len(person_acq[i]))
        for pv in person_acq[i]:
            bfdata=bfdata+" "+str(person_set[pv.person])+" "+str(pv.value)
    return bfdata
def get_instr() :
    global instr
    instr = random.choice(instr_list)
    if (instr == 'ap') :
        return add_person(instr)
    elif (instr == 'ar') :
        return add_relation(instr)
    elif (instr == 'qv') :
        return query_value(instr)
    elif instr == 'qts':
        return instr
    elif (instr == 'qci') :
        return query_circle(instr)
    elif (instr == 'qbs') :
        return instr
    elif (instr == 'ag') :
        return add_group(instr)
    elif (instr == 'atg') :
        return add_to_group(instr)
    elif (instr == 'dfg') :
        return del_from_group(instr)
    elif instr == 'mr':
        return modify_relation(instr)
    elif (instr == 'qgvs') :
        return query_group_value_sum(instr)
    elif (instr == 'qgav') :
        return query_group_age_var(instr)
    elif instr == 'qba':
        return query_best_acq(instr)
    elif (instr == 'qcs') :
        return instr
    elif (instr == 'am') :
        return add_message(instr)
    elif (instr == 'sm') :
        return send_message(instr) 
    elif (instr == 'qsv') :
        return query_social_value(instr)
    elif (instr == 'qrm') :
        return query_received_messages(instr)
    elif instr == 'mrok':
        return mrok()

if __name__ == '__main__':
    # assert(print("5555555555555555555555555555555"))
    # f = open("data.txt", "w")
    # sys.stdout = f
    n = 1000
    for i in range(n) :
        instr = get_instr()
        print(instr)
            # print(person_id_set)
            # print(link_map)
            # print(group_id_set)
            # print(group_map)
