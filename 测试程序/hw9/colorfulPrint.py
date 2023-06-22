# 支持两种模式：正常(NORMAL)和加粗(BOLD)
# 支持8种颜色：灰色、红色、绿色、黄色、蓝色、粉色、青色、白色
class ColorfulPrint():
    MODE_NORMAL = 1
    MODE_BOLD = 2
    COLOR_GRAY = 0
    COLOR_RED = 1
    COLOR_GREEN = 2
    COLOR_YELLOW = 3
    COLOR_BLUE = 4
    COLOR_PINK = 5
    COLOR_CYAN = 6
    COLOR_WHITE = 7
    @staticmethod
    def colorfulPrint(msg, mode, color):
        head = '\033['
        if mode == ColorfulPrint.MODE_NORMAL:
            head = head + '0;'
        elif mode == ColorfulPrint.MODE_BOLD:
            head = head + '1;'
        else:
            print('\033[1;31;40m' + ' ***** MODE ERROR ***** ' + '\033[0m')
            return
        if 0 <= color <= 7:
            head = head + str(30 + color) + ';40m'
        else:
            print('\033[1;31;40m' + ' ***** COLOR ERROR ***** ' + '\033[0m')
        tail = '\033[0m'
        print(head + msg + tail)


if __name__ == "__main__":
    # 彩色打印示例
    ColorfulPrint.colorfulPrint('This is a test', ColorfulPrint.MODE_BOLD, ColorfulPrint.COLOR_BLUE)