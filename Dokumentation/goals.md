# Purpose {#Purpose}

A Post process adds traceability between artifats to to documentation generated with Doxygen. 

# Goals {#Goals}

[$Goal 1]
The postprocessor supports the user of the documentation with navigation within the documentation. 
 
 
Mit Doxygen werden Dokumentationen erzeugt, diese sind aber nicht verkn�pft. 
Dazu werden in der Dokumentation Tags eingebaut, die die Beziehung der 
Dokumente wiedergeben. Grunds�tzlich k�nnen beliebige Tags definiert werden. 

   [$Cust 1] 
   
Test {#CUST1}   
   
   
 - Es k�nnen zum einen freie Tags vernwedet werden, die dann aber nicht gep�ft 
   werden und f�r die keine Hierarchie dargestellt werden kann.
   [$Cust 2] 
 - Alternativ kann in einer Ini-Datei die Liste der erlaubten Tags festgelegt 
   werden. Diese Vorgehensweise ist zu bevorzugen
 
 



 