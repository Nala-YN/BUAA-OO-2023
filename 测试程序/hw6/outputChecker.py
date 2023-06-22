from colorfulPrint import ColorfulPrint
import os
STATE_OPEN = 0
STATE_CLOSE = 1
MAX_FLOOR = 11
MIN_FLOOR = 1
reqDic = {}
elevators = {}
flag = [0]
lastOpTime = [0.0]

class Req:
    def __init__(self, req):
        eles = self.parseReq(req)
        self.id = int(eles[0])
        self.ff = int(eles[2])
        self.tf = int(eles[4])
        self.finish = False
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
    def move(self, Floor,lineNum):
        if self.tf==Floor:
            self.finish=True
        else:
            self.ff=Floor
    def isFinish(self):
        return self.finish

class Elevator:
    def __init__(self, str):
        index=str.index(']')
        starttime=float(str[1:index-1])
        self.start=starttime
        str=str[index+1:]
        eles = str.split('-')
        self.maintaincnt=0
        self.inmaintain=False
        if eles[1] == 'Elevator':
            self.id=int(eles[2])
            self.floor = int(eles[3])
            self.maxSize = int(eles[4])
            self.speed = float(eles[5])
        else:
            ColorfulPrint.colorfulPrint('??? Unknow elevator type ???', ColorfulPrint.MODE_BOLD, ColorfulPrint.COLOR_RED)
        self.state = STATE_CLOSE
        self.lastMoveTime = -10.0
        self.lastOpenTime = -10.0
        self.passengers = []
    def setMaintain(self):
        self.inmaintain=True
    def checkMaintain(self,opTime,lineNum):
        if len(self.passengers)!=0:
            printError('Passengers is not empty!',lineNum)
        if opTime<self.start:
            printError('This ele has not operated yet!', lineNum)
    def checkArrive(self, eles, lineNum, opTime):
        if self.state != STATE_CLOSE:
            printError('Elevator moved when the door is not closed!', lineNum)
        if opTime - self.lastMoveTime < self.speed - 0.00001:
            printError('Floor elevator moved too fast!', lineNum)
        if self.floor > MAX_FLOOR or self.floor < MIN_FLOOR:
            printError('Floor elevator in a not-exist floor!', lineNum)
        if self.floor - int(eles[1]) != 1 and self.floor - int(eles[1]) != -1:
            printError('Illegal floor elevator movement!', lineNum)
        self.floor = int(eles[1])
        self.lastMoveTime = opTime
        if self.inmaintain:
            self.maintaincnt=self.maintaincnt+1
            if self.maintaincnt==3:
                printError('Need to stop!',lineNum)
        if opTime<self.start:
            printError('This ele has not operated yet!', lineNum)
    def checkOpen(self, eles, lineNum, opTime):
        if self.state != STATE_CLOSE:
            printError('Elevator door is not closed before open!', lineNum)
        self.state = STATE_OPEN
        self.lastOpenTime = opTime
        if int(eles[1]) != self.floor:
            printError('Elevator floor error!', lineNum)
        if opTime<self.start:
            printError('This ele has not operated yet!', lineNum)
    def checkClose(self, eles, lineNum, opTime):
        if self.state != STATE_OPEN:
            printError('Elevator door is not opened before close!', lineNum)
        self.state = STATE_CLOSE
        if opTime - self.lastOpenTime < 0.4 - 0.00001:
            printError('Close too fast!', lineNum)
        if int(eles[1]) != self.floor:
            printError('Elevator floor error!', lineNum)
        self.lastMoveTime = opTime
        if opTime<self.start:
            printError('This ele has not operated yet!', lineNum)
    def checkIn(self, eles, lineNum, opTime):
        if self.state != STATE_OPEN:
            printError('Passengers can\'t in when the door is closed!', lineNum)
        passengerID = int(eles[1])
        self.passengers.append(passengerID)
        req = reqDic.get(passengerID)
        if req == None:
            printError('Passenger not exist!', lineNum)
        else:
            if req.getFromFloor() != int(eles[2]):
                printError('\'IN\' message unmatched request!', lineNum)
        if len(self.passengers) > self.maxSize:
            printError('Elevator overload!', lineNum)
        if int(eles[2]) != self.floor:
            printError('Elevator floor error!', lineNum)
        if opTime<self.start:
            printError('This ele has not operated yet!', lineNum)
    def checkOut(self, eles, lineNum, opTime):
        if self.state != STATE_OPEN:
            printError('Passengers can\'t out when the door is closed!', lineNum)
        passengerID = int(eles[1])
        if passengerID not in self.passengers:
            printError('Passenger is not in elevator!', lineNum)
        else:
            self.passengers.remove(passengerID)
        req = reqDic.get(passengerID)
        if req == None:
            printError('Passenger not exist!', lineNum)
        else:
            req.move(int(eles[2]), lineNum)
            if req.isFinish():
                reqDic.pop(passengerID)
        if  int(eles[2]) != self.floor:
            printError('Elevator floor error!', lineNum)
        if opTime<self.start:
            printError('This ele has not operated yet!', lineNum)
    def getPassengerNum(self):
        return len(self.passengers)
    def getID(self):
        return self.id
    def getState(self):
        return self.state

def arrive(eles, lineNum, opTime):
    eleID = int(eles[2])
    if elevators.get(eleID) == None:
        printError('Unknown elevator ID!', lineNum)
        return
    elevator = elevators.get(eleID)
    elevator.checkArrive(eles, lineNum, opTime)
def elevatorOpen(eles, lineNum, opTime):
    eleID = int(eles[2])
    if elevators.get(eleID) == None:
        printError('Unknown elevator ID!', lineNum)
        return
    elevator = elevators.get(eleID)
    elevator.checkOpen(eles, lineNum, opTime)
def elevatorClose(eles, lineNum, opTime):
    eleID = int(eles[2])
    if elevators.get(eleID) == None:
        printError('Unknown elevator ID!', lineNum)
        return
    elevator = elevators.get(eleID)
    elevator.checkClose(eles, lineNum, opTime)
def passengerIn(eles, lineNum, opTime):
    eleID = int(eles[3])
    if elevators.get(eleID) == None:
        printError('Unknown elevator ID!', lineNum)
        return
    elevator = elevators.get(eleID)
    elevator.checkIn(eles, lineNum, opTime)
def passengerOut(eles, lineNum, opTime):
    eleID = int(eles[3])
    if elevators.get(eleID) == None:
        printError('Unknown elevator ID!', lineNum)
        return
    elevator = elevators.get(eleID)
    elevator.checkOut(eles, lineNum, opTime)
def maintainEle(eles,lineNum,opTime):
    eleID=int(eles[1])
    if elevators.get(eleID) == None:
        printError('Unknown elevator ID!', lineNum)
        return
    elevator =elevators.get(eleID)
    elevator.setMaintain()
def checkMaintain(eles,lineNum,opTime):
    eleID=int(eles[1])
    if elevators.get(eleID) == None:
        printError('Unknown elevator ID!', lineNum)
        return
    elevator =elevators.get(eleID)
    elevator.checkMaintain(opTime,lineNum)
    elevators.pop(eleID)
def printError(msg, l = 0):
    if l == 0:
        msg = ' ***** ' + msg + ' ***** '
    else:
        msg = ' ***** ' + msg + ' In line: ' + str(l) + ' ***** '
    ColorfulPrint.colorfulPrint(msg, ColorfulPrint.MODE_BOLD, ColorfulPrint.COLOR_RED)
    flag[0] = 0
    os.system("pause")
def initElevator():
    reqDic.clear()
    elevators.clear()
    flag[0] = 1
    lastOpTime[0] = -1.0
    elevators[1] = Elevator('[0.0]ADD-Elevator-1-1-6-0.4')
    elevators[2] = Elevator('[0.0]ADD-Elevator-2-1-6-0.4')
    elevators[3] = Elevator('[0.0]ADD-Elevator-3-1-6-0.4')
    elevators[4] = Elevator('[0.0]ADD-Elevator-4-1-6-0.4')
    elevators[5] = Elevator('[0.0]ADD-Elevator-5-1-6-0.4')
    elevators[6] = Elevator('[0.0]ADD-Elevator-6-1-6-0.4')

def processInput():
    with open('stdin.txt', 'r') as f:
        tot = f.readlines()
        for ele in tot:
            ele = ele.replace('\n', '')
            if 'ADD' in ele:
                temp = ele.split('-')
                elevators[int(temp[2])] = Elevator(ele)
            elif 'MAINTAIN' in ele:
                continue
            else:
                req = Req(ele)
                reqDic[req.getId()] = req


def checkOutput(fileName):
    initElevator()
    processInput()
    with open(fileName, 'r') as f:
        tot = f.readlines()
        count = 1
        for line in tot:
            process(line, count)
            count += 1
    if len(reqDic) != 0:
        printError('Some requests are not satisfied!')
        print('Not satisfied requests ID:')
        for num in reqDic:
            print(num)
    for elevator in elevators.values():
        if elevator.getPassengerNum() != 0:
            printError('Some is trapped in elevator! Elevator id: ' + str(elevator.getID()))
        if elevator.getState() != STATE_CLOSE:
            printError('Elevator is not closed! Elevator id: ' + str(elevator.getID()))
    if flag[0] == 1:
        ColorfulPrint.colorfulPrint(' ===== Accepted ===== ', ColorfulPrint.MODE_BOLD, ColorfulPrint.COLOR_GREEN)
        return True
    else:
        printError('Wrong answer!')
        return False

def process(read, lineNum):
    read = read.replace('\n', '')
    index = read.index(']')
    opTime = float(read[1:index - 1])
    read = read[index + 1:]
    eles = read.split('-')
    if opTime < lastOpTime[0]:
        printError('Incorrect output order!', lineNum)
    lastOpTime[0] = opTime
    if eles[0] == 'ARRIVE':
        arrive(eles, lineNum, opTime)
    elif eles[0] == 'OPEN':
        elevatorOpen(eles, lineNum, opTime)
    elif eles[0] == 'CLOSE':
        elevatorClose(eles, lineNum, opTime)
    elif eles[0] == 'IN':
        passengerIn(eles, lineNum, opTime)
    elif eles[0] == 'OUT':
        passengerOut(eles, lineNum, opTime)
    elif eles[0] == 'MAINTAIN_ACCEPT':
        maintainEle(eles,lineNum,opTime)
    elif eles[0] == 'MAINTAIN_ABLE':
        checkMaintain(eles,lineNum,opTime)
    else:
        printError('Unknown option!', lineNum)

if __name__ == "__main__":
    fileName = 'stdout.txt'
    checkOutput(fileName)