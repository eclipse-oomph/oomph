@ECHO OFF

IF NOT EXIST ..\Concat\descriptor-%1.txt EXIT
IF NOT EXIST ..\Concat\product\product-%1.zip EXIT

COPY /B extractor-%1.exe + ^
  ..\marker.txt +  ^
  ..\Libdata\libdata.jar + ^
  ..\marker.txt +  ^
  ..\Concat\descriptor-%1.txt + ^
  ..\marker.txt +  ^
  ..\Concat\product\product-%1.zip + ^
  ..\marker.txt  ^
  oomph-extractor-%1.exe
