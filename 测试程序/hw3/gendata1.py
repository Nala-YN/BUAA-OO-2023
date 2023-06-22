import random
import re

import sympy

intPool = [0,1,1,1,1,0,0,0,0,0,0,0,0,3,4,5,6]  # 常量池
hasWhiteSpace = False  # 是否加入空白字符
hasLeadZeros = False  # 数字是否有前导零,如果传入sympy的表达式中数字有前导零，sympy将无法识别
maxTerm = 3  # 表达式中的最大项数
maxFactor = 3  # 项中最大因子个数
depth=3
getfunc=0
def rd(a, b):
    return random.randint(a, b)

def getWhiteSpace():
    if hasWhiteSpace == False:
        return ""
    str = ""
    cnt = rd(0, 2)
    for i in range(cnt):
        type = rd(0, 1)
        if type == 0:
            str = str + " "
        else:
            str = str + "\t"
    return str


def getSymbol():
    if rd(0, 1) == 1:
        return "+"
    else:
        return "-"


def getNum(positive):
    result = ""
    integer = intPool[rd(0, len(intPool) - 1)]
    iszero = rd(0, 5)
    for i in range(iszero):
        result = result + "0"
    if hasLeadZeros == False:
        result = ""
    result = result + str(integer)
    if rd(0, 1) == 1:
            result = getSymbol() + result
            # print("num:"+result)
    return result


def getExponent():
    result = "**"
    result = result + getWhiteSpace()
    case = rd(2, 2)
    if rd(0, 1) == 1:
        result = result + "+"
    if case == 0:
        result = result + "0"
    elif case == 1:
        result = result + "1"
    elif case==2:
        result = result + "2"
    elif case==3:
        result=result+"3"
    elif case==4:
        result=result+"4"
    elif case==5:
        result=result+"5"
    elif case==6:
        result=result+"6"
    elif case==7:
        result=result+"7"
    elif case==8:
        result=result+"8"
        # result = result + getNum(True)
    # print("exponent:"+result)
    return result


def getPower():
    temp=rd(0,2)
    if temp ==0:
        result = "x"
    elif temp==1:
        result="y"
    elif temp==2:
        result="z"
    if rd(0, 1) == 1:
        result = result + getWhiteSpace() + getExponent()
    # print("Power:"+result)
    return result


def getTerm(mydepth,hasgetdiff):
    factorNum = rd(1, maxFactor)
    result = ""
    if mydepth>=depth:
        for i in range(factorNum):
            factor = rd(0, 3)
            if hasgetdiff:
                factor=rd(0,1)
            if factor == 0:
                result = result + getNum(False)
            elif factor == 1:
                result = result + getPower()
            elif factor == 2 or factor ==3:
                result =result+getDiff(mydepth+1)
            if i < factorNum - 1:
                result = result + getWhiteSpace() + "*" + getWhiteSpace()
        return result
    if rd(0, 1) == 1:
        result = getSymbol() + getWhiteSpace()
    for i in range(factorNum):
        factor = rd(0, 7)
        if hasgetdiff:
            factor=rd(0,5)
        if factor == 0:
            result = result + getNum(False)
        elif factor == 1:
            result = result + getPower()
        elif factor == 2:
            result = result + "("+getExpr(mydepth+1,hasgetdiff)+")"
            if rd(0, 1) == 1:
                result = result + getWhiteSpace() + getExponent()
        elif factor==3 or factor==4:
            result=result + getFunc()
        elif factor==5:
            result=result+getTri(mydepth+1,hasgetdiff)
        elif factor==6 or factor==7:
                result=result+getDiff(mydepth)
        else:
            result = result + "0"
        if i < factorNum - 1:
            result = result + getWhiteSpace() + "*" + getWhiteSpace()
            # print("term:"+result)
    return result


def getDiff(mydepth):
    result = ""
    temp=rd(0,2)
    if temp == 0:
        fac="x"
    elif temp==1:
        fac="y"
    else:
        fac="z"
    result=result+"d"+fac+"("+getExpr(mydepth,True)+")"
    return result
def getExpr(mydepth,hasgetdiff):
    termNum = rd(1, maxTerm)
    result = getWhiteSpace()
    for i in range(termNum):
        result = result + getSymbol() + getWhiteSpace() + getTerm(mydepth,hasgetdiff) + getWhiteSpace()
            # print("Expr:"+result)
    return result
def getFunc():
    temp=rd(0,2)
    if getfunc==0:
        return "0"
    elif getfunc==1:
        return "f"
    elif getfunc==2:
        temp=rd(0,1)
    if(temp==0):
        return "f"
    elif temp==1:
        return "g"
    elif temp==2:
        return "h"
def getTri(dep,hasgetdiff):
    result=""
    temp=rd(0,1)
    if temp==0:
            result = result + "sin((" + getExpr(dep,hasgetdiff) + "))"
    else:
            result = result + "cos((" + getExpr(dep,hasgetdiff) + "))"
    if(rd(0,2)==2):
        result=result+getExponent()
    return result

def getFuncin():
    global maxTerm
    global maxFactor
    global depth
    global getfunc
    getfunc=3
    maxTerm = 3
    maxFactor = 3
    depth = 2
    expr=getExpr(0,False)
    while(len(expr)>75):
        expr=getExpr(0,False)
    result=""
    getfunc = 0
    depth=1
    f = getExpr(0,False)
    while (len(f) > 30):
        f = getExpr(0,False)
    result=result+"f(x,y,z)="+f+"\n"
    fp=parseDiff(f)
    getfunc=1
    g = getExpr(0,False)
    while (len(g) > 30):
        g = getExpr(0,False)
    result=result+"g(x,y,z)="+g.replace("f","f(x,y,z)")+"\n"
    gp=g.replace("f","("+fp+")")
    gp=parseDiff(gp)
    getfunc=2
    h = getExpr(0,False)
    while (len(h) > 30):
        h = getExpr(0,False)
    getfunc = 2
    f1 = getxiang()
    f2 = getxiang()
    f3 = getxiang()
    f11=f1.replace("f","f(x,y,z)").replace("g","g(x,y,z)")
    f22 = f2.replace("f", "f(x,y,z)").replace("g", "g(x,y,z)")
    f33 = f3.replace("f", "f(x,y,z)").replace("g", "g(x,y,z)")
    result=result+"h(x,y,z)="+h.replace("g","g(x,y,z)").replace("f","f(("+f11+"),"+"("+f22+"),("+f33+"))")+"\n"
    f1p=parseDiff(f1.replace("f","("+fp+")").replace("g","("+gp+")"))
    f2p = parseDiff(f2.replace("f", "(" + fp + ")").replace("g", "(" + gp + ")"))
    f3p = parseDiff(f3.replace("f", "(" + fp + ")").replace("g", "(" + gp + ")"))
    ff=""
    for i in range(len(fp)):
        if fp[i]=="x":
            ff+="("+f1p+")"
        elif fp[i]=="y":
            ff += "(" + f2p + ")"
        elif fp[i]=="z":
            ff += "(" + f3p + ")"
        else:
            ff+=fp[i]
    hp=parseDiff(h.replace("f","("+ff+")").replace("g","("+gp+")"))
    getfunc=3
    f1 = getxiang()
    f2 = getxiang()
    f3 = getxiang()
    f1p = parseDiff(f1.replace("f", "(" + fp + ")").replace("g", "(" + gp + ")").replace("h","("+hp+")"))
    f2p = parseDiff(f2.replace("f", "(" + fp + ")").replace("g", "(" + gp + ")").replace("h","("+hp+")"))
    f3p = parseDiff(f3.replace("f", "(" + fp + ")").replace("g", "(" + gp + ")").replace("h", "(" + hp + ")"))
    ff = ""
    for i in range(len(fp)):
        if fp[i] == "x":
            ff += "(" + f1p + ")"
        elif fp[i] == "y":
            ff += "(" + f2p + ")"
        elif fp[i] == "z":
            ff += "(" + f3p + ")"
        else:
            ff += fp[i]
    f1=f1.replace("f","f(x,y,z)").replace("g","g(x,y,z)").replace("h","h(x,y,z)")
    f2 = f2.replace("f", "f(x,y,z)").replace("g", "g(x,y,z)").replace("h", "h(x,y,z)")
    f3 = f3.replace("f", "f(x,y,z)").replace("g", "g(x,y,z)").replace("h", "h(x,y,z)")
    sym=expr.replace("f","("+ff+")").replace("g","("+gp+")").replace("h","("+hp+")")
    expr = expr.replace("g", "g(x,y,z)").replace("h", "h(x,y,z)").replace("f",
                                                                          "f((" + f1 + "),(" + f2 + "),(" + f3 + "))")
    result+=expr
    return result,sym
    getfunc = False
def getxiang():
    temp = getExpr(0,False)
    while (len(temp) > 30):
        temp = getExpr(depth,False)
    return temp
def parseDiff(input):
    x=sympy.symbols("x")
    y=sympy.symbols("y")
    z=sympy.symbols("z")
    sym=""
    temp=input
    pos = temp.find("d")
    while pos!=-1:
        sym+=temp[0:pos];
        temp=temp[pos:len(temp)]
        i=3
        inbrack=1;
        diff=temp[1]
        while inbrack!=0:
            if temp[i]=="(":
                inbrack=inbrack+1
            elif temp[i]==")":
                inbrack=inbrack-1
            i=i+1
        str1=temp[3:i-1]
        temp=temp[i:len(temp)]
        str2=sympy.expand(str1)
        if diff=="x":
            str2=(sympy.diff(str2,x))
        elif diff=="y":
            str2=(sympy.diff(str2,y))
        else:
            str2=(sympy.diff(str2,z))
        sym+="("+str(str2)+")"
        pos=temp.find("d")
    sym=sym+temp
    sym=sympy.expand(sym);
    return str(sym)
def genData():
    global maxTerm
    global maxFactor
    global depth
    maxTerm = 2  # 表达式中的最大项数
    maxFactor = 2  # 项中最大因子个数
    depth = 1

    expr,sym = getFuncin()
    x = sympy.Symbol('x')
    y=sympy.Symbol('y')
    z=sympy.Symbol('z')
    expr="3\n"+re.sub(r'(\d+)', rd(0, 3) * '0' + r'\1', str(expr))

    return expr,parseDiff(sym)
print(genData())