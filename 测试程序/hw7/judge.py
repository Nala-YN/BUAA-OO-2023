import re
import os
from outputChecker import checkOutput
from colorfulPrint import ColorfulPrint
times = []

if __name__ == "__main__":
    waCount = 0
    accCount = 0
    badCount = 0
    tleCount = 0
    passCount = 0
    failCount = 0
    time1=0
    time2=0
    time3=0
    line1=0
    line2=0
    line3=0
    x = 100000
    for i in range(x):
        ColorfulPrint.colorfulPrint(' >>>>>>>>>>>>>>>>>>>>>>> TEST {} <<<<<<<<<<<<<<<<<<<<<<<< '.format(i + 1), ColorfulPrint.MODE_BOLD, ColorfulPrint.COLOR_BLUE)
        cmd = 'python generate.py > stdin.txt'
        os.system(cmd)
        cmd = 'datainput.exe | java -jar hw7.jar > stdout.txt'
        os.system(cmd)
        temp = checkOutput('stdout.txt')
        if temp:
            passCount += 1
        else:
            failCount += 1
            waCount += 1
            with open('stdin.txt', 'r') as f:
                con = f.read()
            with open('./logWA/stdin' + str(i + 1) + '.txt', 'w') as f:
                f.write(con)
            with open('stdout.txt', 'r') as f:
                con = f.read()
            with open('./logWA/stdout' + str(i + 1) + '.txt', 'w') as f:
                f.write(con)
    if waCount == 0:
        ColorfulPrint.colorfulPrint(' ===== YOU HAVE PASSED ALL TESTPOINT ===== ', ColorfulPrint.MODE_BOLD, ColorfulPrint.COLOR_GREEN)
        ColorfulPrint.colorfulPrint(' ===== TESTPOINT PASSED: ' + str(x) + '/' + str(x) + ' ===== ', ColorfulPrint.MODE_BOLD, ColorfulPrint.COLOR_GREEN)
    else:
        ColorfulPrint.colorfulPrint(' ***** YOU HAVE NOT PASSED ALL TESTPOINT ***** ', ColorfulPrint.MODE_BOLD, ColorfulPrint.COLOR_RED)
        ColorfulPrint.colorfulPrint(' ***** TESTPOINT PASSED: ' + str(x - waCount) + '/' + str(x) + ' ***** ', ColorfulPrint.MODE_BOLD, ColorfulPrint.COLOR_RED)
    ColorfulPrint.colorfulPrint(' ===== Time accept: ' + str(accCount) + '/' + str(x) + ' rate: ' + str(accCount * 100 / x) + '% =====', ColorfulPrint.MODE_BOLD, ColorfulPrint.COLOR_GREEN)
    ColorfulPrint.colorfulPrint(' ##### Bad performance: ' + str(badCount) + '/' + str(x) + ' rate: ' + str(badCount * 100 / x) + '% #####', ColorfulPrint.MODE_BOLD, ColorfulPrint.COLOR_YELLOW)
    ColorfulPrint.colorfulPrint(' ***** Tle: ' + str(tleCount) + '/' + str(x) + ' rate: ' + str(tleCount * 100 / x) + '% *****', ColorfulPrint.MODE_BOLD, ColorfulPrint.COLOR_RED)
    ColorfulPrint.colorfulPrint(' ===== Output correct: ' + str(passCount) + '/' + str(x) + ' rate: ' + str(passCount * 100 / x) + '% =====', ColorfulPrint.MODE_BOLD, ColorfulPrint.COLOR_GREEN)
    ColorfulPrint.colorfulPrint(' ***** Output incorrect: ' + str(failCount) + '/' + str(x) + ' rate: ' + str(failCount * 100 / x) + '% *****', ColorfulPrint.MODE_BOLD, ColorfulPrint.COLOR_RED)
