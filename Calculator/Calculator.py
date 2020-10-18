try:
   from tkinter import *
except:
   from Tkinter import *

class Calculator:

    def __init__(self, master):

        self.master = master
        master.title("Python Calculator")
        self.equation=Entry(master, width=36)
        self.equation.grid(row=0, column=0, columnspan=4, padx=10, pady=10)
        self.createButton()

    def createButton(self):
      b0 = self.addButton(0)
      b1 = self.addButton(1)
      b2 = self.addButton(2)
      b3 = self.addButton(3)
      b4 = self.addButton(4)
      b5 = self.addButton(5)
      b6 = self.addButton(6)
      b7 = self.addButton(7)
      b8 = self.addButton(8)
      b9 = self.addButton(9)
      b0 = self.addButton(0)
      plus = self.addButton('+')
      minus = self.addButton('-')
      multiply = self.addButton('*')
      divide = self.addButton('/')
      clear = self.addButton('C')
      equal = self.addButton('=')
      lparenth = self.addButton('(')
      rparenth = self.addButton(')')
      dot = self.addButton('.')
      delete = self.addButton('<')

      row0 = [clear, lparenth, rparenth, delete]
      row1 = [b1, b2, b3, plus]
      row2 = [b4, b5, b6, minus]
      row3 = [b7, b8, b9, multiply]
      row4 = [dot, b0, equal, divide]

      r = 1
      for row in [row0, row1, row2, row3, row4]:
        c = 0
        for button in row:
          button.grid(row = r, column = c, columnspan = 1)
          c += 1
        r += 1

    def addButton(self, num):
      return Button(self.master, text=num, height=3, width=9, command = lambda: self.clickButton(str(num)))

    def clickButton(self, value):
      current_eq = str(self.equation.get())
      
      try:
        if value == 'C':
          self.equation.delete(-1, END)

        elif value == '=':
          result = str(eval(current_eq))
          self.equation.delete(-1, END)
          self.equation.insert(0, result)

        elif value == '<':
          eq = self.equation.get()[:-1]
          self.equation.delete(0, END)
          self.equation.insert(0, eq)

        else:
          self.equation.delete(0, END)
          self.equation.insert(0, current_eq+value)
      
      except:
        self.equation.delete(0, END)
        self.equation.insert(0, "Error")

if __name__=='__main__':
    
    root = Tk()

    GUI = Calculator(root)

    root.mainloop()