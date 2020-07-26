@ECHO OFF

ECHO "Composing eclipse-inst-%1.exe"

IF NOT EXIST ..\Concat\product\product-%1.zip (
  IF NOT EXIST ..\Concat\product\product-%1.tar (
    ECHO "No product-%1.zip and no product%1.tar"
    EXIT 1
  )

ECHO "Combining"
COPY /B extractor-%1.exe + ^
  ..\marker.txt +  ^
  ..\Libdata\libdata.jar + ^
  ..\marker.txt +  ^
  ..\Concat\descriptor-tar-%1.txt + ^
  ..\marker.txt +  ^
  ..\Concat\product\product-%1.tar + ^
  ..\marker.txt ^
  eclipse-inst-%1.exe
ECHO "Combining done"
  
  EXIT 0
)

IF EXIST ..\Concat\jre.tar.cab (
ECHO "Composing with JRE"
IF NOT EXIST ..\Concat\descriptor-jre-%1.txt EXIT 2
COPY /B extractor-%1.exe + ^
  ..\marker.txt +  ^
  ..\Libdata\libdata.jar + ^
  ..\marker.txt +  ^
  ..\Concat\descriptor-jre-%1.txt + ^
  ..\marker.txt +  ^
  ..\Concat\product\product-%1.zip + ^
  ..\marker.txt  + ^
  ..\Concat\jre.tar.cab + ^
  ..\marker.txt ^
  eclipse-inst-%1.exe
) ELSE (
ECHO "Composing without JRE"
IF NOT EXIST ..\Concat\descriptor-%1.txt EXIT 3
COPY /B extractor-%1.exe + ^
  ..\marker.txt +  ^
  ..\Libdata\libdata.jar + ^
  ..\marker.txt +  ^
  ..\Concat\descriptor-%1.txt + ^
  ..\marker.txt +  ^
  ..\Concat\product\product-%1.zip + ^
  ..\marker.txt ^
  eclipse-inst-%1.exe
)

ECHO "Done"
