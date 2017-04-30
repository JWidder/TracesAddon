RMDIR html /S /q
doxygen
java -cp tracesAddon.jar main.TracesAddon "C:\Users\Johannes Widder\git\TracesAddon"
pause .
"C:\Program Files (x86)\Mozilla Firefox\firefox" html\index.html
exit