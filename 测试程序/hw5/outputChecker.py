import os

from colorfulPrint import ColorfulPrint

class Req:
    def __init__(self, req):
        eles = self.parseReq(req)
        self.id = int(eles[0])
        self.ff = int(eles[2])
        self.tf = int(eles[4])
    def parseReq(self, req):
        req = req.replace('\n', '')
        index = req.index(']')
        req = req[index + 1:]
        return req.split('-')
    def getId(self):
        return self.id
    def getFromTower(self):
        return self.ft
    def getFromFloor(self):
        return self.ff
    def getToTower(self):
        return self.tt
    def getToFloor(self):
        return self.tf

STATE_OPEN = 0
STATE_CLOSE = 1
MAX_FLOOR = 11
MIN_FLOOR = 1
MAX_NUM = 6
MIN_NUM = 0

reqDic = {}
floors = []
states = []
passengers = []
towers = []
lastMoveTime = []
lastOpenTime = []
lastOpTime = []
flag = [0]

def printError(msg, l = 0):
    if l == 0:
        msg = ' ***** ' + msg + ' ***** '
    else:
        msg = ' ***** ' + msg + ' IN LINE: ' + str(l) + ' ***** '
    ColorfulPrint.colorfulPrint(msg, ColorfulPrint.MODE_BOLD, ColorfulPrint.COLOR_RED)
    flag[0] = 0
    os.system("pause");

def initElevator():
    reqDic.clear()
    floors.clear()
    states.clear()
    passengers.clear()
    towers.clear()
    lastMoveTime.clear()
    lastOpenTime.clear()
    lastOpTime.clear()
    flag[0] = 0
    for i in range(6):
        floors.append(1)
        states.append(STATE_CLOSE)
        passengers.append([])
        towers.append(i + 1)
        lastMoveTime.append(-10.0)
        lastOpenTime.append(-10.0)
    lastOpTime.append(-1.0)

def processInput():
    with open('stdin.txt', 'r') as f:
        tot = f.readlines()
        for ele in tot:
            req = Req(ele)
            reqDic[req.getId()] = req

def checkOutput():
    initElevator()
    processInput()
    flag[0] = 1
    with open('stdout.txt', 'r') as f:
        tot = f.readlines()
        count = 1
        for line in tot:
            process(line, count)
            count += 1
    if len(reqDic) != 0:
        printError('SOME REQUESTS ARE NOT SATIFIED!')
    for i in range(6):
        if len(passengers[i]) != 0:
            printError('SOMEONE IS TRAPPED IN ELEVATOR! ELEVATOR ID: ' + str(i + 1))
        if states[i] != STATE_CLOSE:
            printError('ELEVATOR IS NOT CLOSE! ELEVATOR ID: ' + str(i + 1))
    if flag[0] == 1:
        ColorfulPrint.colorfulPrint(' ===== OUTPUT ACCEPTED ===== ', ColorfulPrint.MODE_BOLD, ColorfulPrint.COLOR_GREEN)
        return True
    else:
        printError('WRONG ANSWER!')
        return False

def process(read, lineNum):
    read = read.replace('\n', '')
    index = read.index(']')
    opTime = float(read[1:index - 1])
    read = read[index + 1:]
    eles = read.split('-')

    if opTime < lastOpTime[0]:
        printError('INCORRECT OUTPUT ORDER!', lineNum)
    lastOpTime[0] = opTime

    if eles[0] == 'ARRIVE':
        index = int(eles[2]) - 1
        if not 0 <= index <= 5:
            printError('UNKNOW ELEVATOR ID!', lineNum)
            return
        if states[index] != STATE_CLOSE:
            printError('MOVE WHEN DORR IS OPEN!', lineNum)
        if opTime - lastMoveTime[index] < 0.4 - 0.00001:
            printError('MOVE TOO FAST!', lineNum)
        lastMoveTime[index] = opTime
        if int(eles[1]) > MAX_FLOOR or int(eles[1]) < MIN_FLOOR:
            printError('ELEVATOR ON A NON-EXISTENT FLOOR!', lineNum)
        if floors[index] - int(eles[1]) != 1 and floors[index] - int(eles[1]) != -1:
            printError('ILLEGAL ELEVATOR MOVEMENT!', lineNum)
        floors[index] = int(eles[1])

    elif eles[0] == 'OPEN':
        index = int(eles[2]) - 1
        if not 0 <= index <= 5:
            printError('UNKNOW ELEVATOR ID!', lineNum)
            return
        if states[index] != STATE_CLOSE:
            printError('ELEVATOR ALREADY OPEN!', lineNum)
        states[index] = STATE_OPEN
        lastOpenTime[index] = opTime
        if floors[index] != int(eles[1]):
            printError('ELEVATOR UNMATCHED FLOOR!', lineNum)

    elif eles[0] == 'CLOSE':
        index = int(eles[2]) - 1
        lastMoveTime[index] = opTime
        if not 0 <= index <= 5:
            printError('UNKNOW ELEVATOR ID!', lineNum)
            return
        if states[index] != STATE_OPEN:
            printError('ELEVATOR ALREADY CLOSE!', lineNum)
        states[index] = STATE_CLOSE
        if floors[index] != int(eles[1]):
            printError('ELEVATOR UNMATCH FLOOR!', lineNum)
        if opTime - lastOpenTime[index] < 0.4 - 0.00001:
            printError('CLOSE TOO FAST!', lineNum)

    elif eles[0] == 'IN':
        index = int(eles[3]) - 1
        passengerIndex = int(eles[1])
        if not 0 <= index <= 5:
            printError('UNKNOW ELEVATOR ID!', lineNum)
            return
        if states[index] != STATE_OPEN:
            printError('PASSENGER CAN\'T IN!', lineNum)
        passengers[index].append(passengerIndex)
        req = reqDic.get(passengerIndex)
        if req == None:
            printError('PASSENGER NOT EXIST!', lineNum)
        else:
            if req.getFromFloor() != int(eles[2]):
                printError('IN MESSAGE NUMATCH REQUEST!', lineNum)
        if len(passengers[index]) > MAX_NUM:
            printError('ELEVATOR OVERLOAD!', lineNum)
        if floors[index] != int(eles[2]):
            printError('ELEVATOR UNMATCHED FLOOR!', lineNum)

    elif eles[0] == 'OUT':
        index = int(eles[3]) - 1
        passengerIndex = int(eles[1])
        if not 0 <= index <= 5:
            printError('UNKNOW ELEVATOR ID!', lineNum)
            return
        if states[index] != STATE_OPEN:
            printError('PASSENGER CAN\'T OUT!', lineNum)
        if passengerIndex not in passengers[index]:
            printError('PASSENGER NOT IN ELEVATOR!', lineNum)
        else:
            passengers[index].remove(passengerIndex)
        req = reqDic.get(passengerIndex)
        if req == None:
            printError('PASSENGER NOT EXIST!', lineNum)
        else:
            if req.getToFloor() != int(eles[2]):
                printError('OUT MESSAGE NUMATCH REQUEST!', lineNum)
            reqDic.pop(passengerIndex)

    else:
        printError('UNKNOW OPTION!', lineNum)

def getTower(tower, lineNum):
    if tower == 'A':
        return 1
    elif tower == 'B':
        return 2
    elif tower == 'C':
        return 3
    elif tower == 'D':
        return 4
    elif tower == 'E':
        return 5
    else:
        printError('UNKNOW TOWER!', lineNum)

if __name__ == "__main__":
    checkOutput()