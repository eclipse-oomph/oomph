@ECHO OFF

cd ..\src

"C:\Program Files (x86)\Java\jdk1.5.0_22\bin\javah.exe" -classpath "C:\develop\oomph\git\org.eclipse.oomph\plugins\org.eclipse.oomph.jreinfo\bin" org.eclipse.oomph.jreinfo.JREInfo

REM "C:\Program Files\Java\jdk1.7.0_67\bin\javah" -classpath ..\..\org.eclipse.oomph.jreinfo\bin org.eclipse.oomph.jreinfo.JREInfo
