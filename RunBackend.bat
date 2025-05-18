@echo off
echo ===================================================
echo BUILDING AND DEPLOYING SPRING BOOT APPLICATION
echo ===================================================
echo.
echo Building the application...
call mvn clean package
echo.
echo Checking if application is running...
tasklist /FI "IMAGENAME eq java.exe" 2>NUL | find /I /N "java.exe">NUL
if "%ERRORLEVEL%"=="0" (
    echo Stopping current application...
    taskkill /F /IM java.exe
    timeout /t 2
)
echo.
echo Starting the application...
echo ===================================================
echo APPLICATION LOGS (Press Ctrl+C to stop)
echo ===================================================
echo.
java -jar target\tiengtrungapp-0.0.1-SNAPSHOT.jar
pause\tiengtrungapp-0.0.1-SNAPSHOT.jar