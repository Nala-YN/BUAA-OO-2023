import os
import random
import re

import sympy

intPool = [0, 1, 2, 3, 4,5,1,9,22,33,44,55,11,99,96,69]  # 常量池
hasWhiteSpace = False  # 是否加入空白字符
hasLeadZeros = False  # 数字是否有前导零,如果传入sympy的表达式中数字有前导零，sympy将无法识别
maxTerm = 3  # 表达式中的最大项数
maxFactor = 3  # 项中最大因子个数
depth=3
getfunc=True
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
    case = rd(0, 3)
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


def getTerm(mydepth):
    factorNum = rd(maxFactor-1, maxFactor)
    result = ""
    if mydepth>=depth:
        for i in range(factorNum):
            factor = rd(0, 1)
            if factor == 0:
                result = result + getNum(False)
            elif factor == 1:
                result = result + getPower()
            if i < factorNum - 1:
                result = result + getWhiteSpace() + "*" + getWhiteSpace()
        return result
    if rd(0, 1) == 1:
        result = getSymbol() + getWhiteSpace()
    for i in range(factorNum):
        factor = rd(0, 5)
        if (getfunc):
            factor=rd(0,4)
        if factor == 0:
            result = result + getNum(False)
        elif factor == 1:
            result = result + getPower()
        elif factor == 2 and mydepth != depth:
            result = result + "("+getExpr(mydepth+1)+")"
            if rd(0, 1) == 1:
                result = result + getWhiteSpace() + getExponent()
        elif factor==3 or factor==4and mydepth != depth:
            result=result + getTri(mydepth+1)
        elif factor==5:
            result=result+getFunc()
        else:
            result = result + "0"
        if i < factorNum - 1:
            result = result + getWhiteSpace() + "*" + getWhiteSpace()
            # print("term:"+result)
    return result


def getExpr(mydepth):
    termNum = rd(maxTerm-1, maxTerm)
    result = getWhiteSpace()
    for i in range(termNum):
        result = result + getSymbol() + getWhiteSpace() + getTerm(mydepth) + getWhiteSpace()
            # print("Expr:"+result)
    return result
def getFunc():
    temp=rd(0,2)
    if(temp==0):
        return "f"
    elif temp==1:
        return "g"
    elif temp==2:
        return "h"
def getTri(dep):
    result=""
    temp=rd(0,1)
    if temp==0:
        if rd(0,1)==0:
            result=result+"sin("+getExpr(dep)+")"
        else:
            result = result + "sin((" + getExpr(dep) + "))"
    else:
        if rd(0,1)==0:
            result=result+"cos("+getExpr(dep)+")"
        else:
            result = result + "cos((" + getExpr(dep) + "))"
    if(rd(0,2)==2):
        result=result+getExponent()
    return result
def getFuncin():
    global maxTerm
    global maxFactor
    global depth
    global getfunc
    maxTerm = 2  # 表达式中的最大项数
    maxFactor = 2  # 项中最大因子个数
    depth = 1
    getfunc = True
    f = getExpr(0)
    while (len(f) > 50):
        f = getExpr(0)
    g = getExpr(0)
    while (len(g) > 50):
        g = getExpr(0)
    h = getExpr(0)
    while (len(h) > 50):
        h = getExpr(0)
    getfunc = False
    depth = 1
    f1=getxiang()
    f2=getxiang()
    f3=getxiang()
    f11=f1.replace("f","("+f+")").replace("g","("+g+")").replace("h","("+h+")")
    f22=f2.replace("f","("+f+")").replace("g","("+g+")").replace("h","("+h+")")
    f33 =f3.replace("f", "(" + f + ")").replace("g", "(" + g + ")").replace("h", "(" + h + ")")
    ff=""
    for i in range(len(f)):
        if f[i] =='x':
            ff=ff+"("+f11+")"
        elif f[i] =='y':
            ff=ff+"("+f22+")"
        elif f[i] =='z':
            ff=ff+"("+f33+")"
        else:
            ff=ff+f[i]
    f1=f1.replace("f","f(x,y,z)").replace("g","g(x,y,z)").replace("h","h(x,y,z)")
    f2=f2.replace("f", "f(x,y,z)").replace("g", "g(x,y,z)").replace("h", "h(x,y,z)")
    f3=f3.replace("f", "f(x,y,z)").replace("g", "g(x,y,z)").replace("h", "h(x,y,z)")
    return f,f1,f2,f3,ff,g,h
    getfunc = False
def getxiang():
    temp = getExpr(0)
    while (len(temp) > 20):
        temp = getExpr(depth)
    return temp
def genData():
    global maxTerm
    global maxFactor
    global depth
    global getfunc
    maxTerm = 3  # 表达式中的最大项数
    maxFactor = 3  # 项中最大因子个数
    depth = 2
    getfunc=True
    f=getExpr(0)
    while (len(f) > 100):
        f = getExpr(0)
    g=getExpr(0)
    while (len(g) > 100):
        g = getExpr(0)
    h=getExpr(0)
    while (len(h) > 100):
        h = getExpr(0)
    if("f" in h or "g" in h):
        os.system("pause")
    maxTerm = 4  # 表达式中的最大项数
    maxFactor = 4  # 项中最大因子个数
    depth = 2
    getfunc=False
    expr = getExpr(0)
    while(len(expr)>200):
        expr=getExpr(0)
    sym = expr.replace("f", "(" + f + ")")
    expr=expr.replace("g","g(x,y,z)").replace("h","h(x,y,z)").replace("f","f(x,y,z)")
    sym = sym.replace("g", "("+g+")")
    sym = sym.replace("h", "("+h+")")
    x = sympy.Symbol('x')
    y=sympy.Symbol('y')
    z=sympy.Symbol('z')
    expr="3"+"\n"+"f(x,y,z)="+f+"\n"+"g(x,y,z)="+g+"\n"+"h(x,y,z)="+h+"\n"+re.sub(r'(\d+)', rd(0, 3) * '0' + r'\1', str(expr))
    return expr,str(sym)
print(genData())