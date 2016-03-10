
#!/bin/bash
echo "Input jar log file name: "
read logfile

# kill the previous reasoning process 
PID=`ps -eaf | grep CityPulseWP5 | grep -v grep | awk '{print $2}'`
if [[ "" !=  "$PID" ]]; then
  echo "killing $PID"
  kill -9 $PID
fi

#start reasoning
nohup java -jar CityPulseWP5-jar-with-dependencies.jar > logs/$logfile.log 2>&1 &
echo "WP5 is started!"
